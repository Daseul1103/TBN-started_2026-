<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>TBN한국교통방송 제보접수시스템</title>

<link rel="stylesheet" type="text/css" href="<c:url value='/css/layout.css'/>" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/broadcast.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/receipt.css"/>

<script  type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/jquery.pagination.js"></script>
<script  type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/receipt/receipt.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/receipt/popup.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/autoLoading.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/timeDate.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/paging.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/broadcast/common.js"></script>

<script>

$(document).ready(function() {
	tabMenu="receivedStatus";
	autoLoading = false;
	isPause = false;
	onSearch = false;
	editOpenTotal = 0;
	console.log("receivedStatus");
	var nowCnt = 0;
	var beforeCnt = 0;
	/* changeAutoLoadingFlag(); */
	
	//서울교통방송의 경우 교통방송 전체로
/* 	if(opener.lgnArea == '080'){
		$("#AREA_CODE").val("").prop("selected", true);
	}else{
		$("#AREA_CODE").val(opener.lgnArea).prop("selected", true);
	} */
	
	var todaysDate = currentDate("");
	currentPage = 1;
	
	$("#START_DAY").val(currentDate("-"));
	$("#END_DAY").val(currentDate("-"));
	
	var totalAjax = ajaxMethod("/receipt/appGetTotalPage.ajax", {"RECEIPT_DAY" : todaysDate, "startRow" : currentPage, "AREA_CODE":opener.lgnArea});
	
	nowCnt = totalAjax.totalPage;
	beforeCnt = totalAjax.totalPage;
	
	totalPage = totalAjax.totalPage;	
	$("#resultListTotal span").text(totalAjax.totalSize);
	
 	if(totalAjax.totalSize == 0){
		$("#broadcastList").load("/receipt/noResult.do");
	} else{
		$("#broadcastList").load("/receipt/appStatusList.do", {"RECEIPT_DAY" : todaysDate, "startRow" : currentPage, "AREA_CODE":opener.lgnArea});
	} 
	
	$("#TodayList").scroll(function(){
		var scrollTop = $(this).scrollTop();
		var innerHeight = $(this).innerHeight();
		var scrollHeight = $(this).prop('scrollHeight');
		
		if (scrollTop + innerHeight >= scrollHeight) {
	    	currentPage++;
	    	getListByScrollPaging(currentPage, todaysDate, totalPage);
		}
	});
	
	get_current_time();
	
	// 등록 버튼 클릭 시 실행 함수
	$('#appInsBtn').on('click', function() {
		console.log("등록 버튼 클릭");
		var valCheck = labelYes(); // t 또는 f
		
		if(valCheck) {
			
			var frmChk = confirm("이대로 등록하시겠습니까?");
			
			if(frmChk) {
				let checkedList = [];
				$('input[name="Selection"]:checked').each(function() {
				    checkedList.push($(this).val());
				});
				
				$.ajax({
				    url: "/receipt/appToToday.do",   // 처리할 URL
				    type: "POST",
				    traditional: true,      // 배열 전송할 때 필요!
				    data: {
				        ids: checkedList    // 선택된 ID 배열
				    },
				    success: function(res){
				        alert("성공적으로 등록되었습니다.");
				        location.reload();
				    },
				    error: function(err){
				        console.log(err);
				    }
				});
			} else {
				return false;
			}		
			
		} else {
			return false;
		}
	});
	
	
	// 등록 시 유효성 검사 함수
	function labelYes() {		
		if ($("input[name='Selection']:checked").length === 0) {
	        alert("등록/해제 할 제보를 1개 이상 선택해주세요");
	        return false; 
		 } else {
			 return true;
		 } 		
	}
	
	
	// 10초마다 자동 갱신
	
	// 함수 호출
	setInterval(function () { chkAutoLoad(); }, 30000);
	
	function chkAutoLoad() {
		if($('input[name="Selection"]:checked').length > 0) {
			return false;
		} else {
			console.log("새로고침");
			/* location.reload(); */
 
		 	var totalAjax = ajaxMethod("/receipt/appGetTotalPage.ajax", {"RECEIPT_DAY" : todaysDate, "startRow" : currentPage, "AREA_CODE":opener.lgnArea});
			
			nowCnt = totalAjax.totalPage;
			beforeCnt = totalAjax.totalPage;
			
			totalPage = totalAjax.totalPage;	
			$("#resultListTotal span").text(totalAjax.totalSize);
			
			if(totalAjax.totalSize == 0){
				$("#broadcastList").load("/receipt/noResult.do");
			} else{
				$("#broadcastList").load("/receipt/appStatusList.do", {"RECEIPT_DAY" : todaysDate, "startRow" : currentPage, "AREA_CODE":opener.lgnArea});
			} 

		}
	}
	
	/* 제보접수 was -> 모바일 app로 데이터 전송 (상황해제 / 오류제보) */
	$('#appUptBtn, #appDelBtn').on('click', function(e) {
		e.preventDefault();

		console.log("상황해제/오류제보 버튼 클릭");

		var valCheck = labelYes(); // true or false
		if (!valCheck) {
			return false;
		}

		var frmChk = confirm("이대로 등록하시겠습니까?");
		if (!frmChk) {
			return false;
		}

		let checkedList = [];
		$('input[name="Selection"]:checked').each(function() {
			checkedList.push($(this).val());
		});

		if (checkedList.length === 0) {
			alert("선택된 제보가 없습니다.");
			return false;
		}

		// 어떤 버튼을 눌렀는지 구분
		var actionType = $(this).attr('id'); // appUptBtn 또는 appDelBtn

		$.ajax({
			url: "/receipt/releaseSituationUni.ajax",
			type: "POST",
			traditional: true,
			data: {
				ids: checkedList,
				actionType: actionType
			},
			success: function(res) {
				if (res.result === "success") {
					alert("성공적으로 반영되었습니다.");
					location.reload();
				} else {
					alert(res.msg || "처리 중 오류가 발생했습니다.");
					console.log(res);
				}
			},
			error: function(err) {
				console.log(err);
				alert("서버 오류가 발생했습니다.");
			}
		});
	});
		
		
		
});


