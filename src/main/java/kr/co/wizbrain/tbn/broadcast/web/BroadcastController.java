package kr.co.wizbrain.tbn.broadcast.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import kr.co.wizbrain.tbn.broadcast.service.BroadcastService;
import kr.co.wizbrain.tbn.broadcast.vo.BroadCastListVO;
import kr.co.wizbrain.tbn.broadcast.vo.BroadSearchVO;
import kr.co.wizbrain.tbn.broadcast.vo.BroadcastVO;
import kr.co.wizbrain.tbn.comm.RSSParser;
import kr.co.wizbrain.tbn.map.vo.ImsVO;
import kr.co.wizbrain.tbn.receipt.service.ReceiptService;
import kr.co.wizbrain.tbn.receipt.vo.AreaCodeVO;
import kr.co.wizbrain.tbn.receipt.vo.CriteriaVO;
import kr.co.wizbrain.tbn.receipt.vo.InformerTypeVO;
import kr.co.wizbrain.tbn.receipt.vo.ReportTypeVO;

@Controller
public class BroadcastController {
	public static final Logger logger = LoggerFactory.getLogger(BroadcastController.class);
	public String url="";
	public boolean isClose = false;
	
	@Resource(name="broadcastService")
	private BroadcastService broadcastService;
	
	@Resource(name="receiptService")
	private ReceiptService receiptService;
	
	//(broadcast)주소에 맞게 매핑
	@RequestMapping(value="/broadcast/*.do")
	public ModelAndView broadcastUrlMapping(HttpSession httpSession, HttpServletRequest request,Model model) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.Producer 최초 컨트롤러 진입 httpSession : "+httpSession);
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURL() : "+request.getRequestURL());
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURI() : "+request.getRequestURI());
		logger.debug("▶▶▶▶▶▶▶.request.getContextPath() : "+request.getContextPath());
		
