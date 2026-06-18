package kr.co.wizbrain.tbn.infrm.mapper;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.co.wizbrain.tbn.infrm.vo.InfrmVO;

/**
 * 사용자 매퍼 클래스
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

@Mapper("infrmMapper")
public interface InfrmMapper{ 
	//전체 회원정보 조회
	public List<InfrmVO> selectInfrmList(@Param("InfrmVO") InfrmVO InfrmVO , @Param("selectedCols") List<String> selectedCols) throws Exception;

	public List<InfrmVO> selectInfrmList20(InfrmVO InfrmVO) throws Exception;
	
	public List<InfrmVO> countInfrmList(InfrmVO InfrmVO) throws Exception;
	
	//전체 회원정보 조회(스크롤)
	public List<InfrmVO> selectInfrmList2(@Param("InfrmVO")InfrmVO InfrmVO, @Param("startRnum") int startRnum, @Param("endRnum") int endRnum) throws Exception;
	
	//신규 informer id
	public String getNewId(InfrmVO paramVO);
	//신규 act id
	public String creActId(InfrmVO thvo) throws Exception;

	public String chkPhone(InfrmVO thvo) throws Exception;
	
	public int saveInformer(InfrmVO paramVO);
	
	public int updateInformer(InfrmVO paramVO);

	public void updateActId(InfrmVO paramVO);

	public void saveInformerHist(InfrmVO paramVO);

	public String getUpdateCode(InfrmVO paramVO);

	public int deleteInformer(InfrmVO paramVO);

	public List<InfrmVO> getInformerHistory(InfrmVO ifmVO);

	public List<InfrmVO> getInformerHistoryList(InfrmVO ifmVO);

	public InfrmVO detailInformer(InfrmVO thvo);

	// 24-11-21 : 통신원 월별 제보건수
	public List<InfrmVO> monthReport(@Param("selectYear1") String selectYear1,@Param("selectYear2") String selectYear2,@Param("informerId") String informerId);

	public long countAll(InfrmVO thvo);

	public long countFiltered (InfrmVO vo);
	
	public long applyAppCountFiltered(InfrmVO thvo);

	public List<InfrmVO> findSlice
	(@Param("vo") InfrmVO vo, @Param("startRnum") int startRnum, @Param("endRnum") int endRnum, @Param("orderBy") String orderBy);
	
	
	public List<InfrmVO> applyAppfindSlice
	(@Param("vo") InfrmVO vo, @Param("startRnum") int startRnum, @Param("endRnum") int endRnum, @Param("orderBy") String orderBy);
	
}