</script>
</head>

<body>
	<input type="hidden" name="RECEIPT_ID" id="RECEIPT_ID" /> 
	<input type="hidden" name="CONTENTS" id="CONTENTS" />

	<div id="container">
		<div id="topWrap">
			<!-- bodyWrap -->
			<div id="bodyWrap">
				<!-- contentWrap -->
				<div id="contentWrap" style="width:1400px;">

					<!-- contents -->
					<div id="conteknts">
						<!-- 탭영역 -->
<!-- 						<div id="pop_tab">
							<ul>
								<li><a href="/receipt/receivedStatus.do">
									<img src="../images/h1_0102.gif" alt="금일접수현황" /></a></li>
								<li><a href="/receipt/fullReceivedHistory.do">
									<img src="../images/h1_0103_off.gif" alt="전체접수이력" /></a>
								</li>
								<li><a href="/receipt/imsFromUtic.do">
									<img src="../images/h1_0104_off.gif" alt="돌발접수이력" /></a>
								</li>
								<li><a href="<c:url value="/receipt/lostReceived.do" />"><img
										src="../images/h1_0105_off.gif" alt="분실물현황" /></a></li>
							</ul>
						</div> -->
						
	<!-- board_list -->
	<div class="board_list2" style="width:1400px;">
		<!-- 옵션 + 자동검색 영역 시작 -->
		<div class="searchwrap">
	             	<div>
	                 </div>
	             </div>
		<!-- 옵션 + 자동검색 영역 끝 -->
		<!-- 검색조건 영역 시작 -->
		<div style="width : 100%; height : 80px; margin-bottom : 15px; display: flex; align-items: center; justify-content: space-between;">
			<h1 style="font-size :30px; padding-top:25px;">모바일 앱 제보 내역</h1>
			
			<div style="width : auto; height : auto; border:1px solid rgba(0, 101, 225, 0.8); border-radius: 20px; background-color: rgba(50, 135, 239, 0.1); margin-left: 10px; margin-top: 32px;">
				<p id="resultListTotal" style="font-size:15px; padding: 4px 10px 4px 10px; color:rgba(0, 101, 225, 0.8);">제보건수 <span style="margin-left:2px; font-weight:600; color:rgba(0, 101, 225, 0.8);"></span>건</p>
			</div>
		</div>
		<!-- 검색조건 영역 끝 -->
		<div style="display:flex;align-items: center;justify-content: space-between;">
<!-- 			<div>
				<p id="resultListTotal" style="width: 300px;">
					<img src="../images/ico_result.gif" />
					제보건수 <span style="font-weight:700;"></span>건
				</p>
			</div> -->
