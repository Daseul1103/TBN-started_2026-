<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/pagination.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/jquery-ui-1.9.0.custom.css" rel="stylesheet"  />
<script  type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script  type="text/javascript" charset="utf-8"  src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/jquery.pagination.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>

<!-- DateTimePicker -->
<script src="<%=request.getContextPath()%>/calender/moment.js"></script>
<script src="<%=request.getContextPath()%>/calender/mo_ko.js"></script>
<script src="<%=request.getContextPath()%>/calender/bootstrap-datetimepicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/calender/no-boot-calendar-custom.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/calender/datetimepickerstyle.css" />

<!-- 주소 관련 api -->
<script src="<%=request.getContextPath()%>/js/address/address.js"></script>
<style>
	.tr_style, .tr_style > th, .tr_style > td {
		border : 1px solid #C7C7C7;
		width: 87.5px;
	}

	.tr_style > th {
		background-color : #f5f5f5;
	}
	
	
		/* 버튼 그룹 */
	.button-group {
	    display: flex;
	    gap: 12px;
	}
	
	/* 기본 버튼 공통 */
	.btn {
	    display: inline-flex;
	    align-items: center;
	    gap: 8px;
	    height: 38px;
	    padding: 0 16px;
	    font-size: 14px;
	    font-weight: 500;
	    border-radius: 12px;
	    cursor: pointer;
	    transition: all 0.2s ease;
	    border: 1px solid #e5e7eb;
	    background: #f9fafb;
	    color: #374151;
	    box-shadow: 0 1px 2px rgba(0,0,0,0.05);
	}
	
	/* 아이콘 */
	.btn-icon {
	    width: 16px;
	    height: 16px;
	    object-fit: contain;
	}
	
	/* ------------------ */
	/* Secondary (해촉, 취소 동일) */
	/* ------------------ */
	.btn-secondary {
	    background: white;
	    border: 1px solid #e5e7eb;
	    color: #374151;
	}
	
	.btn-secondary:hover {
	    background: #f3f4f6;
	    border-color: #d1d5db;
	}
	
	/* ------------------ */
	/* Danger (삭제) */
	/* ------------------ */
	.btn-danger {
	    background: #fff;
	    border: 1px solid #fca5a5;
	    color: #ef4444;
	}
	
	.btn-danger:hover {
	    background: #fee2e2;
	    border-color: #ef4444;
	}
	
	/* ------------------ */
	/* Primary (정보 수정) */
	/* ------------------ */
	.btn-primary {
	    background: #2563eb;
	    border: 1px solid #2563eb;
	    color: #fff;
	}
	
	.btn-primary:hover {
	    background: #1e40af;
	    border-color: #1e40af;
	}
	
	/* 클릭 시 살짝 눌림 효과 */
	.btn:active {
	    transform: scale(0.97);
	}
	
	
	
	
	
	/* 상단 이름, 통신원 정보, 소속 등 */
	
	/* 이름 + 배지 한 줄 정렬 */
	.name-wrapper {
	    display: flex;
	    align-items: center;
	    gap: 8px;
	    flex-wrap: wrap; /* 여러 개일 경우 줄바꿈 */
	    margin-top: 20px;
	    width : 486px;
	}
	
	.informer-name {
	    font-size: 22px;
	    font-weight: 700;
	    margin: 0;
	    margin-left: 25px;
	    margin-right: 5px;
	}
	
	/* 공통 배지 */
	.badge {
	    font-size: 12px;
	    font-weight: 600;
	    padding: 4px 10px;
	    border-radius: 999px; /* pill */
	    display: inline-flex;
	    align-items: center;
	}
	
	/* 최고 통신원 - 초록 */
	.badge-best {
	    background: #dcfce7;
	    color: #15803d;
	}
	
	/* 명예 통신원 - 파랑 */
	.badge-honor {
	    background: #dbeafe;
	    color: #1d4ed8;
	}
	
	/* 자원봉사 - 회색 */
	.badge-service {
	    background: #f3f4f6;
	    color: #374151;
	}
	
	
	
	/* 전체 박스 */
	.date-info {
	    display: flex;
	    align-items: center;
	    gap: 24px;
	    margin-left: 120px;
	    margin-top: 10px;
	}
	
	/* 각 항목 */
	.date-item {
	    display: flex;
	    flex-direction: column;
	}
	
	/* 라벨 */
	.date-label {
	    font-size: 13px;
	    color: #6b7280; /* 연회색 */
	    margin: 0;
	}
	
	/* 값 */
	.date-value {
	    font-weight: 700;
	    font-size: 16px;
	    color: #111827;
	    margin-left: 6px;
	}
	
	/* 유효기간 (빨강 강조) */
	.date-expired {
	    font-weight: 700;
	    font-size: 16px;
	    color: #ef4444;
	}
	
	/* 가운데 세로 구분선 */
	.divider {
	    width: 1px;
	    height: 40px;
	    background: #e5e7eb;
	}
	
	
	/* 전체 정렬 */
	.status-wrapper {
	    display: flex;
	    align-items: center;
	    gap: 12px;
	    margin-left: 50px;
	}
	
	/* 라벨 */
	.status-label {
	    font-size: 16px;
	    color: #6b7280;
	    margin: 0;
	}
	
	/* 공통 배지 */
	.status-badge {
	    display: inline-flex;
	    align-items: center;
	    gap: 6px;
	    padding: 6px 14px;
	    font-size: 13px;
	    font-weight: 600;
	    border-radius: 999px;
	    margin-top :2px;
	}
	
	/* 상태 원 */
	.status-dot {
	    width: 8px;
	    height: 8px;
	    border-radius: 50%;
	}
	
	/* 위촉 (초록) */
	.status-active {
	    background: #dcfce7;
	    color: #15803d;
	}
	
	.status-active .status-dot {
	    background: #22c55e;
	}
	
	/* 해촉 (빨강 버전) */
	.status-inactive {
	    background: #fee2e2;
	    color: #b91c1c;
	}
	
	.status-inactive .status-dot {
	    background: #ef4444;
	}
	
	
	.ellipsis-text {
	    font-size: 16px;
	    max-width: 500px;     /* 원하는 너비로 조절 */
	    white-space: nowrap;  /* 한 줄 유지 */
	    overflow: hidden;     
	    text-overflow: ellipsis;
	    margin: 0;
	}
	
	
	
	
	
	.form-table{
    width:904px;
    border-collapse:collapse;
    font-size:14px;
    margin-left:63px;
    margin-top:20px;
}

