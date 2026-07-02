package kr.co.wizbrain.tbn.statistic.mapper;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.wizbrain.tbn.award.vo.AwardVO;
import kr.co.wizbrain.tbn.comm.ParamsDto;
import kr.co.wizbrain.tbn.comm.RecordDto;
import kr.co.wizbrain.tbn.infrm.vo.InfrmVO;
import kr.co.wizbrain.tbn.option.vo.OptInftVo;
import kr.co.wizbrain.tbn.option.vo.OptRptVo;
import kr.co.wizbrain.tbn.receipt.vo.popup.ReceiptSearchVO;
import kr.co.wizbrain.tbn.statistic.vo.StatisticVO;

/**
 * 사용자 매퍼 클래스
 * @author  정다빈
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

@Mapper("statisticMapper")
public interface StatisticMapper{
	
	//월별 제보자별 총 제보건수 집계
	public List<StatisticVO> getReceiptCntList(StatisticVO stvo);
	//전체 방송국 표출
	public List selectArea();
	
	
	// 주소라벨 출력
	public List<InfrmVO> addRabel(List<InfrmVO> datalist) throws Exception;
		
	
	/**********************************************************************************************************************************************************/
	// 기간별 통계                                                                                                                                                                                                                                                                                              //
	/**********************************************************************************************************************************************************/
	
	
	//	가. 교통정보 제공대장 (sheet : 1) 통신원 유형별 현황 , 2) 제보 수단별 현황, 3) 제보 유형별 현황 )
	
	// 1) 통신원 유형별 현황
	// 1-1) 통신원 유형 title & 교통 정보 수집 건수 및 활용 실적 - 제보자 유형 title
	public List selectInfrm();
	
	// 1-2	&	6-1) 통신원별 전체 건수 & 교통 정보 수집 건수 및 활용 실적 - 제보자 유형 별 건수
	public List informerTypeAll(ParamsDto params) throws Exception;
	
	// 1-3	&	6-2) 통신원별 자국/ 타국 전체 건수 & 교통 정보 수집 건수 및 활용 실적 - 제보자 유형별 자국/ 타국 건수
	public List informerTypeOurOther(ParamsDto params) throws Exception;
	
	// 1-4) 통신원 유형별 상세 데이터
	public List informerTypeData(ParamsDto params) throws Exception;
	
	// 24-11-19 : 제보자별 제보현황
	public List informerReport(ParamsDto params) throws Exception;
	
	// 24-11-20 : 제보자별 제보현황 - 총 인원수
	public String allInformer(ParamsDto params) throws Exception;
	
	// 24-11-20 : 제보자별 제보현황 - 총 건수
	public String allReport(ParamsDto params) throws Exception;
	
	// 연간 제보자별 제보현황
		public List yearReceipt(ParamsDto params) throws Exception;
		
		public List totalList(ParamsDto params) throws Exception;
		
		public List totalListOrg(ParamsDto params) throws Exception;
		
		public List<AwardVO> perList(ParamsDto params) throws Exception;
		
		// 연간지역소속별통계
		public List yearOrgStat(ParamsDto params) throws Exception;
	
	// 2) 제보 수단별 현황
	// 2-1) 제보 수단 title
	public List selectRtt();
	
	// 2-2) 제보 수단별 전체 건수
	public List reportmeanTypeAll(ParamsDto params) throws Exception;
	
	// 2-3) 제보 수단 별 상세 데이터
	public List reportmeanTypeOurOther(ParamsDto params) throws Exception;
	
	// 2-4) 제보 수단 별 상세 데이터
	public List reportmeanTypeData(ParamsDto params) throws Exception;
	
	
	
	// 3) 제보 유형별 현황
	// 3-1) 제보 유형별 title
	public List selectRpt();
	
	// 3-2) 제보 유형별 전체 건수
	public List reportTypeAll(ParamsDto params) throws Exception;
	
	// 3-3) 제보 유형별 자국/타국 건수
	public List reportTypeOurOther(ParamsDto params) throws Exception;
	
	// 3-4) 제보 유형별 상세 데이터
	public List reportTypeData(ParamsDto params) throws Exception ;
	
	
	
	
	
	// 나. 긴급 교통 정보 _ 방송 현황 분석
	public List extrBro(ParamsDto params);
	
	
	
	
	// 다. 재난 제보 건수
	public List disastorStat(ParamsDto params);
	
	
	
	
	
	// 라. 무 제보자 현황
	public List muJeboList(ParamsDto params);
	
	
	
	
	// 마. 교통정보 수집 건수 및 활용 실적  (sheet : 1) 수집 건수, 2) 활용 실적 )
	
	// 1) 수집 건수
	// 1-1) 가. 1-1) selectInfrm()
	// 1-2) 가. 1-2) informerTypeAll(params)
	// 1-3) 가. 1-3) informerTypeOurOther(params)
	
	// 1-4) 일별 상세
	public List receiptUseDaily(ParamsDto params);
	
	
	// 2) 활용 실적
	// 2-1) 가. 1-1) selectInfrm()
	
	// 2-2) 월별 합계
	public List dailyRegionSendYNA07A08(ParamsDto params);
	
	// 2-3) 일별 방송국별 데이터
	public List monthSendYNA07A08(ParamsDto params);
	
	
	
	
	
	// 바. 통신원 중/소 분류별 통계 (sheet : 1) 통신원 중소 분류별 통계 )
	
	// 1) 통신원 중소 분류별 통계
	// 1-1) 
	public List statDateCal(ParamsDto params);

	// 1-2)
	public List<RecordDto> orgOrgSub(ParamsDto params);
	
	public List timeBroadData (ParamsDto params);
	
	
	public List timeInformer (ParamsDto params);
	
	public List timeRptType (ParamsDto params);
	
	public List timeRptMean (ParamsDto params);
	
	
	
	
	
	/**********************************************************************************************************************************************************/
	// 월별 통계                                                                                                                                                                                                                                                                                              //
	/**********************************************************************************************************************************************************/
	
	
	
	
	
	
	// 가. 월별 제보자별 제보 건수 (sheet : 1) 월별 제보자별 제보 건수 )
	
	// 1. 월별 제보자별 제보 건수
	// 1-1) 제보자 리스트
	public List monInfrmList(ParamsDto params);
	
	// 1-2) 월별 건수
	public List monInfrmCnt(ParamsDto params);
	
	
	
	
	
	// 나. 교통 통신원 제보 건수 및 접수 직원 가공 건수 
	
	// 1-1) 제보자 유형 title => 가. 1-1) selectInfrm()
		
	// 1-2) 월별 전체 수집 건수
	public List monthReceipt(ParamsDto params);
	
	// 1-3) 월별 자국 수집 건수
	public List monthOurReceipt(ParamsDto params);

	// 1-4) 월별 타국 수집 건수
	public List monthOtherReceipt(ParamsDto params);

	// 1-5) 월별 제보자 수
	public List monthInformer(ParamsDto params);

	// 1-6) 월별 1건 이상 제보자 수
	public List monthInformer1Inform(ParamsDto params);

	
	
	
	
	// 다. 돌발 교통정보 표출 실적 (sheet : 1) 돌발 교통 정보 수집 건수 )
	
	// 1) 돌발 교통 정보 수집 건수
	// 1-1) 무제보자 통신원 목록
	public List nationalIncident(ParamsDto params);
	
	
	
	
	
	// 라. 교통 정보 수집원 현황 (sheet : 1) 교통 정보 수집원 현황 )
	
	// 1) 교통 정보 수집원 현황
	// 1-1) 교통정보 수집원 현황
	public List informerStats(ParamsDto params);
	
	
	
	
	
	// 마. 한국 가스 기술 공사 제보 실적 (sheet : 1) 가스기술공사 제보 실적)
	
	// 1) 가스기술공사 제보 실적
	// 1-1) 제보자 유형별 건수
	public List krGasMonCnt(ParamsDto params);
	
	
	
	
	
	// 바. 한국 국토 정보 공사 제보 현황 (sheet : 1) 국토정보공사 제보 현황 )
	
	// 1) 국토정보공사 제보 현황
	// 1-1) 제보자 유형별 건수
	public List korLxMonCnt(ParamsDto params);
	
	
	
	
	
	// 사. 통신원 소속별 일자별 통계 (sheet : 1) 일자별 제보 건수)
	
	// 1) 일자별 제보 건수
	public List dayReceipt(ParamsDto params);
	
	
	
	
	
	
	// 아. 사회봉사자 일자별 통계 (sheet : 1) 일자별 제보 건수)
	
	// 1) 일자별 제보 건수
	public List volunteer(ParamsDto params);
	
	
	
	
	
	// 자. 연간 제보자별 제보 현황
	
	
	
	
	
	// 차. 연간 지회(통신원 중분류) 실적 평가
		
		
		
		
		
	// 카. 제보자별 제보 현황
	
	
	
	// 타. 모바일 앱 제보 통계
	public List receiptAppStat(ParamsDto params);
	
	
	
	
	
	
	
	
	/**********************************************************************************************************************************************************/
	// 컨트롤러 미 구현 기능들                                                                                                                                                                                                                                                                                              //
	/**********************************************************************************************************************************************************/
	
	
	
	
	
	//<!-- 표준화통계: 소속별 제보자 현황 -->
	public List preMonthRegionsub(ParamsDto params);
	
	public List thisMonthRegionsub(ParamsDto params);

	//<!-- 표준화통계: 유형별 제보자 현황 -->
	public List preMonthInformerType(ParamsDto params);		

	public List thisMonthInformerType(ParamsDto params);

	//<!-- 표준화통계: 인근지역 제보자 현황 -->
	public List preMonthArea(ParamsDto params);

	public List thisMonthArea(ParamsDto params);
	
	//표준화통계:일별 방송자료(전송)/자체처리(비전송)/안내/기관통보
	public List dailySendYNA07A08(ParamsDto params);
	
	//표준화통계:일별 방송국별 방송자료(전송)/자체처리(비전송)/안내/기관통보 
	
	//<!-- 전국통계: 방송국별 수집건수 -->
	public List receiptAll(ParamsDto params);
	
	//<!-- 전국통계: 방송국별 일별 수집건수 -->
	public List dailyReceiptAll(ParamsDto params);
	
	//<!-- 전국통계: 방송국별 방송건수 -->
	public List receiptBroad(ParamsDto params);
	
	//<!-- 전국통계: 방송국별 일별 방송건수 -->
	public List dailyReceiptBroad(ParamsDto params);
	
	//<!-- 전국통계: 방송국별 전송건수 -->
	public List receiptSend(ParamsDto params);
	
	//<!-- 전국통계: 방송국별 일별 전송건수 -->
	public List dailyReceiptSend(ParamsDto params);

	public List korLxInfrm(ParamsDto params);

	public List krGasInfrm(ParamsDto params);

	public void statInfrmType(@Param("areaCode")String areaCode, @Param("ifmId1")String ifmId1);

	public List<OptInftVo> statRtt();

	public void statRptType(@Param("areaCode")String areaCode, @Param("basScod")String basScod);

	public void statRttType(@Param("areaCode")String areaCode, @Param("ifmId1")String ifmId1);

	public void schmonInfrmDelete();
	
	public void schmonInfrmInsert();

	public List dayReceiptInfrm(ParamsDto params);
	
	public List dayReceiptCnt(ParamsDto params);

	public List orgType(ParamsDto params);

	public List searchStatusListToday(ReceiptSearchVO searchVO);
	
	public List searchStatusList(ReceiptSearchVO searchVO);
	
}


