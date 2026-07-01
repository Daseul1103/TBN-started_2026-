package kr.co.wizbrain.tbn.award.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.co.wizbrain.tbn.award.vo.AwardVO;
import kr.co.wizbrain.tbn.comm.RecordDto;

@Mapper("awardMapper")
public interface AwardMapper{
	
	List<AwardVO> selectGrade(AwardVO thvo);
	
	void updateGrade(AwardVO thvo);
	
	List<AwardVO> getAwardInformerList(AwardVO thvo);

	void saveAward(@Param("thvo")AwardVO thvo, @Param("chkArr")List<AwardVO> chkArr);
	
	void deleteAward(@Param("selection")String[] slt);

	List<AwardVO> selectUserAwardList(AwardVO thvo);
	
	String selectAwardCnt();
	
	String selectTodayAward(AwardVO thvo);
	
	void saveAwardReciptent(AwardVO thvo);

	List<AwardVO> getAwardList(AwardVO awvo);

	List<RecordDto> getAwardInformerList2(AwardVO paramVO);

	List<RecordDto> selectUserAwardList2(AwardVO paramVO);
	
}
