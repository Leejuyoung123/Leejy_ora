package org.edu.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.edu.service.IF_BoardService;
import org.edu.service.IF_MemberService;
import org.edu.util.FileDataUtil;
import org.edu.vo.BoardVO;
import org.edu.vo.MemberVO;
import org.edu.vo.PageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
	/*
	 orcl jdbc 연동
	 https://m.blog.naver.com/PostView.nhn?blogId=pyj721aa&logNo=221147393750&proxyReferer=https:%2F%2Fwww.google.com%2F
*/   
	/*home 74line
 	*<a href="#" onclick="$('.popup_base').css('height',$(document).height());$('.contact_pop').show();">
 	*RestFull 방식
 	*@RestController 스프링 (서버단)
 	*JSP = Ajax + JSON + Jquery (클라이언트단)
 	*= 최신CRUD처리  
 	*지금까지 했던 프로젝트 내용 중 CRUD submit(get,post) = 고전방식
 	*ㄴ스프링 @Controller
  															*/
     /*애노테이션 위주 설명
       >@Controller > @RestController
       >public String boardList 메서드 -> return "board/board_list";
	   >기존 model.addAttribute("boardList".list); 담아서 board_list.jsp 이동
       
       >(JSON 변환 public ResponseEntity<List<boardVO>>)  = return
       >entity = new ResponseEntity<>(service.selectReplies(bno)
       >댓글 호출한 페이지에 JSON데이터가 변형되서 jsp로 전송함
       BoardVO boardVO -> (기존 매개변수)
       @RequestBody BoardVO boardVO (jsp 에서 값을 controller 전송할떄) 
       @controller 는 view를 리턴 / 
       @Restcontroller는 json/xml형식으로 응답 객체  ( 데이터만 리턴)
     */

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Inject
	private IF_MemberService memberService;
	
	@Inject
	private IF_BoardService boardService;
	
	@Inject
	private FileDataUtil fileDataUtil;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * 게시물관리 > 삭제 입니다.
	 * @throws Exception 
	 */
	@RequestMapping(value = "/board/delete", method = RequestMethod.POST)
	public String boardDelete(@RequestParam("bno") Integer bno, Locale locale, RedirectAttributes rdat) throws Exception {
		List<String> files = boardService.selectAttach(bno);
		
		boardService.deleteBoard(bno);
		
		//첨부파일 삭제(아래)
		for(String fileName : files) {
			//삭제 명령문 추가(아래)
			File target = new File(fileDataUtil.getUploadPath(), fileName);
			if(target.exists()) {
				target.delete();
			}
		}		
		
		rdat.addFlashAttribute("msg", "삭제");
		return "redirect:/board/list";
	}
	
	/**
	 * 게시물관리 > 수정 입니다.
	 * @throws Exception 
	 */
	@RequestMapping(value = "/board/update", method = RequestMethod.GET)
	public String boardUpdate(@ModelAttribute("pageVO") PageVO pageVO, @RequestParam("bno") Integer bno, Locale locale, Model model) throws Exception {
		BoardVO boardVO = boardService.viewBoard(bno);
		model.addAttribute("boardVO", boardVO);
		model.addAttribute("pageVO", pageVO);
		return "/board/board_update";
	}
	@RequestMapping(value = "/board/update", method = RequestMethod.POST)
	public String boardUpdate(@ModelAttribute("pageVO") PageVO pageVO, MultipartFile file,@Valid BoardVO boardVO,Locale locale, RedirectAttributes rdat) throws Exception {
		if(file.getOriginalFilename() == "") {//조건:첨부파일 전송 값이 없다면
			boardService.updateBoard(boardVO);
		} else {
			//기존등록된 첨부파일 삭제처리(아래)
			List<String> delFiles = boardService.selectAttach(boardVO.getBno());
			for(String fileName : delFiles) {
				//실제파일 삭제
				File target = new File(fileDataUtil.getUploadPath(), fileName);
				if(target.exists()) { //조건:해당경로에 파일명이 존재하면
					target.delete();  //파일삭제
				}//End if
			}//End for
			//아래 신규파일 업로드
			String[] files =fileDataUtil.fileUpload(file);//실제파일업로드후 파일명 리턴
			boardVO.setFiles(files);//데이터베이스 <-> VO(get,set) <-> DAO클래스
			boardService.updateBoard(boardVO);
		}//End if
		
		
		rdat.addFlashAttribute("msg", "수정");
		return "redirect:/board/view?bno=" + boardVO.getBno() + "&page=" + pageVO.getPage();
	}
	
	/**
	 * 게시물관리 > 등록 입니다.
	 * @throws Exception 
	 */
	@RequestMapping(value = "/board/write", method = RequestMethod.GET)
	public String boardWrite(Locale locale, Model model) throws Exception {
		
		return "board/board_write";
	}
	@RequestMapping(value = "/board/write", method = RequestMethod.POST)
	public String boardWrite(MultipartFile file,@Valid BoardVO boardVO,Locale locale, RedirectAttributes rdat) throws Exception {
		//System.out.println("========첨부파일없이 저장===" + file.getOriginalFilename());
		if(file.getOriginalFilename() == "") {
			//첨부파일 없이 저장
			boardService.insertBoard(boardVO);
		}else {
			String[] files = fileDataUtil.fileUpload(file);
			boardVO.setFiles(files);
			boardService.insertBoard(boardVO);			
		}
		rdat.addFlashAttribute("msg", "입력");
		return "redirect:/board/list";
	}
	
	/**
	 * 게시물관리 상세보기 입니다.
	 * @throws Exception 
	 */
	@RequestMapping(value = "/board/view", method = RequestMethod.GET)
	public String boardView(@ModelAttribute("pageVO") PageVO pageVO, @RequestParam("bno") Integer bno,Locale locale, Model model , HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		if( pageVO.getSearchBoard() != null ) {
			// 최초 세션만들어짐 /admin/board/list?searchBoard=notice
			session.setAttribute("session_bod_type", pageVO.getSearchBoard());
		}else {
			//일반 링크 클릭시 /admin/board/view?page=2... 데이터 전송
			// 만들어진 세션을 사용
			pageVO.setSearchBoard((String) session.getAttribute("session_bod_type"));
		}
		BoardVO boardVO = boardService.viewBoard(bno);
		//여기서 부터 첨부파일명 때문에 추가
		List<String> files = boardService.selectAttach(bno);
		String[] filenames = new String[files.size()];
		int cnt = 0;
		for(String fileName : files) {
			filenames[cnt++] = fileName;
		}
		//여러개 파일에서 1개 파일만 받는 것으로 변경
		//String[] filenames = new String[] {files};
		boardVO.setFiles(filenames);//String[]
		//여기까지 첨부파일때문에 추가
		model.addAttribute("boardVO", boardVO);
		model.addAttribute("pageVO", pageVO);
		model.addAttribute("extNameArray", fileDataUtil.getExtNameArray());
		return "board/board_view";
	}
	
	/**
	 * 게시물관리 리스트 입니다.
	 * @throws Exception 
	 */
	@RequestMapping(value = "/board/list", method = RequestMethod.GET)
	public String boardList(@ModelAttribute("pageVO") PageVO pageVO, Locale locale, Model model ,HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		if( pageVO.getSearchBoard() != null ) {
			// 최초 세션만들어짐 /admin/board/list?searchBoard=notice
			session.setAttribute("session_bod_type", pageVO.getSearchBoard());
		}else {
			//일반 링크 클릭시 /admin/board/view?page=2... 데이터 전송
			// 만들어진 세션을 사용
			pageVO.setSearchBoard((String) session.getAttribute("session_bod_type"));
		}
		//PageVO pageVO = new PageVO();//매개변수로 받기전 테스트용
		if(pageVO.getPage() == null) {
			pageVO.setPage(1);//초기 page변수값 지정
		}
		pageVO.setPerPageNum(10);//1페이지당 보여줄 게시물 수 강제지정
		pageVO.setTotalCount(boardService.countBno(pageVO));//강제로 입력한 값을 쿼리로 대체OK.
		
		List<BoardVO> list = boardService.selectBoard(pageVO);
		//모델클래스로 jsp화면으로 boardService에서 셀렉트한 list값을 boardList변수명으로 보낸다.
		//model { list -> boardList -> jsp }
		model.addAttribute("boardList", list);
		model.addAttribute("pageVO", pageVO);
		return "board/board_list";
	}
	
	/**
	 * 스프링 시큐리티 secutiry-context.xml설정한 로그인 처리 결과 화면
	 * @param locale
	 * @param request
	 * @param rdat
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/login_success", method = RequestMethod.GET)
	public String login_success(Locale locale,HttpServletRequest request, RedirectAttributes rdat) throws Exception {
		logger.info("Welcome login_success! The client locale is {}.", locale);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userid = "";//아이디
		String levels = "";//ROLE_ANONYMOUS
		Boolean enabled = false;
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails) {
			//인증이 처리되는 로직(아이디,암호를 스프링시큐리티 던져주고 인증은 스프링에서 알아서 해줌.)
			enabled = ((UserDetails)principal).isEnabled();
		}
		HttpSession session = request.getSession();//세션을 초기화 시켜줌.
		if (enabled) { //인증처리가 완료된 사용자의 권한체크(아래)
			Collection<? extends GrantedAuthority>  authorities = authentication.getAuthorities();
			if(authorities.stream().filter(o -> o.getAuthority().equals("ROLE_ANONYMOUS")).findAny().isPresent())
			{levels = "ROLE_ANONYMOUS";}
			if(authorities.stream().filter(o -> o.getAuthority().equals("ROLE_USER,")).findAny().isPresent())
			{levels = "ROLE_USER,";}
			if(authorities.stream().filter(o -> o.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent())
			{levels = "ROLE_ADMIN";}
			userid =((UserDetails)principal).getUsername();
			//로그인 세션 저장
			session.setAttribute("session_enabled", enabled);//인증확인
			session.setAttribute("session_userid", userid);//사용자아이디
			session.setAttribute("session_levels", levels);//사용자권한
			//=========== 상단은 스프링시큐리티에서 기본제공하는 세션 변수처리
			//=========== 하단은 우리가 추가한는 세션 변수처리
			//회원이름 구하기 추가
			MemberVO memberVO = memberService.viewMember(userid);
			session.setAttribute("session_username", memberVO.getUser_name());//사용자명
        	}
		rdat.addFlashAttribute("msg", "로그인");//result 데이터를 숨겨서 전송
		return "redirect:/";//새로고침 자동 등록 방지를 위해서 아래처럼 처리
	}
	
	/**
	 * 로그인 페이지 파일 입니다.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Locale locale, Model model) {
		
		return "login";
	}
	
	/**
	 * 슬라이드 페이지 파일 입니다.
	 */
	@RequestMapping(value = "/sample/slide", method = RequestMethod.GET)
	public String slide(Locale locale, Model model) {
		
		return "sample/slide";
	}
	
	/**
	 * CONTACT US 페이지 파일 입니다.
	 */
	@RequestMapping(value = "/sample/contactus", method = RequestMethod.GET)
	public String contactus(Locale locale, Model model) {
		
		return "sample/contactus";
	}
	
	/**
	 * BLOG 페이지 파일 입니다.
	 */
	@RequestMapping(value = "/sample/blog", method = RequestMethod.GET)
	public String blog(Locale locale, Model model) {
		
		return "sample/blog";
	}
	
	/**
	 * WORK 페이지 파일 입니다.
	 */
	@RequestMapping(value = "/sample/work", method = RequestMethod.GET)
	public String work(Locale locale, Model model) {
		
		return "sample/work";
	}
	
	/**
	 * we are 페이지 파일 입니다.
	 */
	@RequestMapping(value = "/sample/weare", method = RequestMethod.GET)
	public String weare(Locale locale, Model model) {
		
		return "sample/weare";
	}
	
	/**
	 * html5 테스트용 파일 입니다.
	 */
	@RequestMapping(value = "/sample/htmltest", method = RequestMethod.GET)
	public String htmltest(Locale locale, Model model) {
		
		return "sample/htmltest";
	}
	
	/**
	 * 샘플 파일 홈 입니다.
	 */
	@RequestMapping(value = "/sample", method = RequestMethod.GET)
	public String sample(Locale locale, Model model) {
		
		return "sample/home";
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws Exception 
	 */
	/*
	 * servlet-context에 의해 브라우저의 요청은 HomeController로 넘어가게 된다. 
	 * 여기에서 home( ) 를 보면 현재날짜와 시간을 가져온 후 이것을 문자열을 만든다. 
	 * 이 후 작업의 결과를 model에 담고 home 라는 뷰 이름을 반환한다.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws Exception {
		PageVO pageVO = new PageVO();
		if(pageVO.getPage() == null) {
			pageVO.setPage(1);//초기 page변수값 지정
		}
		pageVO.setPerPageNum(5);//1페이지당 보여줄 게시물 수 강제지정
		pageVO.setTotalCount(boardService.countBno(pageVO));//강제로 입력한 값을 쿼리로 대체OK.
		List<BoardVO> list = boardService.selectBoard(pageVO);
		//첨부파일 출력 떄문에 추가 START
		// 리스트페이지 : list(값) > boardlist > jsp 
		// 메인페이지   : list(값   > list +   > boardlistfiles > jsp
		
		pageVO.setSearchBoard("gallery");
		List<BoardVO> listGallery = boardService.selectBoard(pageVO);
		pageVO.setSearchBoard("notice");
		List<BoardVO> listNotice = boardService.selectBoard(pageVO);
		List<BoardVO> boardListFiles = new ArrayList<BoardVO>();
		for(BoardVO vo:listGallery) {
			List<String> files = boardService.selectAttach(vo.getBno());
			String[] filenames = new String[files.size()];
			int cnt=0;
			for(String fileName : files) {
				filenames[cnt++] = fileName;
			}
			vo.setFiles(filenames);  // 여기까지는 view 상세보기와 똑같다 
			boardListFiles.add(vo);
		}
		model.addAttribute("extNameArray", fileDataUtil.getExtNameArray());
		//첨부파일이 이미지인지 문서파일인지 구분용 jsp변수
		//첨부파일 출력 떄문에 추가 END
		model.addAttribute("boardListGallery", boardListFiles);
		model.addAttribute("boardListNotice", listNotice);		
		return "home";
	}
}