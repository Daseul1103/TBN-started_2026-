package kr.co.wizbrain.tbn.infrm.service;

import java.util.List;
import java.util.Map;

import kr.co.wizbrain.tbn.infrm.vo.InfrmVO;

/**
 * 사용자 서비스 클래스
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

public interface InfrmService {

	//전체 사용자 조회
	public List<InfrmVO> selectInfrmList(InfrmVO InfrmVO, List<String> selectedCols) throws Exception;
	
	public List<InfrmVO> countInfrmList(InfrmVO InfrmVO) throws Exception;
	
	//전체 사용자 조회 (스크롤)
	public List<InfrmVO> selectInfrmList2(InfrmVO InfrmVO, int startRnum, int endRnum) throws Exception;
		
	//특정 사용자 조회
	public InfrmVO selectInfrm(InfrmVO infrmVO) throws Exception;
	//신규 id생성
	public String getNewId(InfrmVO thvo) throws Exception;
	
	//신규 actid생성
	public String creActId(InfrmVO thvo) throws Exception;
	
	public String getInformerId(InfrmVO infrmVO) throws Exception;
	
	public int saveInformer(InfrmVO paramVO);
	
	public int saveInformerApp(InfrmVO paramVO);
	
	public int uptInformerApp(InfrmVO paramVO);
	
	
	public int deleteApp(String applyId);
	public int rejectApp(String applyId);
	
	public void saveInformerHist(InfrmVO paramVO);
	public String getUpdateCode(InfrmVO paramVO);
	
	public void getAppUpdateCode(InfrmVO paramVO);
	public int deleteInformer(InfrmVO paramVO);
	public List<InfrmVO> getInformerHistory(InfrmVO ifmVO);
	public List<InfrmVO> getInformerHistoryList(InfrmVO ifmVO);
	
	public InfrmVO detailInformer(InfrmVO thvo);
	
	// 26-06-19 : APP 통신원 가입 정보 가져오기
	public InfrmVO detailApplyIfrm(InfrmVO thvo);
	
	public String chkPhone(InfrmVO ifmVO) throws Exception;
	
	
	// 통신원 상세에서 월별 제보 건수
	public List<InfrmVO> monthReport(String selectYear, String informerId) throws Exception;

	public long countAll(InfrmVO thvo);
	
	public long countFiltered(InfrmVO thvo);
	
	public long countFilteredAPP(InfrmVO thvo);
	
	public List<InfrmVO> findSliceAPP(InfrmVO thvo, int startRnum, int endRnum, String orderby);
	
	public long applyAppCountFiltered(InfrmVO thvo);

	public List<InfrmVO> findSlice(InfrmVO thvo, int startRnum, int endRnum, String orderBy);
	
	public List<InfrmVO> applyAppfindSlice(InfrmVO thvo, int startRnum, int endRnum, String orderBy);
	
	
	
	// 26-06-22 : APP 통신원 가입 현황 > 통신원 등록 > 통신원 중복 등록 검사
	public int chkDupInfrm(String phoneCell) throws Exception;
	

	public void goCancle (String applyId) throws Exception;
}
