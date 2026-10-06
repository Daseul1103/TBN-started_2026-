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
	
	
	#passwordChangeScreen {
	    min-height: 100vh;
	    align-items: center;
	    justify-content: center;
	    background-color: #f4f6f8;
	    font-family: "Noto Sans KR", "Malgun Gothic", sans-serif;
	    color: #222;
	}
	
	#passwordChangeScreen .password-change-box {
	    box-sizing: border-box;
	    width: 100%;
	    max-width: 430px;
	    padding: 40px;
	    margin: 20px;
	    background-color: #fff;
	    border: 1px solid #e2e6ea;
	    border-radius: 10px;
	    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
	}
	
	#passwordChangeScreen .password-change-box h1 {
	    margin: 0 0 12px;
	    font-size: 26px;
	    text-align: center;
	}
	
	#passwordChangeScreen .description {
	    margin: 0 0 30px;
	    color: #666;
	    font-size: 14px;
	    line-height: 1.6;
	    text-align: center;
	}
	
	#passwordChangeScreen #passwordChangeForm,
	#passwordChangeScreen .input-group {
	    width: 100%;
	}
	
	#passwordChangeScreen .input-group {
	    margin-bottom: 20px;
	}
	
	#passwordChangeScreen .input-group label {
	    display: block;
	    margin-bottom: 8px;
	    font-size: 14px;
	    font-weight: 600;
	}
	
	#passwordChangeScreen .input-group input[type="password"] {
	    display: block;
	    box-sizing: border-box;
	    width: 100% !important;
	    height: 48px;
	    padding: 0 14px;
	    border: 1px solid #cfd5dc;
	    border-radius: 6px;
	    font-size: 15px;
	    outline: none;
	}
	
	#passwordChangeScreen .input-group input[type="password"]:focus {
	    border-color: #2563eb;
	    box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.12);
	}
	
	#passwordChangeScreen .password-rule {
	    margin: 8px 0 0;
	    color: #666;
	    font-size: 13px;
	    line-height: 1.5;
	}
	
	#passwordChangeScreen .error-message {
	    min-height: 20px;
	    margin: -8px 0 14px;
	    color: #dc2626;
	    font-size: 13px;
	}
	
	#passwordChangeScreen .change-button {
	    width: 100%;
	    height: 50px;
	    border: 0;
	    border-radius: 6px;
	    background-color: #2563eb;
	    color: #fff;
	    font-size: 16px;
	    font-weight: 700;
	    cursor: pointer;
	}
	
	#passwordChangeScreen .change-button:hover {
	    background-color: #1d4ed8;
	}
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
	    changeDate = '${login.pwChangedt}';
	    
	    
	    // 22.10.15 수정전
	    // center = initXYByRegionID(lgnArea);

	    // console.log("메 인 화 면 : " + inTelNum);
	    $("#menu").load("/common/menu.do");

	    // 로그인 전 비밀번호 변경 기능 생성
	    var changeDivVal =  chkChangePw(changeDate);   
	    
	    if(changeDivVal) {
	    	alert("비밀번호를 변경한지 90일이 지났습니다. 비밀번호 변경 화면으로 이동합니다.");
	    } else {
	    	
	    	showMainAfterPasswordCheck(); 
	    }
	    
	    // 비밀번호 변경 버튼을 눌렀을 경우 실행 함수
	    $('.change-button').on('click', function() {
	    	
	    	
	    	var newPw = $('#newPassword').val();
	    	var newPwChk = $('#passwordConfirm').val();
	    	
	    	// 비밀번호 입력 정규식 검사
	    	if(newPw == ''){
	    		// 비밀번호입력란에 빈값이 들어왔을 때
	    		alert("새로운 비밀번호를 입력해 주세요.");
	    	} else if(newPwChk == '') {
	    		// 비밀번호 확인 입력란에 빈 값이 들어왔을 때
	    		alert("비밀번호 확인에 새로운 비밀번호를 입력해 주세요.");
	    	} else if(newPw != newPwChk) {
	    		// 비밀번호와 비밀번호 확인이 일치하지 않을 때
	    		alert("입력하신 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
	    	} else {
	    		// 모든 조건을 만족할 때
	    		var pwRegex = /^[A-Za-z0-9!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]{9,15}$/; // 영문/숫자/특수 포함 9~15자

	    		if (!pwRegex.test(newPw)) {
	    		    alert("비밀번호는 9~15자로 입력하고, 영문·숫자·허용된 특수문자만 사용해 주세요.");
	    		} else {
	    		    // 비밀번호 변경 요청
	    		    $.ajax({
	    		        url: '/user/changePassword.ajax', // 실제 컨트롤러 매핑 주소로 변경
	    		        type: 'POST',
	    		        data: {
	    		            newPassword: newPw
	    		        },
	    		        success: function (data) {
	    		            if (data.success) {
	    		                alert('비밀번호가 변경되었습니다.');

	    		                showMainAfterPasswordCheck(); 
	    		            } else {
	    		                alert('비밀번호 변경에 실패했습니다.');
	    		            }
	    		        },
	    		        error: function (xhr, status, error) {
	    		            console.error('비밀번호 변경 요청 실패:', error);
	    		            alert('비밀번호 변경 중 오류가 발생했습니다.');
	    		        }
	    		    });
	    		}
	    	}
	    });
	    
	    
	    function chkChangePw(changeDate) {
	    	console.log("chkChangePw 진입");
	    	console.log("changeDate 값 : " + changeDate);
	    	
	    	const [year, month, day] = changeDate.split('-').map(Number);

	        const passwordChangeDate = new Date(year, month - 1, day);
	        const expiryDate = new Date(passwordChangeDate);
	        expiryDate.setDate(expiryDate.getDate() + 90);

	        const today = new Date();
	        today.setHours(0, 0, 0, 0);

	        if (today >= expiryDate) {
	        	console.log("비밀번호 90일 지남");
	        	
	            $('#container').hide();
	            $('#passwordChangeScreen').css('display', 'flex');
	            
	            return true;
	        } else {
	        	console.log("비밀번호 90일 안지남");
	        
	            $('#passwordChangeScreen').hide();
	            $('#container').show();
	            
	            return false;
	        }
	    }
	   
	});
	
	
	
	// 90일 비밀번호 변경 성공 후 또는 90일 비밀번호 변경 해당하지 않는 경우 실행
	function showMainAfterPasswordCheck() {
		$('#passwordChangeScreen').hide();
		$('#container').show();

		// 여기부터 기존 else 안에 있던 코드
		goMenuSite("/common/firstView.do");
		 
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
	
	
	}
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
	

	<div id="passwordChangeScreen" style="display: none;">
	    <div class="password-change-box">
	        <h1>비밀번호 변경</h1>
	        <p class="description">
	            비밀번호를 변경한 지 90일이 지났습니다.<br>
	            새로운 비밀번호를 입력해 주세요.
	        </p>
	
	        <form id="passwordChangeForm" method="post">
	            <div class="input-group">
	                <label for="newPassword">변경 비밀번호</label>
	                <input type="password"
	                       id="newPassword"
	                       name="newPassword"
	                       autocomplete="new-password"
	                       minlength="9"
	                       maxlength="15"
	                       aria-describedby="passwordRule"
	                       required>
	                <p id="passwordRule" class="password-rule">
	                    영문, 숫자, 특수문자를 사용하여 9~15자로 입력해 주세요.
	                </p>
	            </div>
	
	            <div class="input-group">
	                <label for="passwordConfirm">변경 비밀번호 확인</label>
	                <input type="password"
	                       id="passwordConfirm"
	                       name="passwordConfirm"
	                       minlength="9"
	                       maxlength="15"
	                       autocomplete="new-password"
	                       required>
	            </div>
	
	            <p id="errorMessage" class="error-message" aria-live="polite"></p>
	            <button type="button" class="change-button">비밀번호 변경</button>
	        </form>
	    </div>
	</div>
</body>
</html>