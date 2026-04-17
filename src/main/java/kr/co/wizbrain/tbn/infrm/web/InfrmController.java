package kr.co.wizbrain.tbn.infrm.web;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.wizbrain.tbn.award.service.AwardService;
import kr.co.wizbrain.tbn.award.vo.AwardVO;
import kr.co.wizbrain.tbn.event.service.EventService;
import kr.co.wizbrain.tbn.event.vo.EventVO;
import kr.co.wizbrain.tbn.infrm.service.InfrmService;
import kr.co.wizbrain.tbn.infrm.vo.InfrmVO;
import kr.co.wizbrain.tbn.option.service.AreaOptService;
import kr.co.wizbrain.tbn.option.service.AuthService;
import kr.co.wizbrain.tbn.option.service.InfrmOptService;
import kr.co.wizbrain.tbn.option.vo.AuthVo;
import kr.co.wizbrain.tbn.option.vo.OptAreaVo;
import kr.co.wizbrain.tbn.option.vo.OptInftVo;
import kr.co.wizbrain.tbn.statistic.service.StatisticService;
import kr.co.wizbrain.tbn.statistic.vo.StatisticVO;
import kr.co.wizbrain.tbn.user.service.UserService;
import kr.co.wizbrain.tbn.user.vo.UserVO;
import java.util.*;
/**
 * 사용자 컨트롤러 클래스
 * @author 미래전략사업팀 정다빈
 * @since 2020.07.23
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 *
 *   수정일            수정자              수정내용
 *  -------    -------- ---------------------------
 *  2020.07.23  정다빈           최초 생성
 */

@Controller
public class InfrmController implements ApplicationContextAware {
	
