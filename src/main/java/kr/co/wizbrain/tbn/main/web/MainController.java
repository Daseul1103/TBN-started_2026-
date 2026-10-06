package kr.co.wizbrain.tbn.main.web;

import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.wizbrain.tbn.user.vo.UserVO;
import kr.co.wizbrain.tbn.user.service.UserService;
import kr.co.wizbrain.tbn.comm.SessionListener;
import kr.co.wizbrain.tbn.notice.vo.NoticeVO;
import kr.co.wizbrain.tbn.option.service.AuthService;
import kr.co.wizbrain.tbn.option.service.IntelService;
import kr.co.wizbrain.tbn.option.vo.AuthVo;
import kr.co.wizbrain.tbn.option.vo.IntelVO;
import kr.co.wizbrain.tbn.receipt.service.ReceiptService;

/**
 * 사용자 컨트롤러 클래스
 * 
 * @author 솔루션사업팀 정다빈
 * @since 2021.09.23
 * @version 1.0
 * @see
 *
 * 		<< 개정이력(Modification Information) >>
 *
 *      수정일 수정자 수정내용 ------- -------- --------------------------- 2021.09.23 정다빈
 *      최초 생성
 */

@Controller
public class MainController {

	public static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	/*@Resource(name="receiptService")
	private ReceiptService receiptService;
	*/
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name = "authService")
	private AuthService authService;
	
	@Resource(name="intelService")
	private IntelService intelService;
	
	public String url = "";
	public boolean isClose = false;

	
	//주소에 맞게 매핑
	@RequestMapping(value="/common/*.do")
	public String userUrlMapping(HttpSession httpSession, HttpServletRequest request,Model model) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.main 최초 컨트롤러 진입 httpSession : "+httpSession);
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		String springVersion = org.springframework.core.SpringVersion.getVersion();
//		System.out.println("스프링 프레임워크 버전 : " + springVersion);
//		logger.debug("▶▶▶▶▶▶▶.보내려는 url : "+url);

		return url;
	}

	
	//로그인한 사용자 권한에 따라 메인화면 분기
	@RequestMapping(value="/common/firstView.do")
	public ModelAndView firstView(HttpSession httpSession, HttpServletRequest request,Model model) throws Exception{
		ModelAndView mav = new ModelAndView("jsonView");
		logger.debug("▶▶▶▶▶▶▶.main 최초 컨트롤러 진입 httpSession : "+httpSession);
		String nowUrl = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
		UserVO reqLoginVo = (UserVO) request.getSession().getAttribute("login");
		AuthVo avo = new AuthVo();
		avo.setAuthCode(reqLoginVo.getAuthCode());
		avo.setUrl(nowUrl);
		avo.setCdFlag("2");
		List<AuthVo> alist = authService.selectAuthUrl(avo);
		nowUrl = alist.get(0).getUrl()+".do";
		mav.addObject("chgUrl", nowUrl);
		return mav;
	}
	
	
	
	// 26-09-23 : 비밀번호 90일 이상 미변경시 변경
	@RequestMapping(value="/user/changePassword.ajax")
	public ModelAndView changePwChk(HttpSession httpSession, HttpServletRequest request,Model model, @RequestParam("newPassword") String newPw) throws Exception{
		ModelAndView mav = new ModelAndView("jsonView");
		logger.debug("▶▶▶▶▶▶▶ 비밀번호 90일 변경 확인 진입 httpSession : "+httpSession);

		// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
		UserVO reqLoginVo = (UserVO) request.getSession().getAttribute("login");

		// 새로운 비밀번호 넣기
		
		// 해시로 저장 후 넣어야 함
		String hashedPw = BCrypt.hashpw(newPw, BCrypt.gensalt());
		reqLoginVo.setUserPw(hashedPw);		
		
		// 비밀번호 변경 기능 실행 (비밀번호, 비밀번호 마지막 변경일자 수정)
		userService.changePw(reqLoginVo);
		
		// 비밀번호 변경에 따른 로그 생성
		userService.changePwLog(reqLoginVo);
		
		// DB 저장이 모두 성공한 뒤 세션의 날짜 갱신
		reqLoginVo.setPwChangedt(LocalDate.now().toString()); // 예: 2026-09-29
		
		mav.addObject("success", true);
		return mav;
	}

	
	//새로고침 / 창 닫기 분기 
	@RequestMapping(value = "/user/reloadOrKill.do")
	public String reloadOrKill(@RequestParam(required=false, value="tagId")boolean tagId, HttpSession httpSession, HttpServletRequest request,Model model) throws Exception{
//		logger.debug("▶▶▶▶▶▶▶.새로고침 or 창닫기 tag식별 : "+tagId);
//		this.isClose = tagId;
//		//refresh
//	    if ( isClose ) {
//	    	logger.debug("▶▶▶▶▶▶▶.refresh...");             
//	    	return "redirect:/";
//	    }
//
//	    Thread.sleep(5000);
//	    if ( !this.isClose ) {
//	    	logger.debug("▶▶▶▶▶▶▶.kill session!!!");
//	    	//httpSession.removeAttribute("access");
//	    	
//			httpSession.removeAttribute("login");
//			SessionListener.getInstance().removeSession(httpSession);
//	    }
	    return "redirect:/";
	}
	
	/**
	 * 로그인
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	//inputVo : 사용자가 화면에서 입력한 uservo 정보
	//lvo : db에 insert할 실제 loginvo
	
	@RequestMapping("/login/login.do")
	public @ResponseBody ModelAndView loginPost(@ModelAttribute UserVO inputVo,HttpSession httpSession
	,HttpServletRequest request,Model model) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.loginPost 진입 세션 : "+httpSession);
		logger.debug("▶▶▶▶▶▶▶.받아온 loginVo 값 : "+inputVo.toString());
		
		String msg="";
		UserVO userVo = new UserVO();
		ModelAndView mav = new ModelAndView("jsonView");
		
		try {
			userVo = userService.selectUser(inputVo);
			
			//등록되지 않은 사용자 또는 사용자 입력 오류
			if(userVo ==null || !(BCrypt.checkpw(inputVo.getUserPw(), userVo.getUserPw()))) {
				logger.debug("▶▶▶▶▶▶▶.가입되지 않은 사용자이거나 정보를 잘못 입력하셨습니다");
				msg="가입되지 않은 사용자이거나 정보를 잘못 입력하셨습니다";
				userVo =null;
			//유효한 계정 및 비밀번호 입력 시	
			}else {
                //기존 세션이 존재(중복로그인)
                if(SessionListener.getInstance().isUsing(inputVo.getUserId())) {
                	String relgn = request.getParameter("relgn");
                	//중복로그인
                	if(relgn == null || relgn.equals("") || relgn.equals("null")) {
                		logger.debug("▶▶▶▶▶▶▶.이미 로그인 중인 사용자입니다.");
                    	msg="중복로그인";
                    	mav.addObject("msg", msg);
                    	return mav;
                    //클라이언트 불완전 종료로 인한 재 로그인 처리
                	}else {
                    	logger.debug("▶▶▶▶▶▶▶.기존 세션을 삭제하고 재생성");
                		SessionListener.getInstance().removeSessionById(inputVo.getUserId());
                		userService.logoutUpdate(inputVo);
                	}
                }
                //통신원일 경우 내선번호 입력 확인
                //통신원일 경우 내선번호 미입력시
    			if(userVo.getAuthCode().equals("2")&&(inputVo.getInTel()==null||inputVo.getInTel().equals(""))) {
    				msg = "제보접수자의 경우 내선번호를 입력해주셔야 합니다";
    				userVo =null;
    			}else{//통신원 : 내선번호 존재 시 || 다른 사용자
    				//통신원일 경우
    				if(userVo.getAuthCode().equals("2")) {
    					IntelVO itvo = new IntelVO();
        				// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
        				if(!(userVo.getAuthCode().equals("999"))) {//999 관리자 권한
        					itvo.setAreaCode(userVo.getRegionId());
        				}
        				itvo.setInTel(inputVo.getInTel());
        				List<IntelVO> itList = intelService.selectIntel(itvo);
        				//올바르지 않은 내선번호일경우
    	    			if(itList.size()==0){
    	    				msg = "할당된 내선번호를 입력해 주세요";
    	    				userVo =null;
    	    			}else{
    	    				//다른 사용자가 사용하는 내선번호일경우
    	    				List<UserVO> nowList = SessionListener.getInstance().getNowVOList();
    	    				for (UserVO nvo : nowList) {
								if(nvo.getInTel().equals(inputVo.getInTel())) {
									userVo.setInTel(inputVo.getInTel());
								}
							}
    	    				if(userVo.getInTel()!=null&&!(userVo.getInTel().equals(""))) {
    	    					msg = "현재 사용중인 내선번호입니다";
        	    				userVo =null;
    	    				}else {
    	    					//로그인 성공!
                				//입력한 내선번호 등록
            					userVo.setInTel(inputVo.getInTel());
                				HttpSession loginSession = request.getSession(true);
                				
            	                //세션시간 설정 24*60*60 24시간 (시간/분/초 단위 : 초)
            	                loginSession.setMaxInactiveInterval(24*60*60);
            	                logger.debug("로그인vo 세션시간 : "+loginSession.getMaxInactiveInterval());
            	                //세션에 값 주입
            	                httpSession.setAttribute("login", inputVo);
            	                //세션 + 시간 해쉬맵에 로그인 세션과 현 시간을 저장
            	                SessionListener.getInstance().setSession(loginSession, userVo.getUserId());
            	                //${}세션
            	    			model.addAttribute("user",userVo);
            	    			mav.addObject("uvo", userVo);
    	    				}
    	    			}
    	    		//통신원 이외의 다른 사용자	
    				}else {
        				//로그인 성공!
        				//입력한 내선번호 등록
    					//userVo.setInTel(inputVo.getInTel());
    					
    					//통신원 이외의 사용자는 내선번호 삭제
    					userVo.setInTel("");
    					inputVo.setInTel("");
    					
        				HttpSession loginSession = request.getSession(true);
        				
    	                //세션시간 설정 24*60*60 24시간 (시간/분/초 단위 : 초)
    	                loginSession.setMaxInactiveInterval(24*60*60);
    	                logger.debug("로그인vo 세션시간 : "+loginSession.getMaxInactiveInterval());
    	                //세션에 값 주입
    	                httpSession.setAttribute("login", inputVo);
    	                //세션 + 시간 해쉬맵에 로그인 세션과 현 시간을 저장
    	                SessionListener.getInstance().setSession(loginSession, userVo.getUserId());
    	                //${}세션
    	    			model.addAttribute("user",userVo);
    	    			mav.addObject("uvo", userVo);
    				}
				}
			}
			
		} catch (Exception e) {
			msg="가입되지 않은 사용자이거나 정보를 잘못 입력하셨습니다";
			userVo=null;
			logger.debug("▶▶▶▶▶▶▶.캐치 에러 : "+e);
		} finally {
			mav.addObject("msg", msg);
		}
		return mav;
	}
	

	//로그아웃 처리
	@RequestMapping(value="/login/logout.do")
	public String logout(UserVO loginVo, HttpSession httpSession, Model model, HttpServletRequest request) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.logout 메소드 진입");
		//httpSession.removeAttribute("access");
		//String res=urlRedirect(request);
		return "redirect:/";
	}
	//권한 불필요한 공통로직 처리
	
	//24-11-12 : 하루동안 숨김인지 조회
	/*@RequestMapping(value="/selectShow.ajax")
	public @ResponseBody ModelAndView selectShow(@RequestParam("user")String user, @RequestParam("noticeId")String noticeId) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		List<NoticeVO> ShowList = receiptService.selectShow(user,noticeId);
		List<NoticeVO> NoticeList = receiptService.selectOneNotice(noticeId);
		mav.addObject("showList" , ShowList);
		mav.addObject("noticeList",NoticeList);
		return mav;
	}*/
	
	// 24-11-12 : 하루동안 숨김 없을 때 등록
	/*@RequestMapping(value= "/insertShow.ajax")
	public void insertShow(@RequestParam("today")String today,@RequestParam("user")String user,@RequestParam("noticeId")String noticeId) throws Exception {
		receiptService.insertShow(today,user,noticeId);
	}*/
	
	// 24-11-13 : 하루동안 숨김 클릭 시 업데이트 처리
	/*@RequestMapping(value="/updateShow.ajax")
	public void updateShow(@RequestParam("today")String today,@RequestParam("user")String user,@RequestParam("noticeId")String noticeId) throws Exception {
		receiptService.updateShow(today,user,noticeId);
	}*/
	
}
