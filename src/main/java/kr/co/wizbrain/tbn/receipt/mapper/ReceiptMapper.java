package kr.co.wizbrain.tbn.receipt.mapper;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.co.wizbrain.tbn.mileage.vo.MileageVO;
import kr.co.wizbrain.tbn.notice.vo.NoticeVO;
import kr.co.wizbrain.tbn.receipt.vo.AreaCodeVO;
import kr.co.wizbrain.tbn.receipt.vo.AreaSubCodeVO;
import kr.co.wizbrain.tbn.receipt.vo.ArteryVO;
import kr.co.wizbrain.tbn.receipt.vo.CriteriaVO;
import kr.co.wizbrain.tbn.receipt.vo.InformerStatVO;
import kr.co.wizbrain.tbn.receipt.vo.InformerTypeVO;
import kr.co.wizbrain.tbn.receipt.vo.InformerVO;
import kr.co.wizbrain.tbn.receipt.vo.MissedCallVO;
import kr.co.wizbrain.tbn.receipt.vo.PickUpCallVO;
import kr.co.wizbrain.tbn.receipt.vo.ReceiptVO;
import kr.co.wizbrain.tbn.receipt.vo.ReceiveCallVO;
import kr.co.wizbrain.tbn.receipt.vo.ReportMeanVO;
import kr.co.wizbrain.tbn.receipt.vo.ReportTypeVO;
import kr.co.wizbrain.tbn.receipt.vo.TempSaveVo;
import kr.co.wizbrain.tbn.receipt.vo.popup.EditVO;
import kr.co.wizbrain.tbn.receipt.vo.popup.ReceiptSearchVO;
import kr.co.wizbrain.tbn.receipt.vo.popup.ReceivedStatusVO;
import kr.co.wizbrain.tbn.user.vo.UserVO;

@Mapper("receiptMapper")
public interface ReceiptMapper{
	//전체 회원정보 조회
	//public List<ReceiptVO> selectReceipt(ReceiptVO ReceiptVO) throws Exception;
	
	public int selectBestIfrm(ReceiptVO ReceiptVO) throws Exception;
	
	//제보접수 등록
	public int insertReceipt(ReceiptVO ReceiptVO) throws Exception;
	public int updateMonthlyStat(ReceiptVO vo) throws Exception;

	//25-05-15 : 우수 통신원, 최고 통신원 조회
	public List<MileageVO> selectIfrm(ReceiptVO vo) throws Exception;
	
	// 24-11-11 : 공지사항 조회
	public List<NoticeVO> selectNotice(String today) throws Exception;
	
	//24-11-12 : 하루동안 보지않음 조회
	public List<NoticeVO> selectShow(@Param("user") String user, @Param("noticeId") String noticeId) throws Exception;
	
	// 24-11-12 : 공지사항 한개만 조회
	public List<NoticeVO> selectOneNotice(String noticeId) throws Exception;
	
	// 24-11-12 : 하루동안 보지않음 등록
	public void insertShow(@Param("today")String today,@Param("user")String user,@Param("noticeId")String noticeId) throws Exception;
	
	//24-11-13 : 하루동안 보지않음 업데이트
	public void updateShow(@Param("today")String today,@Param("user")String user,@Param("noticeId")String noticeId) throws Exception;
	
	//통신원(제보접수용) 조회
	//public InformerVO selectInformerByPhone(String phone_cell) throws Exception;
	public List<InformerVO> selectInformerByPhone(String phone_cell) throws Exception;
	public Integer getMonthlyTotal(InformerStatVO vo) throws Exception;
	
	//reportType(제보유형)-대
	public List<ReportTypeVO> firstReportType() throws Exception;
	
	//reportType(제보유형) 셀렉트박스 변경
	public List<ReportTypeVO> reportTypeOnChange(String bas_lcod) throws Exception;
	
	//reportMean(제보처) 셀렉트박스 생성
	public List<ReportMeanVO> selectReportMean() throws Exception;
	
	//selectBox - informerType(통신원타입)
	public List<InformerTypeVO> selectInformerType() throws Exception;
	
	//areaCode(지역구분)-대
	public List<AreaCodeVO> selectAreaCode() throws Exception;
	
	//areaCode(지역구분)-소
	public List<AreaSubCodeVO> selectAreaCodeSub(String area_code) throws Exception;
	
	//통신원검색(이름)
	public List<InformerVO> searchInformerByName(@Param("informer_name")String informer_name, @Param("areaCode")String areaCode) throws Exception;
	
	//통신원검색(번호)
	public List<InformerVO> searchInformerByPhone(@Param("phone_cell")String phone_cell , @Param("areaCode")String areaCode) throws Exception;
	