.form-table th,
.form-table td{
    border:1px solid #dcdcdc;
    padding:2px 12px;
    height:40px;
}

.form-table th{
    background:#eef1f4;
    text-align:left;
    font-weight:500;
}

.form-table input[type="text"],
.form-table select
,.form-table textarea{
    height:32px;
    width:200px;
    border:1px solid #cfcfcf;
    border-radius:4px;
    padding:0 8px;
}

.form-table td{
    background:#ffffff;
}

.form-table label{
    margin-right:20px;
}
</style>
<script>
	$(document).ready(function(){
		
		createSelect();
		
		
		function createSelect() {
			
			// 오늘 년도 구하기
			var startYear = new Date().getFullYear() -8;
			var informer = '${informerInfo.informerId}';
			
			for(var i = 8; i > 0; i--) {
				var inputYear = startYear + i;
				$('#selectYear').append('<option value="' + inputYear + '">'+ inputYear + '</option>');
			} 
			
			 
			$.ajax({
				url : '/infrm/monthReport.do',
				data : { "selectYear" : startYear+8, "informerId" : informer},
				type : 'post',
				success : function(data) {

					// 원래 데이터 비워두기
					$('#avgReport td').each(function() {
		                $(this).text('0');
		            });
					
					$('#celarTd').text('');
					
					if(data.monthReport.length == 0) {
						$('#avgReport td').each(function() {
			                $(this).text('0');
			            });
						
						$('#celarTd').text('');
					} else {
						var ListLeng = data.monthReport.length;
						
						// 월별 값 넣기
						for(var i = 0; i < ListLeng ; i++) {
							nowMonth = data.monthReport[i].month;
							nowCount = data.monthReport[i].row_COUNT;
							
							$('#mon'+nowMonth).text(nowCount);
						}
						
						var minSum1 = 0;
						var minSum2 = 0;
						
						// 1~6 소계
						for (var i = 1; i <= 6; i++) {
						    var value = parseFloat($('#mon' + (i < 10 ? '0' + i : i)).text()) || 0;
						    minSum1 += value; 
						}

						$('#minSum').text(minSum1);
						
						// 7~12 소계
						for (var i = 7; i <= 12; i++) {
						    var value = parseFloat($('#mon' + (i < 10 ? '0' + i : i)).text()) || 0; 
						    minSum2 += value; 
						}
						
						$('#minSum2').text(minSum2);
						$('#sum').text(minSum1+minSum2);
						
					}
					
				}, error : function() {
					console.log("데이터 전송에 오류 발생");
				}
			});
		}
		
		
		 $('#selectYear').on('change', function(){
			var selectVal = $(this).val();
			
			if(selectVal == "clear") {
				$('#avgReport td').each(function() {
	                $(this).text('0');
	            });
				
				$('#celarTd').text('');
			} else {
				var informer = '${informerInfo.informerId}';

				$.ajax({
					url : '/infrm/monthReport.do',
					data : { "selectYear" : selectVal, "informerId" : informer},
					type : 'post',
					success : function(data) {

						// 원래 데이터 비워두기
						$('#avgReport td').each(function() {
			                $(this).text('0');
			            });
						
						$('#celarTd').text('');
						
						if(data.monthReport.length == 0) {
							$('#avgReport td').each(function() {
				                $(this).text('0');
				            });
							
							$('#celarTd').text('');
						} else {
							var ListLeng = data.monthReport.length;
							
							// 월별 값 넣기
							for(var i = 0; i < ListLeng ; i++) {
								nowMonth = data.monthReport[i].month;
								nowCount = data.monthReport[i].row_COUNT;
								
								$('#mon'+nowMonth).text(nowCount);
							}
							
							var minSum1 = 0;
							var minSum2 = 0;
							
							// 1~6 소계
							for (var i = 1; i <= 6; i++) {
							    var value = parseFloat($('#mon' + (i < 10 ? '0' + i : i)).text()) || 0;
							    minSum1 += value; 
							}

							$('#minSum').text(minSum1);
							
							// 7~12 소계
							for (var i = 7; i <= 12; i++) {
							    var value = parseFloat($('#mon' + (i < 10 ? '0' + i : i)).text()) || 0; 
							    minSum2 += value; 
							}
							
							$('#minSum2').text(minSum2);
							$('#sum').text(minSum1+minSum2);
							
						}
						
					}, error : function() {
						console.log("데이터 전송에 오류 발생");
					}
				});
				
			}
		}); 
		
	});
