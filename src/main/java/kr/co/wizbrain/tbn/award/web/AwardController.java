package kr.co.wizbrain.tbn.award.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.co.wizbrain.tbn.award.service.AwardService;
import kr.co.wizbrain.tbn.award.vo.AwardVO;
import kr.co.wizbrain.tbn.comm.RecordDto;
import kr.co.wizbrain.tbn.infrm.service.InfrmService;
import kr.co.wizbrain.tbn.option.service.AreaOptService;
import kr.co.wizbrain.tbn.option.vo.OptAreaVo;
import kr.co.wizbrain.tbn.user.service.UserService;
import kr.co.wizbrain.tbn.user.vo.UserVO;

@Controller
public class AwardController{
	
	private static final Logger logger = LoggerFactory.getLogger(AwardController.class);
	public String url="";
	
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="infrmService")
	private InfrmService infrmService;
	
	@Resource(name="awardService")
	private AwardService awardService;
	
	@Resource(name="areaOptService")
	private AreaOptService areaOptService;

	
	//주소에 맞게 매핑
	@RequestMapping(value="informer/award/*.do")
	public String urlMapping(HttpSession httpSession, HttpServletRequest request,Model model) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.Reporter 최초 컨트롤러 진입 httpSession : "+httpSession);
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURL() : "+request.getRequestURL());
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURI() : "+request.getRequestURI());
		logger.debug("▶▶▶▶▶▶▶.request.getContextPath() : "+request.getContextPath());
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		logger.debug("▶▶▶▶▶▶▶.보내려는 url : "+url);
		return url;
	}
	
	
	
	/**
	 * 시상관리 메인
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value= {"/informer/award/awardMain.do","/informer/award/awardUserMain.do"})
	public ModelAndView awardMain(HttpServletRequest request,Model model) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
		OptAreaVo areaVo = new OptAreaVo();
		AwardVO avo = new AwardVO();
		
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		
		// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
     	UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
     	
		if(!(nlVo.getAuthCode().equals("999"))) {//999 관리자 권한
			avo.setAREA_CODE(nlVo.getRegionId());
			areaVo.setAreaCode(nlVo.getRegionId());
		}
		
		List<OptAreaVo> alist = areaOptService.selectAreaOpt1(areaVo);
		
		//최초 지역코드 idx 맨 처음 (부산교통방송)
		String fArea= alist.get(0).getAreaCode();		
		avo.setAREA_CODE(fArea);
		
		List<AwardVO> awardGrade = awardService.selectGrade(avo);
		
		mv.addObject("informerRegionList", alist);
		mv.addObject("awardGrade", awardGrade.get(0));
		
		//수상자선정인지 수상자 조회인지 분기처리
		mv.setViewName(url);
		
		return mv;
	}
	
	
	
	
	// 시상 관리 > 수상자선정 조회
	@RequestMapping("/informer/award/awardInformerList.do")
	public ModelAndView awardList(@ModelAttribute("AwardVO") AwardVO paramVO,HttpServletRequest request) throws Exception {
		
		ModelAndView mv = new ModelAndView("/informer/award/awardInformerList");
		
		if(!(paramVO.getALL_PER().equals("") || paramVO.getMAIN_PER().equals("") || paramVO.getADD_PER().equals(""))) {
			awardService.updateGrade(paramVO);
		}
		
		
		// 26-06 : 개인정보 암호화 작업 완료
        List<AwardVO> awardInformerList = awardService.getAwardInformerList(paramVO);
     
        mv.addObject("awardInformerList", awardInformerList);
        mv.addObject("awardInformerListSize", awardInformerList.size());
        mv.addObject("params", paramVO);
        
        int awardCnt = awardService.selectAwardCnt();
		mv.addObject("awardCnt", awardCnt);
        
		return mv;
	}
	
	
	
	
	// 시상 관리 > 수상자 선정(등록)
	@RequestMapping("/informer/award/awardInformerSave.do")
	public ModelAndView awardInformerSave(Model model,HttpServletRequest req,@ModelAttribute("AwardVO") AwardVO paramVO) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");  
		
		String[] Selection = req.getParameterValues("Selection");
		logger.debug("Selection===>"+Selection[0]);

		
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

		
		// 개인정보 암호화 대상 x
		awardService.saveAward(paramVO,alist);
		
		mv.addObject("msg", "success");
		
		return mv;
	}
	
	
	
	
	// 시상 관리 > 수상자 조회
	@RequestMapping("/informer/award/selectUserAwardList.do")
	public ModelAndView selectUserAwardList(HttpServletRequest request,Model model, @ModelAttribute("AwardVO") AwardVO paramVO) throws Exception {
		
		ModelAndView mv = new ModelAndView("/informer/award/awardUserList");
		
		// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
     	UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
     	
       //지역관련
		OptAreaVo avo = new OptAreaVo();
		
		if(!(nlVo.getAuthCode().equals("999"))) {//999 관리자 권한
			avo.setAreaCode(nlVo.getRegionId());
			paramVO.setREGION_ID(nlVo.getRegionId());
		}
		
		// 26-06 : 개인정보 암호화 작업 완료
        List<AwardVO> awardInformerList = awardService.selectUserAwardList(paramVO);
        
        mv.addObject("awardInformerList", awardInformerList);
        mv.addObject("awardInformerListSize", awardInformerList.size());
        mv.addObject("params", paramVO);
        
		return mv;
	}
	
	
	
	
	
	// 시상 관리 > 수상자 선정 엑셀 다운로드
	@RequestMapping("/informer/award/excelDownloadInformerList.do")
	public String exselDownload(Model model,@ModelAttribute("AwardVO") AwardVO paramVO) throws Exception {
		
		logger.debug("★★★"+paramVO);	
		
		// 26-06 : 개인정보 암호화 완료
		List<RecordDto> awardInformerList = awardService.getAwardInformerList2(paramVO);
		
		model.addAttribute("fileName", "수상자선정.xls");
		
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
	
	
	
	
	// 수상자 조회 엑셀 다운로드
	@RequestMapping("/informer/award/excelDownloadUserList.do")
	public String exselDownloadUserList(Model model,@ModelAttribute("AwardVO") AwardVO paramVO) throws Exception {
		logger.debug("★★★"+paramVO);
		
		// 26-06 : 개인정보 암호화 완료
		List<RecordDto> awardInformerList = awardService.selectUserAwardList2(paramVO);
		
		model.addAttribute("fileName", "수상자.xls");
		
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
	
	
	
	
	// 시상 관리 > 수상자 선정 제외
	@RequestMapping("/informer/award/deleteAward.do")
	public ModelAndView deleteAward(Model model,HttpServletRequest req,@ModelAttribute("AwardVO") AwardVO paramVO) throws Exception {
		
		ModelAndView mv = new ModelAndView("jsonView");
		
		String[] Selection = req.getParameterValues("Selection");
		logger.debug("Selection===>"+Selection[0]);
		
		// 개인정보 암호화 대상 x
		awardService.deleteAward(Selection);
		
		mv.addObject("msg", "success");
		return mv;
		
	}
	
	
	
	
	
}
