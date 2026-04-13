package kr.co.wizbrain.tbn.receipt.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kong.unirest.HttpRequest;
import kr.co.wizbrain.tbn.cid.TcpAndWebClient;
import kr.co.wizbrain.tbn.notice.vo.NoticeVO;
import kr.co.wizbrain.tbn.receipt.service.ReceiptService;
import kr.co.wizbrain.tbn.receipt.vo.AreaCodeVO;
import kr.co.wizbrain.tbn.receipt.vo.AreaSubCodeVO;
import kr.co.wizbrain.tbn.receipt.vo.ArteryVO;
import kr.co.wizbrain.tbn.receipt.vo.CriteriaVO;
import kr.co.wizbrain.tbn.receipt.vo.InformerStatVO;
import kr.co.wizbrain.tbn.receipt.vo.InformerVO;
import kr.co.wizbrain.tbn.receipt.vo.MissedCallVO;
import kr.co.wizbrain.tbn.receipt.vo.PickUpCallVO;
import kr.co.wizbrain.tbn.receipt.vo.ReceiptVO;
import kr.co.wizbrain.tbn.receipt.vo.ReceiveCallVO;
import kr.co.wizbrain.tbn.receipt.vo.ReportMeanVO;
import kr.co.wizbrain.tbn.receipt.vo.ReportTypeVO;
import kr.co.wizbrain.tbn.receipt.vo.TempSaveVo;
import kr.co.wizbrain.tbn.receipt.vo.popup.EditVO;
import kr.co.wizbrain.tbn.receipt.vo.popup.ReceiptSearchVO;
import kr.co.wizbrain.tbn.receipt.vo.popup.ReceivedStatusVO;
import kr.co.wizbrain.tbn.user.vo.UserVO;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;


