package org.edu.controller;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.edu.service.IF_BoardService;
import org.edu.vo.BoardTypeVO;
import org.edu.vo.BoardVO;
import org.edu.vo.PageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
// controller 의 모든값을 여기서 거쳐감
public class ControllerAdviceException {
	private static final Logger logger = LoggerFactory.getLogger(ControllerAdviceException.class);

	@Inject
	private IF_BoardService boardService;

	
	// 컨트롤러의 매핑이 걸린 모든 메서드(페이지)에서 공통으로
	@ModelAttribute("boardTypeMenu") // jsp boardTypeMenu 에서 쓰임
	public List<BoardTypeVO> boardTypeMenu() throws Exception {

		List<BoardTypeVO> boardTypeMenu = boardService.selectBoardType();
		return boardTypeMenu;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView errorModelAndView(Exception ex, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		// 모델앤뷰에서 셋뷰네임은 jsp 파일명과 매칭
		HttpSession session = request.getSession();
		// 기존 로그인 세션 가져오기
		// levels == role_admin admin error실행 else 아니면 사용자 에러페이지
		if (session.getAttribute("session_levels") == "ROLE_ADMIN") {
			modelAndView.setViewName("admin/error_controller");
		} else {
			modelAndView.setViewName("error_controller");
		}
		modelAndView.addObject("exception", ex);
		// jsp type명 error_controller.jsp 모델 앤 뷰 에서 셋 뷰 네임은 jsp 파일명과 매칭
		// 보낼 변수 , 매개변수
		// 에러 발생시 이전페이지 URL을 session 변수를 이용해서 JSP로 보내는 코딩
		// 세션으로 저장해서 jsp 페이지에 prevPage변수로 보내는작업 (아래코드 )
		// 링크이동시 이전페이지의 저장값을 이동한 페이지에서 사라지기 떄문에 stateless 라고함
		// 스테이트리스 상태에서 계속 값을 유지하려면 session 변수 사용
		String referer = request.getHeader("Referrer");
		request.getSession().setAttribute("prevPage", referer);
		return modelAndView;
	}
}