</script>
</head>
<body style="background-color:rgba(228, 228, 228, 0.31);">
<div id="container">
    <div id="topWrap" style="width:1100px;"> 
        <!-- bodyWrap -->
        <div id="bodyWrap" class="clfix" style="width:1100px;">
            <!-- contentWrap -->
            <div id="contentWrap" style="width:1050px;">
  				<!-- 통신원 등록 시작 -->
  				<!-- 통신원 등록 상단 헤더(타이틀, 버튼) -->
            	<div style="display: flex; padding-left: 20px; padding-top: 20px; justify-content: space-between; align-items: center;">
            		<div style="display: flex; align-items: baseline; border-left: 5px solid blue;">
            			<h1 style="font-size: 28px; margin-right: 20px; margin-left: 5px;">통신원 등록</h1>
            			<p style="font-size:15px;"><span style="color: rgba(47, 46, 47, 0.77);">통신원 관리 ></span> 통신원 등록</p>
            		</div>
            		<div class="button-group">   
					    <button class="btn btn-secondary" onclick="self.close();">
					        <img src="/images/cancleInformer.svg" class="btn-icon"/>
					        취소
					    </button>
					
					    <button class="btn btn-primary" onclick="saveInformer();" alt="등록" title="등록">
					        <img src="/images/updateInformer.svg" class="btn-icon"/>
					        정보 등록
					    </button>
					
					</div>
            	</div>
            	<!-- 통신원 등록 상단 헤더(타이틀, 버튼) 끝 -->
            	<!-- form 시작 -->
            	<form id="informerEditFrm" name="informerEditFrm" method="post" enctype="multipart/form-data">
            		<input type="hidden" id="pageDiv" name="pageDiv" value="${pageDiv}"></input>
	            	<!-- 통신원 등록 - 기본정보 입력란 -->
	            	<div style="background-color: white;width: 1030px;height: 520px;margin-left: 20px;margin-top: 15px;border-radius: 10px; display: flex; flex-direction: column;">
	            		<!-- 기본정보 타이틀 -->
	            		<div style="display: flex; width: 1030px; height: 40px; align-items: center; margin-top: 10px; margin-bottom:5px;">
		            		<!-- 기본정보 아이콘 -->
		            		<div style="width: 35px; height: 35px; background-image: url(/images/standardInfo.svg); background-size: contain; margin-left: 20px; margin-right: 8px;"></div>
		            		<p style="font-size : 18px; font-weight : 500;">기본 정보</p>
		            	</div>
		            	
		            	<div style="width: 1030px; height: 205px;">
							<table class="form-table">
								<tr>
								    <th rowspan="2">통신원 종류</th>								
								    <th>통신원 분류 1)</th>								
								    <td>
								        <input type="radio" name="flagBroad" id="flagBroad0" value="0"/>
								        <label for="flagBroad0">일반통신원</label>
								
								        <input type="radio" name="flagBroad" id="flagBroad1" value="1"/>
								        <label for="flagBroad1">방송통신원</label>
								    </td>
								</tr>								
								<tr>								
								    <th>통신원 분류 2)</th>								
								    <td>
								        <input type="checkbox" name="flagBest" id="flagBest" value="Y"/>
								        <label for="flagBest">최고통신원</label>
								
								        <input type="checkbox" name="honor" id="honor" value="Y"/>
								        <label for="honor">명예통신원</label>
								
								        <input type="checkbox" name="flagService" id="flagService"/>
								        <label for="flagService">자원봉사</label>
								    </td>								
								</tr>								
							</table>
		
							<table class="form-table">
								<colgroup>
									<col style="width:183px"/>
									<col style="width:269px"/>
									<col style="width:183px"/>
									<col/>
								</colgroup>
								
								<tr>								
								    <th>통신원 이름 <span style="color:red">*</span></th>								
								    <td>
										<input type="text" id="informerName" name="informerName"/>
								    </td>
								    <th>통신원 ID <span style="color:red">*</span></th>
								    <td>
								    	<input type="text" id="actId" name="actId"/>
								    </td>
								</tr>	
															
								<tr>								
							    	<th>통신원 유형</th>								
								    <td>
										<select style="width:155px;" id="informerTypeSel" name="informerType">
											<c:forEach var="informerType" items="${informerTypeList}">
												<option value="${informerType.ifmId1}" 
												<c:if test="${informerType.ifmId1 eq informerInfo.informerType}">selected</c:if>>
												${informerType.ifmName}</option>
											</c:forEach>
										</select>
								    </td>
								    <th>소속 방송국</th>
								    <td>
									    <select style="width:155px;" id="areaCodeSel" name="areaCode">
											<c:forEach var="informerArea" items="${informerAreaList}">
												<option value="${informerArea.areaCode}" <c:if test="${informerArea.areaCode eq informerInfo.areaCode}">selected</c:if>>${informerArea.areaName}</option>
											</c:forEach>
										</select>	
								    </td>						
								</tr>
								
								<tr>								
								    <th>소속 기관</th>								
								    <td>
                                        <select style="width:155px;" id="orgIdSel" name="orgId">
													 <c:choose>
	                                                    	<c:when test="${fn:length(informerOrgList)==0}">
	                                                    		<option value="">무소속</option>
	                                                    	</c:when>
	                                                    	<c:otherwise>
	                                                     			<option value="" selected>무소속</option>
	                                                     		<c:forEach var="informerOrg" items="${informerOrgList}">
		                                                    		<option value="${informerOrg.ifmId2}" 
		                                                    		<c:if test="${informerOrg.ifmId2 eq informerInfo.orgId}">selected</c:if>>
		                                                    		${informerOrg.ifmName}</option>
	                                                    		</c:forEach>
	                                                    	</c:otherwise>
	                                                    </c:choose>
	                                                 <!-- <option value="">무소속</option> -->
												</select>
								    </td>
								    <th>소속 기관(세부)</th>
								    <td>
								    	<select style="width:155px;" id="orgSubIdSel" name="orgSubId">
                                            <c:choose>
                                            	<c:when test="${fn:length(informerOrgSubList)==0}"></c:when>
                                                <c:otherwise>
                                                   	<c:forEach var="informerOrgSub" items="${informerOrgSubList}">
                                                    	<option value="${informerOrgSub.ifmId3}" <c:if test="${informerOrgSub.ifmId3 eq informerInfo.orgSubId}">selected</c:if>>${informerOrgSub.ifmName}</option>
		                                            </c:forEach>
                                                 </c:otherwise>
                                             </c:choose>
	                                            <option value="">무소속</option>
                                        </select>
								    </td>
								</tr>
								
								<tr>								
								    <th>사진</th>								
								    <td>
										<input type="hidden" class="input_base" id="localFilePath" name="localFilePath" value="${informerInfo.localFilePath}" />
                                        <input type="file" id="file" name="file" onchange="changePicture($(this));" style="width:200px;"/>
								    </td>
								    <th>활동 상태</th>
								    <td>
										<input type="text" class="input_base" id="flageAct" name="flageAct" value="신규가입" disabled />
								    </td>
								</tr>									
							</table>
							
							<table class="form-table">
								<colgroup>
									<col style="width:183px"/>
									<col style="width:269px"/>
									<col style="width:183px"/>
									<col/>
								</colgroup>
								<tr>								
								    <th>가입일자</th>								
								    <td>
								    	<c:set var="nowDate" value="<%=new java.util.Date() %>"/>
										<fmt:formatDate value="${nowDate}" pattern="yyyy-MM-dd" />
								    </td>
								    <th>생일</th>
								    <td>
										<div class='input-group date' id='datetimepicker1' style="display: inline;">
											<input type='text' class="dateText" name="birthday" id="birthday" style="width:120px;"/>
											<input type="radio" class="input_base" id="birthdayDiv2" name="birthdayDiv" value="+" checked />
	                                        <label style="color: #585c5f; margin-right:0px;">양력</label>
	                                        <input type="radio" class="input_base" id="birthdayDiv2" name="birthdayDiv" value="-" />
	                                        <label style="color: #585c5f; margin-right:0px;">음력</label>
										</div>
								    </td>
								</tr>
								<tr>								
								    <th>최종학력</th>								
								    <td>
								    	<input type="text" id="lastSchool" name="lastSchool" />
								    </td>
								    <th>통신원 직업</th>
								    <td>
										<input type="text" id="informerJob" name="informerJob" />
								    </td>
								</tr>
							</table>	
		            	</div>
	            	</div>
	            	<!-- 통신원 등록 - 기본정보 입력란 -->
	            	
	            	
	            	<div style="background-color: white;width: 1030px;height: 450px;margin-left: 20px;margin-top: 15px;border-radius: 10px; display: flex; flex-direction: column;">
	            		<!-- 기본정보 타이틀 -->
	            		<div style="display: flex; width: 1030px; height: 40px; align-items: center; margin-top: 10px; margin-bottom:5px;">
		            		<!-- 기본정보 아이콘 -->
		            		<div style="width: 35px; height: 35px; background-image: url(/images/address.svg); background-size: contain; margin-left: 20px; margin-right: 8px;"></div>
		            		<p style="font-size : 18px; font-weight : 500;">연락처 /주소</p>
		            	</div>
		            	
		            	<div style="width: 1030px; height: 205px;">
							<table class="form-table">
								<colgroup>
									<col style="width:183px"/>
									<col style="width:269px"/>
									<col style="width:183px"/>
									<col/>
								</colgroup>
								
								<tr>							
								    <th>핸드폰 번호 <span style="color:red">*</span></th>								
								    <td>
								        <input type="text" id="phoneCell" name="phoneCell" style="font-weight: bold;" maxlength="11" onkeydown='return onlyNumber(event)' onkeyup='removeChar(event)'/>
								    </td>
								    <th>집 전화번호</th>								
								    <td>
								        <input type="text" id="phoneHome" name="phoneHome" style="" maxlength="13" onkeydown='return onlyNumber(event)' onkeyup='removeChar(event)'/>
								    </td>
								</tr>								
								<tr>																    								
								</tr>			
								<tr>								
								    <th>TRS</th>								
								    <td>
								        <input type="text" id="trsNo" name="trsNo" />
								    </td>	
								    <th>회사 전화번호</th>
								    <td>
								    	<input type="text" id="phoneOffice" name="phoneOffice" style="" maxlength="13" onkeydown='return onlyNumber(event)' onkeyup='removeChar(event)'/>
								    </td>							
								</tr>
								<tr>								
								    <th>차량 번호</th>								
								    <td>
								        <input type="text" id="carNum" name="carNum"  maxlength="15"/>
								    </td>
								    <th>차량 종류</th>
								    <td>
								    	<input type="text" id="carType" name="carType"  maxlength="15"/>
								    </td>								
								</tr>					
							</table>
							
							<table class="form-table">
								<tr>		
									<th rowspan="2">주소(개인)</th>								
								    <th>우편 번호 / 주소</th>								
								    <td>								        
                                        <input type="hidden" id="address" name="address"/>
                                        <input type="hidden" id="addressHome" name="addressHome"/>
                                        <input type="text" id="adressHome3" name="zipcode" readonly></input>
                                        <input type="text" id="addressHome1" name="addressHome1" style="margin-right:10px; width:230px;" readonly/>
                                        <input type="button" id="searchAddress1" name="searchAddress1" onclick="searchAddress(1);" style="width:70px;" value="주소검색"/>
								    </td>								
								</tr>
								<tr>								
								    <th>상세 주소</th>								
								    <td>
								        <input type="text" id="addressHome2" name="addressHome2" style="width:452px;"/>
								    </td>								
								</tr>
								<tr>		
									<th rowspan="2">주소(회사)</th>								
								    <th>우편 번호 / 주소</th>								
								    <td>
								        <input type="hidden" id="addressOffice" name="addressOffice" />
                                        <input type="text" id="addressOffice3" name="zipcode2" readonly></input>
                                        <input type="text" id="addressOffice1" name="addressOffice1" style="margin-right:10px; width:230px;" readonly/>
                                        <input type="button" id="searchAddress2" name="searchAddress2" onclick="searchAddress(2);" style="width:70px;" value="주소검색"/>
								    </td>								
								</tr>
								<tr>								
								    <th>상세 주소</th>								
								    <td>
								        <input type="text" id="addressOffice2" name="addressOffice2" style="width:452px;" />
								    </td>								
								</tr>
							
							</table>
		               </div>
		            	
		            </div>
		            
		            <div style="background-color: white;width: 1030px;height: 300px;margin-left: 20px;margin-top: 15px;border-radius: 10px; display: flex; flex-direction: column;">
	            		<!-- 기본정보 타이틀 -->
	            		<div style="display: flex; width: 1030px; height: 40px; align-items: center; margin-top: 10px; margin-bottom:5px;">
		            		<!-- 기본정보 아이콘 -->
		            		<div style="width: 35px; height: 35px; background-image: url(/images/memoIcon.svg); background-size: contain; margin-left: 20px; margin-right: 8px;"></div>
		            		<p style="font-size : 18px; font-weight : 500;">메모</p>
		            	</div>
		            	
		            	<div style="width:1030px;">
						    <table class="form-table">
								<colgroup>
									<col style="width:452px"/>
									<col style="width:452px"/>

								</colgroup>
								
						        <tr>
						            <th>전달 사항(500자)</th>
						            <th>메모(100자)</th>
						        </tr>
						
						        <tr>
						            <td rowspan="3">
						            	<textarea class="table_sel" id="memo" name="memo" cols="50" rows="8" style="width:400px; height:110px;resize:none;" maxlength="500" ></textarea>
						            </td>
						            <td>
						            	<input type="text" id="memo1" name="memo1" maxlength="100" style="width:400px;"/>
						            </td>
						        </tr>
						
						        <tr>
						            <th>추가 메모(50자)</th>
						        </tr>
						
						        <tr>
						            <td>
						            	<input type="text" id="memo2" name="memo2" maxlength="50" style="width:400px;"/>
						            </td>
						        </tr>
						
						    </table>
						</div>
					</div>			
            	</form>
            	<!-- form 끝 -->
            	<!-- 통신원 등록 끝 -->
            	
            </div>
            <!-- //contentWrap -->
        </div>
        <!-- //bodyWrap -->
    </div>
