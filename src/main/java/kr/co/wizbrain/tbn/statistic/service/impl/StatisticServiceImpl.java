package kr.co.wizbrain.tbn.statistic.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.wizbrain.tbn.award.vo.AwardVO;
import kr.co.wizbrain.tbn.comm.ParamsDto;
import kr.co.wizbrain.tbn.infrm.vo.InfrmVO;
import kr.co.wizbrain.tbn.option.vo.OptInftVo;
import kr.co.wizbrain.tbn.receipt.vo.popup.ReceiptSearchVO;
import kr.co.wizbrain.tbn.statistic.mapper.StatisticMapper;
import kr.co.wizbrain.tbn.statistic.service.StatisticService;
import kr.co.wizbrain.tbn.statistic.vo.StatisticVO;

/**
 * 사용자 서비스 구현 클래스
 * @author 미래전략사업팀 정다빈
 * @since 2020.07.23
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 *
 *   수정일            수정자              수정내용
 *  -------    -------- ---------------------------
 *  2021.11.   정다빈     최초 생성
 *  2024.10.24 정다빈     항목별 index 부여 및 개편
 */

@Service("statisticService")
public class StatisticServiceImpl implements StatisticService{
	
	@Resource(name="statisticMapper")
	private StatisticMapper statisticMapper;
	
	
	/**********************************************************************************************************************************************************/
	// 기간별 통계                                                                                                                                                                                                                                                                                              //
	/**********************************************************************************************************************************************************/
	
	//월별 제보자별 총 제보건수 집계
	@Override
	public List<StatisticVO> getReceiptCntList(StatisticVO stvo) {
		return statisticMapper.getReceiptCntList(stvo);
	}
	
	//전체 방송국 표출	
	@Override
	public List selectArea() {
		return statisticMapper.selectArea();
	}
		
	
	// 주소 라벨 출력
	@Override
	public List<InfrmVO> addRabel(List<InfrmVO> datalist) throws Exception {
		return statisticMapper.addRabel(datalist);
	}
	
		
	// 가. 교통 정보 제공 대장
	
	// 1) 통신원 유형별 현황
	// 1-1) 통신원 유형 title & 교통 정보 수집 건수 및 활용 실적 - 제보자 유형 title
	@Override
	public List selectInfrm() {
		return statisticMapper.selectInfrm();
	}
	
	// 1-2	&	6-1) 통신원별 전체 건수 & 교통 정보 수집 건수 및 활용 실적 - 제보자 유형 별 건수
	@Override
	public List informerTypeAll(ParamsDto params) throws Exception {
		return statisticMapper.informerTypeAll(params);
	}

	// 1-3	&	6-2) 통신원별 자국/ 타국 전체 건수 & 교통 정보 수집 건수 및 활용 실적 - 제보자 유형별 자국/ 타국 건수
	@Override
	public List informerTypeOurOther(ParamsDto params) throws Exception {
		return statisticMapper.informerTypeOurOther(params);
	}
	
	// 1-4) 통신원 유형별 상세 데이터
	@Override
	public List informerTypeData(ParamsDto params) throws Exception {
		return statisticMapper.informerTypeData(params);
	}
	
	
	// 24-11-19 : 제보자별 제보현황
	@Override
	public List informerReport(ParamsDto params) throws Exception {
		return statisticMapper.informerReport(params);
	}
	
	// 24-11-20 : 제보자별 제보현황 - 총 인원수
	@Override
	public String allInformer(ParamsDto params) throws Exception {
		return statisticMapper.allInformer(params);
	}
	
	// 24-11-20 : 제보자별 제보현황 - 총 건수
	@Override
	public String allReport(ParamsDto params) throws Exception {
		String allReport = statisticMapper.allReport(params);
		
		if(allReport == null) {
			allReport = "0";
			return allReport;
		} else {
			return allReport;
		}
	}
	
	// 연간 제보자별 제보현황
		public List yearReceipt(ParamsDto params) throws Exception {
			return statisticMapper.yearReceipt(params);
		}
		
		public List totalList(ParamsDto params) throws Exception {
			return statisticMapper.totalList(params);
		}
		
		public List totalListOrg(ParamsDto params) throws Exception {
			return statisticMapper.totalListOrg(params);
		}
		
		public List<AwardVO> perList(ParamsDto params) throws Exception {
			return statisticMapper.perList(params);
		}
		
		
		// 연간지역소속별통계
		public List yearOrgStat(ParamsDto params) throws Exception {
			return statisticMapper.yearOrgStat(params);
		}
	
	// 2) 제보 수단별 현황
	// 2-1) 제보 수단 title
	@Override
	public List selectRtt() {
		return statisticMapper.selectRtt();
	}

