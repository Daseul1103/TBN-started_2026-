package kr.co.wizbrain.tbn.event.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import kr.co.wizbrain.tbn.award.service.AwardService;
import kr.co.wizbrain.tbn.award.web.AwardController;
import kr.co.wizbrain.tbn.comm.excel.FileDownloadView;
import kr.co.wizbrain.tbn.event.service.EventService;
import kr.co.wizbrain.tbn.event.vo.EventVO;
import kr.co.wizbrain.tbn.event.vo.eFileVO;
import kr.co.wizbrain.tbn.infrm.service.InfrmService;
import kr.co.wizbrain.tbn.infrm.vo.InfrmVO;
import kr.co.wizbrain.tbn.option.service.AreaOptService;
import kr.co.wizbrain.tbn.option.vo.OptAreaVo;
import kr.co.wizbrain.tbn.user.service.UserService;
import kr.co.wizbrain.tbn.user.vo.UserVO;

@Controller
public class EventController implements ApplicationContextAware{
	private static final Logger logger = LoggerFactory.getLogger(AwardController.class);
	public String url="";
	private WebApplicationContext context = null;
	
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="infrmService")
	private InfrmService infrmService;
	
	@Resource(name="awardService")
	private AwardService awardService;
	
	@Resource(name="eventService")
	private EventService eventService;
	
	@Resource(name="areaOptService")
	private AreaOptService areaOptService;
	
	//주소에 맞게 매핑
	@RequestMapping(value="informer/event/*.do")
	public String urlMapping(HttpSession httpSession, HttpServletRequest request,Model model) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.Reporter 최초 컨트롤러 진입 httpSession : "+httpSession);
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURL() : "+request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0]);
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURI() : "+request.getRequestURI());
		logger.debug("▶▶▶▶▶▶▶.request.getContextPath() : "+request.getContextPath());
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		logger.debug("▶▶▶▶▶▶▶.보내려는 url : "+url);
		return url;
	}
	
	
	@RequestMapping("/informer/event/eventMainContent.do")
	public ModelAndView eventMainContent(Model model
			,HttpServletRequest request
			,@ModelAttribute("EventVO") EventVO paramVO) throws Exception {
		
		ModelAndView mv = new ModelAndView("/informer/event/eventMainContent");
		
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
		
		//지역관련
		OptAreaVo avo = new OptAreaVo();
		
		//관리자 제외 지역코드 삽입
		if(!(nlVo.getAuthCode().equals("999"))) {//999 관리자 권한
			avo.setAreaCode(nlVo.getRegionId());
		}
		
		mv.addObject("informerRegionList", areaOptService.selectAreaOpt1(avo));
		
		return mv;
		
	}
	
	
	
	
	// 행사 관리 > 행사 목록 조회
	@RequestMapping("/informer/event/eventList.do")
	public ModelAndView eventList(Model model
			,HttpServletRequest request
			,@ModelAttribute("EventVO") EventVO paramVO) throws Exception {
		
		ModelAndView mv = new ModelAndView("/informer/event/eventList");
		
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
		
		//관리자 제외 지역코드 삽입
		if(!(nlVo.getAuthCode().equals("999"))) {//999 관리자 권한
			
			paramVO.setREGION_ID(nlVo.getRegionId());
			
		} else{
			if(paramVO.getREGION_ID()==null || paramVO.getREGION_ID().equals("")) {
				
				//최초 지역코드 idx 맨 처음 (부산교통방송)
				String fArea= areaOptService.selectAreaOpt1(new OptAreaVo()).get(0).getAreaCode();
				
				paramVO.setREGION_ID(fArea);
			}
		}
		
		// 개인정보 암호화 대상 X
        List<EventVO> eventList = eventService.getEventList(paramVO);		
        
        mv.addObject("eventList", eventList);
        
		return mv;
		
	}
	
	
	
	
	
	// 행사 관리 > 행사 목록 조회 페이징 처리
	@RequestMapping("/informer/event/eventListPagenation.do")
	public ModelAndView eventListPagenation(Model model,@ModelAttribute("EventVO") EventVO paramVO) throws Exception {
		
		ModelAndView mv = new ModelAndView("/informer/event/eventListPagenation");
		
		// 개인정보 암호화 대상 X
		List<EventVO> eventList = eventService.getEventList(paramVO);
       
        mv.addObject("eventListSize", eventList.size());
        
		return mv;
		
	}
	
	
	
	
	
	// 행사 관리 > 행사 상세 정보 조회
	@RequestMapping("/informer/event/detailEventView.do")
	public ModelAndView detailEventView(Model model,@ModelAttribute("EventVO") EventVO paramVO) throws Exception {
		
		ModelAndView mv = new ModelAndView("/informer/event/eventDetailView");
		
		if(paramVO.getEVENT_ID() != null && !paramVO.getEVENT_ID().equals("")){
			
			// 개인정보 암호화 대상 X
			List<EventVO> eventInfo = eventService.getEventList(paramVO);	
			
			// 행사 상세 정보
			if(eventInfo.size() == 1){
				mv.addObject("eventInfo", eventInfo.get(0));			
				mv.addObject("editDiv", "update");
			}
			
			List<EventVO> fileInfo = eventService.getFileList(paramVO);
			
			if(fileInfo.size() != 0) {
				mv.addObject("fileInfo", fileInfo);			
			}
			
			// 26-07: 개인정보 암호화 완료
			List<InfrmVO> attendanceList = eventService.getAttendanceList(paramVO);	// 행사 참석자 목록
			
			mv.addObject("attendanceList", attendanceList);
			
		} else {
			mv.addObject("editDiv", "new");
		}
		
		return mv;
		
	}
	
	
	
	
	// 행사 관리 > 행사 상세 조회
	@RequestMapping("/informer/event/detailEvent.do")
	public ModelAndView detailEvent(Model model,@ModelAttribute("EventVO") EventVO paramVO) throws Exception {
		ModelAndView mv = new ModelAndView("/informer/event/eventDetail");
		
		if(paramVO.getEVENT_ID() != null && !paramVO.getEVENT_ID().equals("")){
			List<EventVO> eventInfo = eventService.getEventList(paramVO);				// 행사 상세 정보
			if(eventInfo.size() == 1){
				mv.addObject("eventInfo", eventInfo.get(0));
			}
			
			List<EventVO> fileInfo = eventService.getFileList(paramVO);
			if(fileInfo.size() != 0) {
				mv.addObject("fileInfo", fileInfo);			
			}
		}
		
		return mv;
	}
	
	
	
	
	
	// 행사 관리 > 참석자 상세 조회
	@RequestMapping("/informer/event/detailEventAttendance.do")
	public ModelAndView detailEventAttendance(Model model,@ModelAttribute("EventVO") EventVO paramVO) throws Exception {
		ModelAndView mv = new ModelAndView("/informer/event/eventDetailAttendance");
		
		if(paramVO.getEVENT_ID() != null && !paramVO.getEVENT_ID().equals("")){
			
			List<InfrmVO> attendanceList = eventService.getAttendanceList(paramVO);	// 참석자 목록
			mv.addObject("attendanceList", attendanceList);
			
		}
		
		return mv;
	}
	
	
	
	
	// 행사 관리 > 행사 등록 페이지로 이동
	@RequestMapping("/informer/event/editEvent.do")
	public ModelAndView editEvent(Model model,@ModelAttribute("EventVO") EventVO paramVO) throws Exception {
		
		ModelAndView mv = new ModelAndView("/informer/event/eventEdit");
		
		if(paramVO.getEVENT_ID() != null && !paramVO.getEVENT_ID().equals("")){
			
			List<EventVO> eventInfo = eventService.getEventList(paramVO);		// 행사 상세 정보
			
			if(eventInfo.size() == 1){
				
				// 추가적으로 필요한 첨부 파일 정보 가져오기
				List<EventVO> fileInfo = eventService.getFileList(paramVO);
				
				if(fileInfo.size() != 0) {
					mv.addObject("fileInfo", fileInfo);			
				}
				
				mv.addObject("eventInfo", eventInfo.get(0));
				mv.addObject("editDiv", "update");
			}
		} else {
			mv.addObject("editDiv", "new");
		}
		
		return mv;
	}
	

	
	// 행사 관리 > 행사 저장
	@RequestMapping("/informer/event/saveEvent.do")
	public ModelAndView saveEvent(@RequestParam("multiFile") List<MultipartFile> multiFileList
			,HttpSession httpSession, 
			HttpServletRequest request, Model model,@ModelAttribute("EventVO") EventVO paramVO) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");

		// 개인정보 암호화 대상 X
		int cnt = eventService.saveEvent(paramVO);
		
		// 개인정보 암호화 대상 X
		String nowEvenetId = eventService.selectEventId();

		try {
			String fileDir = context.getServletContext().getRealPath("/")+"eventFile/"+ nowEvenetId +"/";
			FileUploadSave fus = new FileUploadSave();
			
			List<eFileVO> fileList = fus.fileUploadMultiple(multiFileList,fileDir,paramVO);
			
			if(fileList.size()!=0) {
				// 개인정보 암호화 대상 X
				eventService.insertFile(fileList);
			}
			
		} catch(Exception e) {
			logger.debug("에러메시지 : "+e.toString());
			mv.addObject("msg","저장에 실패하였습니다");
		}
		
		
		mv.addObject("cnt", cnt);

		return mv;
	}
	
	
	// 행사 관리 > 참석자 등록 페이지
	@RequestMapping("/informer/event/editEventAttendance.do")
	public ModelAndView editAttendance(Model model
			,@RequestParam(required=false, value="selArea") String selArea
			,@ModelAttribute("EventVO") EventVO paramVO,HttpServletRequest request) throws Exception {
		 
		ModelAndView mv = new ModelAndView("/informer/event/eventAttendanceEdit");
		
		EventVO thvo = new EventVO();
		
		// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
		
		//지역관련
		OptAreaVo avo = new OptAreaVo();
		if(!(nlVo.getAuthCode().equals("999"))) {//999 관리자 권한
			avo.setAreaCode(nlVo.getRegionId());
		}
		
		mv.addObject("informerRegionList", areaOptService.selectAreaOpt1(avo));
		
		mv.addObject("EVENT_ID", paramVO.getEVENT_ID());
		
		return mv;
	}
	
	
	
	
	// 행사 관리 > 참석자 등록 조회(미참석자 목록 조회)
	@RequestMapping("/informer/event/informerList4Event.do")
	public ModelAndView informerList4Event(Model model,@ModelAttribute("EventVO") EventVO paramVO) throws Exception {
		
		ModelAndView mv = new ModelAndView("/informer/event/informerList");
		
		if(paramVO.getEVENT_ID() != null && !paramVO.getEVENT_ID().equals("")){
			
			// 26-07 : 개인정보 암호화 완료
			List<InfrmVO> informerList = eventService.getInformerList4Event(paramVO);		// 미참석자 목록
			
			mv.addObject("informerList", informerList);
		}
		
		return mv;
	}
	
	
	// 행사 관리 > 참석자 저장
	@RequestMapping("/informer/event/saveAttendance.do")
	public ModelAndView saveAttendance(Model model,@ModelAttribute("EventVO") EventVO paramVO
			,@RequestParam(required=false, value="EVENT_ID") String EVENT_ID
			,@RequestParam(required=false, value="informerList") String informerList
			) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		List<String> alist = new ArrayList<>(); 
		
		int cnt = 0;
		
		paramVO.setEVENT_ID(EVENT_ID);
		
		if(informerList != null && ! informerList.equals("")){
			String[] INFORMER_ID_ARRAY = informerList.split(",");
			
			for(int i = 0; i < INFORMER_ID_ARRAY.length; i++){
				alist.add(INFORMER_ID_ARRAY[i]);
			}
			
			// 개인정보 암호화 대상 X
			cnt = eventService.saveAttendance(paramVO,alist);
		}
		
		mv.addObject("cnt", cnt);

		return mv;
	}
	
	
	
	// 행사 관리 > 행사 수정
	@RequestMapping("/informer/event/updateEvent.do")
	public ModelAndView updateEvent(Model model,@ModelAttribute("EventVO") EventVO paramVO) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		
		// 개인정보 암호화 대상 X
		int cnt = eventService.updateEvent(paramVO);
		
		mv.addObject("cnt", cnt);

		return mv;
	}
	
	
	
	
	// 행사 관리 > 행사 삭제
	@RequestMapping("/informer/event/deleteEvent.do")
	public ModelAndView deleteEvent(Model model,@ModelAttribute("EventVO") EventVO paramVO) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		if(paramVO.getEVENT_ID() != null && !paramVO.getEVENT_ID().equals("")){			
			
			// 만약 파일이 있다면
			// 개인정보 암호화 대상 X
			List<EventVO> fileList = eventService.selectFileList2(paramVO);
			
			if(fileList.size() > 0) {
				
				// DB에서 파일 삭제
				// 개인정보 암호화 대상 X
				eventService.deleteFile(paramVO);
				
				String nowEvenetId = paramVO.getEVENT_ID();
				
				String fileDir = context.getServletContext().getRealPath("/")+"eventFile/"+ nowEvenetId +"/";
				
				// 실제 디렉토리에서 파일 삭제
				FileUploadSave fus = new FileUploadSave();
				
				fus.deleteFile(fileList,fileDir);
			}			
			
			// 개인정보 암호화 대상 X
			int cnt = eventService.deleteEvent(paramVO);
			
			mv.addObject("cnt", cnt);
			
			return mv;
		}
		mv.addObject("cnt", 0);

		return mv;
	}
	
	
	
	// 파일 삭제
	@RequestMapping("/informer/event/eventOneDelete.do")
	public ModelAndView deleteOneEvent(Model model,@RequestParam("fileId") String fileId,
			@RequestParam("eventId")String eventId, @ModelAttribute("EventVO") EventVO paramVO) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
				
				String fileDir = context.getServletContext().getRealPath("/")+"eventFile/"+ eventId +"/";
				// 실제 디렉토리에서 파일 삭제
				
				// 개인정보 암호화 대상 X
				String fileName = eventService.selectFileName(fileId);
				FileUploadSave fus = new FileUploadSave();
				fus.deleteFileOne(fileDir,fileName);
				
				
				// DB에서 파일 삭제
				// 개인정보 암호화 대상 X
				eventService.deleteFileOne(fileId);
				
				mv.addObject("fileName", fileName);
				mv.addObject("cnt", 1);
		
				return mv;
	}
	
	
	
	// 행사 관리 > 참석자 삭제
	@RequestMapping("/informer/event/deleteAttendance.do")
	public ModelAndView deleteAttendance(Model model,@ModelAttribute("EventVO") EventVO paramVO) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		int cnt = 0;
		
		// 현재 참석자 삭제가 개별이어서, 굳이 아래와 같이 하지않아도 됨...
		if(paramVO.getEVENT_ID() != null && !paramVO.getEVENT_ID().equals("")){
			
			if(paramVO.getINFORMER_ID() != null && !paramVO.getINFORMER_ID().equals("")){
				
				String[] INFORMER_ID_ARRAY = ((String)paramVO.getINFORMER_ID()).split(",");
				
				for(int i = 0; i < INFORMER_ID_ARRAY.length; i++){
					paramVO.setINFORMER_ID(INFORMER_ID_ARRAY[i]);
					
					// 개인정보 암호화 대상 X
					cnt += eventService.deleteAttendance(paramVO);
				}
				
				mv.addObject("cnt", cnt);
				
				return mv;
			}
		}
 		mv.addObject("cnt", 0);

		return mv;
	}
	
	
	
	
	
	// 행사 관리 > 파일 다운로드
	@RequestMapping(value="/EventfileDownload.do")
	public ModelAndView EventfileDownload(@RequestParam("fileId")String fileId ,ModelAndView mView
			,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		EventVO fvo = new EventVO();
		
		fvo.setFILE_ID(fileId);
		
		// 개인정보 암호화 대상 X
		fvo = eventService.selectFileList(fvo).get(0);
		
		String filePath = fvo.getFILE_DIR()+fvo.getFILE_NAME();
		
		fvo.setFilePath(filePath);
		mView.addObject("fvo", fvo);
		
		// 응답을 할 bean의 이름 설정
		mView.setViewName("fileDownView");

		
		return mView;
	}

	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = (WebApplicationContext) applicationContext;
	}
}