<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
//한달 전
Calendar mon = Calendar.getInstance();
mon.add(Calendar.MONTH , -1);
System.out.println("%%%%%%%%%%%%%%%% : "+mon);
String beforeMonth = new java.text.SimpleDateFormat("YYYYMMdd").format(mon.getTime());
System.out.println("%%%%%%%%%%%%%%%% : "+beforeMonth);

//2
Date monthAgo=new Date(new Date().getTime() - 60*60*24*1000*30L);
System.out.println("%%%%%%%%%%%%%%%% : "+monthAgo);
%>


<!-- DateTimePicker -->
<script src="<%=request.getContextPath()%>/calender/moment.js"></script>
<script src="<%=request.getContextPath()%>/calender/mo_ko.js"></script>
<script src="<%=request.getContextPath()%>/calender/bootstrap-datetimepicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/calender/no-boot-calendar-custom.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/calender/datetimepickerstyle.css" />
<style>
	/* 24-10-29 : 통계에서 기간별/월별 버튼 관련 스타일 추가 */
	.stat_btn { /* 버튼 공통 스타일 */
		width:106px;
		height:38px;
		
		border-radius : 10px;
		font-size : 16px;
		
		background-color: #888;
		border : 1px solid #a5a5a5 !important;
		box-shadow: 0px 4px 5px #a5a5a5;
		
		font-weight : bold;
		color : white;
	}
	
	.stat_now_btn { /* 현재 클릭 된 버튼에 줄 스타일 */
		background-color:#137072;
		border : 1px solid #135252 !important;
	}
	
	.btn_div { /* 버튼의 위치 조정을 위한 컨테이너 박스 */
		margin-top : 25px;
	}
	
