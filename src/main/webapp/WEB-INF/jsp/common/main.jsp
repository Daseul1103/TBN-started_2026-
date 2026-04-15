<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- https => http 요청 허용 -->
<meta http-equiv="Content-Security-Policy-Report-Only" content="upgrade-insecure-requests">
<style>
	#goNotice:hover {
	    color: blue;
	    cursor: pointer;
	}
</style>
</head>
	<script>
	var ps;
	var map;
	var geocoder;

	// 22.10.15 
	var apiChk = 1;

	var cctvList;

	var marker; // 싱글마커
	var cctvMarkers = []; // cctv마커그룹
	var resultMarkers = []; // 장소검색결과 마커그룹
	var iframeGroup = []; // 재생되고 있는 cctvIframe group
	var trafficFlag = true;
	var cctvFlag = true;
	var searchFlag = false;

	// 22.10.15 수정전
	// var infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
	// var center = new kakao.maps.LatLng(37.52334726919335, 126.92522447784268);

	// 22.10.15 수정후 
	var infowindow;
	var center;

	var coordX;
	var coordY;
	var level = 4;

	// 제보접수 페이지
	var todaysDate = currentDate(""); // 오늘날짜
	var isCtrl;
	var currentPage;
	var totalPage;

	// 전체접수 
	var allSchVo;

	// 예약접수
	var firstInformerVO;
	var secondInformerVO;
	var thirdInformerVO;

	// 자동로딩 : 접수현황, 방송
	var autoLoading;
	var isPause;
	var onSearch;
	var autoLoadingFunction;
	var interval;
	var editOpenTotal;

	var updateFlag = ''; // 금일접수/전체접수 행 업데이트 구분용
	
	var pollingForCall; // 전체 전화 (픽업시 통신원반영)
	var pollingForPickup; // 전체 전화목록
	var pollingForMissedCall; // 부재중 전화

	var isPick = false;
	var isMiss = false;

	// PD지원 child창
	var childwin = null;

	// datePicker
	var dates;

	// 로그인 사용자의 소속 방송국
	var lgnArea;
	// 로그인 사용자의 내선번호
	var inTelNum;
	var userName;
	var userId;
	var authCode;
	// 메뉴이동 및 화면 관련
	var goUrl = "";

	$(document).ready(function() {
	    $("body").css("background", "none");
	    lgnArea = '${login.regionId}';
	    inTelNum = '${login.inTel}';
	    userName = '${login.userName}';
	    userId = '${login.userId}';
	    authCode = '${login.authCode}';
	    
	    
	    
	    // 22.10.15 수정전
	    // center = initXYByRegionID(lgnArea);

	    // console.log("메 인 화 면 : " + inTelNum);
	    $("#menu").load("/common/menu.do");

	    // 로그인 성공 시 사용자 권한에 따라 화면 분기
	    goMenuSite("/common/firstView.do");

	    // 자식 iframe으로부터 받은 리스너
	    /*
	    window.addEventListener('message', function(e) {
	        console.log("From Iframe: " + e.data);
	        $("#X_COORDINATE").val(e.data.xCh);
	        $("#Y_COORDINATE").val(e.data.yCh);

	        if (typeof e.origin !== 'undefined') {
	            console.log("e.origin : " + e.origin);
	        }
	    });
	    */
	        
	    
	   // function NoticeSelect() {
	    	var date = new Date();
			var today = ("0" + date.getFullYear()).slice(-2) + "/" + ("0" + (date.getMonth() + 1)).slice(-2) + "/" + ("0" + date.getDate()).slice(-2);


		  	
	        $.ajax({
	            url: "/common/selectNotice.ajax",
	            data: { 'today': today },
	            type: "POST",
	            success: function(data) {
	                appendNotice(data);

	                var nCount = data.moreCount;
	                
	                if(nCount > 0){
	                	$('#goNotice').text(">> " + nCount +"개의 공지사항이 더 있습니다. (보러가기)");
	 	                $('#moreNotice').show();
	                } else {
	                	return false;
	                }
	               
	            },
	            error: function(xhr, status, error) {
	                console.log('공지사항 불러오기 ajax 요청에 문제가 있습니다.');
	            }
	        });
	   // }
	    
	   // 더 많은 공지사항 보러가기 기능
	    $('#goNotice').on('click', function() {
	    	$(".notice_container").hide();
	    	
	    	var goUrl = '/notice/notice.do';
	    	goMenuSite(goUrl);
	    });

        // 공지사항 append 함수
        function appendNotice(data) {
    
        	// 값이 있는 경우에만 생성
        	if(data.NoticeList.length !== 0) {
        		// 공지사항(화면) 생성에 필요한 값들
                var title = data.NoticeList[0].notice_TITLE;
                var writer = data.NoticeList[0].writer_NAME;
                var writeDate = data.NoticeList[0].start_DATE;
                var endDate = data.NoticeList[0].end_DATE;
                var content = data.NoticeList[0].notice_CONTENT;
                
                // 특수문자 변환 작업 (개행 이외 특수문자 4종 " ' < >  변환 필요)
                title = title.replaceAll("&gt;", ">");
                title = title.replaceAll("&lt;", "<");
                title = title.replaceAll("&quot;", '"');
                title = title.replaceAll("&apos;", "'");
   
                content = content.replaceAll("&gt;", ">");
                content = content.replaceAll("&lt;", "<");
                content = content.replaceAll("&quot;", '"');
                content = content.replaceAll("&apos;", "'");
                
                $('#input_title').text(title);
                $('#input_writer').text("작성자 : " + writer);
                $('#input_writeDate').text("작성일 : " + writeDate);
                $('.notice_content').text(content);
                
                
        	} else {	
        		// 팝업 숨기기
        		$(".notice_container").hide();
        		return false;
        	}
        	

        }
        
	    var toggleMainPopup = function() {
	  
	        // 쿠키 제어 함수
	        var handleCookie = {
	            // 쿠키 쓰기
	            setCookie: function(name, val, exp) {
	                var date = new Date();
	                date.setTime(date.getTime() + exp * 24 * 60 * 60 * 1000); // 만료일 계산
	                document.cookie = name + "=" + val + ";expires=" + date.toUTCString() + ";path=/"; // 쿠키 설정
	            },
	            // 쿠키 읽기
	            getCookie: function(name) {
	                var value = document.cookie.match("(^|;) ?" + name + "=([^;]*)(;|$)");
	                return value ? value[2] : null; // 쿠키값 반환
	            }
	        };

	        // 쿠키 값을 읽고 팝업을 보이거나 숨김
	        if (handleCookie.getCookie("today") === "y") {
	            $(".notice_container").hide(); // 쿠키가 "y"이면 팝업 숨기기
	        } else {
	            $(".notice_container").show(); // 쿠키가 없으면 팝업 보이기
	        }

	        
	        
	    }
	    
	 	// 오늘 하루 보지 않기 버튼 클릭 시 실행되는 함수
        var clickButton = function() {
            var handleCookie = {
                // 쿠키 쓰기
                setCookie: function(name, val, exp) {
                    var date = new Date();
                    date.setTime(date.getTime() + exp * 24 * 60 * 60 * 1000); // 만료일 계산
                    document.cookie = name + "=" + val + ";expires=" + date.toUTCString() + ";path=/"; // 쿠키 설정
                }
            };

            handleCookie.setCookie("today", "y", 1); // "today" 쿠키를 "y"로 설정, 만료 1일
            $(".notice_container").hide(); // 팝업 숨기기
        }
    
        toggleMainPopup();
        
	    $(document).on('click', '.cancle_img', function() {
        	var checkPop = $('#show1').prop('checked'); // 체크박스가 체크되었는지 확인
    		
	        if (checkPop) {
	            // 하루 동안 보지 않기 버튼이 체크된 경우
	            clickButton(); // 쿠키 설정 함수 호출
	        } else {
	            // 체크되지 않으면 그냥 팝업 숨기기
	            $(".notice_container").hide(); // 팝업 숨기기
	        }
        });
	   
	});
	
	
