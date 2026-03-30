package kr.co.wizbrain.tbn.user.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.wizbrain.tbn.option.service.AreaOptService;
import kr.co.wizbrain.tbn.option.service.AuthService;
import kr.co.wizbrain.tbn.option.vo.AuthVo;
import kr.co.wizbrain.tbn.option.vo.OptAreaVo;
import kr.co.wizbrain.tbn.user.service.UserService;
import kr.co.wizbrain.tbn.user.vo.UserVO;
import kr.co.wizbrain.tbn.comm.util.PasswordPolicy;
import kr.co.wizbrain.tbn.infrm.vo.InfrmVO;

/**
 * 사용자 컨트롤러 클래스
 * @author 솔루션사업팀 정다빈
 * @since 2021.07.23
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 *
 *   수정일            수정자              수정내용
 *  -------    -------- ---------------------------
 *  2021.07.23  정다빈           최초 생성
 */

@Controller
public class UserController {
	
	public static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Resource(name="userService")
	private UserService userService;

	@Resource(name="authService")
	private AuthService authService;
	
	@Resource(name="areaOptService")
	private AreaOptService areaOptService;

	public String url="";
	public boolean isClose = false;
	
	
	//주소에 맞게 매핑
	@RequestMapping(value="/user/*.do")
	public String userUrlMapping(HttpSession httpSession, HttpServletRequest request,Model model) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.user 최초 컨트롤러 진입 httpSession : "+httpSession);
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURL() : "+request.getRequestURL());
		logger.debug("▶▶▶▶▶▶▶.request.getRequestURI() : "+request.getRequestURI());
		logger.debug("▶▶▶▶▶▶▶.request.getContextPath() : "+request.getContextPath());
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		logger.debug("▶▶▶▶▶▶▶.보내려는 url : "+url);
		return url;
	}
	
	//22.03.16 긴급생성
	//1.  소메뉴 존재 항목에 관하여 권한 별 분기처리
	@RequestMapping(value="/user/first.do")
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
		if(nowUrl.equals("/user/first.do")) {
			nowUrl = "/user/userMain.do";
		}
		mav.addObject("chgUrl", nowUrl);
		
		// ####### 권한별 정보관리 화면 분기처리 ########## 
		
		return mav;
	}
	
	//초기 검색버튼 활성화
	@RequestMapping(value= {"/user/userMain.do","/user/loginHistorySch.do","/user/useHistorySch.do"})
	public ModelAndView userMain(HttpServletRequest request) throws Exception {
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		ModelAndView mav = new ModelAndView(url);
		// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
		AuthVo authVo = new AuthVo();
		authVo.setAuthCode(nlVo.getAuthCode());
		List<AuthVo> authList= null;
		authList = authService.selectAuthType(authVo);
		mav.addObject("authList", authList);
		return mav;
	}
	
	
	//사용자 목록 조회
	@RequestMapping(value="/user/userList.do")
	public @ResponseBody ModelAndView userList( @ModelAttribute("userVO") UserVO userVO,HttpServletRequest request) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.회원정보 조회 목록!!!!!!!!!!!!!!!!");
		
		UserVO uvo = new UserVO();
		//url로 h,g 판별하여 해당하는 값만 조회
		ModelAndView mav = new ModelAndView("jsonView");
		List<UserVO> userList= null;
		try {
			// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
			UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
			if(userVO.getSearchValue()!=null) {
				uvo=userVO;
			}
			if(!(nlVo.getAuthCode().equals("999"))) {//999 관리자 권한
				uvo.setRegionId(nlVo.getRegionId());
				//관리자 권한이 아닐경우 관리자는 목록조회 안되도록
				uvo.setAuthCode("998");
			}
			userList = userService.selectUserList(uvo);
			mav.setViewName("/user/userList");
			mav.addObject("userList", userList);
		} catch (Exception e) {
			logger.debug("에러메시지 : "+e.toString());
		}
		return mav;
	}

	/**
	 * 사용자 등록, 상세 페이지 이동 분기
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/editUser.do")
	public ModelAndView editUser(HttpServletRequest request, @ModelAttribute UserVO userVO, RedirectAttributes redirectAttributes) throws Exception {
		ModelAndView mv = new ModelAndView("/user/userEdit");
		UserVO uvo = new UserVO();
		UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
		
		if (request.getParameter("pr1")!=null&&!(request.getParameter("pr1").equals(""))) {
			uvo.setUserId(request.getParameter("pr1"));
			uvo=userService.selectUser(uvo);
			mv.addObject("editDiv", "update");
			mv.setViewName("/user/userDetail");
		} else {
			mv.setViewName("/user/userInsert");
			mv.addObject("editDiv", "new");
		}
		mv.addObject("userInfo", uvo);
		//
		List<AuthVo> authList= null;
		List<OptAreaVo> positionList= null;
		AuthVo authVo = new AuthVo();
		OptAreaVo areaVo = new OptAreaVo();
		
		authVo.setAuthCode(nlVo.getAuthCode());
		if(!(nlVo.getAuthCode().equals("999"))) {//999 관리자 권한
			areaVo.setAreaCode(nlVo.getRegionId());
		}
		authList = authService.selectAuthType(authVo);
		mv.addObject("authList", authList);
		
		positionList = areaOptService.selectAreaOpt1(areaVo);
		mv.addObject("positionList", positionList);
		return mv;
	}
	
	
	@RequestMapping(value="/user/insertUser.ajax", method=RequestMethod.POST)
	public ModelAndView insertUser(HttpServletRequest request, @ModelAttribute UserVO userVO,
	                               RedirectAttributes redirectAttributes) throws Exception {

	    ModelAndView mav = new ModelAndView("jsonView");
	    
	    try {
	    	//0)보안취약점 authCode 변조 불가
	    	String atcd = request.getParameter("authCode");
	    	//현재 사용자가 관리자가 아닐 경우 관리자 권한 임명 불가
	    	UserVO reqLoginVo = (UserVO) request.getSession().getAttribute("login");
		    if(
		    	!(reqLoginVo.getAuthCode().equals("999"))
		    	&&atcd.equals("999")	
		    ) {
		    	mav.addObject("cnt", 0);
		    	mav.addObject("msg", "허용되지 않은 접근입니다.");
		    	return mav;
		    }    	
	        // 1) 비번/확인 값
	        String pw  = userVO.getUserPw();
	        String pw2 = request.getParameter("userPw2"); // 폼에 있음(VO에 없어도 request로 받으면 됨)

	        // 2) 일치 체크
	        if (pw == null || pw.isEmpty() || pw2 == null || pw2.isEmpty() || !pw.equals(pw2)) {
	            mav.addObject("cnt", 0);
	            mav.addObject("msg", "비밀번호/비밀번호 확인이 일치하지 않습니다.");
	            return mav;
	        }

	        // 3) 정책 체크(핵심)
	        if (!PasswordPolicy.isStrong(pw)) {
	            mav.addObject("cnt", 0);
	            mav.addObject("msg", PasswordPolicy.failMessage());
	            return mav;
	        }

	        // 4) 해시 후 저장
	        String hashedPw = BCrypt.hashpw(pw, BCrypt.gensalt());
	        userVO.setUserPw(hashedPw);

	        userService.insertUser(userVO);
	        mav.addObject("cnt", 1);
	        return mav;

	    } catch (Exception e) {
	        logger.debug("에러메시지 : " + e.toString());
	        mav.addObject("cnt", 0);
	        mav.addObject("msg", "저장 중 오류가 발생했습니다.");
	        return mav;
	    }
	}

	//검색한 id 조회
	@RequestMapping(value="/user/findUserId.do")
	public @ResponseBody ModelAndView findUserId( @ModelAttribute("userVO") UserVO userVO,HttpServletRequest request) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.회원정보 조회 목록!!!!!!!!!!!!!!!!");
		
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		
		ModelAndView mav = new ModelAndView("jsonView");
		UserVO disUser= null;
		try {
			disUser = userService.selectUser(userVO);
			mav.addObject("data", disUser.getUserId());
		} catch (Exception e) {
			logger.debug("에러메시지 : "+e.toString());
			mav.addObject("msg","search_not");
		}
		return mav;
	}
	

	//사용자 수정 페이지 진입
	@RequestMapping(value="/user/userUpdate.do")
	public @ResponseBody ModelAndView userUpdate( @ModelAttribute("userVO") UserVO userVO,HttpServletRequest request) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.회원정보 조회 목록!!!!!!!!!!!!!!!!");
		
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		
		ModelAndView mav = new ModelAndView("jsonView");
		UserVO disUser= null;
		try {
			disUser = userService.selectUser(userVO);
			logger.debug("▶▶▶▶▶▶▶.시험코드 결과값들:"+disUser);
			
			mav.addObject("data", disUser);
			mav.setViewName(url);
		} catch (Exception e) {
			logger.debug("에러메시지 : "+e.toString());
		}
		return mav;
	}
	
	
	@RequestMapping(value="/user/userDelete.ajax" , method=RequestMethod.POST)
	public ModelAndView deleteUser(Model model, @ModelAttribute("userVO") UserVO userVO,
            HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		
		userService.deleteUser(userVO);

		return mv;
	}
	
	
	
	@RequestMapping(value="/user/userUpdate.ajax", method=RequestMethod.POST) // 가능하면 POST로 고정 권장
	public @ResponseBody ModelAndView userUpdateForm(@ModelAttribute("userVO") UserVO userVO,
	                                                HttpServletRequest request) throws Exception {

	    ModelAndView mav = new ModelAndView("jsonView");

	    try {
	        String pw  = userVO.getUserPw();               // 변경 시에만 입력
	        String pw2 = request.getParameter("userPw2");  // 확인값

	        //260114 변경사항
	        
	        
	        // 비번 변경을 시도한 경우에만 정책 강제
	        if (pw != null && !pw.isEmpty()) {

	        	// 정책 체크(핵심)
	            if (!PasswordPolicy.isStrong(pw)) {
	                mav.addObject("cnt", 0);
	                mav.addObject("msg", PasswordPolicy.failMessage());
	                return mav;
	            }
	        	
	            // 확인값 일치 체크(화면에서 “변경시에만 입력”이라도 Burp로는 들어옴)
	            if (pw2 == null || pw2.isEmpty() || !pw.equals(pw2)) {
	                mav.addObject("cnt", 0);
	                mav.addObject("msg", "비밀번호/비밀번호 확인이 일치하지 않습니다.");
	                return mav;
	            }

	            // 해시 처리
	            String hashedPw = BCrypt.hashpw(pw, BCrypt.gensalt());
	            userVO.setUserPw(hashedPw);

	        } else {
	            // 비번 변경 의도 없음 → 업데이트 쿼리에서 비번 컬럼이 건드려지지 않게 해야 함
	            // (MyBatis 쪽에서 <if test="userPw != null and userPw != ''"> 로 update 하도록 되어 있어야 안전)
	            userVO.setUserPw(null);
	        }

	        userService.updateUser(userVO);

	        mav.addObject("cnt", 1);
	        mav.addObject("msg", "OK");
	        return mav;

	    } catch (Exception e) {
	        logger.debug("에러메시지 : " + e.toString());
	        mav.addObject("cnt", 0);
	        mav.addObject("msg", "수정 중 오류가 발생했습니다.");
	        return mav;
	    }
	}
	
	/**
	 * 사용자 권한 등급 설정
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/authorityMgmt.do")
	public ModelAndView authorityMgmt(@ModelAttribute("userVO") UserVO userVO,HttpServletRequest request) throws Exception {
		url = request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0];
		ModelAndView mav = new ModelAndView("jsonView");
		UserVO disUser= null;
		try {
			disUser = userService.selectUser(userVO);
			logger.debug("▶▶▶▶▶▶▶.시험코드 결과값들:"+disUser);
			
			List<UserVO> userList= null;
			
			mav.addObject("authList", userList);
			mav.setViewName(url);
		} catch (Exception e) {
			logger.debug("에러메시지 : "+e.toString());
			mav.addObject("msg","에러가 발생했습니다.");
		}
		return mav;
	}
	
	
	//사용자 로그인이력 조회
	@RequestMapping(value="/user/loginHistoryList.do")
	public @ResponseBody ModelAndView loginHistory( @ModelAttribute("userVO") UserVO userVO,HttpServletRequest request) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.회원정보 조회 목록!!!!!!!!!!!!!!!!");
		
		UserVO uvo = new UserVO();
		//url로 h,g 판별하여 해당하는 값만 조회
		ModelAndView mav = new ModelAndView("jsonView");
		List<UserVO> userList= null;
		try {
			// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
			UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
			if(userVO.getSearchValue()!=null) {
				uvo=userVO;
			}
			if(!(nlVo.getAuthCode().equals("999"))) {//999 관리자 권한
				uvo.setRegionId(nlVo.getRegionId());
			}
			userList = userService.loginHistory(uvo);
			mav.setViewName("/user/loginHistoryList");
			mav.addObject("userList", userList);
		} catch (Exception e) {
			logger.debug("에러메시지 : "+e.toString());
		}
		return mav;
	}
	
	//사용자 사용이력 조회
	@RequestMapping(value="/user/useHistoryList.do")
	public @ResponseBody ModelAndView useHistoryList( @ModelAttribute("userVO") UserVO userVO,HttpServletRequest request) throws Exception{
		logger.debug("▶▶▶▶▶▶▶.회원정보 조회 목록!!!!!!!!!!!!!!!!");
		
		UserVO uvo = new UserVO();
		//url로 h,g 판별하여 해당하는 값만 조회
		ModelAndView mav = new ModelAndView("jsonView");
		List<UserVO> userList= null;
		try {
			// 현재 세션에 대해 로그인한 사용자 정보를 가져옴
			UserVO nlVo = (UserVO) request.getSession().getAttribute("login");
			if(userVO.getSearchValue()!=null) {
				uvo=userVO;
			}
			if(!(nlVo.getAuthCode().equals("999"))) {//999 관리자 권한
				uvo.setRegionId(nlVo.getRegionId());
			}
			userList = userService.loginHistory(uvo);
			mav.setViewName("/user/useHistoryList");
			mav.addObject("userList", userList);
		} catch (Exception e) {
			logger.debug("에러메시지 : "+e.toString());
		}
		return mav;
	}
	
}
