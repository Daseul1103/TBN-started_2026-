package kr.co.wizbrain.tbn.statistic.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import javax.xml.registry.infomodel.*;import org.apache.http.impl.conn.LoggingSessionInputBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import kr.co.wizbrain.tbn.award.vo.AwardVO;
import kr.co.wizbrain.tbn.comm.BaseController;
import kr.co.wizbrain.tbn.comm.ParamsDto;
import kr.co.wizbrain.tbn.comm.RecordDto;
import kr.co.wizbrain.tbn.infrm.service.InfrmService;
import kr.co.wizbrain.tbn.infrm.vo.InfrmVO;
import kr.co.wizbrain.tbn.option.service.AreaOptService;
import kr.co.wizbrain.tbn.option.service.InfrmOptService;
import kr.co.wizbrain.tbn.option.service.RptOptService;
import kr.co.wizbrain.tbn.option.service.RttOptService;
import kr.co.wizbrain.tbn.option.vo.OptAreaVo;
import kr.co.wizbrain.tbn.option.vo.OptInftVo;
import kr.co.wizbrain.tbn.receipt.vo.popup.ReceiptSearchVO;
import kr.co.wizbrain.tbn.statistic.service.StatisticService;
import kr.co.wizbrain.tbn.user.vo.UserVO;

/**
 * 사용자 컨트롤러 클래스
 * @author  정다빈
 * @since 2020.07.23
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 *
 *   수정일            수정자              수정내용
 *  -------    -------- ---------------------------
 *  2021.11.   정다빈     최초 생성
 *  2024.10.24 정다빈     항목별 index 부여 및 개편
 */

@Controller
public class StatisticController extends BaseController{
	
	public static final Logger logger = LoggerFactory.getLogger(StatisticController.class);
	
	@Resource(name="statisticService")
	private StatisticService statisticService;

	//지역
	@Resource(name="areaOptService")
	private AreaOptService areaOptService;
	//제보유형
	@Resource(name="rptOptService")
	private RptOptService rptOptService;
	//통신원유형
	@Resource(name="infrmOptService")
	private InfrmOptService infrmOptService;
	//제보수단
	@Resource(name="rttOptService")
	private RttOptService rttOptService;
  
	@Resource(name = "infrmService")
  	private InfrmService infrmService;
	  
	public String url="";
	public boolean isClose = false;
	