</div>
<script>
$(document).ready(function(){
	console.log("통신원 등록/수정 새창 수정반영");
	dateFunc("birthday","","${informerInfo.birthday}");
	
    //상세페이지의 경우 주소 분할
    var tagHome = "${informerInfo.address}";
    var tagOfs = "${informerInfo.addressOffice}";

    //파일 관련하여 있으면 값 대입
    var tagFile = "${informerInfo.localFilePath}";
    
    	
    if(typeof tagHome !== "undefined" && tagHome !=""){
    	addressSp("addressHome",tagHome,"--");
    }
    if(typeof tagOfs !== "undefined" && tagOfs !=""){
    	addressSp("addressOffice",tagOfs,"--");
    }

    $("#addressOffice1").val(tagOfs.split("--")[0]);
	$("#addressOffice2").val(tagOfs.split("--")[1]);
    
    
    //소속방송국,통신원유형 클릭 시 -> id랑 소속기관 소속기관 세부 갱신
    $("#areaCodeSel , #informerTypeSel").on("change",function(){
    	$("#orgIdSel option").remove();
    	$("#orgSubIdSel option").remove();
    	
    	var ajaxData = ajaxMethod
    	("/informer/reSel.do",
			{
				areaCode:$("#areaCodeSel option:selected").val()
				,informerType:$("#informerTypeSel option:selected").val()
				,sflag:$(this).attr("id")	
			}
		);
    	
    	//소속방송국 경우 ->actid 변경
    	if(typeof ajaxData.newActId !="undefined"){
    		$("#actId").val(ajaxData.newActId);
    	}
    	//지역코드값과 통신원유형으로 2,3을 불러옴
    	
    	var alData1 = ajaxData.sList1;
    	var alData2 = ajaxData.sList2;
    	
    	//셀렉트박스 갱신 하위영역들 2,3
    	if (alData1.length==0) {
    		console.log("alData1.length==0");
    		$("#orgIdSel").append("<option value=''>무소속</option>");
		}else{
			console.log("alData1.length!=0");
			for (var i = 0; i < alData1.length; i++) {
	    		$("#orgIdSel").append("<option value='"+alData1[i].ifmId2+"'>"+alData1[i].ifmName+"</option>");	
			}	
			$("#").append("<option value=''>무소속</option>");
		}
		if (alData2.length==0) {
			console.log("alData2.length==0");
			$("#orgSubIdSel").append("<option value=''>무소속</option>");
		}else{
			console.log("alData2.length!=0");
			for (var i = 0; i < alData2.length; i++) {
	    		$("#orgSubIdSel").append("<option value='"+alData2[i].ifmId3+"'>"+alData2[i].ifmName+"</option>");
			}	
			$("#orgSubIdSel").append("<option value=''>무소속</option>");
		}
		
    });
    
    
    //소속기관 클릭 시 -> 소속기관 세부 갱신
    $("#orgIdSel").on("change",function(){
    	$("#orgSubIdSel option").remove();
    	var ajaxData = ajaxMethod
    	("/informer/reSel.do",
			{
				areaCode:$("#areaCodeSel option:selected").val()
				,informerType:$("#informerTypeSel option:selected").val()
				,orgId:$("#orgIdSel option:selected").val()
				,sflag:$(this).attr("id")
			}
		);
    	//지역코드값과 통신원유형으로 2,3을 불러옴
    	var alData2 = ajaxData.sList2;
    	//셀렉트박스 갱신 하위영역들 2,3
		if (alData2.length==0) {
			$("#orgSubIdSel").append("<option value=''>무소속</option>");
		}else{
			for (var i = 0; i < alData2.length; i++) {
	    		$("#orgSubIdSel").append("<option value='"+alData2[i].ifmId3+"'>"+alData2[i].ifmName+"</option>");
			}	
		}
    });
    
    const fullUrl = window.location.href;
    console.log(fullUrl);
    var loginArea = '${login.regionId}';
    console.log("loginArea"+loginArea);
    if (fullUrl.indexOf("pr1") == -1) {
        console.log("신규등록");
    }
    document.getElementById("areaCodeSel").value =loginArea;
  	//자식 iframe으로부터 받은 리스너
	/* window.addEventListener('message', function(e) {
		 //console.log(e.data); // { hello: 'parent' }
		  if(e.data.chId=="1"){
			  $("#addressHome1").val(e.data.ch1);
		  }else if(e.data.chId=="2"){
			  $("#addressOffice1").val(e.data.ch2);
		  }
		  if (typeof e.origin !== 'undefined') {
			console.log("e.origin : "+e.origin);
		  }
	}); */
    
});