@Controller
public class ReceiptController {
	public static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);
	
	@Resource(name="receiptService")
	private ReceiptService receiptService;
	
	public String url="";
	public boolean isClose = false;
	
	//주소에 맞게 매핑
	@RequestMapping(value="/receipt/*.do")
	public ModelAndView ReceiptUrlMapping(HttpSession httpSession, HttpServletRequest request,Model model) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.Receipt 최초 컨트롤러 진입 httpSession : "+httpSession);
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURL() : "+request.getRequestURL());
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURI() : "+request.getRequestURI());
		logger.debug("▶▶▶▶▶▶▶.request.getContextPath() : "+request.getContextPath());
		
		ModelAndView mv = new ModelAndView();
		
		//selectBox - 제보유형(대)
		List<ReportTypeVO> reportFirstList = receiptService.firstReportType();
		mv.addObject("reportFirstList", reportFirstList);
		
		//selectBox - 지역구분(대)
		List<AreaCodeVO> areaCodeList = receiptService.selectAreaCode();
		mv.addObject("areaCodeList", areaCodeList);
		
		//selectBox - 제보처
		List<ReportMeanVO> reportMeanTypeList = receiptService.selectReportMean();
		mv.addObject("reportMeanTypeList", reportMeanTypeList);
		
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		logger.debug("▶▶▶▶▶▶▶.보내려는 url : "+url);
 		mv.setViewName(url);
		return mv;
	}
	
	//pickup call 테이블에 전화온것이 있을경우 조회
	//실시간 풀링
	@RequestMapping("/receipt/checkIfPickUpInfoExists.ajax")
	public ModelAndView checkIfPickUpInfoExists(PickUpCallVO vo) throws Exception {
		logger.debug("---------------------checkIfPickUpInfoExists---------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		List<PickUpCallVO> list = receiptService.checkIfPickUpInfoExists(vo);
		
		mv.addObject("data", list);
		return mv;
	}
	
	
	//전화온 테이블에셔 전화 받았을 시  
	//받은 데이터를 삭제하고(통신원 목록에 해당 데이터를 띄울시) delete
	//그 데이터는 수신목록에 띄워야 하니까 insert -> 23 01 17 cid에서 처리하도록 수정
	@RequestMapping("/receipt/deletePickUpInfo.ajax")
	public ModelAndView deletePickUpInfo(PickUpCallVO vo) throws Exception {
		logger.debug("---------------------deletePickUpInfo---------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		int reulst = receiptService.deletePickUpInfo(vo);
		//receiptService.insertReceiveCall(vo);
		mv.addObject("data", reulst);
		return mv;
	}
	
	@RequestMapping("/receipt/subscribe.do")
	public void subscribe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.debug("---------------------Subscribing---------------------");
		TcpAndWebClient telClient;
		PrintWriter writer;
		
		telClient = new TcpAndWebClient();
		
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");
		logger.debug("header 생성");
		
		writer = response.getWriter();
		logger.debug("writer생성");
		
		try {
			while(true) {
				String data = telClient.readDataFromTelServer();
				System.out.println("data: " + data);
				writer.write("data: { \"message\" : \"msg : PushEvent from server" + data + "\" }\n\n");
				logger.debug("Success to submit to Fromt: " + data);
			}
		} catch (IOException e) {
			e.printStackTrace();
			writer.close();
		} catch (SQLException e) {
			e.printStackTrace();
			writer.close();
		} finally {
			writer.close();
		}
		
	}
	
//	@RequestMapping("/receipt/test.do")
//	public ModelAndView subscribe(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
//		logger.debug("---------------------Subscribing---------------------");
//		ModelAndView mv = new ModelAndView("/receipt/noResult");
//		
//		response.setContentType("text/event-stream");
//		response.setCharacterEncoding("UTF-8");
//		logger.debug("header 생성");
//		
//		PrintWriter writer = response.getWriter();
//		logger.debug("writer생성");
//		
//		for (int i = 0; i < 2; i++) {
//			writer.write("data: { \"message\" : \"msg : PushEvent from server" + i + "\" }\n\n");
//			logger.debug(i + "번째 데이터전송완료");
//		}
//			
//		writer.close();
//		return mv;
//	}
	//수신전화 목록
	@RequestMapping("/receipt/pickupCallList.do")
	public ModelAndView pickupCallList(HttpServletRequest request,ReceiveCallVO vo) throws Exception {
		//logger.info("------------------pickupCallList진입------------------");
		ModelAndView mv = new ModelAndView("/receipt/pickupCallList");
		
		String AREA_CODE = request.getParameter("AREA_CODE");
		String INTEL_NUM = request.getParameter("INTEL_NUM");
		List<ReceiveCallVO> receivedCallList = receiptService.selectPickupCall(vo);
		mv.addObject("receivedCallList", receivedCallList);
		mv.addObject("receivedCallListSize", receivedCallList.size());

		return mv;
	}
	
	//부재중 전화목록 조회
	@RequestMapping("/receipt/missedCallList.do")
	public ModelAndView missedCallList(HttpServletRequest request) throws Exception {
		//logger.info("------------------missedCallList진입------------------");
		ModelAndView mv = new ModelAndView("/receipt/missedCallList");
		
		String AREA_CODE = request.getParameter("AREA_CODE");
		List<MissedCallVO> missedCallList = receiptService.selectMissedCall(AREA_CODE);
		mv.addObject("missedCallList", missedCallList);
		mv.addObject("missedCallListSize", missedCallList.size());
		
		return mv;
	}
	
	@RequestMapping("/receipt/getNewMissedCallInfo.ajax")
	public ModelAndView getNewMissedCallInfo(HttpServletRequest request) throws Exception {
//		logger.info("------------------getNewMissedCallInfo진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		
//		String AREA_CODE = request.getParameter("AREA_CODE");
//		List<MissedCallVO> missedCallList = receiptService.selectMissedCall(AREA_CODE);
//		mv.addObject("missedCallList", missedCallList);
//		mv.addObject("missedCallListSize", missedCallList.size());

		return mv;
	}
	
	@RequestMapping("/receipt/selectEditVO.do")
	public ModelAndView selectEditVO(String RECEIPT_ID, String RNUM, String UPDATE_FLAG) throws Exception{
		logger.info("------------------selectEditVO진입------------------");
		ModelAndView mv = new ModelAndView("/receipt/editResultList");
		//selectBox - 제보유형(대)
		List<ReportTypeVO> reportFirstList = receiptService.firstReportType();
		mv.addObject("reportFirstList", reportFirstList);
		
		//selectBox - 지역구분(대)
		List<AreaCodeVO> areaCodeList = receiptService.selectAreaCode();
		mv.addObject("areaCodeList", areaCodeList);
		
		//selectBox - 제보처
		List<ReportMeanVO> reportMeanTypeList = receiptService.selectReportMean();
		mv.addObject("reportMeanTypeList", reportMeanTypeList);
		
		EditVO vo = receiptService.selectEditVO(RECEIPT_ID, UPDATE_FLAG);
		vo.setRNUM(RNUM);
		mv.addObject("editVO", vo);
		
		return mv;
	}
	
	@RequestMapping("/receipt/cancleEditVO.do")
	public ModelAndView cancleEditVO(String RECEIPT_ID, String RNUM ,String UPDATE_FLAG) throws Exception{
		logger.info("------------------cancleEditVO진입------------------");
		ModelAndView mv = new ModelAndView("/receipt/showEditResult");
		ReceivedStatusVO vo = receiptService.showEditResult(RECEIPT_ID,UPDATE_FLAG);
		vo.setRNUM(RNUM);
		mv.addObject("editVO", vo);
		return mv;
	}
	
	@RequestMapping("/receipt/updateReceipt.ajax")
	public ModelAndView updateReceipt(EditVO vo) throws Exception {
		logger.info("------------------selectEditVO진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		
		int result = receiptService.updateReceipt(vo);
		vo.setRNUM(vo.getRNUM());
		System.out.println("EditVO: " + vo.toString());
		
		mv.addObject("result", result);
		return mv;
	}
	
	@RequestMapping("/receipt/showEditResult.do")
	public ModelAndView showEditResult(String RECEIPT_ID, String RNUM ,String UPDATE_FLAG) throws Exception {
		logger.info("------------------showEditResult진입------------------");
		ModelAndView mv = new ModelAndView("/receipt/showEditResult");
		
		ReceivedStatusVO vo = receiptService.showEditResult(RECEIPT_ID ,UPDATE_FLAG);
		vo.setRNUM(RNUM);
		System.out.println("showEditResult: " + vo.toString());
		
		mv.addObject("editVO", vo);
		return mv;
	}
	
	@RequestMapping("/receipt/insertReceipt.do")
	public ModelAndView insertReceipt(HttpServletRequest request, ReceiptVO vo) throws Exception {
		logger.info("------------------insertReceipt진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		
		// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
		UserVO reqLoginVo = (UserVO) request.getSession().getAttribute("login");
		if(!reqLoginVo.getAuthCode().equals("2")) {
			mv.addObject("msg", "제보등록은 접수자만 가능합니다");
			return mv;
		}
		vo.setMEMO(vo.getMEMO().replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		
		String ifrmType = vo.getINDIVIDUAL_TYPE();
		
		// 제보 접수 등록
		int result = receiptService.insertReceipt(vo);
		
		System.out.println("vo: " + vo.toString());
		System.out.println("insertRecipt result: "+result);
		mv.addObject("result", result);
		return mv;
	}
	
	@RequestMapping("/receipt/updateMonthlyStat.ajax")
	public ModelAndView updateMonthlyStat(ReceiptVO vo) throws Exception{
		logger.info("------------------updateMonthlyStat진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		System.out.println("VO: " +vo.toString());
		int result = receiptService.updateMonthlyStat(vo);
		System.out.println("updateMonthlyStat result: " + result);
		mv.addObject("result", result);
		return mv;
	}
	
	//selectBox - 제보유형(중)
	@RequestMapping("/receipt/reportTypeOnChange.ajax")
	public ModelAndView reportTypeOnChange(String BAS_LCOD) throws Exception{
		logger.info("------------------reportTypeOnChange진입------------------");
		//System.out.println("bas_lcod: " + BAS_LCOD);
		ModelAndView mv = new ModelAndView("jsonView");
		List<ReportTypeVO> reportTypeList = receiptService.reportTypeOnChange(BAS_LCOD);
		//System.out.println("reportTypeList: " + reportTypeList);
		mv.addObject("data", reportTypeList);
		return mv;
	}
	
	//selectBox - 지역구분(소)
	@RequestMapping("/receipt/selectAreaCodeSub.ajax")
	public ModelAndView selectAreaCodeSub(String AREA_CODE) throws Exception{
		logger.info("------------------selectAreaCodeSub진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		List<AreaSubCodeVO> subAreaCodeList = receiptService.selectAreaCodeSub(AREA_CODE);
		mv.addObject("data", subAreaCodeList);
		return mv;
	}
	
	//통신원 - 검색:연락처(IP-PBX용)
	@RequestMapping("/receipt/selectInformerByPhone.ajax")
	public ModelAndView selectInformerByPhone(String phone_cell, String month1, String month2, String month3) throws Exception {
		logger.info("------------------selectInformerByPhone진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		List<InformerVO> list = receiptService.selectInformerByPhone(phone_cell);
		
		if(list.size() > 0) {
			InformerVO vo = list.get(0);
			if(vo.getMEMO() != null) {
				vo.setMEMO(vo.getMEMO().replace("\r\n", "<br>"));
			}
			
			Integer MON_CNT= receiptService.getMonthlyTotal(new InformerStatVO(vo.getINFORMER_ID(), month1));
			if(MON_CNT == null) MON_CNT = 0;
			vo.setSTAT1(MON_CNT);
			
			MON_CNT = receiptService.getMonthlyTotal(new InformerStatVO(vo.getINFORMER_ID(), month2));
			if(MON_CNT == null) MON_CNT = 0;
			vo.setSTAT2(MON_CNT);
			
			MON_CNT = receiptService.getMonthlyTotal(new InformerStatVO(vo.getINFORMER_ID(), month3));
			if(MON_CNT == null) MON_CNT = 0;
			vo.setSTAT3(MON_CNT);
			
			mv.addObject("data", vo);
		} else {
			mv.addObject("data", "noInfoFound");
		}
		return mv;
	}
	
	//통신원 - 검색:이름(1),코드(2),연락처(3)
	@RequestMapping("/receipt/popUpInformerList.do")
	public ModelAndView popUpInformerList(HttpServletRequest request) throws Exception{
		logger.info("------------------popUpInformerList진입------------------");
		ModelAndView mv = new ModelAndView();
		String searchType = request.getParameter("searchType");
		String parameter = URLDecoder.decode(request.getParameter("SEARCH_TEXT"),"utf-8");
		String areaCode = request.getParameter("nowUserArea");
		
		System.out.println("Parameter: "+parameter);
		System.out.println("SearchType: " + searchType);
		
		List<InformerVO> informerList = null;
		if(searchType.equals("1")) {
			informerList = receiptService.searchInformerByName(parameter , areaCode);
		} else if(searchType.equals("2")) {
			informerList = receiptService.searchInformerByCode(parameter , areaCode);
		} else if(searchType.equals("3")) { 
			informerList = receiptService.searchInformerByPhone(parameter , areaCode);
		} else if(searchType.equals("4")) {//금일접수 편집에서 불러올경우
			informerList = receiptService.searchInformerByName(parameter, areaCode);
		}
		
		mv.addObject("informerList", informerList);
		mv.addObject("informerListSize", informerList.size());
		mv.setViewName("/receipt/popUpInformerList");
		return mv;
	}
	
	@RequestMapping("/receipt/selectInformerByID.ajax")
	public ModelAndView selectInformerByID(String INFORMER_ID, String month1, String month2, String month3 ) throws Exception{
		logger.info("------------------selectInformerByID진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		InformerVO vo = receiptService.selectInformerByID(INFORMER_ID);
		
		Integer MON_CNT= receiptService.getMonthlyTotal(new InformerStatVO(INFORMER_ID, month1));
		if(MON_CNT == null) MON_CNT = 0;
		vo.setSTAT1(MON_CNT);
		
		MON_CNT = receiptService.getMonthlyTotal(new InformerStatVO(INFORMER_ID, month2));
		if(MON_CNT == null) MON_CNT = 0;
		vo.setSTAT2(MON_CNT);

		MON_CNT = receiptService.getMonthlyTotal(new InformerStatVO(INFORMER_ID, month3));
		if(MON_CNT == null) MON_CNT = 0;
		vo.setSTAT3(MON_CNT);

		System.out.println("stat Informer VO: " + vo.toString());
		mv.addObject("data", vo);
		return mv;
	}
	
	//통신원 - 메모
	@RequestMapping("/receipt/saveInformerMemo.ajax")
	public ModelAndView saveInformerMemo(InformerVO vo) throws Exception {
		logger.info("------------------saveInformerMemo진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		
		vo.setMEMO(vo.getMEMO().replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		int result = receiptService.saveInformerMemo(vo);
		mv.addObject("result", result);
		return mv;
	}
	
	@RequestMapping("/receipt/listUpArtery.ajax")
	public ModelAndView listUpArtery(ArteryVO vo) throws Exception {
		logger.info("------------------listUpArtery진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		List<ArteryVO> arteryList = receiptService.selectArtery(vo);
		logger.info("도로명조회 결과 size: " + arteryList.size());
		mv.addObject("arteryList", arteryList);
		return mv;
	} 
	
	@RequestMapping("/receipt/listUpNodeLink.ajax")
	public ModelAndView listUpNodeLink(ArteryVO vo) throws Exception {
		logger.info("------------------listUpNodeLink진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		List<ArteryVO> nodeLinkList = receiptService.selectNodeLink(vo);
		logger.info("nodeLinkList 결과 size: " + nodeLinkList.size());
		mv.addObject("nodeLinkList", nodeLinkList);
		return mv;
	}
	
	//부재중 - 삽입
	@RequestMapping("/receipt/insertMissedCall.ajax")
	public ModelAndView insertMissedCall(MissedCallVO vo) throws Exception {
		logger.info("------------------insertMissedCall진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		int result = receiptService.insertMissedCall(vo);
		logger.info("부재중목록저장 결과: "+result);
		mv.addObject("result", result);
		return mv;
	}
	
	@RequestMapping("/recipet/getMissedCallInfoById.ajax")
	public ModelAndView getMissedCallInfoById(String MISSED_CALL_ID) throws Exception {
		System.out.println("missedId: " + MISSED_CALL_ID);
		logger.info("------------------getMissedCallInfoById진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		MissedCallVO vo = new MissedCallVO();
		vo.setMISSED_CALL_ID(MISSED_CALL_ID);
		vo = receiptService.getMissedCallInfoById(vo);
		System.out.println("vo: " + vo.toString());
		mv.addObject("data", vo);
		return mv;
	}
	
	
	
	// 금일 제보 접수 첫 진입 및 조회
	@RequestMapping("/receipt/receivedStatusList.do")
	public ModelAndView receivedStatusList(CriteriaVO cri) throws Exception{
		logger.info("------------------receivedStatusList진입------------------");
//		ModelAndView mv = new ModelAndView("/receipt/receivedStatusList");
		ModelAndView mv = new ModelAndView();
		System.out.println("RECEIPT_DAY " + cri.getRECEIPT_DAY());
		System.out.println("startRow: " + cri.getStartRow());
		System.out.println("AREA_CODE: " + cri.getAREA_CODE());
		
		int size = 300;
		cri.setSize(size);
		cri.setStartRow((cri.getStartRow()-1) * size);
		
		List<ReceivedStatusVO> receivedStatusList = receiptService.receivedStatusList(cri);
		//System.out.println("receivedStatusList vo: " + receivedStatusList.get(0).toString());
		
		mv.addObject("receivedStatusList", receivedStatusList);
		mv.setViewName("/receipt/receivedStatusList");
		//mv.addObject("receivedStatusListSize", receivedStatusList.size());
		return mv;
	}
	
	// 모바일 앱 조회 화면 진입
	@RequestMapping("/receipt/appStatus.do")
	public ModelAndView appStatus(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("/receipt/appStatus");
		return mv;
	}
	
	// 25-11-18 : 모바일 앱 제보 조회
	@RequestMapping("/receipt/appStatusList.do")
	public ModelAndView appStatusList(CriteriaVO cri) throws Exception{
		logger.info("------------------receivedStatusList진입------------------");

		ModelAndView mv = new ModelAndView();
		System.out.println("RECEIPT_DAY " + cri.getRECEIPT_DAY());
		System.out.println("startRow: " + cri.getStartRow());
		System.out.println("AREA_CODE: " + cri.getAREA_CODE());
		
		int size = 300;
		cri.setSize(size);
		cri.setStartRow((cri.getStartRow()-1) * size);
		
		List<ReceivedStatusVO> appList = receiptService.appStatusList(cri);
		
		mv.addObject("appStatusList", appList);
		mv.setViewName("/receipt/appStatusList");

		return mv;
	}
	
	
	@RequestMapping("/receipt/appToToday.do")
	  public ModelAndView addDownload(Model model, HttpServletRequest request, @RequestParam(value="ids") List<String> ids) throws Exception {
		  ModelAndView mv = new ModelAndView("jsonView");  
		  
		  List<ReceivedStatusVO> alist = new ArrayList<>();
		  
		  // 제보접수자  ID 및 이름 정보 가져오기
		  UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
		  
		  for (int i = 0; i < ids.size(); i++) { //ID 값 나누기
			  ReceivedStatusVO awvo = new ReceivedStatusVO();
			  awvo.setRECEIPT_ID(ids.get(i));
			  alist.add(awvo);
		  }
		  
		  // today에 등록 시키기
		  receiptService.insertAppStatus(alist,nlVo);
		  
		  // chk flag 업데이트 시키기 => Y
		  receiptService.updateAppStatus(alist);
		  
		  // 등록 성공 시 메시지 보내기 (if로 조건 태우기)
		  mv.addObject("msg", "success");
		  
		  return mv;

	  }
	
	@RequestMapping("/receipt/getTotalPage.ajax")
	public ModelAndView getTotalPage(CriteriaVO cri) throws Exception {
		logger.info("------------------getTotalPage진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		System.out.println("cri: " + cri.toString());
		int total = receiptService.countReceivedStatusList(cri);
		System.out.println("total: " + total);
		
		int size = 300;
		int totalPages = total/size;
		if(total % size > 0) {
			totalPages++;
		}
		System.out.println("totalPage: " + totalPages);
		
		mv.addObject("totalPage", totalPages);
		mv.addObject("totalSize", total);
		return mv;
	}
	
	
	@RequestMapping("/receipt/appGetTotalPage.ajax")
	public ModelAndView appGetTotalPage(CriteriaVO cri) throws Exception {
		logger.info("------------------getTotalPage진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		System.out.println("cri: " + cri.toString());
		int total = receiptService.countAppStatusList(cri);
		System.out.println("total: " + total);
		
		int size = 300;
		int totalPages = total/size;
		if(total % size > 0) {
			totalPages++;
		}
		System.out.println("totalPage: " + totalPages);
		
		mv.addObject("totalPage", totalPages);
		mv.addObject("totalSize", total);
 		return mv;
	}
	
	
	@RequestMapping("/receipt/getSearchTotalPage.ajax")
	public ModelAndView getSearchTotalPage(ReceiptSearchVO vo) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		
		int total = receiptService.countSearchStatusList(vo);
		System.out.println("total: " + total);
		System.out.println("vo: " + vo.toString());
		
		int size = 300;
		int totalPages = total/size;
		if(total % size > 0) {
			totalPages++;
		}
		
		mv.addObject("totalPage", totalPages);
		mv.addObject("totalSize", total);
		return mv;
	}
	
	@RequestMapping("/receipt/searchFullStatus.do")
	public ModelAndView searchFullStatus(HttpServletRequest request, ReceiptSearchVO searchVO) throws Exception{
		logger.info("------------------searchFullStatus진입------------------");
		ModelAndView mv = new ModelAndView();
//		ReceiptSearchVO searchVO = new ReceiptSearchVO(request.getParameter("REPORT_TYPE")
//												, request.getParameter("REPORT_TYPE2")
//												, request.getParameter("REPORTMEAN_TYPE")
//												, request.getParameter("FLAG_SEND")
//												, request.getParameter("FLAG_BROD")
//												, request.getParameter("AREA_CODE")
//												, request.getParameter("CONTENT")
//												, request.getParameter("START_DAY")
//												, request.getParameter("END_DAY"));
//		
		int size = 300;
		int startRow = Integer.parseInt(request.getParameter("startRow"));
		System.out.println("startRow: " + startRow);
		
		searchVO.setSize(size);
		searchVO.setStartRow((startRow-1) * size);
		System.out.println("resultVO: " + searchVO.toString());
		
		List<ReceivedStatusVO> fullStatusList = receiptService.searchStatusList(searchVO);
		logger.info("searchFullStatus 결과 size: " + fullStatusList.size());
		mv.addObject("receivedStatusList", fullStatusList);
		mv.addObject("receivedStatusListSize", fullStatusList.size());
		
		String SEARCH_TAB = request.getParameter("SEARCH_TAB");
		System.out.println("search_tab: "+ SEARCH_TAB);
		//여기서 쿼리 다르게 해야함...;; 금일접수랑 전체접수랑 갈리니까..
		//테이블도 다르고
		if(SEARCH_TAB.equals("receivedStatusSearch")) {
			mv.setViewName("/receipt/receivedStatusList");
		} else {
			mv.setViewName("/receipt/fullReceivedHistoryList");
		}
		return mv;
	}
	
	@RequestMapping("/receipt/updateFlagSTT.ajax")
	public ModelAndView updateFlagSTT(String MISSED_CALL_ID) throws Exception {
		logger.info("------------------searchFullStatus진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		int result = receiptService.updateFlagSTT(MISSED_CALL_ID);
		mv.addObject("result", result);
		return mv;
	}
	
	@RequestMapping("/receipt/updatePersonalMemo.do")
	public ModelAndView updatePersonalMemo(UserVO vo) throws Exception {
		logger.info("------------------updatePersonalMemo진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		int result = receiptService.updatePersonalMemo(vo);
		System.out.println("result: " + result);
		mv.addObject("result", result);
		return mv;
	}
	
	@RequestMapping("/receipt/selectPersonalMemo.do")
	public ModelAndView selectPersonalMemo(UserVO vo) throws Exception {
		logger.info("------------------selectPersonalMemo진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		UserVO memoVO = receiptService.selectPersonalMemo(vo);
		System.out.println("vo: " + memoVO.toString());
		mv.addObject("data", memoVO);
		return mv;
	}
	//임시저장
	@RequestMapping("/receipt/tempSave.do")
	public ModelAndView tempSave(HttpServletRequest request, @ModelAttribute TempSaveVo vo, RedirectAttributes redirectAttributes){
		logger.info("------------------tempSave진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		vo.setMEMO(vo.getMEMO().replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
		try {
			receiptService.tempSave(vo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("임시저장 실패 : "+e);
		}
		logger.debug("임시저장 리턴");
		return mv;
	}
	//당일 로그인 사용자에 대한 임시저장 전체목록 불러오기
	@RequestMapping("/receipt/tempsaveList.ajax")
	public ModelAndView tempsaveList(TempSaveVo vo) throws Exception {
		logger.info("------------------tempsaveList진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		List<TempSaveVo> tsList = receiptService.tempsaveList(vo);
		mv.addObject("tsList", tsList);
		return mv;
	}
	//선택한  임시저장 데이터를 불러와서 반영
	@RequestMapping("/receipt/tempsavePush.ajax")
	public ModelAndView tempsavePush(TempSaveVo vo) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		TempSaveVo tsvo = receiptService.tempsavePush(vo);
		mv.addObject("tsvo", tsvo);
		return mv;
	}
	//선택한  임시저장 데이터 삭제
	@RequestMapping("/receipt/tempSaveDelete.ajax")
	public ModelAndView tempSaveDelete (
			HttpServletRequest request
//			,@RequestParam(required=false, value="TS_TIME") String TS_TIME
//			,@RequestParam(required=false, value="TS_TIME") String RECEPTION_ID
			,@RequestParam(required=false, value="chkArr[]")List<String> chkArr
			, RedirectAttributes redirectAttributes) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		try {
			receiptService.tempSaveDelete(chkArr);
		} catch (Exception e) {
			mv.addObject("msg","삭제에 실패하였습니다"+e);
		}
		return mv;
	}
	
	/*
	 * 전체접수용 테이블 분할 관련 230118
	 * */
	
	//최초 전체 행 수 검색
	@RequestMapping("/receipt/getTotalPageFR.ajax")
	public ModelAndView getTotalPageFR(CriteriaVO cri) throws Exception {
		logger.info("------------------getTotalPage진입------------------");
		ModelAndView mv = new ModelAndView("jsonView");
		System.out.println("cri: " + cri.toString());
		int total = receiptService.countReceivedStatusListFR(cri);
		System.out.println("total: " + total);
		
		int size = 300;
		int totalPages = total/size;
		if(total % size > 0) {
			totalPages++;
		}
		System.out.println("totalPage: " + totalPages);
		
		mv.addObject("totalPage", totalPages);
		mv.addObject("totalSize", total);
		return mv;
	}
	
	//검색 시 행 갯수 검색
	@RequestMapping("/receipt/getSearchTotalPageFR.ajax")
	public ModelAndView getSearchTotalPageFR(ReceiptSearchVO vo) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		
		String angleBracket = vo.getCONTENT1(); //검색어 1 가져오기
		String angleBracket2 = vo.getCONTENT2(); // 검색어 2 가져오기
		
		String angleBracketL = "&lt;";
		String angleBracketR = "&gt;";
		
		angleBracket = angleBracket.replace(angleBracketL, "<");
		angleBracket = angleBracket.replace(angleBracketR, ">");
		
		angleBracket2 = angleBracket2.replace(angleBracketL, "<");
		angleBracket2 = angleBracket2.replace(angleBracketR, ">");
		
		vo.setCONTENT1(angleBracket);
		vo.setCONTENT2(angleBracket2);
		
		int total = receiptService.countSearchStatusListFR(vo);
		System.out.println("total: " + total);
		System.out.println("vo: " + vo.toString());
		
		
		
		
		int size = 300;
		int totalPages = total/size;
		if(total % size > 0) {
			totalPages++;
		}
		
		mv.addObject("totalPage", totalPages);
		mv.addObject("totalSize", total);
		return mv;
	}
	
	@RequestMapping("/receipt/receivedStatusListFR.do")
	public ModelAndView receivedStatusListFR(CriteriaVO cri) throws Exception{
		logger.debug("------------------receivedStatusList FR 진입------------------");
		ModelAndView mv = new ModelAndView("/receipt/receivedStatusList");
		System.out.println("RECEIPT_DAY " + cri.getRECEIPT_DAY());
		System.out.println("startRow: " + cri.getStartRow());
		System.out.println("AREA_CODE: " + cri.getAREA_CODE());
		
		int size = 300;
		cri.setSize(size);
		cri.setStartRow((cri.getStartRow()-1) * size);
		
		List<ReceivedStatusVO> receivedStatusList = receiptService.receivedStatusListFR(cri);
		//System.out.println("receivedStatusList vo: " + receivedStatusList.get(0).toString());
		
		mv.addObject("receivedStatusList", receivedStatusList);
		//mv.addObject("receivedStatusListSize", receivedStatusList.size());
		return mv;
	}
	
	
	
	
	// 전체 접수 이력 검색 (검색결과 0 이상인 경우)
	@RequestMapping("/receipt/searchFullStatusFR.do")
	public ModelAndView searchFullStatusFR(HttpServletRequest request, ReceiptSearchVO searchVO) throws Exception{
		logger.info("------------------searchFullStatus진입------------------");
		ModelAndView mv = new ModelAndView();
//		ReceiptSearchVO searchVO = new ReceiptSearchVO(request.getParameter("REPORT_TYPE")
//												, request.getParameter("REPORT_TYPE2")
//												, request.getParameter("REPORTMEAN_TYPE")
//												, request.getParameter("FLAG_SEND")
//												, request.getParameter("FLAG_BROD")
//												, request.getParameter("AREA_CODE")
//												, request.getParameter("CONTENT")
//												, request.getParameter("START_DAY")
//												, request.getParameter("END_DAY"));
//		
		int size = 300;
		int startRow = Integer.parseInt(request.getParameter("startRow"));
		System.out.println("startRow: " + startRow);
		
		String angleBracket = searchVO.getCONTENT1(); //검색어 1 가져오기
		String angleBracket2 = searchVO.getCONTENT2(); // 검색어 2 가져오기
		
		String angleBracketL = "&lt;";
		String angleBracketR = "&gt;";
		
		angleBracket = angleBracket.replace(angleBracketL, "<");
		angleBracket = angleBracket.replace(angleBracketR, ">");
		
		angleBracket2 = angleBracket2.replace(angleBracketL, "<");
		angleBracket2 = angleBracket2.replace(angleBracketR, ">");
		
		searchVO.setCONTENT1(angleBracket);
		
		
		
		
		searchVO.setCONTENT2(angleBracket2);
		
		
		searchVO.setSize(size);
		searchVO.setStartRow((startRow-1) * size);
		System.out.println("resultVO: " + searchVO.toString());
		
		List<ReceivedStatusVO> fullStatusList = receiptService.searchStatusListFR(searchVO);
		logger.info("searchFullStatus 결과 size: " + fullStatusList.size());
		mv.addObject("receivedStatusList", fullStatusList);
		mv.addObject("receivedStatusListSize", fullStatusList.size());
		
		String SEARCH_TAB = request.getParameter("SEARCH_TAB");
		System.out.println("search_tab: "+ SEARCH_TAB);
		if(SEARCH_TAB.equals("receivedStatus")) {
			mv.setViewName("/receipt/receivedStatusList");
		} else {
			mv.setViewName("/receipt/fullReceivedHistoryList");
		}
		return mv;
	}
	
	
	// 26-03-30 : 상태 변경 시 실행되는 컨트롤러
	@RequestMapping("/receipt/releaseSituationUpdate.ajax")
	public ModelAndView transferGWUpdate(
	        Model model,
	        HttpServletRequest request,
	        @RequestParam(value = "ids") List<String> ids,
	        @RequestParam(value = "actionType") String actionType) throws Exception {

	    logger.info("------------------ 모바일 앱 제보 상황해제 및 오류제보 등록 진입 ------------------");
	    logger.info("ids        : {}", ids);
	    logger.info("actionType : {}", actionType);

	    ModelAndView mv = new ModelAndView("jsonView");

	    try {
	        // 1. 파라미터 검증
	        if (ids == null || ids.isEmpty()) {
	            mv.addObject("result", "fail");
	            mv.addObject("msg", "선택된 receiptId가 없습니다.");
	            return mv;
	        }

	        if (actionType == null || "".equals(actionType.trim())) {
	            mv.addObject("result", "fail");
	            mv.addObject("msg", "처리 구분값(actionType)이 없습니다.");
	            return mv;
	        }

	        // 버튼 id를 실제 업무 구분값으로 치환
	        String procType = "";
	        if ("appUptBtn".equals(actionType)) {
	            procType = "RELEASE";   // 상황해제
	        } else if ("appDelBtn".equals(actionType)) {
	            procType = "ERROR";     // 오류제보
	        } else {
	            mv.addObject("result", "fail");
	            mv.addObject("msg", "잘못된 처리 구분값입니다. actionType=" + actionType);
	            return mv;
	        }

	        // 로컬 테스트
	       String gwUrl = "http://localhost:7999/tbnAppGw/releaseSituation.do";
	        
	        // 실 제보접수 서버(모바일 앱 연계서버)
	        //String gwUrl = "http://192.101.1.191:7999/tbnAppGw/releaseSituation.do";

	        // GW로 보낼 JSON body
	        Map<String, Object> body = new HashMap<String, Object>();
	        body.put("ids", ids);
	        body.put("procType", procType);

	        HttpResponse<String> gwResponse = Unirest.post(gwUrl)
	                .header("Content-Type", "application/json; charset=UTF-8")
	                .body(body)
	                .asString();

	        logger.info("GW 응답 status = {}", gwResponse.getStatus());
	        logger.info("GW 응답 body   = {}", gwResponse.getBody());

	        if (gwResponse.getStatus() == 200) {
	            mv.addObject("result", "success");
	            mv.addObject("msg", "GW 전송 완료");
	            mv.addObject("gwBody", gwResponse.getBody());
	            mv.addObject("gwStatus", gwResponse.getStatus());
	        } else {
	            mv.addObject("result", "fail");
	            mv.addObject("msg", "GW 호출 실패");
	            mv.addObject("gwStatus", gwResponse.getStatus());
	            mv.addObject("gwBody", gwResponse.getBody());
	        }

	    } catch (Exception e) {
	        logger.error("releaseSituationUni 처리 중 오류", e);
	        mv.addObject("result", "fail");
	        mv.addObject("msg", "상황해제/오류제보 처리 중 오류");
	        mv.addObject("error", e.toString());
	    }

	    return mv;
	}
	
	
	
	
	// 26-03-30 : 상태 되돌리기 시 실행되는 컨트롤러
	@RequestMapping("/receipt/releaseSituationReset.ajax")
	public ModelAndView transferGWReset(
	        Model model,
	        HttpServletRequest request,
	        @RequestParam(value = "ids") List<String> ids,
	        @RequestParam(value = "actionType") String actionType) throws Exception {

	    logger.info("------------------ 모바일 앱 제보 상황해제 및 오류제보 되돌리기 진입 ------------------");
	    logger.info("ids        : {}", ids);
	    logger.info("actionType : {}", actionType);

	    ModelAndView mv = new ModelAndView("jsonView");

	    try {
	        // 1. 파라미터 검증
	        if (ids == null || ids.isEmpty()) {
	            mv.addObject("result", "fail");
	            mv.addObject("msg", "선택된 receiptId가 없습니다.");
	            return mv;
	        }

	        if (actionType == null || "".equals(actionType.trim())) {
	            mv.addObject("result", "fail");
	            mv.addObject("msg", "처리 구분값(actionType)이 없습니다.");
	            return mv;
	        }

	        // 버튼 id를 실제 업무 구분값으로 치환
	        String procType = "";
	        if ("appUptBtn".equals(actionType)) {
	            procType = "RELEASE";   // 상황해제
	        } else if ("appDelBtn".equals(actionType)) {
	            procType = "ERROR";     // 오류제보
	        } else if ("appResetBtn".equals(actionType)) {
	        	procType = "RESET"; // 되돌리기
	        } else {
	            mv.addObject("result", "fail");
	            mv.addObject("msg", "잘못된 처리 구분값입니다. actionType=" + actionType);
	            return mv;
	        }

	        // 로컬 테스트
	        String gwUrl = "http://localhost:7999/tbnAppGw/releaseSituationReset.do";
	        
	        // 실 제보접수 서버(모바일 앱 연계서버)
	        //String gwUrl = "http://192.101.1.191:7999/tbnAppGw/releaseSituationReset.do";

	        // GW로 보낼 JSON body
	        Map<String, Object> body = new HashMap<String, Object>();
	        body.put("ids", ids);
	        body.put("procType", procType);

	        HttpResponse<String> gwResponse = Unirest.post(gwUrl)
	                .header("Content-Type", "application/json; charset=UTF-8")
	                .body(body)
	                .asString();

	        logger.info("GW 응답 status = {}", gwResponse.getStatus());
	        logger.info("GW 응답 body   = {}", gwResponse.getBody());

	        if (gwResponse.getStatus() == 200) {
	            mv.addObject("result", "success");
	            mv.addObject("msg", "GW 전송 완료");
	            mv.addObject("gwBody", gwResponse.getBody());
	            mv.addObject("gwStatus", gwResponse.getStatus());
	        } else {
	            mv.addObject("result", "fail");
	            mv.addObject("msg", "GW 호출 실패");
	            mv.addObject("gwStatus", gwResponse.getStatus());
	            mv.addObject("gwBody", gwResponse.getBody());
	        }

	    } catch (Exception e) {
	        logger.error("releaseSituationUni 처리 중 오류", e);
	        mv.addObject("result", "fail");
	        mv.addObject("msg", "상황해제/오류제보 처리 중 오류");
	        mv.addObject("error", e.toString());
	    }

	    return mv;
	}
	
	
	
	
	/*@RequestMapping("/receipt/releaseSituationUni.ajax")
	public ModelAndView transferGW(
	        Model model,
	        HttpServletRequest request,
	        @RequestParam(value = "ids") List<String> ids,
	        @RequestParam(value = "actionType") String actionType) throws Exception {

	    logger.info("------------------ releaseSituationUni 진입 ------------------");
	    logger.info("ids        : {}", ids);
	    logger.info("actionType : {}", actionType);

	    ModelAndView mv = new ModelAndView("jsonView");

	    try {
	        // 1. 파라미터 검증
	        if (ids == null || ids.isEmpty()) {
	            mv.addObject("result", "fail");
	            mv.addObject("msg", "선택된 receiptId가 없습니다.");
	            return mv;
	        }

	        if (actionType == null || "".equals(actionType.trim())) {
	            mv.addObject("result", "fail");
	            mv.addObject("msg", "처리 구분값(actionType)이 없습니다.");
	            return mv;
	        }

	        // 버튼 id를 실제 업무 구분값으로 치환
	        String procType = "";
	        if ("appUptBtn".equals(actionType)) {
	            procType = "RELEASE";   // 상황해제
	        } else if ("appDelBtn".equals(actionType)) {
	            procType = "ERROR";     // 오류제보
	        } else {
	            mv.addObject("result", "fail");
	            mv.addObject("msg", "잘못된 처리 구분값입니다. actionType=" + actionType);
	            return mv;
	        }

	        // 로컬 테스트
	        String gwUrl = "http://localhost:7999/tbnAppGw/releaseSituation.do";
	        // 실 제보접수 서버(모바일 앱 연계서버)
	        //String gwUrl = "http://192.101.1.191:7999/tbnAppGw/releaseSituation.do";

	        // GW로 보낼 JSON body
	        Map<String, Object> body = new HashMap<String, Object>();
	        body.put("ids", ids);
	        body.put("procType", procType);

	        HttpResponse<String> gwResponse = Unirest.post(gwUrl)
	                .header("Content-Type", "application/json; charset=UTF-8")
	                .body(body)
	                .asString();

	        logger.info("GW 응답 status = {}", gwResponse.getStatus());
	        logger.info("GW 응답 body   = {}", gwResponse.getBody());

	        if (gwResponse.getStatus() == 200) {
	            mv.addObject("result", "success");
	            mv.addObject("msg", "GW 전송 완료");
	            mv.addObject("gwBody", gwResponse.getBody());
	            mv.addObject("gwStatus", gwResponse.getStatus());
	        } else {
	            mv.addObject("result", "fail");
	            mv.addObject("msg", "GW 호출 실패");
	            mv.addObject("gwStatus", gwResponse.getStatus());
	            mv.addObject("gwBody", gwResponse.getBody());
	        }

	    } catch (Exception e) {
	        logger.error("releaseSituationUni 처리 중 오류", e);
	        mv.addObject("result", "fail");
	        mv.addObject("msg", "상황해제/오류제보 처리 중 오류");
	        mv.addObject("error", e.toString());
	    }

	    return mv;
	}*/
	
}