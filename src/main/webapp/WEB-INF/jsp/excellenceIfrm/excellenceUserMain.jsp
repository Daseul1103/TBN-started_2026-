<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
	//한달 전
	Calendar mon = Calendar.getInstance();
	mon.add(Calendar.MONTH, -1);
	System.out.println("%%%%%%%%%%%%%%%% : " + mon);
	String beforeMonth = new java.text.SimpleDateFormat("YYYYMMdd").format(mon.getTime());
	System.out.println("%%%%%%%%%%%%%%%% : " + beforeMonth);

	//2
	Date monthAgo = new Date(new Date().getTime() - 60 * 60 * 24 * 1000 * 30L);
	System.out.println("%%%%%%%%%%%%%%%% : " + monthAgo);
%>

<script>
	$(document).ready(function() {
		search();
	})

	/**
	 * 검색
	 */
	function search() {
		//시작
		var stDt = $("#sMon").val();
		if ($("#sMon").val().length == 1) {
			stDt = "0" + stDt;
		}
		$('#SDATE').val($('#sYear').val() + stDt);
		//종료
		var eDt = $("#eMon").val();
		if ($("#eMon").val().length == 1) {
			eDt = "0" + eDt;
		}
		$('#EDATE').val($('#eYear').val() + eDt);

		var options = {
			url : "/excellenceIfrm/excellenceUserList.do",
			type : "post",
			target : '#listDiv',
			success : function() {
			},
			error : function(res, error) {
				alert("에러가 발생했습니다." + error);
			}
		};
		$('#searchFrm').ajaxSubmit(options);
	}

	
	//시상자 제외(수상자 선정 화면으로 통신원 보내기)
	function deleteAward(){
		var selectionCheck =  searchFrm.Selection;
		var selectionCnt = 0;
		if($("input:checkbox[name=Selection]:checked").length!=0){
			if(!confirm("체크된 " + $("#searchType option:selected").text() + "를\n 수상취소 하시겠습니까?" )){
				return;
			}
			let tagVal = $("#searchFrm").serialize();
			
			console.log(tagVal);
			$.ajax
			(
				{
					type : "post" ,
					url : "/excellenceIfrm/deleteExcellence.do" ,
					dataType : "json" ,
					data : tagVal,
					cache : false ,
					success:function(data){
						if(data.msg == "success") alert("정상적으로 수상취소 되었습니다");
						search();
					} ,
	
					error:function(data,error){
						alert("시스템에 문제가 생겼습니다.");
					}
	
				}
			);
			
		}else{
			alert("수상자를 선택해 주세요");
		}
		
	}
	
	//시상자목록  -> 수상자선정 화면 전환
/* 	function changePage() {
		$.ajax({
			type : "post",
			url : "/informer/excellenceIfrm/excellenceIfrmMain.do",
			dataType : "html",
			data : $("#searchFrm").serialize(),
			cache : false,
			success : function(html) {
				$('#contentWrap').html(html);
			},

			error : function(data, error) {
				//alert("시스템에 문제가 생겼습니다." + data);
			}

		});
	} */
	
	// 페이지 이동
	function changePage(url){
		
		if(url == 'select') {
			$.ajax
			(
				{
					type : "post" ,
					url : "/informer/excellenceIfrm/excellenceIfrmMain.do",
					dataType : "html" ,
					cache : false ,
					success:function(html){
						$('#contentWrap').html(html);
					} ,
		
					error:function(data,error){
						alert("시스템에 문제가 생겼습니다." + data);
					}
		
				}
			);
		} else if(url == 'list'){
			$.ajax
			(
				{
					type : "post" ,
					url : "/excellenceIfrm/excellenceUserMain.do" ,
					dataType : "html" ,
					cache : false ,
					success:function(html){
						$('#contentWrap').html(html);
					} ,
					error:function(data,error){
						alert("시스템에 문제가 생겼습니다." + data);
					}
				}
			);
		} else {
			$.ajax
			(
				{
					type : "post" ,
					url : "/excellenceIfrm/standardMain.do" ,
					dataType : "html" ,
					cache : false ,
					success:function(html){
						$('#contentWrap').html(html);
					} ,
					error:function(data,error){
						alert("시스템에 문제가 생겼습니다." + data);
					}
		
				}
			);
		}
	}
	
	//엑셀 다운로드 기능 제공
	function excelDownload() {
		rkFlag = true;
		searchFrm.action = "/excellenceIfrm/excelDownloadUserList.do";
		searchFrm.submit();
		rkFlag = true;
	}