//다중 펑션 버튼에 따른 분기
function btnClick(str){
	if(str=="save"){                                 
		saveInformer();  
    } else if(str=="delete"){      
    	delInformer();
    }  else {
    	changeAct(str);
    }
}


function searchAddress(num){
    /* API 미적용하고 DB 적용할 경우를 대비하여 주석 
    var url = '<c:url value="/informer/searchAddress.do"/>?num=' + num;
    var windowName = "주소검색";
    var popupW = 480;  // 팝업 넓이
    var popupH = 600;  // 팝업 높이
    var left = Math.ceil((window.screen.width - popupW)/2);
    var top = Math.ceil((window.screen.height - popupH)/2);
    popObj = window.open(url, windowName, 'width=' + popupW +  ', height=' + popupH +  ', left=' + left +  ', top=' + top + ', toolbar=no, location=no, status=no, menubar=no, scrollbars=no, resizable=no');
     */
    var tagId ="";
   	console.log(tagId);
	new daum.Postcode({
        oncomplete: function(data) { //선택시 입력값 세팅
        	console.log("주소창 : "+data);
        	var chVal = data.address; // 주소 넣기
        	var postVal = data.zonecode;
        	//window.parent.postMessage({ch1: chVal, chId:"1"}, '*');
        	if(num==1){
        		$("#addressHome1").val(chVal);
        		$('#adressHome3').val(postVal);
        	}else{
        		$("#addressOffice1").val(chVal);
        		$('#addressOffice3').val(postVal);
        	}
        }
   }).open();   
}



