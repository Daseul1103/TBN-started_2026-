package kr.co.wizbrain.tbn.user.service;

import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.wizbrain.tbn.user.vo.UserVO;

/**
 * 사용자 서비스 클래스
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

public interface UserService {
	// 90일 비밀번호 변경
	public void changePw(UserVO userVO) throws Exception;
	
	// 90일 비밀번호 변경 로그
	public void changePwLog(UserVO userVO) throws Exception;
	
	//전체 사용자 조회
	public List<UserVO> selectUserList(UserVO userVO) throws Exception;
	
	//사용자 등록
	public void insertUser(UserVO userVO) throws Exception;
	
	//특정 사용자 조회
	public UserVO selectUser(UserVO userVO) throws Exception;

	//사용자 정보 수정
	public void updateUser(UserVO uvo);
	
	//사용자 비밀번호 변경 시 업데이트 날짜 변경
	public void updatePwDate(UserVO uvo);
	
	//사용자 비밀번호 변경 시 로그 테이블에 데이터 삽입
	public void updatePwLog(UserVO uvo);
	
	//사용자 삭제
	public void deleteUser(UserVO uvo);
	
	//로그인 정보 주입(어플리케이션 연동)
	public void insertLogin(UserVO lvo);
		
	//로그아웃 시 접근 정보 1->0(어플리케이션 연동)
	public void logoutUpdate(UserVO lvo);
	
	//로그인 정보 주입(어플리케이션 연동)
	public void errloginDelete(UserVO lvo);
	
	//사용이력 조회
	public List<UserVO> loginHistory(UserVO uvo);
}