</style>
<script>
	$(document).ready(function(){
		console.log("표준화통계 진입");
		//데이트타임피커
		//시작 ,종료일 설정
		var stdate=moment().subtract(1, 'months').startOf('month').format('YYYY-MM-DD');
		var endate=moment().subtract(1, 'months').endOf('month').format('YYYY-MM-DD');
		//common.js에 생성한 함수 참조(달력생성)
		dateFunc('stdt','edt',stdate,endate);
		
		
		
		
		// 24-10-30 : 기간별/ 월별 버튼 클릭 시 이벤트 생성
		
		// 기간별 버튼을 클릭했을 경우
		$('#rangeDate').on('click', function(){
			
			// 클릭한 버튼에 따라 클래스 부여 및 제거
			$('#rangeDate').addClass('stat_now_btn');
			$('#monthDate').removeClass('stat_now_btn');
			
			$('#month_body').hide();
			$('#another_month').hide();
			$('#range_body').show();
			$('#schDtBody').show();
		});
		
		
		// 월별 버튼을 클릭했을 경우
		$('#monthDate').on('click', function(){
			
			// 클릭한 버튼에 따라 클래스 부여 및 제거
			$('#monthDate').addClass('stat_now_btn');
			$('#rangeDate').removeClass('stat_now_btn');
			
			$('#range_body').hide();
			$('#schDtBody').hide();
			$('#month_body').show();
			$('#another_month').show();
		});
		
	});
	
	function goStats(type, url){
		$("#org_id").val($("#orgIdSel").val());
		console.log("통계 서브밋?");

		// 날짜 데이터 보내기용
		var startDate = $('#stdt').val();
		var endDate = $('#edt').val();
		
		// 날짜 비교용
		var startDateType = new Date(startDate); 
		var endDateType = new Date(endDate);
		
			if(type == 'range') {

				if(startDate == "" || endDate == "") {
					alert("시작일과 종료일을 올바르게 선택해주세요.");
					return false;
				} else if(endDateType < startDateType){					
					alert("종료일은 시작일 이전일 수 없습니다.");
					return false;
				} else if (url=='stats/volunteer.do') {
					/* 26-01-26 : 사회봉사자 통계를 기간별로 옮기면서 기간 검색 조건식 추가 */
					
					var startDate = $('#stdt').val();
					var endDate = $('#edt').val();
					var start = new Date($('#stdt').val());
					var end = new Date($('#edt').val());
					
					if (start.getFullYear() !== end.getFullYear() ||start.getMonth()!== end.getMonth()) {
						alert('사회봉사자 일자별 통계는 같은 년월로만 조회 가능합니다.');
						return false;
					} else {
						$('#start_date').val(startDate.replaceAll("-",""));
						$('#end_date').val(endDate.replaceAll("-",""));
						$('#city').val("("+$("#areaOptSel option:selected").text().substr(0,2)+")");

						
						rkFlag = true;	
						frmExcel.action = '<c:url value="/"/>'+url;
						frmExcel.submit();	
						rkFlag = true;
					}
					
				}else {
					
					$('#start_date').val(startDate.replaceAll("-",""));
					$('#end_date').val(endDate.replaceAll("-",""));
					$('#city').val("("+$("#areaOptSel option:selected").text().substr(0,2)+")");

					
					rkFlag = true;	
					frmExcel.action = '<c:url value="/"/>'+url;
					frmExcel.submit();	
					rkFlag = true;
				}
			} else {
				
				$('#city').val("("+$("#areaOptSel option:selected").text().substr(0,2)+")");
				
				var stDt=$("#sDate").val();
				$("#org_id").val($("#orgIdSel").val());

				if($("#sDate").val().length==1){
					stDt="0"+stDt;
				}

				// 통신원 소속별 일자별 통계의 경우, YYYYMMDD 형식으로 보내야 하므로 뒤에 추가적으로 DD를 붙여야함
				if(url == 'stats/dayReceipt.do' || url == 'stats/volunteer.do') {
					$('#start_date').val($('#sYear').val()+stDt+'01');
					// 쿼리에서  알아서 잘라 마지막 일자를 구하기 때문에 DD는 01로 보낸다

					rkFlag = true;
					frmExcel.action = '<c:url value="/"/>'+url;
					frmExcel.submit();
					rkFlag = true; 
				} else if(url == 'stats/muInformer2.do') { // 무제보자의 경우 yyyy01 ~ yyyy12 값이 필요
					$('#start_date').val($('#sYear').val()+"01");
					var dateVal = ($('#sDate').val() < 10) ? '0' + Number($('#sDate').val()) : $('#sDate').val();
					
					$('#end_date').val($('#sYear').val() + dateVal);
					
					console.log("기준일 :" + $('#start_date').val());
					console.log("기준일 끝 :" + $('#end_date').val());
					
					rkFlag = true;
					frmExcel.action = '<c:url value="/"/>'+url;
					frmExcel.submit();
					rkFlag = true; 
					
				} else {
					$('#start_date').val($('#sYear').val()+stDt);

					rkFlag = true;
					frmExcel.action = '<c:url value="/"/>'+url;
					frmExcel.submit();
					rkFlag = true; 
				}

			}
}
	