	// 2-2) 제보 수단별 전체 건수
	public List reportmeanTypeAll(ParamsDto params) throws Exception {
		return statisticMapper.reportmeanTypeAll(params);
	}

	// 2-3) 제보 수단 별 상세 데이터
	public List reportmeanTypeOurOther(ParamsDto params) throws Exception {
		return statisticMapper.reportmeanTypeOurOther(params);
	}
	// 2-4) 제보 수단 별 상세 데이터
	public List reportmeanTypeData(ParamsDto params) throws Exception {
		return statisticMapper.reportmeanTypeData(params);
	}
	
	
	// 3) 제보 유형별 현황
	// 3-1) 제보 유형별 title
	@Override
	public List selectRpt() {
		return statisticMapper.selectRpt();
	}

	// 3-2) 제보 유형별 전체 건수
	public List reportTypeAll(ParamsDto params) throws Exception {
		return statisticMapper.reportTypeAll(params);
	}

	// 3-3) 제보 유형별 자국/타국 건수
	public List reportTypeOurOther(ParamsDto params) throws Exception {
		return statisticMapper.reportTypeOurOther(params);
	}

	// 3-4) 제보 유형별 상세 데이터
	public List reportTypeData(ParamsDto params) throws Exception {
		return statisticMapper.reportTypeData(params);
	}
	
	
	
	
	
	// 나. 긴급 교통 정보_ 방송 현황 분석
	@Override
	public List extrBro(ParamsDto params) {
		return statisticMapper.extrBro(params);
	}
	
	
	
	
	// 다. 재난 제보 건수
	@Override
	public List disastorStat(ParamsDto params) {
		return statisticMapper.disastorStat(params);
	}
	
	
	
	
	// 라. 무제보자 현황
	@Override
	public List muJeboList(ParamsDto params) {
		return statisticMapper.muJeboList(params);
	}
	
	
	
	
	// 마. 교통 정보 수집 건수 및 활용 실적
	
	// 1) 수집 건수
	// 1-1) 가. 1-1) selectInfrm()
	// 1-2) 가. 1-2) informerTypeAll(params)
	// 1-3) 가. 1-3) informerTypeOurOther(params)
	
	// 1-4) 일별 상세
	@Override
	public List receiptUseDaily(ParamsDto params) {
		return statisticMapper.receiptUseDaily(params);
	}
	
	
	// 2) 활용 실적
	// 2-1) 가. 1-1) selectInfrm()
	
	// 2-2) 월별 합계
	@Override
	public List monthSendYNA07A08(ParamsDto params) {
		return statisticMapper.monthSendYNA07A08(params);
	}
	
	// 2-3) 일별 방송국별 데이터
	@Override
	public List dailyRegionSendYNA07A08(ParamsDto params) {
		return statisticMapper.dailyRegionSendYNA07A08(params);
	}
	
	
	
	
	
	// 바. 통신원 중/소 분류별 통계
	
	// 1) 통신원 중소 분류별 통계
	// 1-1) 
	@Override
	public List statDateCal(ParamsDto params) {
		return statisticMapper.statDateCal(params);
	}
	
	// 1-2)
	@Override
	public List orgOrgSub(ParamsDto params) {
		return statisticMapper.orgOrgSub(params);
	}
	
	
	@Override
	public List timeBroadData (ParamsDto params) {
		return statisticMapper.timeBroadData(params);
	}
	
	
	@Override
	public List timeInformer (ParamsDto params) {
		return statisticMapper.timeInformer(params);
	}
	
	@Override
	public List timeRptType (ParamsDto params){
		return statisticMapper.timeRptType(params);
	}
	
	@Override
	public List timeRptMean (ParamsDto params) {
		return statisticMapper.timeRptMean(params);
	}
	
	
	
	
	
	
	/**********************************************************************************************************************************************************/
	// 월별 통계                                                                                                                                                                                                                                                                                              //
	/**********************************************************************************************************************************************************/
	
	
	// 가. 월별 제보자별 제보 건수
	
	// 1. 월별 제보자별 제보 건수
	// 1-1) 제보자 리스트
	@Override
	public List monInfrmList(ParamsDto params) {
		/*return statisticMapper.muJeboList(params);*/
		return statisticMapper.monInfrmList(params);
	}
	
	// 1-2) 월별 건수
	@Override
	public List monInfrmCnt(ParamsDto params) {
		return statisticMapper.monInfrmCnt(params);
	}
	
	
	
	
	
	// 나. 교통 통신원 제보 건수 및 접수 직원 가공 건수
	
	// 1-1) 제보자 유형 title => 가. 1-1) selectInfrm()
	