	public static final Logger logger = LoggerFactory.getLogger(InfrmController.class);
	
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="areaOptService")
	private AreaOptService areaOptService;
	
	@Resource(name="eventService")
	private EventService eventService;
	
	@Resource(name="awardService")
	private AwardService awardService;
	
	@Resource(name="infrmService")
	private InfrmService infrmService;
	
	@Resource(name="infrmOptService")
	private InfrmOptService infrmOptService;
	
	@Resource(name="statisticService")
	private StatisticService statisticService;
	
	@Resource(name = "authService")
	private AuthService authService;
	
	public String url="";
	
	//주소에 맞게 매핑
	@RequestMapping(value="/informer/*.do")
	public String ReporterUrlMapping(HttpSession httpSession, HttpServletRequest request,Model model) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.Reporter 최초 컨트롤러 진입 httpSession : "+httpSession);
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURL() : "+request.getRequestURL());
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURI() : "+request.getRequestURI());
		logger.debug("▶▶▶▶▶▶▶.request.getContextPath() : "+request.getContextPath());
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		logger.debug("▶▶▶▶▶▶▶.보내려는 url : "+url);
		return url;
	}
	
	// 24-11-21 : 통신원 월별 제보건수 표출
	@RequestMapping(value="/infrm/monthReport.do")
	public ModelAndView monthReport(@RequestParam("selectYear")String selectYear, @RequestParam("informerId") String informerId) throws Exception {
		List<InfrmVO> monthReport = infrmService.monthReport(selectYear,informerId);
		
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("monthReport",monthReport);
		return mav;
	}
	
	
	//22.03.16 긴급생성
	//1.  소메뉴 존재 항목에 관하여 권한 별 분기처리
	@RequestMapping(value="/informer/first.do")
	public ModelAndView first(HttpSession httpSession, HttpServletRequest request,Model model) throws Exception{
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		ModelAndView mav = new ModelAndView("jsonView");
		
		// ####### 권한별 정보관리 화면 분기처리 ##########
		// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
		UserVO reqLoginVo = (UserVO) request.getSession().getAttribute("login");

		AuthVo authVo = new AuthVo();
		authVo.setAuthCode(reqLoginVo.getAuthCode());
		authVo.setUrl(url);
		authVo.setCdFlag("3");
		
		List<AuthVo> alist = authService.selectAuthUrl(authVo);
		String nowUrl = alist.get(0).getUrl()+".do";
		//권한에 따라 산출된 최초 url이 동일할 경우
		//(코드관리의 모든 기능을 사용 가능할 경우)
		//재귀함수 무한루프 방지
		if(nowUrl.equals("/informer/first.do")) {
			nowUrl = "/informer/informerMain.do";
		}
		mav.addObject("chgUrl", nowUrl);
		
		// ####### 권한별 정보관리 화면 분기처리 ########## 
		
		return mav;
	}
	
	//초기 검색버튼 활성화
	@RequestMapping(value= {"/informer/informerMain.do"})
	public ModelAndView initMain(HttpServletRequest request) throws Exception {
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		ModelAndView mav = new ModelAndView(url);
		// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
		//지역관련
		OptAreaVo avo = new OptAreaVo();
		if(!(nlVo.getAuthCode().equals("999"))) {//999 관리자 권한
			avo.setAreaCode(nlVo.getRegionId());
		}
		
		mav.addObject("informerTypeList", infrmOptService.selectInft1(new OptInftVo()));
		mav.addObject("informerRegionList", areaOptService.selectAreaOpt1(avo));
		mav.addObject("informerAreaList", this.areaOptService.selectAreaOpt1(avo));
		return mav;
	}

	
	@PostMapping("/infrm/datatable.do")
	public ModelAndView listForDatatable(
			@ModelAttribute("ifmVO") InfrmVO vo,
	        @RequestParam(required=false) String sortName,
            @RequestParam(required=false) String sortDir,
            HttpServletRequest request
	) {
	    ModelAndView mav = new ModelAndView("jsonView");

	    int start  = vo.getStart()  != null ? vo.getStart()  : 0;
	    int length = vo.getLength() != null ? vo.getLength() : 100;
	    int startR = start + 1;
	    int endR   = start + length;

	    // 화이트리스트 (화면별 허용 컬럼만)
	    Set<String> WL = new HashSet<>(Arrays.asList(
	        "INFORMER_ID","AREA_NAME","INFORMER_TYPE_NAME","ORG_NAME",
	        "INFORMER_NAME","PHONE_CELL","ZIPCODE","FLAG_ACT","REG_ORDER"
	    ));
	    // 현재 세션에 대해 로그인한 사용자 정보를 가져옴
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
	    // 첫 진입이라면
		if(vo.getSearchValue() == null) {
			// 모든 사용자 지역 코드 삽입
			vo.setAreaCode(nlVo.getRegionId());
		} 
	    
	    // 재사용 헬퍼: BaseVO에서 안전한 ORDER BY 생성
	    String orderBy ="";
	    if(sortName.equals("")||sortDir.equals("")) {
	    	orderBy="REG_ORDER DESC";
	    }else {
	    	orderBy=sortName+" "+sortDir;
	    }

	    //long total    = infrmService.countAll(vo);
	    long filtered = infrmService.countFiltered(vo); // WHERE만 동일, 페이징 X
	    List<InfrmVO> rows = infrmService.findSlice(vo, startR, endR, orderBy);

	    mav.addObject("draw", vo.getDraw());
	    mav.addObject("recordsTotal", filtered);
	    mav.addObject("recordsFiltered", filtered);
	    mav.addObject("data", rows);
	    return mav;
	}
	
	// 25-03-28 : 스크롤 시 조회 기능
	@RequestMapping(value="/informer/ifrmNextScroll.ajax") 
	public ModelAndView ifrmNextScroll(@RequestParam("startRnum") int startRnum , @RequestParam("endRnum") int endRnum ,
			@ModelAttribute("ifmVO") InfrmVO ifmVO, HttpServletRequest request) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.회원정보 조회 목록!!!!!!!!!!!!!!!!");
		ModelAndView mav = new ModelAndView("jsonView");
		
		InfrmVO thvo = new InfrmVO();
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
		List<InfrmVO> infrmList= null;
		
		try {
			thvo=ifmVO;
			if(thvo.getAreaCode()==null || thvo.getAreaCode().equals("")) {
				thvo.setAreaCode(nlVo.getRegionId());
			}
			
			infrmList = infrmService.selectInfrmList2(thvo,startRnum,endRnum); // service 수정
			
			/*mav.setViewName("/informer/informerList");*/
			mav.addObject("informerList", infrmList);
			mav.addObject("cnt", infrmList.size());
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return mav;
	}
	

	/**
	 * 통신원 이력 정보
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/informer/showInformerHistory.do")
	public ModelAndView showInformerHistory(Model model
			,@ModelAttribute("ifmVO") InfrmVO ifmVO
			) throws Exception {
		ModelAndView mv = new ModelAndView("/informer/informerHistory");
		
		AwardVO awvo = new AwardVO();
		awvo.setAREA_CODE(ifmVO.getAreaCode());
		awvo.setINFORMER_ID(ifmVO.getInformerId());
		
		EventVO evo = new EventVO();
		evo.setINFORMER_ID(ifmVO.getInformerId());
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		
		StatisticVO stvo = new StatisticVO();
		stvo.setStateDt(sdf.format(date));
		
		if(ifmVO.getInformerId() != null && ! (ifmVO.getInformerId().equals(""))){
			List<String> paramList = null;
			List informerInfo = infrmService.selectInfrmList(ifmVO, paramList);
			if(informerInfo.size() == 1){
				mv.addObject("informerInfo", informerInfo.get(0));
			}
			mv.addObject("nowYear", sdf.format(date));
			
			List<AwardVO> awardList = awardService.getAwardList(awvo);					// 시상 정보
			mv.addObject("awardList", awardList);
			
			List<EventVO> eventList = eventService.getEvtList(evo);					// 행사 참석 정보
			mv.addObject("eventList", eventList);
			
			List<InfrmVO> informerHistoryList = infrmService.getInformerHistory(ifmVO);	// 변경 내역 정보
			mv.addObject("informerHistoryList", informerHistoryList);
			
//			List<StatisticVO> receiptCntList = statisticService.getReceiptCntList(stvo);		// 월별 건수 정보
//			mv.addObject("receiptCntList", receiptCntList);
		}
		
		return mv;
	}
	
	

	/**
	 * 사용자 등록, 상세 페이지 이동 분기
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/informer/editInformer.do")
	public ModelAndView editUser(HttpServletRequest request, @ModelAttribute("ifmVO") InfrmVO ifmVO, RedirectAttributes redirectAttributes) throws Exception {
		/*ModelAndView mv = new ModelAndView("/informer/informerNew");*/
		ModelAndView mv = new ModelAndView();
		
		InfrmVO thvo = new InfrmVO();
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
		OptInftVo iTypeVo = new OptInftVo();
		OptAreaVo areaVo = new OptAreaVo();
		
		List<OptAreaVo> aList = new ArrayList<>();//소속방송국
		List<OptInftVo> t1List = new ArrayList<>();//통신원유형
		List<OptInftVo> t2List = new ArrayList<OptInftVo>();//기관
		List<OptInftVo> t3List = new ArrayList<OptInftVo>();//세부기관
		