	//통신원검색(코드)
	public List<InformerVO> searchInformerByCode(@Param("act_id")String act_id, @Param("areaCode")String areaCode) throws Exception;
	
	//통신원검색(ID)
	public InformerVO selectInformerByID(String INFORMER_ID) throws Exception;
		
	//통신원 전달사항 저장
	public int saveInformerMemo(InformerVO vo) throws Exception;
	
	//지역별 도로명 조회
	public List<ArteryVO> selectArtery(ArteryVO vo) throws Exception;
	
	//지역별 노드링크 조회
	public List<ArteryVO> selectNodeLink(ArteryVO vo) throws Exception;
	
	//수신목록
	public List<ReceiveCallVO> selectPickupCall(ReceiveCallVO vo) throws Exception;
	
	//부재중목록
	public List<MissedCallVO> selectMissedCall(String AREA_CODE) throws Exception;
	
	//부재중목록 추가
	public int insertMissedCall(MissedCallVO vo) throws Exception;
	
	//STT를 가져오기 위한 부재중목록 객체
	public MissedCallVO getMissedCallInfoById(MissedCallVO vo) throws Exception;
		
	//팝업 - receivedStatus
	//public List<ReceivedStatusVO> receivedStatusList(String RECEIPT_DAY) throws Exception;
	public List<ReceivedStatusVO> receivedStatusList(CriteriaVO cri) throws Exception;	
	
	// 팝업 - 모바일 앱 제보
	public List<ReceivedStatusVO> appStatusList(CriteriaVO cri) throws Exception;
	
	// 팝업 - 모바일 제보 => 금일 제보 테이블로 insert
	public void insertAppStatus(@Param("list") List<ReceivedStatusVO> list, @Param("nlVo") UserVO nlVo) throws Exception;
	
	// 팝업 - 모바일 제보 검증 flag update
	public void updateAppStatus(List<ReceivedStatusVO> list) throws Exception;
	  
	//팝업 - 검색
	public List<ReceivedStatusVO> searchStatusList(ReceiptSearchVO vo) throws Exception;
	//public List<ReceivedStatusVO> searchStatusList(CriteriaVO cri) throws Exception;
	
	//팝업 - COUNT
	public int countReceivedStatusList(CriteriaVO vo) throws Exception;
	
	
	// 모바일 app - count
	public int countAppStatusList(CriteriaVO cri) throws Exception;
	
	//팝업(검색) - COUNT
	public int countSearchStatusList(ReceiptSearchVO vo) throws Exception;
	
	//팝업 - 수정페이지 불러오기
	public EditVO selectEditVO(@Param("RECEIPT_ID") String RECEIPT_ID , @Param("UPDATE_FLAG") String UPDATE_FLAG) throws Exception;
	
	//팝업 - 수정등록
	public int updateReceipt(EditVO vo) throws Exception;
	
	//팝업 - 수정결과
	public ReceivedStatusVO showEditResult(@Param("RECEIPT_ID") String RECEIPT_ID , @Param("UPDATE_FLAG") String UPDATE_FLAG) throws Exception;
	
	//STT Flag update
	public int updateFlagSTT(String MISSED_CALL_ID) throws Exception; 
	
	//개인메모 저장
	public int updatePersonalMemo(UserVO vo) throws Exception;
	
	//개인메모 불러오기
	public UserVO selectPersonalMemo(UserVO vo) throws Exception;
	
	//polling
	public List<PickUpCallVO> checkIfPickUpInfoExists(PickUpCallVO vo) throws Exception;
	public int deletePickUpInfo(PickUpCallVO vo) throws Exception;
	//임시저장
	public void tempSave(TempSaveVo vo);
	public List<TempSaveVo> tempsaveList(TempSaveVo vo);
	public TempSaveVo tempsavePush(TempSaveVo vo);
	//수신목록 테이블에 추가
	public void insertReceiveCall(PickUpCallVO vo);

	/*
	 * 전체접수용 테이블 분할 관련 230118
	 * */
	
	//팝업 - COUNT
	public int countReceivedStatusListFR(CriteriaVO vo) throws Exception;
	
	//팝업(검색) - COUNT
	public int countSearchStatusListFR(ReceiptSearchVO vo) throws Exception;
	
	public List<ReceivedStatusVO> receivedStatusListFR(CriteriaVO cri) throws Exception;

	public List<ReceivedStatusVO> searchStatusListFR(ReceiptSearchVO vo) throws Exception;
	
	public void tempSaveDelete(HashMap<String, Object> map);
}