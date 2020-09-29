package org.edu.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileDataUtil {
	
	private ArrayList<String> extNameArray = new ArrayList<String>() 
	{
		{
			add("gif");
			add("jpg");
			add("png");
		}
	};
	//첨부파일 업로드 경로 변수값으로 가져옴 servlet-context.xml
	@Resource(name="uploadPath")
	private String uploadPath;
	
	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}
	/**
	 * 게시물 이미지일때 미리보기 메서드 구현(IE, 크롬에서 공통)
	 */
	@RequestMapping(value="/image_preview", method=RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] imagePreview(@RequestParam("filename") String fileName, HttpServletResponse response) throws IOException {
		FileInputStream fis = null;//변수초기화
		ByteArrayOutputStream baos = new ByteArrayOutputStream();//인스턴스 변수생성
		fis = new FileInputStream(uploadPath + "/" + fileName); 
		//생성자는 여러 타입의 매개변수를 받지만 공통적으로 파일의 주소와 이름의 정보를 가진 객체를 받아 해당 파일과의 입력 스트림을 생성합니다.
		int readCount = 0;
		/*모두 현재 파일 포인터 위치를 기준으로 함 (파일 포인터 앞의 내용은 없는 것처럼 작동)
		int read() : 1byte씩 내용을 읽어 정수로 반환
		int read(byte[] b) : 파일 내용을 한번에 모두 읽어서 배열에 저장
		int read(byte[] b. int off, int len) : 'len'길이만큼만 읽어서 배열의 'off'번째 위치부터 저장*/
		
		byte[] buffer = new byte[1024];
		byte[] fileArray = null;
		while((readCount = fis.read(buffer)) != -1) {
			baos.write(buffer,0,readCount);
		}
		fileArray = baos.toByteArray();//자료 변환 후 변수에 저장
		fis.close();
		baos.close();
		return fileArray;
	}
	/**
	 * 게시물 상세보기에서 첨부파일 다운로드 메서드 구현(공통)
	 * 이미지 미리보기 크로스 브라우징 (IE,크롬) 처리 회원정보 수정시 이름 세션값 수정처리 예정
	 *
	 */
	@RequestMapping(value="/download", method=RequestMethod.GET)
	@ResponseBody
	public FileSystemResource fileDownload(@RequestParam("filename") String fileName, HttpServletResponse response) {
		File file = new File(uploadPath + "/" + fileName);
		response.setContentType("application/download; utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		return new FileSystemResource(file);
	}
	
	/**
	 * 파일 업로드 메서드(공통)
	 * @throws IOException 
	 */
	public String[] fileUpload(MultipartFile file) throws IOException {
		String originalName = file.getOriginalFilename();//jsp에서 전송받은 파일의 이름 가져옴
		UUID uid = UUID.randomUUID();//랜덤문자 구하기
		String saveName = uid.toString() + "." + originalName.split("\\.")[1];//한글 파일명 처리 때문에...
		String[] files = new String[] {saveName};//형변환
		byte[] fileData = file.getBytes();
		File target = new File(uploadPath, saveName);
		FileCopyUtils.copy(fileData, target);
		return files;
	}
	/*
	 * 한글 파일명 처리 떄문에 img[] images.jsp (.) 을기준으로해서 분리 img[1] images img[2] jpg 확장자 .을
	 * 분리하려면 "\\." == FilecopyUtils.copy == 파일 전송 순서 1.JSP 게시판 입력폼 첨부파일을 포함 입력버튼 클릭
	 * 2.서버로 전송 /tmp 폴더 이동 3.copy 명령어가 실행되면 서버에 /tmp 내용이 c:/egov/workspace/upload 폴더
	 * 저장.
	 */
	public ArrayList<String> getExtNameArray() {
		return extNameArray;
	}

	public void setExtNameArray(ArrayList<String> extNameArray) {
		this.extNameArray = extNameArray;
	}
}