	//주소에 맞게 매핑
	@RequestMapping(value="/stats/*.do")
	public String StatisticUrlMapping(HttpSession httpSession, HttpServletRequest request,Model model) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.Statistic 최초 컨트롤러 진입 httpSession : "+httpSession);
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURL() : "+request.getRequestURL());
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURI() : "+request.getRequestURI());
		logger.debug("▶▶▶▶▶▶▶.request.getContextPath() : "+request.getContextPath());
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		logger.debug("▶▶▶▶▶▶▶.보내려는 url : "+url);
		return url;
	}
	
	/** 표준화통계
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/stats/standard.do")
	public ModelAndView standardMain(Model model,HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("/stats/standardMain");  
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
		//지역관련
		OptAreaVo avo = new OptAreaVo();
		List<OptInftVo> t2List = new ArrayList<OptInftVo>();//기관
		OptInftVo iTypeVo = new OptInftVo();
		
		//관리자 제외 지역코드 삽입
		if(!(nlVo.getAuthCode().equals("999"))) {//999 관리자 권한
			avo.setAreaCode(nlVo.getRegionId());
		}
		iTypeVo.setIfmId1("0");
		/*iTypeVo.setAreaCode(nlVo.getRegionId());*/
		
		// 관리자인 경우 부산 교통 방송이 먼저 선택되어 있기 때문에 부산 교통방송으로 설정
		if(nlVo.getAuthCode().equals("999")) {
			iTypeVo.setAreaCode("051");
		} else { // 관리자가 아닌 경우 해당 지역으로 설정하기
			iTypeVo.setAreaCode(nlVo.getRegionId());
		}
		
		
		t2List=infrmOptService.selectInft2(iTypeVo);//기관
		
		mv.addObject("informerRegionList", areaOptService.selectAreaOpt1(avo));
		mv.addObject("informerOrgList",t2List);//기관
		return mv;
	}

	
	
	// 25-01-06 : 방송국 선택 변경 시 > 통신원 소속별 일자별 통계의 소속 변경 ajax
	@RequestMapping("/stats/orgChange.ajax")
	public ModelAndView orgChange(@RequestParam("areaCode") String areaCode ,Model model, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		OptInftVo iTypeVo = new OptInftVo();
		List<OptInftVo> t2List = new ArrayList<OptInftVo>();
		
		iTypeVo.setIfmId1("0");
		iTypeVo.setAreaCode(areaCode); // 받아온 지역 코드 값으로 쿼리 조회하기
		t2List=infrmOptService.selectInft2(iTypeVo);
		
		mav.addObject("informerOrgList",t2List);
		return mav;
	}
	
	/**1. 교통정보 제공대장
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/stats/receiptBroad.do")
	public String receiptBroad(HttpServletRequest request,Model model) throws Exception {
		
		ParamsDto params = getParams(true);
		try {
			logger.debug("제공대장 city : "+params.getString("city"));
			
			logger.debug("request.getCharacterEncoding() = " + request.getCharacterEncoding());
			logger.debug("city raw = " + request.getParameter("city"));
			
			List titleMain = new ArrayList();
			List headMain = new ArrayList();
			List sumMain = new ArrayList();
			List sumOurOtherMain = new ArrayList();
			List dataMain = new ArrayList();
			
			//제보자별 현황
			List headList = statisticService.selectInfrm();	//1.1.0  통신원 유형 title
			List sumList = statisticService.informerTypeAll(params);	//1.1.1 통신원 별 전체건수
			List sumOurOtherList = statisticService.informerTypeOurOther(params);	//1.1.2 통신원별 자국/타국 전체건수
			List dataList = statisticService.informerTypeData(params);	//1.1.3 통신원 유형별 상세 데이터
			
			titleMain.add(0,"제보자별 현황");
			headMain.add(0, headList);
			sumMain.add(0, sumList);
			sumOurOtherMain.add(0, sumOurOtherList);
			dataMain.add(0, dataList);
			
			//제보수단별 현황
			headList = statisticService.selectRtt();	//1.2.0 - 제보수단 title
			sumList = statisticService.reportmeanTypeAll(params);	//1.2.1 제보수단별 전체건수
			sumOurOtherList = statisticService.reportmeanTypeOurOther(params);	//1.2.2 제보수단별 자국/타국 건수
			dataList = statisticService.reportmeanTypeData(params);	//1.2.3 제보수단별 상세 데이터
			
			titleMain.add(1,"제보수단별 현황");
			headMain.add(1, headList);
			sumMain.add(1, sumList);
			sumOurOtherMain.add(1, sumOurOtherList);
			dataMain.add(1, dataList);
			
			//제보유형별 현황
			headList = statisticService.selectRpt();	//제보유형 title
			sumList = statisticService.reportTypeAll(params);	//제보유형별 건수
			sumOurOtherList = statisticService.reportTypeOurOther(params);	//제보유형별 자국/타국 건수
			dataList = statisticService.reportTypeData(params);	//제보유형별 상세 데이터
			
			titleMain.add(2,"제보유형별 현황");
			headMain.add(2, headList);
			sumMain.add(2, sumList);
			sumOurOtherMain.add(2, sumOurOtherList);
			dataMain.add(2, dataList);
			
			model.addAttribute("mapping", "standardInformerType");
			model.addAttribute("fileName", "교통정보 제공대장"+params.getString("city")+".xls");
			
			model.addAttribute("titleMain", titleMain);
			model.addAttribute("headMain", headMain);
			model.addAttribute("sumMain", sumMain);
			model.addAttribute("sumOurOtherMain", sumOurOtherMain);
			model.addAttribute("dataMain", dataMain);
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}

	
	// 교통 정보 제공 대장 - 시간별
	@RequestMapping("stats/receiptBroadTime.do")
	public String receiptBroadTime (HttpServletRequest request,Model model) throws Exception {
		ParamsDto params = getParams(true);
		try {
			List timeData = new ArrayList();
			List ifrmData = new ArrayList();
			List rptTData = new ArrayList();
			List rptMData = new ArrayList();
			List ifrmDatatype = new ArrayList();
			List rptTDatatype = new ArrayList();
			List rptMDatatype = new ArrayList();	
			
			// 시간대별 데이터 가져오기
			timeData = statisticService.timeBroadData(params);
			// 제보자별 현황
			ifrmData = statisticService.informerTypeAll(params);;
			// 제보유형 현황
			rptTData = statisticService.reportTypeAll(params);
			// 제보수단별 현황
			rptMData = statisticService.reportmeanTypeAll(params);
			// 제보자별 현황
			ifrmDatatype = statisticService.selectInfrm();
			// 제보유형 현황
			rptTDatatype = statisticService.selectRpt();
			// 제보수단별 현황
			rptMDatatype =  statisticService.selectRtt();
			
			model.addAttribute("mapping", "standardInformerTypeTime");
			model.addAttribute("fileName", "교통정보 제공대장(시간대 별)"+params.getString("city")+".xls");
			model.addAttribute("titleName", "교통정보 제공대장"+params.getString("city"));
			model.addAttribute("start_date", params.get("start_date"));
			model.addAttribute("end_date", params.get("end_date"));
			
			model.addAttribute("timeData", timeData);
			model.addAttribute("ifrmData", ifrmData);
			model.addAttribute("rptTData", rptTData);
			model.addAttribute("rptMData", rptMData);
			model.addAttribute("ifrmDatatype", ifrmDatatype);
			model.addAttribute("rptTDatatype", rptTDatatype);
			model.addAttribute("rptMDatatype", rptMDatatype);
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}
	
	
	
	/*2. 긴급교통정보_방송현황분석 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("stats/extrBro.do")
	public String extrBro(Model model,HttpServletRequest request) throws Exception {
		
		ParamsDto params = getParams(true);
		try {
			List Data = statisticService.extrBro(params);//2. 긴급교통정보_방송현황분석
			
			model.addAttribute("mapping", "extrBro");
			model.addAttribute("fileName", "긴급교통정보_방송현황분석"+params.getString("city")+".xls");
			model.addAttribute("titleName", "긴급교통정보 처리건수 실적"+params.getString("city"));
			model.addAttribute("sheetNames1", "긴급교통정보 처리건수 실적");
			model.addAttribute("Data", Data);
			model.addAttribute("start_date", params.get("start_date"));
			model.addAttribute("end_date", params.get("end_date"));
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}
	/*3. 재난 제보건수
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("stats/disastorStat.do")
	public String disastorStat(Model model,HttpServletRequest request) throws Exception {
		
		ParamsDto params = getParams(true);
		try {
			List Data = statisticService.disastorStat(params);	//무 제보자 통신원 목록
			
			model.addAttribute("mapping", "disastorStat");
			model.addAttribute("fileName", "재난 제보건수"+params.getString("city")+".xls");
			model.addAttribute("titleName", "재난 제보건수"+params.getString("city"));
			model.addAttribute("sheetNames1", "재난 제보건수");
			model.addAttribute("Data", Data);
			model.addAttribute("start_date", params.get("start_date"));
			model.addAttribute("end_date", params.get("end_date"));
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}
	
	/*4. 월별 제보자별 제보건수
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("stats/muInformer.do")
	public String muJebo(Model model,HttpServletRequest request) throws Exception {
		
		ParamsDto params = getParams(true);
		/*int monthCnt = Integer.parseInt(params.getString("start_date").substring(4,6));
		List<Integer> monList = new ArrayList<>();
		//1월부터 해당월까지의 무제보자
		for (int i = 0; i < monthCnt; i++) {
			monList.add(i);
		}
		params.add("monList", monList);*/
		try {
			List headList = statisticService.monInfrmList(params);	//제보자 리스트
			List Data = statisticService.monInfrmCnt(params);	//월별 건수
			
			model.addAttribute("mapping", "muJebo");
			model.addAttribute("fileName", "월별 제보자별 제보건수("+params.getString("start_date")+") "+params.getString("city")+".xls");
			model.addAttribute("titleName", "월별 제보자별 제보건수("+params.getString("start_date")+") "+params.getString("city"));
			model.addAttribute("sheetNames1", "월별 제보자별 제보건수");
			model.addAttribute("headList", headList);
			model.addAttribute("Data", Data);
			model.addAttribute("start_date", params.get("start_date"));
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}
	
	/*03. 무 제보자 현황
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("stats/muInformer2.do")
	public String muJebo2(Model model,HttpServletRequest request) throws Exception {
		
		ParamsDto params = getParams(true);
		/*int monthCnt = Integer.parseInt(params.getString("start_date").substring(4,6));
		List<Integer> monList = new ArrayList<>();
		//1월부터 해당월까지의 무제보자
		for (int i = 0; i < monthCnt; i++) {
			monList.add(i);
		}
		params.add("monList", monList);*/
		try {
			List headList = statisticService.muJeboList(params);	//무 제보자 통신원 목록
			List Data = statisticService.muJeboList(params);	//무 제보자 통신원 목록
			
			model.addAttribute("mapping", "muJebo2");
			model.addAttribute("fileName", "무 제보자 현황("+params.getString("end_date")+") "+params.getString("city")+".xls");
			model.addAttribute("titleName", "무 제보자 현황("+params.getString("end_date")+") "+params.getString("city"));
			model.addAttribute("sheetNames1", "무 제보자 현황");
			model.addAttribute("headList", headList);
			model.addAttribute("Data", Data);
			model.addAttribute("start_date", params.get("start_date"));
			model.addAttribute("start_date", params.get("end_date"));
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}
	
	/*4. 교통통신원 제보건수 및 접수직원 가공건수
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/stats/receiptInformer.do")
	public String receiptInformer(HttpServletRequest request,Model model) throws Exception {
		ParamsDto params = getParams(true);
		try {
			List sheetNames = new ArrayList();
			List monthReceiptMain = new ArrayList();
			List monthOurReceiptMain = new ArrayList();
			List monthOtherReceiptMain = new ArrayList();
			List monthInformerMain = new ArrayList();
			List monthInformer1InformMain = new ArrayList();
			
			List informerType = statisticService.selectInfrm();	//제보자 유형 title
			
			List nullCatch = new ArrayList();
			
			for(int i=0; i<informerType.size(); i++){
				RecordDto record = (RecordDto) informerType.get(i);
				params.remove("informer_type");
				params.add("informer_type",record.get("CODE"));
				List monthReceipt = statisticService.monthReceipt(params);		//월별 전체 수집건수
				List monthOurReceipt = statisticService.monthOurReceipt(params); //월별 자국 수집건수
				List monthOtherReceipt = statisticService.monthOtherReceipt(params); //월별 타국 수집건수
				List monthInformer = statisticService.monthInformer(params); //월별 제보자 수
				List monthInformer1Inform = statisticService.monthInformer1Inform(params); //월별 1건이상 제보자 수
				
				
				if (monthReceipt == null || monthReceipt.isEmpty()) {
					RecordDto defaultData = new RecordDto();
					defaultData.put("CNT", 0);
					defaultData.put("STAT_MONTH", params.get("start_date"));  // thDateTime에 맞는 값을 넣어주세요
	
					// 리스트에 추가
					monthReceipt = new ArrayList<>();
					monthReceipt.add(defaultData);
				} else {
					
				}
				
				if (monthOurReceipt == null || monthOurReceipt.isEmpty()) {
					RecordDto defaultData = new RecordDto();
					defaultData.put("CNT", 0);
					defaultData.put("STAT_MONTH", params.get("start_date"));  // thDateTime에 맞는 값을 넣어주세요
	
					// 리스트에 추가
					monthOurReceipt = new ArrayList<>();
					monthOurReceipt.add(defaultData);
				}
				
				if (monthOtherReceipt == null || monthOtherReceipt.isEmpty()) {
					RecordDto defaultData = new RecordDto();
					defaultData.put("CNT", 0);
					defaultData.put("STAT_MONTH", params.get("start_date"));  // thDateTime에 맞는 값을 넣어주세요
	
					// 리스트에 추가
					monthOtherReceipt = new ArrayList<>();
					monthOtherReceipt.add(defaultData);
				}
				
				sheetNames.add(i, record.get("CODE_NAME"));
				monthReceiptMain.add(i, monthReceipt);
				monthOurReceiptMain.add(i, monthOurReceipt);
				monthOtherReceiptMain.add(i, monthOtherReceipt);
				monthInformerMain.add(i, monthInformer);
				monthInformer1InformMain.add(i, monthInformer1Inform);
			}
	
			model.addAttribute("mapping", "standardInformUse");
			model.addAttribute("fileName", "교통통신원 제보건수 및 접수직원 가공건수"+params.getString("city")+".xls");
			model.addAttribute("sheetNames", sheetNames);
			
			model.addAttribute("monthReceipt", monthReceiptMain);
			model.addAttribute("monthOurReceiptMain", monthOurReceiptMain);
			model.addAttribute("monthOtherReceiptMain", monthOtherReceiptMain);
			model.addAttribute("monthInformerMain", monthInformerMain);
			model.addAttribute("monthInformer1InformMain", monthInformer1InformMain);
			model.addAttribute("start_date", params.get("start_date"));
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}
	

	/*5. 교통정보 수집건수 및 활용실적
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/stats/receiptUse.do")
	public String receiptUse(Model model,HttpServletRequest request) throws Exception {
		ParamsDto params = getParams(true);
		try {
			List headList = statisticService.selectInfrm();	//제보자 유형 title
			List useAllList = statisticService.informerTypeAll(params);	//제보자 유형별 건수
			List useOurOther = statisticService.informerTypeOurOther(params);	//제보자 유형별 자국/타국 건수
			List useDaily = statisticService.receiptUseDaily(params);	//일별 상세
			
			List regionHead = statisticService.selectInfrm();	//방송국 title
			List sumMonth = statisticService.monthSendYNA07A08(params);	// 월별 합계
			List Data = statisticService.dailyRegionSendYNA07A08(params);	// 일별 방송국별 데이터
			
			int eSize = headList.size(); // 통신원 유형 크기
			
			model.addAttribute("mapping", "standartdReceiptUse");
			model.addAttribute("fileName", "교통정보 수집건수 및 활용실적"+params.getString("city")+".xls");
			
			model.addAttribute("sheetNames1", "1) 수집건수");
			model.addAttribute("headList", headList);
			model.addAttribute("useAllList", useAllList);
			model.addAttribute("useOurOther", useOurOther);
			model.addAttribute("useDaily", useDaily);
			model.addAttribute("ourRegion", params.get("session_user_region"));
			
			model.addAttribute("sheetNames2", "2) 활용실적");
			model.addAttribute("regionHead", regionHead);
			model.addAttribute("sumMonth", sumMonth);
		//	model.addAttribute("sumDaily", sumDaily);
			model.addAttribute("Data", Data);
			model.addAttribute("start_date", params.get("start_date"));
			model.addAttribute("end_date", params.get("end_date"));
			model.addAttribute("eSize",eSize);
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}

	//24-11-19 : 제보자별 제보현황 컨트롤러
	@RequestMapping("/stats/informerReceipt.do")
	public String informerReport(Model model, HttpServletRequest request) throws Exception {
	    ParamsDto params = getParams(true);

	    try {
	        List dataList = statisticService.informerReport(params);
	        int allInformer = dataList.size();

	        model.addAttribute("mapping", "informerReport");
	        model.addAttribute("fileName", "제보자별 제보현황" + params.getString("city") + ".xls");

	        model.addAttribute("sheetNames1", "제보자별 제보현황");
	        model.addAttribute("dataList", dataList);
	        model.addAttribute("start_date", params.get("start_date"));
	        model.addAttribute("end_date", params.get("end_date"));
	        model.addAttribute("allInformer", allInformer);

	        return "hssfExcel";
	    } catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}
	
	
	
	// 25-02-27 : 제보자별 제보현황 (연간) 컨트롤러 
	@RequestMapping("/stats/yearReceipt.do")
	public String yearReceipt (Model model, HttpServletRequest request) throws Exception {
		ParamsDto params = getParams(true);
		try {
			// 시상관리 % 가져오기
			List<AwardVO> perList = statisticService.perList(params);
	
			// 최대 값(total) 가져오기
			List totalList = statisticService.totalList(params);
					
			List dataList = statisticService.yearReceipt(params); // 엑셀에 사용할 데이터 리스트 가져오기
			
			int allInformer = dataList.size(); // 총 인원수 구해오기 
			// 총 건수 구해오기
			
			model.addAttribute("mapping", "yearReceipt");
			model.addAttribute("fileName", "연간 제보자별 제보현황(" + params.getString("city") + ".xls");
			
			model.addAttribute("sheetName1","연간 제보자별 제보현황");
			model.addAttribute("dataList", dataList);
			model.addAttribute("start_date", params.get("start_date"));
			model.addAttribute("end_date", params.get("end_date"));
			model.addAttribute("allInformer",allInformer);
			model.addAttribute("perList",perList);
			model.addAttribute("totalList",totalList);
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}
	
	
	// 25-02-27 : 연간 지역소속별 통계(수상 관련)
	@RequestMapping("stats/yearOrgStat.do")
	public String yearOrgStat (Model model, HttpServletRequest request) throws Exception {
		ParamsDto params = getParams(true);
		try {
			// 시상관리 % 가져오기
			List<AwardVO> perList = statisticService.perList(params);
					
			List dataList = statisticService.yearOrgStat(params); // 엑셀에 사용할 데이터 리스트 가져오기
			
			// 최대 값(total) 가져오기
			List totalList = statisticService.totalListOrg(params);
					
			int allInformer = dataList.size(); // 총 인원수 구해오기 
			// 총 건수 구해오기
			
			model.addAttribute("mapping", "yearOrgStat");
			model.addAttribute("fileName", "연간 지역소속별 통계(" + params.getString("city") + ".xls");
			
			model.addAttribute("sheetName1","연간 지역소속별 통계");
			model.addAttribute("dataList", dataList);
			model.addAttribute("start_date", params.get("start_date"));
			model.addAttribute("end_date", params.get("end_date"));
			model.addAttribute("allInformer",allInformer);
			model.addAttribute("perList",perList);
			model.addAttribute("totalList",totalList);
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}
	
	
	
	
	/**전국통계: 돌발 교통정보 표출실적
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/stats/incidentStats.do")
	public String incidentStats(Model model) throws Exception {
		ParamsDto params = getParams(true);
		try {
			int monthCnt = Integer.parseInt(params.getString("start_date").substring(4,6));
			List<Integer> monList = new ArrayList<>();
			
			for (int i = 0; i < monthCnt; i++) {
				monList.add(i);
			}
			params.add("monList", monList);
			List Data = statisticService.nationalIncident(params);	
			
			model.addAttribute("mapping", "nationalIncident");
			model.addAttribute("fileName", "돌발 교통정보 제공실적"+params.getString("city")+".xls");
			model.addAttribute("titleName", params.getString("start_date").substring(0,4)+"년 돌발교통정보 수집건수"+params.getString("city"));
			model.addAttribute("sheetNames1", "돌발교통정보 수집건수");
			model.addAttribute("Data", Data);
			model.addAttribute("start_date", params.get("start_date"));
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}
	
	/** 7. 교통정보 수집원 현황
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/stats/informerStats.do")
	public String informerStats(Model model) throws Exception {
		ParamsDto params = getParams(true);
		try {
			int monthCnt = Integer.parseInt(params.getString("start_date").substring(4,6));
			
			List Data = statisticService.informerStats(params);	//교통정보 수집원 현황
			
			model.addAttribute("mapping", "standardInformer");
			model.addAttribute("fileName", "교통정보 수집원 현황"+params.getString("city")+".xls");
			model.addAttribute("titleName", "교통정보 수집원 현황"+params.getString("city"));
			model.addAttribute("sheetNames1", "교통정보 수집원 현황");
			model.addAttribute("Data", Data);
			model.addAttribute("start_date", params.get("start_date"));
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}
	
	/*8. 한국가스기술공사 제보실적
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("stats/krGas.do")
	public String krGas(Model model,HttpServletRequest request) throws Exception {
		ParamsDto params = getParams(true);
		try {
			List Data = statisticService.krGasMonCnt(params);	//제보자 유형별 건수
			
			model.addAttribute("mapping", "korLx");
			model.addAttribute("fileName", "한국가스기술공사 제보실적"+params.getString("city")+".xls");
			model.addAttribute("titleName", "한국가스기술공사 통신원 제보실적"+params.getString("city"));
			model.addAttribute("sheetNames1", "가스기술공사 제보실적");
			model.addAttribute("Data", Data);
			model.addAttribute("start_date", params.get("start_date"));
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
		
	}
	
	/*9. 한국국토정공사 제보현황
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("stats/korLx.do")
	public String korLx(Model model,HttpServletRequest request) throws Exception {
		
		ParamsDto params = getParams(true);
		try {
			List Data = statisticService.korLxMonCnt(params);	//제보자 유형별 건수
			
			model.addAttribute("mapping", "korLx");
			model.addAttribute("fileName", "한국국토정보공사 제보현황"+params.getString("city")+".xls");
			model.addAttribute("titleName", "한국국토정보공사 통신원 제보현황"+params.getString("city"));
			model.addAttribute("sheetNames1", "국토정보공사 제보현황");
			model.addAttribute("Data", Data);
			model.addAttribute("start_date", params.get("start_date"));
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}

	/* 통신원 소속별 일자별 통계 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("stats/dayReceipt.do")
	public String dayReceipt(Model model,HttpServletRequest request) throws Exception {
		
		ParamsDto params = getParams(true);
		try {
			List sheetNames = new ArrayList();//기관별 시트명
			List informerListMain =new ArrayList(); //왼쪽 통신원부 
			List cntListMain =new ArrayList(); //오른쪽 건수
			
			List dayReceiptList = new ArrayList();
			
			dayReceiptList = statisticService.dayReceipt(params);
			
			RecordDto record = (RecordDto) dayReceiptList.get(0);
			model.addAttribute("mapping", "dayReceipt");
			model.addAttribute("fileName", record.get("ORG_NAME")+" 통신원 일자별 제보건수"+params.getString("city")+".xls");
			model.addAttribute("titleName", record.get("ORG_NAME")+" 통신원 일자별 제보건수"+params.getString("city")+".xls");
			model.addAttribute("sheetNames1", "일자별 제보건수");
			//해당 부분은 위 부분과 대조하여 검토
			model.addAttribute("dayReceiptList", dayReceiptList);
			model.addAttribute("start_date", params.get("start_date"));
			model.addAttribute("org_id", params.get("org_id"));
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}

	/* 통신원 중소 분류별 통계
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("stats/orgOrgSub.do")
	public String orgOrgSub(Model model,HttpServletRequest request) throws Exception {
		
		ParamsDto params = getParams(true);
		//문자열        
		List eraList =  statisticService.statDateCal(params); 
		params.add("chkArr", eraList);
		List sheetNames = new ArrayList();//기관별 시트명
		List informerListMain =new ArrayList(); //왼쪽 통신원부 
		List cntListMain =new ArrayList(); //오른쪽 건수
		
		List orgOrgSubList = new ArrayList();
		
		orgOrgSubList = statisticService.orgOrgSub(params);
		
		RecordDto record = (RecordDto) orgOrgSubList.get(0);
		model.addAttribute("mapping", "orgOrgSub");
		model.addAttribute("fileName", "통신원 중소 분류별 통계"+params.getString("city")+".xls");
		model.addAttribute("titleName", "통신원 중소 분류별 통계"+params.getString("city")+".xls");
		model.addAttribute("sheetNames1", "통신원 중소 분류별 통계");
		//해당 부분은 위 부분과 대조하여 검토
		model.addAttribute("orgOrgSub", orgOrgSubList);
		model.addAttribute("eraList", eraList);
		model.addAttribute("start_date", params.get("start_date"));
		model.addAttribute("org_id", params.get("org_id"));
		return "hssfExcel";
	}

	/* 사회봉사자 일자별 통계
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("stats/volunteer.do")
	public String volunteer(Model model,HttpServletRequest request) throws Exception {
		
		ParamsDto params = getParams(true);
		
		List sheetNames = new ArrayList();//기관별 시트명
		List informerListMain =new ArrayList(); //왼쪽 통신원부 
		List cntListMain =new ArrayList(); //오른쪽 건수
		
		List vltList = new ArrayList();
		//flagService
		vltList = statisticService.volunteer(params);
		
		//List orgType = statisticService.orgType(params);//지역별 통신원 하부 기관 조회
		
		/*RecordDto record = (RecordDto) vltList.get(0);*/
		model.addAttribute("mapping", "volunteer");
		model.addAttribute("fileName", "사회봉사자 일자별 통계"+params.getString("city")+".xls");
		model.addAttribute("titleName", "사회봉사자 일자별 통계"+params.getString("city")+".xls");
		model.addAttribute("sheetNames1", "일자별 제보건수");
		//해당 부분은 위 부분과 대조하여 검토
		model.addAttribute("vltList", vltList);
		model.addAttribute("start_date", params.get("start_date"));
		model.addAttribute("end_date", params.get("end_date"));
		model.addAttribute("org_id", params.get("org_id"));
		
		return "hssfExcel";
	}
	
	//금일접수현황 엑셀 다운로드
	@RequestMapping("/stats/receiptDownToday.ajax")
	public String receiptDownToday(Model model,HttpServletRequest request,ReceiptSearchVO searchVO) throws Exception {
		logger.info("------------------금일접수현황 엑셀 다운로드------------------");
		ParamsDto params = getParams(true);
		try {
			if(searchVO.getFLAG_IMPORTANT()==null){
				searchVO.setFLAG_IMPORTANT("");
			}
			List Data = statisticService.searchStatusListToday(searchVO);
			
			model.addAttribute("mapping", "receiptDown");
			model.addAttribute("fileName", "제보접수통계("+searchVO.getSTART_DAY()+"~"+searchVO.getEND_DAY()+")"+".xls");
			model.addAttribute("titleName", "제보접수통계("+searchVO.getSTART_DAY()+"~"+searchVO.getEND_DAY()+")");
			model.addAttribute("sheetNames1", "제보접수통계");
			model.addAttribute("Data", Data);
			//model.addAttribute("start_date", params.get("start_date"));
			
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}
	
	//전체접수현황 엑셀 다운로드
	@RequestMapping("/stats/receiptDown.ajax")
	public String receiptDown(Model model,HttpServletRequest request,ReceiptSearchVO searchVO) throws Exception {
		logger.info("------------------전체접수현황 엑셀 다운로드------------------");
		ParamsDto params = getParams(true);
		try {
			if(searchVO.getFLAG_IMPORTANT()==null){
				searchVO.setFLAG_IMPORTANT("");
			}
			List Data = statisticService.searchStatusList(searchVO);
			
			model.addAttribute("mapping", "receiptDown");
			model.addAttribute("fileName", "제보접수통계("+searchVO.getSTART_DAY()+"~"+searchVO.getEND_DAY()+")"+".xls");
			model.addAttribute("titleName", "제보접수통계("+searchVO.getSTART_DAY()+"~"+searchVO.getEND_DAY()+")");
			model.addAttribute("sheetNames1", "제보접수통계");
			model.addAttribute("Data", Data);
			//model.addAttribute("start_date", params.get("start_date"));
			return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	}
	
	
		// 통신원 관리에서 엑셀 다운로드
	  @RequestMapping({"/stats/informerDown.ajax"})
	  public String informerDown(Model model, HttpServletRequest request, InfrmVO searchVO, @RequestParam(value = "selectedCols", required = false) List<String> selectedCols) throws Exception {
	    ParamsDto params = getParams(true);
	    System.out.println("resultVO: " + searchVO.toString());
	    UserVO nlVo = (UserVO)request.getSession().getAttribute("login");
	    //검색조건에 맞게 나오도록 조정(스크롤 조건 아니고)
	    searchVO.setDownFlag("1");
	    /*if (!nlVo.getAuthCode().equals("999"))*/
	    /*searchVO.setAreaCode(nlVo.getRegionId());*/
	    
	    List<InfrmVO> Data = new ArrayList<>();
	    model.addAttribute("mapping", "informerDown");

	    Data = this.infrmService.selectInfrmList(searchVO,selectedCols);
	    model.addAttribute("fileName", "통신원 목록.xls");
	    model.addAttribute("titleName", "통신원 목록");
	    model.addAttribute("sheetNames1", "통신원 목록");
	    model.addAttribute("selectData", selectedCols);
	    model.addAttribute("Data", Data);
	    return "hssfExcel";
	  }
	  
	  
	  // 통신원 관리에서 주소 라벨 출력
	  @RequestMapping({"/stat/addDownload.do"})
	  public String addDownload(Model model, HttpServletRequest request,@ModelAttribute("InfrmVO") InfrmVO searchVO) throws Exception {
		  try{
			  ModelAndView mv = new ModelAndView("jsonView");  
			  String[] Selection = request.getParameterValues("Selection");
			  String[] labelT = request.getParameterValues("labelType");
			  
			  List<InfrmVO> alist = new ArrayList<>();
			  
			  for (int i = 0; i < Selection.length; i++) { //ID 값 나누기
				  	InfrmVO awvo = new InfrmVO();
					awvo.setInformerId(Selection[i]);
					alist.add(awvo);
			  }                      
			  
			  // ID 값 나눈 배열로 데이터 가져오기
			  List<InfrmVO> dataList = statisticService.addRabel(alist);
			  
			  // 라벨 타입 나누기
			  String labelType = labelT[0];
			  
			  model.addAttribute("mapping", "addDownload");
			  model.addAttribute("fileName", "통신원 주소 라벨 출력("+labelType + ").xls");
			  model.addAttribute("titleName", "통신원 주소 라벨 출력");
			  model.addAttribute("sheetNames1", "주소 라벨 시트");
			  model.addAttribute("labelType", labelType); // 라벨 구분 (16/24)
			  model.addAttribute("dataList", dataList); // 엑셀 다운로드에 사용할 데이터 
			  
			  return "hssfExcel";
		} catch (Exception e) {// try 블록에서 발생한 모든 예외(Exception 및 하위)를 여기서 잡음
	        e.printStackTrace();// 예외 스택트레이스를 표준에러(stderr)로 출력 (보통 톰캣 콘솔/catalina.out 쪽)
	        // 로그 레벨 ERROR로 예외의 문자열만 기록 
	        logger.error(e.toString(),e);  
	        throw e; // 잡은 예외를 다시 던져서(재전파) 상위(Spring 예외처리기 등)에서 처리하게 함
	    }
	  }
}