/**
 * 변경 이력 저장 항목
 *
 * 301 0   신규가입
 * 301 1   소속변경
 * 301 2   해촉
 * 301 3   위촉
 * 301 4   시민->통신원
 * 301 5   일반->방송
 * 301 6   방송->일반
 * 301 7   신분증발급
 */

/**
 * 통신원 저장
 */
function saveInformer(){
	 
	//핸드폰 정규식
	 var regPhone = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;
	 //전화번호 정규식
	 var regCall = /^[0-9]{2,3}-?[0-9]{3,4}-?[0-9]{4}/;
	 //주민번호 정규식
	 var regJumin = /^(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4][0-9]{6}$/;
	 
	 // 반드시 입력해야 할 항목이 추가 되면 작성 해야함.(필수항목)
	 if($('#actId').val() == ""){
        alert("통신원 ID를 입력하세요.");
        $('#actId').focus();
        return false;
    }
	 
	if($('#informerName').val() == ""){
        alert("통신원 이름을 입력하세요.");
        $('#informerName').focus();
        return false;
    }
	
	if($('#phoneCell').val()== "" && $('#phoneHome').val()== "" && $('#phoneOffice').val()== ""){
		alert("핸드폰/집/회사 번호중 하나는 필수값입니다");
        $('#phoneCell').focus();
        return false;
	}
	
	
	//핸드폰
	if ($('#phoneCell').val() != "" ){
		if(!regPhone.test($('#phoneCell').val())){
	        alert("올바른 핸드폰번호 형식을 입력해주세요");
	        $('#phoneCell').focus();
	        return false;
		} 
    } 
	
	 //집전화
	if ($('#phoneHome').val() != "" ){
		if(!regCall.test($('#phoneHome').val())){
			alert("올바른 전화번호 형식을 입력하세요");
			$('#phoneHome').focus();
			return false;
		}
	}
	 //회사전화
	if ($('#phoneOffice').val() != "" ){
		if(!regCall.test($('#phoneOffice').val())){
			alert("올바른 전화번호 형식을 입력하세요");
			$('#phoneOffice').focus();
			return false;
		}
	}
	
	//집주소 ( 모든 조건이 일치한 경우 )
	if ($('#addressHome1').val() != "" && $('#addressHome2').val() != ""){
		$("#address").val(addressChk("address","--"));
		$("#addressHome").val(addressChk("addressHome"," "));
	}
	
	// 집주소 ( 우편번호와 도로명 주소는 입력 된 상태인데, 상세 주소가 적히지 않은 경우 )
	if ($('#addressHome1').val() != "" && $('#addressHome2').val() == "" ) {
		alert("상세 주소를 입력해 주세요.");
		$('#addressHome2').focus();
		return false;
	}
	
	// 집주소 ( 상세 주소는 적혀있는데, 우편번호와 도로명 주소는 입력되지 않은 경우 )
	if($('#addressHome1').val() == "" && $('#addressHome2').val() != "") {
		alert("우편번호 및 도로명 주소를 입력해 주세요");
		return false;
	}
	
	//회사주소
	if ($('#addressOffice1').val() != "" && $('#addressOffice2').val() != ""){
		$("#addressOffice").val(addressChk("addressOffice","--"));
	}
    
	// 회사 주소 ( 우편 번호와 도로명 주소는 입력 된 상태인데, 상세 주소가 적히지 않은 경우 )
	if( $('#addressOffice1').val() != "" && $('#addressOffice2').val() == "" ) {
		alert(" 상세 주소를 입력해 주세요. ");
		$('#addressOffice2').focus();
		return false;
	}
	
	// 회사 주소 ( 상세 주소는 적혀있는데, 우편 번호와 도로명 주소는 입력되지 않은 경우 )
	if( $('#addressOffice1').val() == "" && $('#addressOffice2').val() != "" ) {
		alert(" 우편번호 및 도로명 주소를 입력해 주세요");
		return false;
	}
    
	//통신원 이력관련
	var histCode = '';
    if('${informerInfo.informerId}' == '' || '${informerInfo.informerId}' == null){
    	histCode = '0';
    } else {
    	if('${informerInfo.flagBroad}' == '0' && $('#flagBroad1').is(':checked')){
            histCode = '5';
        } else if('${informerInfo.flagBroad}' == '1' && $('#flagBroad0').is(':checked')){
            histCode = '6';
        }
    	
    	if('${informerInfo.informerType}' == '2' && $('#informerType').val() == '0'){
    		if(histCode != '' && histCode != null){
    			histCode += ',4';    			
    		} else {
    			histCode = '4';
    		}
    	}
    	
    	if('${informerInfo.areaCode}' != $('#areaCodeSel').val()){
    		if(histCode != '' && histCode != null){
                histCode += ',1';              
            } else {
                histCode = '1';
            }
    	}
    }
    
    if($('#honor').is(':checked')){
    	$('#honor').val('Y');
    }else{
    	$('#honor').val('N');
    }
    if($('#flagService').is(':checked')){
    	$('#flagService').val('Y');
    }else{
    	$('#flagService').val('N');
    }
    if($('#flagBest').is(':checked')){
    	$('#flagBest').val('Y');
    }else{
    	$('#flagBest').val('N');
    }
    
    var fileChg = false;
    if('${informerInfo.localFilePath}' != $('#localFilePath').val()){
    	fileChg = true;
    	//$('#birthday').val(date);
    }
    
    
    // 주석 처리 시 date에 하이픈 들어감 : 현재는 하이픈 제거 상태
    var date = $('#birthday').val().replace(/-/g,'');
    $('#birthday').val(date);

    let queryString = $("#informerEditFrm").serialize();
    let addStr ="&histCode="+histCode+"&fileChg="+fileChg+"&flagBroad="+$('input[name="flagBroad"]:checked').val()+"&honor="+$('#honor').val()+"&flagService="+$('#flagService').val()+"&flagBest="+$('#flagBest').val();
    queryString+=addStr;
   	console.log(queryString);
    var options = {
            url:"/informer/saveInformer.do?"+queryString,
            type:"post",
            dataType: "json",
            data:queryString,
            async : false,
            success: function(res){
                if(res.cnt > 0){
                    alert("저장되었습니다.");
                    opener.search();
                    self.close();
                } else {
                	if(res.badFileType != null){
                		alert("사진파일 첨부는 이미지 파일만 가능합니다.")
                	} else if(typeof res.createFileError !== "undefined" && res.createFileError){
                	    alert("사진파일 저장에 실패했습니다.");
                	} else if(typeof res.msg !== "undefined" && res.msg != null){
                		alert(res.msg);
                	}else {
                		alert("저장에 실패했습니다.");
                	}
                }
            } ,
            error: function(res,error){
                alert("에러가 발생했습니다."+error);
            }
    };
    $('#informerEditFrm').ajaxSubmit(options);
}

