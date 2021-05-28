package com.study.data.web;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.study.attach.vo.AttachVO;
import com.study.common.util.StudyAttachUtils;
import com.study.common.vo.ResultMessageVO;
import com.study.data.service.IDataBoardService;
import com.study.data.vo.DataBoardVO;
import com.study.data.vo.DataSearchVO;
import com.study.exception.BizException;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.exception.BizPasswordNotMatchedException;

//빈등록 어노테이션 다 같은 뜻임
// 빈등록 스프링 어노테이션 : @Component (@Controller, @Service, @Repository) 
// JSR표준 : @Bean

@Controller
public class DataController {

	@Inject
	private IDataBoardService boardService;

	@Inject
	private StudyAttachUtils attachUtils;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping({ "/data/list.wow", "/data/list.ygy" })
	public String list(@ModelAttribute("searchVO") DataSearchVO searchVO, Model model) { // searchCategoty,
		// searchjob 을 다
		// 세팅해줌
		logger.info("searchVO={}" + searchVO); // searchVO 객체를 파라미터로 넘겨서 log로 확인 / log4j.xml
		model.addAttribute("searchVO", searchVO);
		List<DataBoardVO> list = boardService.getBoardList(searchVO);
		model.addAttribute("results", list);

		return "data/list";
	}

	// 클라이언트가 우리쪽으로 치고들어오는 url
	// 반환이 void라는건 요청 이름기반해서 view 이름이됨.

	// 클라이언트 메서드를 이메서드와 매핑 함.
	// 메서드 안에 if boNo!=null 안쓰고, pramas를 써줘서 boNo라는 파람이 있어야 여기와 매핑함.
	// requestMapping의 path는 value의 별칭임. 서로 같은 말임.
	// @어노테이션의 기본값 설정 value인데, 의미가 불명확해서 path라는 의미있는 이름으로 매핑해줌.

	@RequestMapping(value = "/data/view.wow", params = "boNo")
	public void view(int boNo, Model model) throws IOException, BizException {
		DataBoardVO vo = boardService.getBoard(boNo);
		model.addAttribute("board", vo);
	}

	// regist.wow : GET 요청인 꼉우 화면으로 , POST 요청인 경우 등록 처리
//	@RequestMapping(path="/data/regist.wow", method =RequestMethod.GET)
	@GetMapping("/data/regist.wow")
	public String registGet(@ModelAttribute("board") DataBoardVO board) {
		return "data/regist";
	}

//	@RequestMapping(path="/data/regist.wow", method =RequestMethod.POST) // , method=RequestMethod.POST)
	@PostMapping("/data/regist.wow")
	public String registPost(DataBoardVO board,
			@RequestParam(name = "boFiles", required = false) MultipartFile[] boFiles, HttpServletResponse resp,
			HttpServletRequest req, Model model) throws BizException, Exception {

		// 업로드파일이 존재하는 경우 저장후 해당 정보를 vo에 설정
		if (boFiles != null) {
			List<AttachVO> attaches = attachUtils.getAttachListByMultiparts(boFiles, "DATA", "data/2021");
			board.setAttaches(attaches);
		}
		// vo에 저장된 업로드정보는 서비스단에서 처리한다.

		board.setBoIp(req.getRemoteAddr());
		boardService.registBoard(board);
		logger.debug("board={0}", board);
		return "redirect:/data/view.wow?boNo=" + board.getBoNo();
	}
	
	
//		@ExceptionHandler
//		@ExceptionHandler같은 경우는 @Controller, @RestController가 적용된 Bean내에서 발생하는 예외를 잡아서 하나의 메서드에서 처리해주는 기능을 한다.



//		@ControllerAdvice
//		@ExceptionHandler가 하나의 클래스에 대한 것이라면, @ControllerAdvice는 모든 @Controller 즉, 전역에서 발생할 수 있는 예외를 잡아 처리해주는 annotation이다.

		// 예외는 service에서 만들어서 던져짐
//		@ExceptionHandler(BizNotFoundException.class)
//		public Object notFoundEx(BizNotFoundException e) {
//			ResultMessageVO resultMessageVO = new ResultMessageVO();
//			resultMessageVO.setMessage("해당 글이 존재하지 않습니다.");
//			resultMessageVO.setResult(true);
//			resultMessageVO.setTitle("edit");
//			resultMessageVO.setUrl("freeList.wow");
//			resultMessageVO.setUrlTitle("목록으로");
//			model.addAttribute("resultMessageVO", resultMessageVO);
//			
//			return "common/message";
//		}
		
	@ExceptionHandler(BizNotFoundException.class)
	public String BizNotFoundExceptionHandler(BizNotFoundException e, Model model) {
		logger.warn(e.getMessage(), e);
		ResultMessageVO messageVO = new ResultMessageVO();
		messageVO.setMessage("해당 글이 존재하지 않습니다.")
				.setResult(false)
				.setTitle("조회실패")
				.setUrl("list.wow")
				.setUrlTitle("목록으로");
		model.addAttribute("messageVO", messageVO);
		
		return "common/message";
	}
	
	@ExceptionHandler(BizPasswordNotMatchedException.class)
	public String BizPasswordNotMatchedExceptionHandler(BizPasswordNotMatchedException e, Model model) {
		logger.warn(e.getMessage(), e);
		ResultMessageVO messageVO = new ResultMessageVO();
		messageVO.setMessage("입력하신 패스워드가 올바르지 않습니다.")
				.setResult(false)
				.setTitle("패스워드 오류")
				.setUrl("list.wow")
				.setUrlTitle("목록으로");
		model.addAttribute("messageVO", messageVO);
		
		return "common/message";
	}
	
	@ExceptionHandler(BizNotEffectedException.class)
	public String BizNotEffectedExceptionHandler(BizNotEffectedException e, Model model) {
		logger.warn(e.getMessage(), e);
		ResultMessageVO messageVO = new ResultMessageVO();
		messageVO.setMessage("요청하신 처리를 수행하지 못했습니다.")
				.setResult(false)
				.setTitle("처리 실패")
				.setUrl("list.wow")
				.setUrlTitle("목록으로");
		model.addAttribute("messageVO", messageVO);
		
		return "common/message";
	}
	
	
	
	@GetMapping(value = "/data/edit.wow", params="boNo")
	public String edit(@RequestParam int boNo, Model model) throws BizException {
		logger.info("boNo={}"+boNo);
		
		DataBoardVO data = null;
		data = boardService.getBoard(boNo);
		model.addAttribute("board", data);

		return "data/edit";
	}
	

//	@RequestMapping(value="/free/freeModify.wow", method = RequestMethod.POST)
	@PostMapping("/data/modify.wow")
	public ModelAndView modify(
					@RequestParam(name = "boFiles", required = false) MultipartFile[] boFiles, 
					@ModelAttribute("board") DataBoardVO board) throws BizException, IOException {
		logger.info("DataBoardVO={}", board);
		ModelAndView mav = new ModelAndView();
		
		// 첨부파일 처리(registPost 동일)
		// 업로드파일이 존재하는 경우 저장후 해당 정보를 vo에 설정
		if (boFiles != null) {
			List<AttachVO> attaches = attachUtils.getAttachListByMultiparts(boFiles, "DATA", "data/2021");
			board.setAttaches(attaches);
		}
		
		boardService.modifyBoard(board);		
		mav.setViewName("redirect:/data/view.wow?boNo=" + board.getBoNo());
		
		return mav;
	}
}