</script>
</head>
<body>

	<div id="container" class="container">
		<div id='loginId' class="loginId" style="margin:3px 150px 0 0;">
			<div>
				<strong style="font-family: auto;">
				<c:if test="${login.authCode ne 999}">${login.regionName}</c:if> 
				${login.userName}</strong>님이 로그인하셨습니다.
				<a href="${path}/login/logout.do" style="color:mediumblue; font-weight:700;">로그아웃</a>
 					<a href="#" onclick="openPersonalMemo();" style="color:green; font-weight:700;">개인메모</a>	
 			</div>
		</div>
		<div id="header" class="header">
			<div id='logo' class="logo">
				<a href="${path}"><img src="../images/util_logo_tbn.png" alt="tbn한국교통방송 제보접수시스템"/></a>
			</div>
			<div id="menu" class="menu-bar"></div>
		</div>
	
			    <div class="notice_container" style="postion:absolute; z-index:999">
			        <div class="notice_title">
			            <div class="title_t">
			                <h2 id="input_title"></h2>
			            </div>
			            <div class="writer_info">
			                <div class="writer_div">
			                    <div class="writer_img"></div>
			                    <p id="input_writer"></p>
			                </div>
			                <div class="writeDate_div">
			                    <div class="writeDate_img"></div>
			                    <p id="input_writeDate"></p>
			                </div>
			            </div>
			        </div>
			        <div class="line"></div>
			        
			       	<!-- 공지사항 팝업 -->
			        <div class="notice_content" style="white-space: pre-line; font-size:18px; line-height: 1.5;">
			            
			        </div>

			        <div class="line"></div>
			        <div id="moreNotice" style="display : none; margin-bottom: 15px;">
			        	<p id="goNotice"></p>
			        </div>
			        <div class="notice_cancle">
			            <div class="show24">
			                <input type="checkbox" id="show1"> <!-- i 값 동적으로 바꿔야 함 -->
			                <label for="show1">하루동안 보지않기.</label>
			            </div>
			            <div class="cancle_div">
			                <div class="cancle_img" id="cancle">
			                    <input type="hidden" id="endDate" value="<!-- endDate 값 여기에 삽입 -->">
			                    <input type="hidden" id="user_id" value="<!-- userId 값 여기에 삽입 -->">
			                    <input type="hidden" id="notice_id" value="<!-- noticeId 값 여기에 삽입 -->">
			                </div>
			            </div>
			        </div>
			    </div>
			
		<div id="mainDiv" class="mainDiv">
			
		</div>
	</div>
</body>
</html>