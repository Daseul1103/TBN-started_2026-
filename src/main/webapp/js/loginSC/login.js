/*
 * 최초 index 페이지 진입 시
 * 세션 및 로그인 여부에 따라 로그인화면으로 전송할지
 * 메인화면으로 전송할 지 판별 
 * */

function stMainIdx(sessionVo){
	
	// 페이지가 완전히 종료(unload)될 때 호출
    // 새로고침인지 브라우저 종료인지 구분하기 위한 플래그 설정
	window.onunload = function() {
	    reloadOrKill(false);
	}
	
	// 페이지 최초 로드 시 실행
    // 새로고침 여부를 판단하기 위한 초기 설정
	$(function() {
		reloadOrKill(true);
	});
	
	// 로그인 세션 존재 여부 확인
	if(sessionVo==''){
		 // 세션이 없으면 로그인 화면으로 이동
		$("#changeBody").load("/common/login.do");
	}else{
		// 세션이 있으면 메인 화면으로 이동
		$("#changeBody").load("/common/main.do");	
	}
	
	// 브라우저 종료 또는 새로고침 직전에 실행
	$(window).bind("beforeunload", function (e){
		
		// → 새로고침이 아닌 브라우저(탭) 종료로 판단하여 로그아웃 처리
		//reloadOrKill(true);
		if(!rkFlag){
			$("#changeBody").load("/user/reloadOrKill.do");
		}else{
			// 새로고침인 경우에는 로그아웃하지 않고 플래그만 초기화
			rkFlag=false;
		}
	});
}

/*
 * 로그인 처리 및 불완전 접속 종료 시 
 * 기존 세션을 끊고 신규 세션 생성
 * */
function inputLogin(inputVal){
	//console.log("입력값기준 로그인 처리함수");
	$.ajax({
		url: "/login/login.do",
		type: "POST",
		dataType: "json",
		data: inputVal,
		// ajax 통신 성공 시 로직 수행
		success: function(json){
			//서버측으로 부터 받은 별도의 에러메시지가 없을 경우 로그인 처리
			if(json.msg=="" || typeof json.msg ==="undefined"){
				$("#changeBody").empty();
				$("#changeBody").load("/common/main.do");
			}else{
				if(json.msg=="중복로그인"){
					//console.log(json.msg);
					var con_test = confirm("현재 로그인 사용자입니다. 재 접속하시겠습니까?");
					if(con_test == true){
						ajaxMethod("/login/login.do?relgn=1",inputVal,"/common/main.do","","changeBody");
					}
				}else{
					alert(json.msg);
				}
			}
		},
		error : function() {
			////console.log("에러가 발생하였습니다."+json.msg);
		},
		//finally 기능 수행
		complete : function() {
			////console.log("파이널리.");
		}
	});
}

/*
 * 로그인 처리 및 불완전 접속 종료 시 
 * 기존 세션을 끊고 신규 세션 생성
 * */
function pwChkInput(obj){
	//특수문자 정규식
	var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;
	if (str_space.exec(obj.value)){ //공백체크
        obj.focus();
        obj.value = obj.value.replace(' ',''); // 공백제거
        return false;	
	}
}