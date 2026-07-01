package kr.co.wizbrain.tbn.mileage.web;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import kr.co.wizbrain.tbn.award.service.AwardService;
import kr.co.wizbrain.tbn.award.vo.AwardVO;
import kr.co.wizbrain.tbn.award.web.AwardController;
import kr.co.wizbrain.tbn.comm.BaseController;
import kr.co.wizbrain.tbn.infrm.service.InfrmService;
import kr.co.wizbrain.tbn.mileage.service.MileageService;
import kr.co.wizbrain.tbn.mileage.vo.MileageVO;
import kr.co.wizbrain.tbn.option.service.AreaOptService;
import kr.co.wizbrain.tbn.option.service.InfrmOptService;
import kr.co.wizbrain.tbn.option.vo.OptAreaVo;
import kr.co.wizbrain.tbn.option.vo.OptInftVo;
import kr.co.wizbrain.tbn.user.vo.UserVO;

@Controller
public class MileageController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(AwardController.class);
	public String url="";
	
	@Resource(name="infrmService")
	private InfrmService infrmService;
	
	@Resource(name="infrmOptService")
	private InfrmOptService infrmOptService;
	
	@Resource(name="areaOptService")
	private AreaOptService areaOptService;
		
	@Resource(name="mileageService")
	private MileageService mileageService;

	@Resource(name="awardService")
	private AwardService awardService;
	
	//주소에 맞게 매핑
	@RequestMapping(value= {"/mileage/*.do","/bestIfrm/*.do","/excellenceIfrm/*.do"})
	public String ReporterUrlMapping(HttpSession httpSession, HttpServletRequest request,Model model) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.Reporter 최초 컨트롤러 진입 httpSession : "+httpSession);
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURL() : "+request.getRequestURL());
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURI() : "+request.getRequestURI());
		logger.debug("▶▶▶▶▶▶▶.request.getContextPath() : "+request.getContextPath());
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		logger.debug("▶▶▶▶▶▶▶.보내려는 url : "+url);
		return url;
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	/*     < 굿 제보 마일리지 >     */
	////////////////////////////////////////////////////////////////////////////////////
	
	// 1. 굿 제보 마일리지 최초 이동 ( + 검색 옵션 셋팅 등 = 검색 기능 X )
	@RequestMapping(value= {"/informer/mileage/mileageMain.do"})
	public ModelAndView mileageMain(HttpServletRequest request) throws Exception {
		logger.debug("▶▶ 굿 제보 마일리지 메뉴 클릭");
		
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		ModelAndView mav = new ModelAndView(url);
		
		// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
		
		OptAreaVo avo = new OptAreaVo();
		if(!(nlVo.getAuthCode().equals("999"))) {
			avo.setAreaCode(nlVo.getRegionId()); // 관리자는 제외하고 
		}

		mav.addObject("informerTypeList", infrmOptService.selectInft1(new OptInftVo()));
		mav.addObject("informerRegionList", areaOptService.selectAreaOpt1(avo));
		mav.addObject("informerAreaList", this.areaOptService.selectAreaOpt1(avo));
		
		mav.setViewName("/mileage/mileageMain");
		
		return mav;
	}
	

	
	// 2. 굿 제보 마일리지 이동 및 검색
	@RequestMapping(value = {"/mileage/mileageMainLoad.do","/mileage/mileageMainsearch.do" })
	public ModelAndView mileagList (HttpServletRequest request, Model model,
		@ModelAttribute("MileageVO") MileageVO MileageVO) throws Exception {
		logger.debug("▶▶ 굿 제보 마일리지로 이동");
		ModelAndView mav = new ModelAndView("/mileage/mileageList");// ModelAndView 객체 생성

		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0]; // 이동할 페이지 url 추출
		
		// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");	
		
		// 첫 이동일 경우, 현재 날짜의 년월을 기준으로 조회 (현재 날짜의 -1달)
		/*if (MileageVO.getStandardDate().equals("")) {
		    LocalDate currentDate = LocalDate.now().minusMonths(1);
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
		    MileageVO.setStandardDate(currentDate.format(formatter));
		} else {
		    String beforeDate = MileageVO.getStandardDate();
		    beforeDate = beforeDate.replace("-", "");
		    MileageVO.setStandardDate(beforeDate);
		}*/


		
		// 목록 불러오기
		List<MileageVO> mileList = null;
		mileList = mileageService.mileList(MileageVO); // 초기 조회 , 검색 결과 반환

		
		mav.addObject("mileageList", mileList);
		mav.addObject("mileageListcnt", mileList.size());
		
		return mav; // ModelAndView 객체 반환
	}
	
	
	// 3. 굿 제보 마일리지 엑셀 다운로드 기능 생성
	@RequestMapping(value="/mileage/mileExcelDown.do")
	public String mileExcel(@ModelAttribute("MileageVO") MileageVO MileageVO, Model model,
	HttpServletRequest request) throws Exception {
/*		ParamsDto params = getParams(true);*/
		
		//String startDate;
		// 첫 이동일 경우, 현재 날짜의 년월을 기준으로 조회 (현재 날짜의 -1달)
		/*if (MileageVO.getStandardDate().equals("")) {
			LocalDate currentDate = LocalDate.now().minusMonths(1);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
			MileageVO.setStandardDate(currentDate.format(formatter));
			startDate = currentDate.format(formatter);
		} else {
			String beforeDate = MileageVO.getStandardDate();
			beforeDate = beforeDate.replace("-", "");
			MileageVO.setStandardDate(beforeDate);
			startDate = beforeDate;
		}*/
				
		List<MileageVO> mileList = null;
		mileList = mileageService.mileList(MileageVO);
		
		
		model.addAttribute("mapping", "mileageExcel");
		model.addAttribute("fileName", "굿 제보 마일리지(" + MileageVO.getStdt() +" ~ "+MileageVO.getEdt()+ ").xls");
		model.addAttribute("titleName", "굿 제보 마일리지" );
		model.addAttribute("mileData", mileList);

		return "hssfExcel";

	}
	
	
	// 4. 굿 제보 통신원 선정 엑셀
	@RequestMapping(value="/mileage/excellenceIfrmMain.do")
	public ModelAndView excellenceIfrmMain(HttpServletRequest request) throws Exception {
		logger.debug("▶▶ 우수 통신원(우수 제보자 X) 기준 탭 클릭");
		
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		ModelAndView mav = new ModelAndView(url);
		
		// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
				
		OptAreaVo avo = new OptAreaVo();
		if(!(nlVo.getAuthCode().equals("999"))) {
			avo.setAreaCode(nlVo.getRegionId()); // 관리자는 제외하고 
		}

		mav.addObject("informerTypeList", infrmOptService.selectInft1(new OptInftVo()));
		mav.addObject("informerRegionList", areaOptService.selectAreaOpt1(avo));
		mav.addObject("informerAreaList", this.areaOptService.selectAreaOpt1(avo));
				
		mav.setViewName("/mileage/excellenceIfrmMain");
		
		return mav;
	}

	
	// 5. 굿 제보 마일리지> 굿 제보 통신원 선정 
	@RequestMapping(value="/mileage/excellenceIfrmMainsearch.do") 
	public ModelAndView excelleneceSearch (@ModelAttribute("MileageVO") MileageVO MileageVO, Model model,
			HttpServletRequest request) throws Exception {
		logger.debug("▶▶ 굿 제보 통신원 선정으로 이동");
		ModelAndView mav = new ModelAndView("/mileage/excellenceIfrmList");// ModelAndView 객체 생성
		
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0]; // 이동할 페이지 url 추출
		
		// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");	
		
		// 날짜 작업
		/*String startDate;
		String endDate;
		String standardDate = MileageVO.getStandardDate();
		
		int year = Integer.parseInt(standardDate);
		startDate = (year - 1) + "08";  // 전년도 8월
		endDate = year + "07";  // 해당년도 7월
		
		MileageVO.setStartDate(startDate);
		MileageVO.setEndDate(endDate);*/
		
		// 목록 조회하기
		List<MileageVO> mileList = mileageService.excellenceList(MileageVO);
		
		mav.addObject("mileageList", mileList);
		mav.addObject("mileageListcnt", mileList.size());
		
		return mav;
	}
	
	
	// 6. 굿 제보 통신원 엑셀 다운로드
	@RequestMapping(value="/mileage/excellenceIfrmExcelDown.do")
	public String excellenceIfrmExcel (@ModelAttribute("MileageVO") MileageVO MileageVO, Model model,
			HttpServletRequest request) throws Exception {
		
		// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");	
		
		// 목록 조회하기
		List<MileageVO> mileList = mileageService.excellenceList(MileageVO);
		
		model.addAttribute("mapping", "excellenceExcel");
		model.addAttribute("fileName", "굿 제보 통신원(" + MileageVO.getStandardDate() + ").xls");
		model.addAttribute("titleName", "굿 제보 통신원(" + MileageVO.getStandardDate() + ")" );
		model.addAttribute("mileData", mileList);

		return "hssfExcel";
	}
	
	
	
	// 굿 제보 통신원 등록
	@RequestMapping(value="/mileage/excellenceIfrmInsert.do")
	public ModelAndView excellenceIfrmInsert(
	        @ModelAttribute MileageVO mvo,
	        @RequestParam(value = "Selection", required = false) List<String> selectionList,
	        Model model, HttpServletRequest request) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");   
		
		// 맨 앞에 N 떼는 사전 작업 진행
		for (int i = 0; i < selectionList.size(); i++) {
		    String val = selectionList.get(i);
		    if (val != null && val.length() > 1) {
		        selectionList.set(i, val.substring(1));
		    }
		}

		
		mileageService.excellenceIfrmInsert(mvo, selectionList);
		mv.addObject("msg", "success");
		return mv;
			
	}
	
	
	
	// 굿 제보 통신원 선정 취소   
	@RequestMapping(value="/mileage/excellenceIfrmDelete.do")
	public ModelAndView excellenceIfrmDelete(
	        @ModelAttribute MileageVO mvo,
	        @RequestParam(value = "Selection", required = false) List<String> selectionList,
	        Model model, HttpServletRequest request) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");   
		
		// 맨 앞에 Y 떼는 사전 작업 진행
		for (int i = 0; i < selectionList.size(); i++) {
		    String val = selectionList.get(i);
		    if (val != null && val.length() > 1) {
		        selectionList.set(i, val.substring(1));
		    }
		}

		
		mileageService.excellenceIfrmDelete(mvo, selectionList);
		mv.addObject("msg", "success");
		return mv;
			
	}
	
	
	// 7. 선정 기준 화면으로 최초 이동
	@RequestMapping(value="/mileage/standard.do")
	public ModelAndView standardMain(HttpServletRequest request) throws Exception {
		logger.debug("▶▶ 마일리지 선정 기준 탭 클릭");
		
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		ModelAndView mav = new ModelAndView(url);
		
		mav.setViewName("/mileage/standardMain");
		
		return mav;
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	/*     < 우수 제보자 >     */
	////////////////////////////////////////////////////////////////////////////////////
	
	// 1. 우수 제보자 최초 이동 ( + 검색 옵션 셋팅 등 )
		@RequestMapping(value= {"/informer/excellenceIfrm/excellenceIfrmMain.do", "/excellenceIfrm/excellenceUserMain.do"})
		public ModelAndView excellenceMain(HttpServletRequest request) throws Exception {
			logger.debug("▶▶ 우수 제보자 메뉴 클릭");
			
			url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
			
			if(url.equals("/informer/excellenceIfrm/excellenceIfrmMain")) {
				url = "/excellenceIfrm/excellenceIfrmMain";
			}
			
			ModelAndView mav = new ModelAndView("jsonView");
			
			// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
			UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
			
			OptAreaVo avo = new OptAreaVo();
			if(!(nlVo.getAuthCode().equals("999"))) {
				avo.setAreaCode(nlVo.getRegionId()); // 관리자는 제외하고 
			}

			// 시상 배점 기준 가져오기
			List<AwardVO> excellGrade = mileageService.excellGrade(avo);
			
			mav.addObject("informerTypeList", infrmOptService.selectInft1(new OptInftVo()));
			mav.addObject("informerRegionList", areaOptService.selectAreaOpt1(avo));
			mav.addObject("informerAreaList", this.areaOptService.selectAreaOpt1(avo));
			mav.addObject("excellGrade", excellGrade.get(0));
			
			mav.setViewName(url);
			
			return mav;
		}
		
		
	// 2. 우수 제보자 이동 및 검색
		@RequestMapping(value = "/excellenceIfrm/excellenceIfrmMainList.do")
		public ModelAndView excellenceList (@ModelAttribute("AwardVO") AwardVO paramVO, HttpServletRequest request) 
		throws Exception {
			logger.debug("▶▶ 우수 제보자로 이동");
			ModelAndView mav = new ModelAndView("/excellenceIfrm/excellenceInformerList");// ModelAndView 객체 생성

			// 시상별 배점 갱신
			if(!(paramVO.getALL_PER().equals("") || paramVO.getMAIN_PER().equals("") || paramVO.getADD_PER().equals(""))) {
				mileageService.updateGrade(paramVO);
			}

			// 목록 불러오기
			List<AwardVO> awardInformerList = mileageService.getExcellInformerList(paramVO);
			
			mav.addObject("awardInformerList", awardInformerList);
	        mav.addObject("awardInformerListSize", awardInformerList.size());
	        mav.addObject("params", paramVO);

			return mav; // ModelAndView 객체 반환
		}
	
		
	// 2-1. 우수 제보자 수상자 선정 저장
		@RequestMapping("/excellenceIfrm/excellenceInformerSave.do")
		public ModelAndView excellenceInformerSave(Model model,HttpServletRequest req,@ModelAttribute("AwardVO") AwardVO paramVO) throws Exception {
			ModelAndView mav = new ModelAndView("jsonView");  
			String[] Selection = req.getParameterValues("Selection");
			logger.debug("Selection===>"+Selection[0]);

			// AW_NAME decode 처리
		    String awName = paramVO.getAW_NAME();

		    if(awName != null && awName.contains("%")) {
		        awName = URLDecoder.decode(awName, "UTF-8");
		        paramVO.setAW_NAME(awName);
		    }
		    
		    List<AwardVO> alist = new ArrayList<>();
		    
			for (int i = 0; i < Selection.length; i++) {
				AwardVO awvo = new AwardVO();
				String[] str = Selection[i].split("%%");
				awvo.setINFORMER_ID(str[0]);
				awvo.setRPT_GRADE(str[1]);
				awvo.setMAIN_GRADE(str[2]);
				awvo.setADD_GRADE(str[3]);
				awvo.setALL_RANK(str[4]);
				alist.add(awvo);	
			}

			mileageService.saveAward(paramVO,alist);
			
			mav.addObject("msg", "success");
			return mav;
		}
		
	
	// 3. 우수제보자 수상자 조회 이동 및 검색
		@RequestMapping(value="/excellenceIfrm/excellenceUserList.do")
		public ModelAndView selectUserExcellenceList(HttpServletRequest request,Model model,@ModelAttribute("AwardVO") AwardVO paramVO) 
		throws Exception {
			logger.debug("▶▶ 우수 제보자 > 수상자 조회로 이동");
			ModelAndView mav = new ModelAndView("/excellenceIfrm/excellenceUserList");
			// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
	     	UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
	     	
	     	//지역관련
			OptAreaVo avo = new OptAreaVo();
			if(!(nlVo.getAuthCode().equals("999"))) {//999 관리자 권한
				avo.setAreaCode(nlVo.getRegionId());
				paramVO.setREGION_ID(nlVo.getRegionId());
			}
			
	        List awardInformerList = mileageService.selectUserAwardList(paramVO);
	        
	        mav.addObject("awardInformerList", awardInformerList);
	        mav.addObject("awardInformerListSize", awardInformerList.size());
	        mav.addObject("params", paramVO);
	        
			return mav;
		}
		
	// 3-1. 수상자 선정 취소
		@RequestMapping("/excellenceIfrm/deleteExcellence.do")
		public ModelAndView deleteExcellence(Model model,HttpServletRequest req,@ModelAttribute("AwardVO") AwardVO paramVO) throws Exception {
			ModelAndView mav = new ModelAndView("jsonView");
			
			String[] Selection = req.getParameterValues("Selection");
			logger.debug("Selection===>"+Selection[0]);

			mileageService.deleteAward(Selection);
			
			mav.addObject("msg", "success");
			return mav;
		}
	
	
	// 4. 우수 제보자 엑셀 다운로드 기능 생성
		@RequestMapping("/excellenceIfrm/excelDownloadInformerList.do")
		public String exselDownload(Model model,@ModelAttribute("AwardVO") AwardVO paramVO) throws Exception {
			logger.debug("★★★"+paramVO);
			List<AwardVO> awardInformerList = mileageService.getAwardInformerList2(paramVO);
			
			model.addAttribute("fileName", "우수 제보자 수상자선정.xls");
			model.addAttribute("columnTitles", new String[]{
					"순위"
					,"ID"
					,"이름"
					,"소속"
					,"연락처"
					,"제보건수" 
					,"제보점수"
					,"주요제보건수"
					,"주요제보점수"
					,"전월건수"
					,"전월점수"
					,"총점"
			});
			model.addAttribute("columnNames", new String[]{
					"RNUM",
					"ACT_ID",
					"INFORMER_NAME",
					"ORG_NAME",
					"PHONE_CELL",
					"MON_CNT",
					"RPT_GRADE",
					"MAIN_CNT",
					"MAIN_GRADE",
					"ADD_CNT",
					"ADD_GRADE",
					"ALL_RANK",
			});
			model.addAttribute("exportData", awardInformerList);
			
			return "hssfView";
		}
		
	
		
	// 4-1. 우수 제보자 > 수상자 조회 엑셀 다운로드 기능 
		@RequestMapping("/excellenceIfrm/excelDownloadUserList.do")
		public String exselDownloadUserList(Model model,@ModelAttribute("AwardVO") AwardVO paramVO) throws Exception {
			List awardInformerList = mileageService.selectUserAwardList2(paramVO);
			
			model.addAttribute("fileName", "우수 제보자 수상자.xls");
			model.addAttribute("columnTitles", new String[]{
					"방송국",
					"소속", 
					"ID", 
					"이름",
					"전화번호",
					"시상종류", 
					"시상명", 
					"총점", 
					"등록일"  
			});
			model.addAttribute("columnNames", new String[]{
					"REGION_NM",
					"ORG_NAME",
					"ACT_ID",
					"INFORMER_NAME",
					"PHONE_CELL",
					"AW_TYPE",
					"AW_NAME",
					"ALL_RANK",
					"REG_DATE"
			});
			model.addAttribute("exportData", awardInformerList);
			
			return "hssfView";
		}
	
	
	////////////////////////////////////////////////////////////////////////////////////
	/*     < 최고 통신원 >     */
	////////////////////////////////////////////////////////////////////////////////////
		
	// 1. 최고 통신원 최초 이동 ( + 검색 옵션 셋팅 등 )
		@RequestMapping(value= {"/informer/bestIfrm/bestIfrmMain.do"})
		public ModelAndView bestMain(HttpServletRequest request) throws Exception {
			logger.debug("▶▶ 최고 통신원 메뉴 클릭");
				
				url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
				ModelAndView mav = new ModelAndView(url);
				
				// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
				UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
				
				OptAreaVo avo = new OptAreaVo();
				if(!(nlVo.getAuthCode().equals("999"))) {
					avo.setAreaCode(nlVo.getRegionId()); // 관리자는 제외하고 
				}

				mav.addObject("informerTypeList", infrmOptService.selectInft1(new OptInftVo()));
				mav.addObject("informerRegionList", areaOptService.selectAreaOpt1(avo));
				mav.addObject("informerAreaList", this.areaOptService.selectAreaOpt1(avo));
				mav.addObject("login", nlVo);
				mav.setViewName("/bestIfrm/bestIfrmMain");
				
				return mav;
			}
			
			
		// 2. 최고 통신원 이동 및 검색
			@RequestMapping(value = "/bestIfrm/bestIfrmList.do")
			public ModelAndView bestList (HttpServletRequest request, Model model,
				@ModelAttribute("MileageVO") MileageVO MileageVO) throws Exception {
				logger.debug("▶▶ 최고 통신원으로 이동");
				ModelAndView mav = new ModelAndView("/bestIfrm/bestIfrmList");// ModelAndView 객체 생성

				/*url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];*/ // 이동할 페이지 url 추출
				
				// 목록 불러오기 1
				// 통신원 아이디, 통신원 이름, 통신원 소속 등 기본 정보 가져오기
				List<MileageVO> bestIfrmList = mileageService.bestIfrmList(MileageVO); 

				mav.addObject("bestIfrmList", bestIfrmList);
				mav.addObject("bestIfrmListCount", bestIfrmList.size());
				
				return mav; // ModelAndView 객체 반환
			}
			
			
			// 3. 최고 통신원 엑셀 다운로드 기능 생성
}