		ModelAndView mv = new ModelAndView();
		//List<BroadCastListVO> todaysList = broadcastService.selectTodaysList();
		//mv.addObject("todaysList", todaysList);
		//System.out.println("todaysList: " + todaysList.toString());
		//System.out.println("(Broad)todaysListSize: " + todaysList.size());
		
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		logger.debug("▶▶▶▶▶▶▶.보내려는 url : "+url);
		mv.setViewName(url);
		return mv;
	}
		
	//(PD)주소에 맞게 매핑
	@RequestMapping(value="/producer/*.do")
	public ModelAndView producerUrlMapping(HttpSession httpSession, HttpServletRequest request,Model model) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.Producer 최초 컨트롤러 진입 httpSession : "+httpSession);
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURL() : "+request.getRequestURL());
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURI() : "+request.getRequestURI());
		logger.debug("▶▶▶▶▶▶▶.request.getContextPath() : "+request.getContextPath());
		
		ModelAndView mv = new ModelAndView();
		
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		logger.debug("▶▶▶▶▶▶▶.보내려는 url : "+url);
		mv.setViewName(url);
		return mv;
	}
	
	//(Caster)주소에 맞게 매핑
	@RequestMapping(value="/caster/*.do")
	public ModelAndView casterUrlMapping(HttpSession httpSession, HttpServletRequest request,Model model) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.Caster 최초 컨트롤러 진입 httpSession : "+httpSession);
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURL() : "+request.getRequestURL());
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURI() : "+request.getRequestURI());
		logger.debug("▶▶▶▶▶▶▶.request.getContextPath() : "+request.getContextPath());
		
		ModelAndView mv = new ModelAndView();
		
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		logger.debug("▶▶▶▶▶▶▶.보내려는 url : "+url);
		mv.setViewName(url);
		return mv;
	}
	
	@RequestMapping("/broadcast/countBroadcastList.ajax")
	public ModelAndView countBroadcastList(CriteriaVO cri) throws Exception {
		logger.debug("▶▶▶▶▶▶▶.countBroadcastList 컨트롤러 진입");
		ModelAndView mv = new ModelAndView("jsonView");
		
		//int total = receiptService.countReceivedStatusList(cri);
		int total = broadcastService.countBroadcastList(cri);
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
	
	
	
	// PD/CASTER 첫 진입 시 방송 목록 조회
	@RequestMapping("/broadcast/selectBroadcastList.do")
	public ModelAndView selectBroadcastList(CriteriaVO cri) throws Exception {
		logger.debug("▶▶▶▶▶▶▶.selectBroadcastList 컨트롤러 진입");
		ModelAndView mv = new ModelAndView("/producer/common/todayList");
		System.out.println("RECEIPT_DAY: " + cri.getRECEIPT_DAY());
		System.out.println("startRow: " + cri.getStartRow());
		
		int size = 300;
		cri.setSize(size);
		cri.setStartRow((cri.getStartRow()-1) * size);
		
		
		// PD/CASTER 첫 진입 시 실행
		List<BroadCastListVO> todaysList = broadcastService.selectTodaysList(cri);
		mv.addObject("todaysList", todaysList);
		System.out.println("(Broad)todaysListSize: " + todaysList.size());
		return mv;
	}
	
	
	
	
	@RequestMapping("/producer/broadImsList.do")
	public ModelAndView broadImsList() throws Exception {
		logger.debug("▶▶▶▶▶▶▶.broadImsList 컨트롤러 진입");
		ModelAndView mv = new ModelAndView("/producer/common/broadImsList");
		
		RSSParser parser = new RSSParser();
		List<ImsVO> imsList = parser.imsParser();
		
		mv.addObject("imsList", imsList);
		return mv;
	}
	
	@RequestMapping("/braodcast/countSearchBroadcastList.ajax")
	public ModelAndView countSearchBroadcastList(BroadSearchVO vo) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.countSearchBroadcastList 컨트롤러 진입");
		ModelAndView mv = new ModelAndView("jsonView");
		
//		BroadSearchVO vo = new BroadSearchVO();
//		String REPORT_TYPE = request.getParameter("REPORT_TYPE");
//		String RECEIPT_DAY = request.getParameter("RECEIPT_DAY");
//		String REPORT_TYPE2 = request.getParameter("REPORT_TYPE2");
//		String AREA_CODE = request.getParameter("AREA_CODE");
//		String AREA_CODE_SUB = request.getParameter("AREA_CODE_SUB");
//		String INDIVIDUAL_TYPE = request.getParameter("INDIVIDUAL_TYPE");
//		String FLAG_BROD = request.getParameter("FLAG_BROD");
//		String CONTENT = request.getParameter("CONTENT");
//		int startRow = Integer.parseInt(request.getParameter("startRow"));
//		
//		vo.setREPORT_TYPE(REPORT_TYPE);
//		vo.setREPORT_TYPE2(REPORT_TYPE2);
//		vo.setRECEIPT_DAY(RECEIPT_DAY);
//		vo.setAREA_CODE(AREA_CODE);
//		vo.setAREA_CODE_SUB(AREA_CODE_SUB);
//		vo.setINDIVIDUAL_TYPE(INDIVIDUAL_TYPE);
//		vo.setFLAG_BROD(FLAG_BROD);
//		vo.setCONTENT(CONTENT);
//		vo.setStartRow(startRow);
		
		int total = broadcastService.countSearchBroadcastList(vo);
		int size = 300;
		vo.setSize(size);
		int totalPages = total/size;
		if(total % size > 0) {
			totalPages++;
		}
		
		mv.addObject("totalPage", totalPages);
		mv.addObject("totalSize", total);
		
		return mv;
	}
	
	
	// PD/CASTER 화면에서 조회 시 실행
	@RequestMapping("/broadcast/searchBroadcastList.do")
	public ModelAndView searchBroadcastList(BroadSearchVO vo) throws Exception {
		logger.debug("▶▶▶▶▶▶▶.searchBroadcastList 컨트롤러 진입");
		ModelAndView mv = new ModelAndView("/producer/common/todayList");
		
//		BroadSearchVO vo = new BroadSearchVO();
//		String REPORT_TYPE = request.getParameter("REPORT_TYPE");
//		String RECEIPT_DAY = request.getParameter("RECEIPT_DAY");
//		String REPORT_TYPE2 = request.getParameter("REPORT_TYPE2");
//		String AREA_CODE = request.getParameter("AREA_CODE");
//		String AREA_CODE_SUB = request.getParameter("AREA_CODE_SUB");
//		String INDIVIDUAL_TYPE = request.getParameter("INDIVIDUAL_TYPE");
//		String FLAG_BROD = request.getParameter("FLAG_BROD");
//		String CONTENT = request.getParameter("CONTENT");
//		int startRow = Integer.parseInt(request.getParameter("startRow"));
		int size = 300;
		vo.setStartRow((vo.getStartRow()-1) * size);
		vo.setSize(size);
		
//		vo.setREPORT_TYPE(REPORT_TYPE);
//		vo.setREPORT_TYPE2(REPORT_TYPE2);
//		vo.setRECEIPT_DAY(RECEIPT_DAY);
//		vo.setAREA_CODE(AREA_CODE);
//		vo.setAREA_CODE_SUB(AREA_CODE_SUB);
//		vo.setINDIVIDUAL_TYPE(INDIVIDUAL_TYPE);
//		vo.setFLAG_BROD(FLAG_BROD);
//		vo.setCONTENT(CONTENT);
//		vo.setStartRow((startRow-1) * size);
		
		System.out.println("broadSearchVO: " + vo.toString());
		
		List<BroadCastListVO> searchResultList = broadcastService.searchBroadcastList(vo);
		mv.addObject("todaysList", searchResultList);
		System.out.println("(Broad)searchResultListSize: " + searchResultList.size());
		return mv;
	}
	
	@RequestMapping("/broadcast/todayListSearch.do")
	public ModelAndView todayListSearch() throws Exception {
		logger.debug("▶▶▶▶▶▶▶.todayListSearch 컨트롤러 진입");
		ModelAndView mv = new ModelAndView("/producer/common/todayListSearch");
		
		//selectBox - 제보유형(대)
		List<ReportTypeVO> reportFirstList = receiptService.firstReportType();
		mv.addObject("reportFirstList", reportFirstList);
		
		//selectBox - 접수방송국
		List<AreaCodeVO> areaCodeList = receiptService.selectAreaCode();
		mv.addObject("areaCodeList", areaCodeList);
		
		//selectBox - 통신원타입
		List<InformerTypeVO> informerTypeList = receiptService.selectInformerType();
		mv.addObject("informerTypeList", informerTypeList);
		
		return mv;
	}
	
	@RequestMapping("/broadcast/updateBroadTime.ajax")
	public ModelAndView updateBroadTime(
			@RequestParam(value="idList[]")String[] idList) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.updateBroadTime 컨트롤러 진입");
		ModelAndView mv = new ModelAndView("jsonView");
		
		//String[] receiptIdArry = idList.split(",");
		List<BroadcastVO> blist = new ArrayList<BroadcastVO>();
		
		
		for(int i=0; i<idList.length; i++) {
			BroadcastVO broadCastVO = new BroadcastVO();
			broadCastVO.setRECEIPT_ID(idList[i]);
			
			if(idList[i] != null && idList[i].startsWith("APP")) {
		        broadCastVO.setUPDATE_TABLE("RECEIPT_APP");
		    } else {
		    	broadCastVO.setUPDATE_TABLE("REPORT_RECEIPT_TODAY");
		    }
			
			blist.add(broadCastVO);
		}
		int result = broadcastService.updateBroadTime(blist);
		
		return mv;
	}
	
	@RequestMapping("/broadcast/updateBroadFlagTo.ajax")
	public ModelAndView updateFlagToY(@RequestParam(value="idList[]")String[] idList, String FLAG_BROD) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.updateBroadFlagTo 컨트롤러 진입");
		ModelAndView mv = new ModelAndView("jsonView");
		//String[] receiptIdArry = idList.split(",");
		List<BroadcastVO> blist = new ArrayList<BroadcastVO>();
		int resultTotal = 0;
		
		/*for(int i=0; i<idList.length; i++) {
			BroadcastVO broadCastVO = new BroadcastVO();
			broadCastVO.setRECEIPT_ID(idList[i]);
			broadCastVO.setFLAG_BROAD(FLAG_BROD);
			blist.add(broadCastVO);
		}*/
		
		for(int i = 0; i < idList.length; i++) {
		    BroadcastVO broadCastVO = new BroadcastVO();
		    broadCastVO.setRECEIPT_ID(idList[i]);
		    broadCastVO.setFLAG_BROAD(FLAG_BROD);

		    if(idList[i] != null && idList[i].startsWith("APP")) {
		        broadCastVO.setUPDATE_TABLE("RECEIPT_APP");
		    } else {
		    	broadCastVO.setUPDATE_TABLE("REPORT_RECEIPT_TODAY");
		    }

		    blist.add(broadCastVO);
		}
		
		int result = broadcastService.updateBroadFlagTo(blist);
		if(result == 1) resultTotal++;
		
		logger.debug("resultTotal: " + resultTotal);
		mv.addObject("result", resultTotal);
		return mv;
	}

}