<!-- 			<div>
				<a href="javascript:goStats('stats/receiptDownToday.ajax');">
					<img src="../images/btn_excel_down2.gif" alt="엑셀다운로드" style="width: 90px;"/>
				</a>			
			</div> -->
		</div>		
		<!-- 방송리스트 header 시작 -->
		<div style="width: 1400px;">
			
			<ul id="broadcastListHead"
			    style="width:1400px;
			           background:#ffffff;
			           border-radius:16px 16px 0 0;
			           overflow:hidden;
			           box-shadow:0 8px 24px rgba(0,0,0,0.06);
			           border:1px solid #e6ebf2;
			           border-bottom:none;
			           margin:0;
			           padding:0;">
			
			    <li style="display:flex;
			               align-items:center;
			               height:45px;
			               background:#f3f5f8;
			               font-size:14px;
			               font-weight:600;
			               color:#4a5568;">
			
			        <span style="width:60px; text-align:center;">선택</span>
			        <span style="width:60px; text-align:center;">번호</span>
			        <span style="width:100px; text-align:center;">일자</span>
			        <span style="width:100px; text-align:center;">제보시간</span>
			        <span style="width:100px; text-align:center;">유형(대)</span>
			        <span style="width:150px; text-align:center;">제보자</span>
			        <span style="width:516px; text-align:center;">내용</span>
			        <span style="width:80px; text-align:center;">제보처</span>
			        <span style="flex:1; text-align:center;">교통방송</span>
			
			    </li>
			</ul>
		</div>
		<!-- 방송리스트 header 끝 -->
		<!-- 검색결과 시작 -->
		<form id="listFrm" name="listFrm">
			<div id="TodayList" class="receivedList" style="border-bottom:1px solid #b7b7b7;">
				<div>
					<ul id="broadcastList"  class="receiptPopList">
						<li style="padding-top: 15px;">결과를 불러오는 중입니다.</li>
					</ul>
				</div>
				<!-- receivedStatusList.jsp -->
			</div>
		</form>
		<!-- 모바일 앱 제보 범례 div 시작 -->
		<div style="display:flex; align-items: center; margin-top: 10px; justify-content: space-between;">
			<div style="display:flex;">
				<div style="display:flex; align-items:center; gap:8px; padding:6px 14px; background:#dbeeff; border-radius:8px; height:32px;">
				    <div style="width:18px; height:10px; background:aliceblue; border-radius:3px; border:1px solid #6ea8ff;"></div>
				    <span style="font-size:14px; color:#2f4f6f; font-weight:500;">등록된 모바일 제보</span>
				</div>
				
				<div style="display:flex; align-items:center; gap:8px; padding:6px 14px; background:#f1f3f6; border-radius:8px; height:32px; margin-left:10px;">
				    <div style="width:18px; height:10px; background:#ffffff; border-radius:3px; border:1px solid #c5ccd6;"></div>
				    <span style="font-size:14px; color:#4a5568; font-weight:500;">미등록된 모바일 제보</span>
				</div>
				
				<div style="display:flex; align-items:center; gap:8px; padding:6px 14px; background:rgba(225, 227, 230, 0.8); border-radius:8px; height:32px; margin-left:10px;">
				    <div style="width:18px; height:10px; background:#e0e4ea; border-radius:3px; border:1px solid #b8c0cc;"></div>
				    <span style="font-size:14px; color:#4f5b6b; font-weight:500;">상황 해제된 모바일 제보</span>
				</div>
			</div>

			<span style="margin-right:20px;width: 350px;display: flex;justify-content: space-around;">
			    <button id="appInsBtn"
			        style="display:flex; align-items:center; gap:8px; background:linear-gradient(135deg,#3b6fd8,#2f5fbf); color:#ffffff; padding:9px 20px; border:none; border-radius:8px; font-size:14px; font-weight:600; cursor:pointer; box-shadow:0 3px 10px rgba(47,95,191,0.25); transition:all 0.2s ease;"
			        onmouseover="this.style.transform='translateY(-2px)'; this.style.boxShadow='0 6px 14px rgba(47,95,191,0.3)';"
			        onmouseout="this.style.transform='none'; this.style.boxShadow='0 3px 10px rgba(47,95,191,0.25)';">
			
			        <span style="display:inline-flex; align-items:center; justify-content:center; width:18px; height:18px; background:rgba(255,255,255,0.2); border-radius:50%; font-size:14px; font-weight:bold;">
			            +
			        </span>
			        등록
			    </button>
			    
			    <button id="appUptBtn"
			        style="display:flex; align-items:center; gap:8px; background:linear-gradient(135deg,#3b6fd8,#2f5fbf); color:#ffffff; padding:9px 20px; border:none; border-radius:8px; font-size:14px; font-weight:600; cursor:pointer; box-shadow:0 3px 10px rgba(47,95,191,0.25); transition:all 0.2s ease;"
			        onmouseover="this.style.transform='translateY(-2px)'; this.style.boxShadow='0 6px 14px rgba(47,95,191,0.3)';"
			        onmouseout="this.style.transform='none'; this.style.boxShadow='0 3px 10px rgba(47,95,191,0.25)';">
			
			        <span style="display:inline-flex; align-items:center; justify-content:center; width:18px; height:18px; background:rgba(255,255,255,0.2); border-radius:50%; font-size:14px; font-weight:bold;">
			            +
			        </span>
			         상황해제
			    </button>
			    
			    <button id="appDelBtn"
			        style="display:flex; align-items:center; gap:8px; background:linear-gradient(135deg,#3b6fd8,#2f5fbf); color:#ffffff; padding:9px 20px; border:none; border-radius:8px; font-size:14px; font-weight:600; cursor:pointer; box-shadow:0 3px 10px rgba(47,95,191,0.25); transition:all 0.2s ease;"
			        onmouseover="this.style.transform='translateY(-2px)'; this.style.boxShadow='0 6px 14px rgba(47,95,191,0.3)';"
			        onmouseout="this.style.transform='none'; this.style.boxShadow='0 3px 10px rgba(47,95,191,0.25)';">
			
			        <span style="display:inline-flex; align-items:center; justify-content:center; width:18px; height:18px; background:rgba(255,255,255,0.2); border-radius:50%; font-size:14px; font-weight:bold;">
			            +
			        </span>
			         오류제보
			    </button>
			</span>
			</div>

		<!-- 검색결과 끝 -->
	</div>
					</div>
					<!-- //contents -->
				</div>
				<!-- //contentWrap -->
			</div>
			<!-- //bodyWrap -->
		</div>
	</div>
</body>
</html>