	// 1-2) 월별 전체 수집 건수
	@Override
	public List monthReceipt(ParamsDto params) {
		return statisticMapper.monthReceipt(params);
	}
	
	// 1-3) 월별 자국 수집 건수
	@Override
	public List monthOurReceipt(ParamsDto params) {
		return statisticMapper.monthOurReceipt(params);
	}
	
	// 1-4) 월별 타국 수집 건수
	@Override
	public List monthOtherReceipt(ParamsDto params) throws Exception {
		return statisticMapper.monthOtherReceipt(params);
	}
	
	// 1-5) 월별 제보자 수
	@Override
	public List monthInformer(ParamsDto params) throws Exception {
		return statisticMapper.monthInformer(params);
	}
	
	// 1-6) 월별 1건 이상 제보자 수
	@Override
	public List monthInformer1Inform(ParamsDto params) throws Exception {
		return statisticMapper.monthInformer1Inform(params);
	}
	
	
	
	
	
	// 다. 돌발 교통 정보 표출 실적
	
	// 1) 돌발 교통 정보 수집 건수
	// 1-1) 무제보자 통신원 목록
	@Override
	public List nationalIncident(ParamsDto params) {
		return statisticMapper.nationalIncident(params);
	}
	
	
	
	
	
	// 라. 교통 정보 수집원 현황
	
	// 1) 교통 정보 수집원 현황
	// 1-1) 교통정보 수집원 현황
	@Override
	public List informerStats(ParamsDto params) {
		return statisticMapper.informerStats(params);
	}
	
	
	
	
	
	// 마. 한국 가스 기술 공사 제보 실적
	
	// 1) 가스기술공사 제보 실적
	// 1-1) 제보자 유형별 건수
	@Override
	public List krGasMonCnt(ParamsDto params) {
		return statisticMapper.krGasMonCnt(params);
	}
	
	
	
	
	// 바. 한국 국토 정보 공사 제보 현황
	
	// 1) 국토정보공사 제보 현황
	// 1-1) 제보자 유형별 건수
	@Override
	public List korLxMonCnt(ParamsDto params) {
		return statisticMapper.korLxMonCnt(params);
	}
	
	
	
	
	
	// 사. 통신원 소속별 일자별 통계
	
	// 1) 일자별 제보 건수
	@Override
	public List dayReceipt(ParamsDto params) {
		return statisticMapper.dayReceipt(params);
	}
	
	
	
	
	
	// 아. 사회 봉사자 일자별 통계
	
	// 1) 일자별 제보 건수
	@Override
	public List volunteer(ParamsDto params) {
		return statisticMapper.volunteer(params);
	}
	
	
	
	
	
	// 자. 연간 제보자별 제보 현황
	
	
	
	
	
	// 차. 연간 지회(통신원 중분류) 실적 평가
	
	
	
	
	
	// 카. 제보자별 제보 현황
	
	
	
	// 타. 모바일 앱 제보 통계
	@Override
	public List receiptAppStat(ParamsDto params) {
		return statisticMapper.receiptAppStat(params);
	}
	
	
	
	
	
	
	
	
	
	/**********************************************************************************************************************************************************/
	// 컨트롤러 미 구현 기능들                                                                                                                                                                                                                                                                                              //
	/**********************************************************************************************************************************************************/
	
	@Override
	public List korLxInfrm(ParamsDto params) {
		return statisticMapper.korLxInfrm(params);
	}
	
	@Override
	public List krGasInfrm(ParamsDto params) {
		return statisticMapper.krGasInfrm(params);
	}
	
	@Override
	public void statInfrmType(String areaCode, String ifmId1) {
		 statisticMapper.statInfrmType(areaCode,ifmId1);
	}
		
	@Override
	public List<OptInftVo> statRtt() {
		return statisticMapper.statRtt();
	}
	@Override
	public void statRptType(String areaCode, String basScod) {
		 statisticMapper.statRptType(areaCode,basScod);
	}
	@Override
	public void statRttType(String areaCode, String ifmId1) {
		 statisticMapper.statRttType(areaCode,ifmId1);
	}
	
	public void schmonInfrmInsert(){
		statisticMapper.schmonInfrmInsert();
	}
	
	public void schmonInfrmDelete(){
		statisticMapper.schmonInfrmDelete();
	}
	
	@Override
	public List searchStatusListToday(ReceiptSearchVO searchVO) {
		return statisticMapper.searchStatusListToday(searchVO);
	}
	
	@Override
	public List searchStatusList(ReceiptSearchVO searchVO) {
		return statisticMapper.searchStatusList(searchVO);
	}
	
	@Override
	public List orgType(ParamsDto params) {
		return statisticMapper.orgType(params);
	}
	
}