$('#areaOptSel').on("click", function() {

		var areaCode = $('#areaOptSel').val();

		$.ajax({
		    type: 'POST',
		    url: '/stats/orgChange.ajax',
		    data: { "areaCode": areaCode },
		    dataType: 'json',  
		    success: function(data) {
		        var $select = $('#orgIdSel');
		        $select.empty();
		        
		        var informerOrgList = data.informerOrgList;

		        informerOrgList.forEach(function(informerOrg) {
		            var optionHTML = '<option value="' + informerOrg.ifmId2 + '">' + informerOrg.ifmName + '</option>';
		            $select.append(optionHTML);
		        });
		    },
		    error: function() {
		        console.log("오류 발생");
		    }
		});

});
</script>
    <div id="contentWrap">
        <!-- <div id="posi"><img src="../images/ico_home.gif" alt="home" />통계관리 > 표준화통계</div> -->
        <!-- contents -->
                <div id="contents">
                    <h1 class='content-title'>방송국별 통계</h1>
                    <div class="btn_div">
                    	<button id="rangeDate" class="stat_btn stat_now_btn">기간별</button>
                    	<button id="monthDate" class="stat_btn">월별</button>
                    </div>
                    
                    <!-- board_list -->
                    <div class="board_list">
                        <!-- 검색조건 영역 시작 -->
                        <form id="frmExcel" name="frmExcel" method="post">
                        <div class="rounding_wrap mgt10">
                            <div class="wrap_top"></div>
                            <div class="wrap_center">
                                <fieldset class="searchField">
	                                <legend>통계관리 검색조건(기간별)</legend>
									방송국선택 : 
							       <select id="areaOptSel" name="REGION_ID">
			                            <c:forEach var="informerRegion" items="${informerRegionList}" varStatus="idx">
			                                <option value="${informerRegion.areaCode}" ><c:out value="${informerRegion.areaName}"/></option>
			                            </c:forEach>
									</select>
									<input type="hidden" id="start_date" name="start_date">
                        			<input type="hidden" id="end_date" name="end_date"> 
									<input type="hidden" id="city" name="city"/>
									<input type="hidden" id="org_id" name="org_id"/>
									<div class="form_daterange" style="display: inline-flex;align-items: center;gap: 5px;" id="schDtBody">
										시작일 : 
										<div class='input-group date' id='datetimepicker1'>
											<input type='text' class="form-control dt_search" name="stdt" id="stdt" required/>
										</div>
										 종료일 :  
										<div class='input-group date' id='datetimepicker2'>
											<input type="text" class="form-control dt_search" id="edt" name="edt" required/>
										</div>
										
									</div> 
									<div id="another_month" style="display:none;">
										<c:set var="today" value="<%=monthAgo%>" />
		                                <c:set var="datetime"><fmt:formatDate value="${today}" pattern="yyyy" /></c:set>
		                                                                 기준년
		                                <select id="sYear" name="sYear">
				                            <c:forEach var="i" begin="0" end="10">
					                            <c:choose>
													<c:when test="${i == 0}">
						                                <option value="${datetime}" selected>${datetime}년</option>
						                            </c:when>
						                            <c:otherwise>
						                                <option value="${datetime-i}" >${datetime-i}년</option>
						                            </c:otherwise>		                            
					                            </c:choose>
				                            </c:forEach>
										</select>
										기준월
		                                <c:set var="today" value="<%=monthAgo%>" />
		                                <c:set var="datetime"><fmt:formatDate value="${today}" pattern="MM" /></c:set>
		                                <select id="sDate" name="sDate">
				                            <c:forEach var="i" begin="01" end="12">
					                            <c:choose>
													<c:when test="${i == datetime}">
						                                <option value="${i}" selected>${i}월</option>
						                            </c:when>
						                            <c:otherwise>
						                                <option value="${i}" >${i}월</option>
						                            </c:otherwise>		                            
					                            </c:choose>
				                            </c:forEach>
										</select>		
									</div>
                                </fieldset>
                            </div>
                            <div class="wrap_bottom"></div>
                        </div>
                        </form>
                       
                        <!-- 검색조건 영역 끝 -->
                        <!-- 검색결과 시작-->
                        <div class="admin_result_sc">
                            <table border="0" cellpadding="0" class="list01">
                                
                                <colgroup>                                
                                <col width="*" />
                                <col width="200" />
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th scope="col">내용</th>
                                        <th scope="col">다운로드</th>
                                    </tr>
                                </thead>
                                <tbody id="range_body"> <!--  기간별 테이블  -->
                                	<tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('range','stats/receiptBroad.do');">교통정보 제공대장</a></td>
                                        <td><a href="javascript:goStats('range','stats/receiptBroad.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('range','stats/receiptBroadTime.do');">교통정보 제공대장(시간대 별)</a></td>
                                        <td><a href="javascript:goStats('range','stats/receiptBroadTime.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                     <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('range','stats/extrBro.do');">긴급교통정보_방송현황분석</a></td>
                                        <td><a href="javascript:goStats('range','stats/extrBro.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('range','stats/disastorStat.do');">재난 제보건수</a></td>
                                        <td><a href="javascript:goStats('range','stats/disastorStat.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('range','stats/receiptUse.do');">교통정보 수집건수 및 활용실적</a></td>
                                        <td><a href="javascript:goStats('range','stats/receiptUse.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    <!-- 2024 08 28 장동현 담당 요청사항 -->
                                    <tr>
                                        <td class="txt_left">
                                        	<img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('range','stats/orgOrgSub.do');">통신원 중/소 분류별 통계</a>
                                        </td>
                                        <td><a href="javascript:goStats('range','stats/orgOrgSub.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>                                   
             
             						 <!--  24-11-18 : 제보자별 제보현황 -->
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('range','stats/informerReceipt.do');">제보자별 제보현황</a></td>
                                        <td><a href="javascript:goStats('range','stats/informerReceipt.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    
                                     <!-- 24-11-18 : 연간 제보자별 제보현황  => 기간별로 옮겨야 함 -->
                                   <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('range','stats/yearReceipt.do');">연간 제보자별 제보현황</a></td>
                                        <td><a href="javascript:goStats('range','stats/yearReceipt.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    <!-- 25-03-10 : 연간 지역별(소속별) 통계 -->
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('range','stats/yearOrgStat.do');">연간 지역(소속)별 실적통계</a></td>
                                        <td><a href="javascript:goStats('range','stats/yearOrgStat.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    <!-- 26-01-26 : 강원 요청으로 사회봉사자 통게 -> 기간별로 이동 -->
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('range','stats/volunteer.do');">사회봉사자 일자별 통계</a></td>
                                        <td><a href="javascript:goStats('range','stats/volunteer.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    <!-- 26-04-30 : 모바일 앱 제보 통계 -->
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('range','stats/receiptAppStat.do');">모바일 앱 제보 통계</a></td>
                                        <td><a href="javascript:goStats('range','stats/receiptAppStat.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                </tbody>
                                
                                
                                
                                
                                <tbody id="month_body" style="display : none;"> <!--  월별 테이블  -->
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('month','stats/muInformer.do');">월별 제보자별 제보건수</a></td>
                                        <td><a href="javascript:goStats('month','stats/muInformer.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>

                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('month','stats/receiptInformer.do');">교통통신원 제보건수 및 접수직원 가공건수</a></td>
                                        <td><a href="javascript:goStats('month','stats/receiptInformer.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('month','stats/incidentStats.do');">돌발 교통정보 표출 실적</a></td>
                                        <td><a href="javascript:goStats('month','stats/incidentStats.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('month','stats/muInformer2.do');">무 제보자 현황</a></td>
                                        <td><a href="javascript:goStats('month','stats/muInformer2.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('month','stats/informerStats.do');">교통정보 수집원 현황</a></td>
                                        <td><a href="javascript:goStats('month','stats/informerStats.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('month','stats/krGas.do');">한국가스기술공사 제보실적</a></td>
                                        <td><a href="javascript:goStats('month','stats/krGas.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('month','stats/korLx.do');">한국국토정보공사 제보현황</a></td>
                                        <td><a href="javascript:goStats('month','stats/korLx.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" />
                                        	<a href="javascript:goStats('month','stats/dayReceipt.do');">통신원 소속별 일자별 통계 </a>
											<select class="table_sel"  style="width:165px;" id="orgIdSel" name="orgId">
												 <c:choose>
                                                   	<c:when test="${fn:length(informerOrgList)==0}">
                                                   		
                                                   	</c:when>
                                                   	<c:otherwise>
                                                    		<c:forEach var="informerOrg" items="${informerOrgList}">
                                                    		<option value="${informerOrg.ifmId2}" 
                                                    			<c:if test="${informerOrg.ifmId2 eq informerInfo.orgId}">selected</c:if>
                                                    		>${informerOrg.ifmName}</option>
                                                   		</c:forEach>
                                                   	</c:otherwise>
                                                   </c:choose>
                                                <option value="">무소속</option>
                                            </select>
                                        </td>
                                        <td><a href="javascript:goStats('month','stats/dayReceipt.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                              
      
                                     <!-- 24-11-18 : 연간 제보자별 제보현황  => 기간별로 옮겨야 함 -->
                                   <!--  <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('stats/yearReceipt.do');">연간 제보자별 제보현황</a></td>
                                        <td><a href="javascript:goStats('stats/yearReciept.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr>
                                    
                                     24-11-18 : 연간지역(소속)별 통계 - 한마음대회 포상 관련 
                                    <tr>
                                        <td class="txt_left"><img src="../images/ico_excel.gif" alt="" class="mglsub03" /><a href="javascript:goStats('stats/yearOrgStat.do');">연간 지역(소속)별 실적통계</a></td>
                                        <td><a href="javascript:goStats('stats/yearOrgStat.do');"><img src="../images/btn_excel_down.gif" alt="엑셀다운로드" /></a></td>
                                    </tr> -->

                                </tbody>
                            </table>
                        </div>
                        <!-- 검색결과 끝-->
                        
                    </div>
                </div>
                <!-- //contents -->
    </div>