/*		//관리자 제외 지역코드 삽입
		if(!(nlVo.getAuthCode().equals("999"))) {//999 관리자 권한
			areaVo.setAreaCode(nlVo.getRegionId());
			thvo.setAreaCode(nlVo.getRegionId());
			iTypeVo.setAreaCode(nlVo.getRegionId());
		}*/

		aList=areaOptService.selectAreaOpt1(areaVo);
		//통신원유형
		t1List = infrmOptService.selectInft1(new OptInftVo());
		//상세 및 수정
		if (request.getParameter("pr1")!=null&&!(request.getParameter("pr1").equals(""))) {//상세
			mv.setViewName("/informer/informerNew");
			
			mv.addObject("pageDiv", "update");
			thvo.setInformerId(request.getParameter("pr1"));
			thvo=infrmService.detailInformer(thvo);
			
			areaVo.setAreaCode(nlVo.getRegionId());
			thvo.setAreaCode(nlVo.getRegionId());
			iTypeVo.setAreaCode(thvo.getAreaCode());
			
			iTypeVo.setIfmId1(thvo.getInformerType());
			t2List=infrmOptService.selectInft2(iTypeVo);//기관
			if(thvo.getOrgId()==null||thvo.getOrgId().equals("")) {//통신원 소속기관이 무소속일 경우
				if(t2List.size()!=0) {//무소속인데 기관목록은 존재할 경우
					iTypeVo.setIfmId2(t2List.get(0).getIfmId2());
				}else{//해당 통신원종류 하위기관 미존재시
					
				}
			}else {//기관이 무소속이 아닐경우
				iTypeVo.setIfmId2(thvo.getOrgId());
			}
			t3List=infrmOptService.selectInft3(iTypeVo);//세부기관
		} else {//등록
			mv.setViewName("/informer/informerNewInsert");
			
			mv.addObject("pageDiv", "new");
			
			/*26-04-13 : 현재 로그인 된 사용자의 소속에 따라 소속 기관, 소속기관(세부) 드롭박스 오류 해결*/
			iTypeVo.setAreaCode(nlVo.getRegionId());
			areaVo.setAreaCode(nlVo.getRegionId());
			thvo.setAreaCode(nlVo.getRegionId());
			
			iTypeVo.setIfmId1(t1List.get(0).getIfmId1());
			t2List=infrmOptService.selectInft2(iTypeVo);//기관
			if(t2List.size()!=0) {
				iTypeVo.setIfmId2(t2List.get(0).getIfmId2());
			}
			t3List=infrmOptService.selectInft3(iTypeVo);//세부기관
/*			thvo.setAreaCode(aList.get(0).getAreaCode());*/
			/*26-04-16 : 본부 요청으로 진입 시 자동으로 id 생성*/
			thvo.setActId(infrmService.creActId(thvo));
		}
		mv.addObject("informerInfo", thvo);//통신원정보
		mv.addObject("informerAreaList", aList);//소속방송국
		mv.addObject("informerTypeList",t1List);//통신원유형
		mv.addObject("informerOrgList",t2List);//기관
		mv.addObject("informerOrgSubList",t3List);//세부기관
		
		return mv;
	}
	//등록에서 지역코드,통신원타입 클릭 시
	@RequestMapping("/informer/reSel.do")
	public ModelAndView reSel
		(
			HttpSession httpSession, HttpServletRequest request,Model model
			,@RequestParam(required=false, value="areaCode") String areaCode
			,@RequestParam(required=false, value="informerType") String informerType
			,@RequestParam(required=false, value="orgId") String orgId
			,@RequestParam(required=false, value="sflag") String sflag
		) 
		throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		OptInftVo thvo = new OptInftVo();
		InfrmVO ivo = new InfrmVO();
		List<OptInftVo> t2List = new ArrayList<OptInftVo>();//기관
		List<OptInftVo> t3List = new ArrayList<OptInftVo>();//세부기관
		ivo.setAreaCode(areaCode);
		String newActId = "";
		thvo.setAreaCode(areaCode);
		thvo.setIfmId1(informerType);
		thvo.setIfmId2(orgId);
		//소속기관 변경시
		if (sflag.equals("orgIdSel")) {
			t3List=infrmOptService.selectInft3(thvo);
		} else {//변경한게 지역코드,통신원유형일경우
			if(sflag.equals("areaCodeSel")) {//지역코드 변경시
				//actid변경
				newActId=infrmService.creActId(ivo);
				mv.addObject("newActId",newActId);
			}
			t2List=infrmOptService.selectInft2(thvo);
			mv.addObject("sList1",t2List);
			if(t2List.size()!=0) {
				thvo.setIfmId2(t2List.get(0).getIfmId2());
				t3List=infrmOptService.selectInft3(thvo);
			}
		}
		mv.addObject("sList2",t3List);
		return mv;
	}
	
	
	/**
	 * 통신원 저장
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/informer/saveInformer.do", method=RequestMethod.POST)
	public @ResponseBody ModelAndView saveInformer(HttpServletRequest req, HttpServletResponse res
			,@RequestParam(required=false, value="histCode") String histCode
			,@RequestParam(required=false, value="fileChg") String fileChg
			,@RequestParam(required=false, value="createFileError") String createFileError
			,@RequestParam(value="file", required=false) MultipartFile imgFile
			,@ModelAttribute("ifmVO") InfrmVO ifmVO) throws Exception {
		try {

			ModelAndView mv = new ModelAndView("jsonView");
			
			
			// 신규 가입인 경우 => pageDiv : 신규/수정 구분 값
			if(ifmVO.getPageDiv().equals("new")) {
				
				// 전화번호 중복인지 확인
				if(infrmService.chkPhone(ifmVO)!=null) {
					mv.addObject("msg","다른 통신원이 사용중인 전화번호입니다");
					return mv;
				}
				
				// 신규 가입인 경우 통신원 ID 생성(actId X)
				ifmVO.setInformerId(infrmService.getNewId(ifmVO));
			}
			
			//221129 경로관련 로직 변경
			String fdir = "resources/picture/"+ifmVO.getInformerId()+"/";
			//String dir = req.getServletContext().getRealPath(fdir);
			//context.getServletContext().getRealPath("/")+ "picture/" + paramVO.getInformerId()+ "/";

			
			/*기존 소스*/
			/*if(Boolean.parseBoolean(fileChg)){									// 사진이 변경된 경우만 저장.
				if(req instanceof MultipartHttpServletRequest){ 										// req 객체와 MultipartHttpServletRequest타입이 같다면,
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req; 	// req를 MultipartHttpServletRequest타입으로 캐스팅.
					MultipartFile imgFile = multipartRequest.getFile("file"); 					// 화면으로 부터 전달 받은 컨텐츠를 얻어옴.
					if(imgFile != null){
						if(imgFile.getContentType().startsWith("image")){
							saveFileInfo(imgFile, ifmVO);
							
							if(createFileError != null && !createFileError.equals("")){
								mv.addObject("createFileError", "createFileError");
								return mv;
							}
						}else{
							mv.addObject("badFileType", "badFileType");
							return mv;
						}
					}
				} else {
					res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected multipart request");
				}
			}*/
			
			
			/*변경 후 소스*/
			if(Boolean.parseBoolean(fileChg)){  // 사진 변경 시만

			    if(imgFile != null && !imgFile.isEmpty()){

			        if(imgFile.getContentType().startsWith("image")){

			            saveFileInfo(imgFile, ifmVO);

			            if(createFileError != null && !createFileError.equals("")){
			                mv.addObject("createFileError", "createFileError");
			                return mv;
			            }

			        }else{
			            mv.addObject("badFileType", "badFileType");
			            return mv;
			        }
			    }
			}
			
			// impl에서 insert, update 분기 처리함
			int cnt = infrmService.saveInformer(ifmVO);
			
			if(ifmVO.getHistCode() != null && !ifmVO.getHistCode().equals("")){
				String[] histCodeArray = ((String)ifmVO.getHistCode()).split(",");
				for(int i = 0; i < histCodeArray.length; i++){
					ifmVO.setUpdateCode(histCodeArray[i]);
					ifmVO.setUpdateText(infrmService.getUpdateCode(ifmVO));
					
					infrmService.saveInformerHist(ifmVO);
				}
			}
			
			mv.addObject("cnt", cnt);

			return mv;
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}
	
	/**
	 * 통신원 활동여부(해촉, 위촉)
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/informer/changeAct.do")
	public ModelAndView changeAct(Model model
			,@ModelAttribute("ifmVO") InfrmVO ifmVO
			,@RequestParam(required=false, value="chgFlagAct") String chgFlagAct
			,@RequestParam(required=false, value="upCode") String upCode
			,@RequestParam(required=false, value="stopDate") String stopDate
			,@RequestParam(required=false, value="pageDiv") String pageDiv
			) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		InfrmVO thvo = new InfrmVO();
		thvo = ifmVO;
		thvo.setFlagAct(chgFlagAct);
		//thvo.setAreaCode(areaCode);
		thvo.setUpdateCode(upCode);
		thvo.setStopDate(stopDate);
		int cnt = infrmService.saveInformer(thvo);
		if(thvo.getUpdateCode() != null && !thvo.getUpdateCode().equals("")){
			thvo.setUpdateText(infrmService.getUpdateCode(thvo));
			
			infrmService.saveInformerHist(thvo);
		}
		
		mv.addObject("cnt", cnt);

		return mv;
	}
	
	/**
	 * 통신원 삭제
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/informer/deleteInformer.do")
	public ModelAndView deleteInformer(Model model, @ModelAttribute InfrmVO paramVO) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		
		if(paramVO.getInformerId() != null && !paramVO.getInformerId().equals("")){
			int cnt = infrmService.deleteInformer(paramVO);
			
			mv.addObject("cnt", cnt);
			
			return mv;
		}
		mv.addObject("cnt", 0);

		return mv;
	}
	
	private WebApplicationContext context = null;
	
	/**
	 * 업로드할 파일의 상세 정보 얻어옴
	 * 
	 * @param videoFile
	 * @param chVidFileMng
	 * @return
	 * @throws Exception 
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private InfrmVO saveFileInfo(MultipartFile imgFile
			,  @ModelAttribute InfrmVO paramVO
			) throws Exception {
		String dir = context.getServletContext().getRealPath("/")+ "picture/" + paramVO.getInformerId()+ "/";//getContextPath() + "/picture/"; 
		// getRealPath(paramVO.getString("path123") + "/" + paramVO.getString("fileName123"));
		File makeDir = new File(dir);
		
		if(!makeDir.exists()){ 											// 디렉터리가 없으면,
			makeDir.mkdirs(); 											// 디렉터리 생성.
		}
		
		String filePath = dir + imgFile.getOriginalFilename();
		
		File makeFile = new File(filePath);
		
		paramVO.setLocalFilePath(imgFile.getOriginalFilename()); 	// 파일명
		//paramVO.put("PHOTO_PATH", filePath); 						// 파일 경로
		//paramVO.put("PHOTO_FILE_TYP_CD", imgFile.getContentType()); 	// 컨텐츠 타입
		
		if(paramVO.getPageDiv().equals("update")){
			InfrmVO pvo = infrmService.detailInformer(paramVO);
			if(pvo.getLocalFilePath()!=null){
				if(pvo.getLocalFilePath().equals(paramVO.getLocalFilePath())){
					return paramVO;
				}
			}
		}
		
		if(imgFile != null && !imgFile.isEmpty()){
			try {
				imgFile.transferTo(makeFile); 							// 파일 저장
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
		return paramVO;
	}
	
	public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.context = (WebApplicationContext) applicationContext;
	}
	
	
	
}