</script>
<div id="contentWrap">
	<form id="searchFrm" name="searchFrm">
		<div id="searchDiv">
			<div id="contents">
				<h1 class='content-title'>우수 제보자</h1>
				<!-- board_list -->
				<div class="board_list">
					<!-- 서브메뉴 탭영역 시작 -->
					<div class="gnb_tab">
						<ul class="lst_tab">
							<li><a href="javascript:changePage('select')">우수제보자 선정</a></li>
							<li class="ns"></li>
							<li class="on">우수제보자 조회</li>
							<li class="ns"></li>
							<li><a href="javascript:changePage('standard')">선정 기준</a></li>
						</ul>
					</div>
					<div style="position: absolute;right: 0px;top: 52px;">
				       <select id="areaOptSel" name="AREA_CODE">
                           <c:forEach var="informerRegion" items="${informerRegionList}" varStatus="idx">
                               <option value="${informerRegion.areaCode}" ><c:out value="${informerRegion.areaName}"/></option>
                           </c:forEach>
						</select>
					</div>
					
					<!-- 검색조건 영역 시작 -->
					<div class="rounding_wrap mgt10">
						<div class="wrap_top"></div>
						<div class="wrap_center">
							<fieldset>
								종류  <!-- onchange="search();" -->
								<select class="table_sel1" id="searchType"
									 name="searchType">
									<option value="0">우수제보자(시민,애청자,기관)</option>
									<!-- <option value="1">주요정보제공우수자</option>
									<option value="2">실적증가자</option> -->
								</select> 
									기준시작 : 
									<input type="hidden" id="SDATE" name="SDATE"
										maxlength="15" class="input_base" readonly="readonly"
										alt="시작일" title="" value=""
										style="width: 70px; align: center;" />
									<c:set var="today" value="<%=monthAgo%>" />
									<c:set var="datetime">
										<fmt:formatDate value="${today}" pattern="yyyy" />
									</c:set>
									<select id="sYear" name="sYear">
										<c:forEach var="i" begin="0" end="10">
											<c:choose>
												<c:when test="${i == 0}">
													<option value="${datetime}" selected>${datetime}년</option>
												</c:when>
												<c:otherwise>
													<option value="${datetime-i}">${datetime-i}년</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
									<c:set var="today" value="<%=monthAgo%>" />
									<c:set var="datetime">
										<fmt:formatDate value="${today}" pattern="MM" />
									</c:set>
									<select id="sMon" name="sMon">
										<c:forEach var="i" begin="01" end="12">
											<c:choose>
												<c:when test="${i == datetime}">
													<option value="${i}" selected>${i}월</option>
												</c:when>
												<c:otherwise>
													<option value="${i}">${i}월</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
									 기준종료 : 
									<input type="hidden" id="EDATE" name="EDATE"
										maxlength="15" class="input_base" readonly="readonly"
										alt="시작일" title="" value=""
										style="width: 70px; align: center;" />
									<c:set var="today" value="<%=monthAgo%>" />
									<c:set var="datetime">
										<fmt:formatDate value="${today}" pattern="yyyy" />
									</c:set>
									<select id="eYear" name="eYear">
										<c:forEach var="i" begin="0" end="10">
											<c:choose>
												<c:when test="${i == 0}">
													<option value="${datetime}" selected>${datetime}년</option>
												</c:when>
												<c:otherwise>
													<option value="${datetime-i}">${datetime-i}년</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
									
									<c:set var="today" value="<%=monthAgo%>" />
									<c:set var="datetime">
										<fmt:formatDate value="${today}" pattern="MM" />
									</c:set>
									<select id="eMon" name="eMon">
										<c:forEach var="i" begin="01" end="12">
											<c:choose>
												<c:when test="${i == datetime}">
													<option value="${i}" selected>${i}월</option>
												</c:when>
												<c:otherwise>
													<option value="${i}">${i}월</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
 
								<select class="table_sel1" id="searchNum" name="searchNum">
									<option value="30">30명 출력</option>
									<option value="50">50명 출력</option>
									<option value="70">70명 출력</option>
									<option value="100">100명 출력</option>
								</select>
								<img src="../images/btn_search.gif" onclick="search();" style="cursor: pointer" alt="검색" />
							</fieldset>
						</div>
						<div class="wrap_bottom"></div>
					</div>
				</div>
				<!-- list -->
				<div id="listDiv"></div>
			</div>
		</div>
		<!-- <input type="hidden" name="AW_NAME" id="AW_NAME" /> -->
	</form>
</div>