package kr.co.wizbrain.tbn.award.service;

import java.util.List;

import kr.co.wizbrain.tbn.award.vo.AwardVO;
import kr.co.wizbrain.tbn.comm.ParamsDto;
import kr.co.wizbrain.tbn.comm.RecordDto;

public interface AwardService{
	
	List<AwardVO> selectGrade(AwardVO thvo);
	
	void updateGrade(AwardVO thvo);
	
	//시상받는 통신원 리스트
	public List<AwardVO> getAwardInformerList(AwardVO thvo) throws Exception;
	//시상 개수
	public int selectAwardCnt() throws Exception;
	//시상 저장
	public void saveAward(AwardVO thvo, List<AwardVO> alist) throws Exception;
	
	//시상 제외(삭제)
	public void deleteAward(String[] slt) throws Exception;
	
	public List<AwardVO> selectUserAwardList(AwardVO thvo) throws Exception;
	
	public List<AwardVO> getAwardList(AwardVO awvo);

	List<RecordDto> getAwardInformerList2(AwardVO paramVO);

	List<RecordDto> selectUserAwardList2(AwardVO paramVO);
}