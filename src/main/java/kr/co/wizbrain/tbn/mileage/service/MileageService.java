package kr.co.wizbrain.tbn.mileage.service;

import java.util.List;

import kr.co.wizbrain.tbn.award.vo.AwardVO;
import kr.co.wizbrain.tbn.comm.RecordDto;
import kr.co.wizbrain.tbn.mileage.vo.MileageVO;
import kr.co.wizbrain.tbn.option.vo.OptAreaVo;

public interface MileageService {
	
	// 굿 제보 마일리지 조회
	public List<MileageVO> mileList(MileageVO MileageVO) throws Exception;
		
	// 우수 통신원 조회
	public List<MileageVO> excellenceList(MileageVO MileageVO) throws Exception;
	
	// 시상별 배점 갱신
	void updateGrade(AwardVO thvo);
	
	// 우수 제보자 시상 리스트
	public List<AwardVO> getExcellInformerList(AwardVO thvo) throws Exception;
	
	// 시상별 배점 조회
	List<AwardVO> excellGrade(OptAreaVo thvo);
	
	// 굿 제보 통신원 등록 
	public void excellenceIfrmInsert(MileageVO mvo, List<String> selection) throws Exception;
	
	// 굿 제보 통신원 선정 취소
	public void excellenceIfrmDelete(MileageVO mvo, List<String> selection) throws Exception;
	
	// 우수 제보자 > 수상자 조회 
	public List<AwardVO> selectUserAwardList(AwardVO thvo) throws Exception;
	
	//시상 저장
	public void saveAward(AwardVO thvo, List<AwardVO> alist) throws Exception;
	
	//시상 제외(삭제)
	public void deleteAward(String[] slt) throws Exception;
	
	// 우수 제보자 > 수상자 선정 엑셀 다운로드
	List<RecordDto> getAwardInformerList2(AwardVO paramVO);
	
	// 우수 제보자 > 수상자 조회 엑셀 다운로드
	List<RecordDto> selectUserAwardList2(AwardVO paramVO);
	
	// 최고 통신원 기본 정보 조회
	public List<MileageVO> bestIfrmList(MileageVO MileageVO);

}