/**
 * 해촉, 위촉 수정
 */
function changeAct(flag){
	var histCode = '';
	var actYn = '';
	
    if('${informerInfo.flagAct}' == 'Y' && '${informerInfo.flagAct}' != flag){
    	histCode = '2';
    	actYn = 'N'
    } else if('${informerInfo.flagAct}' == 'N' && '${informerInfo.flagAct}' != flag){
    	histCode = '3';
    	actYn = 'Y'
    }
    let queryString = $("#informerEditFrm").serialize();
   	console.log(queryString);
	var options = {
			url:"/informer/changeAct.do?"+queryString,
            type:"post",
            data: {
            	chgFlagAct:      flag,
            	//areaCode:      $('#areaCodeSel').val(),
            	//honor:         '${informerInfo.honor}',
            	//flagService:  '${informerInfo.flagService}',
            	upCode:   histCode,
            	stopDate:     'Y',
            	pageDiv:       $('#pageDiv').val(),
            },
            dataType: "json",
            async : false,
            success: function(res, html){
            	alert("저장되었습니다.");
                opener.search();
                self.close();
            } ,
            error: function(res,error){
                alert("에러가 발생했습니다."+error);
            }
    };
    $.ajax(options);
}

/**
 * 통신원 삭제
 */
function delInformer(){
	if('${informerInfo.flagAct}' == 'Y'){
		alert("삭제하기전에 해촉을 해주세요.");
		return false;
	}
	
    var options = {
            url:"/informer/deleteInformer.do",
            type:"post",
            data: {
                informerId:   $('#informerId').val()
            },
            dataType: "json",
            async : false,
            success: function(res, html){
                if(res.cnt > 0){
                    alert("삭제되었습니다.");
                    opener.search();
                    self.close();
                } else {
                    alert("삭제에 실패하였습니다.");
                }
            } ,
            error: function(res,error){
                alert("에러가 발생했습니다."+error);
            }
    };
    if(confirm('삭제 하시겠습니까? \n (해당 통신원에 대한 모든 정보가 시스템에서 삭제됩니다)')){
        $.ajax(options);
    }
}

/**
 * 주소 설정
 */
function setAddress(str, target){
	$('#'+target).val(str + ' ');
	$('#'+target).focus();
}

/**
 * 사진 변경 시
 */
function changePicture(obj){
	$('#localFilePath').val((obj.val()).replace("C:\\fakepath\\", ""));
}
</script>
</body>
</html>
