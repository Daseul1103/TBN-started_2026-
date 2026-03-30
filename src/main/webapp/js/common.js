var timeRefresh=null;
var pastSide="";//이전 사이드바 메뉴

/************************************************************************
함수명 : ajaxMethod
설 명 : ajax 처리 함수 
인 자 : url(컨트롤러에 매핑된 주소), data(전송 값), 
	   callback(ajax 통신 후 이동 주소), 
	   message(전송 성공 시 메시지),
	   thDiv(로드할 대상 div 또는 그 외의 dom 요소)
사용법 : 화면에서 서버로 ajax통신으로 값 전송시 사용
작성일 : 2020-07-30
작성자 : 미래전략사업팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.07.30   정다빈       최초작성
************************************************************************/
function ajaxMethod(url, data, callback, message,thDiv){
	var output = new Array();
	$.ajax({
		url : url,
		type : "post",
		dataType : "json",
		data : data,
		/*ajax는 비동기 식이지만 함수형태로 호출 시 success 이전에 
		*화면이 구성되어 값을 제대로 전송받지 못함
		*함수로 호출할 경우 false로 지정
		*/
		async : false,
		success : function(json) {
//			//console.log("아작스 서버 연동 성공");
//			//console.log(json);
//			//console.log(json.msg);
			if(json.msg=="" || typeof json.msg ==="undefined"){//서버 단에서 에러가 없을 경우
				if(message!="" && typeof message !== "undefined"){//에러는 아니지만 전달메시지가 있을경우
					alert(message);
				}
				//콜백 화면 이동 : div 값 변경일경우 사용
				//주소 자체 이동, body 전체 화면 변환일경우 수정 필요
				if(callback!="" && typeof callback!=="undefined"){
					if(thDiv!="" && typeof thDiv!=="undefined"){
						$("#"+thDiv).load(callback);
					}else{
						$("#content").load(callback);
					}
				}
			}else{
				//검색 결과가 없는 것이라면
				if(json.msg=="search_not"){
					json="";
				}else{
					alert(json.msg);
				}
			}
			output = json;
		},
		error : function() {
			//console.log("에러가 발생하였습니다.");
		},
		//finally 기능 수행
		complete : function() {
		}
	});
	return output;
}

/************************************************************************
함수명 : reloadOrKill
설 명 : ajax 처리 함수 
인 자 : url(컨트롤러에 매핑된 주소), data(전송 값), 
	   callback(ajax 통신 후 이동 주소), 
	   message(전송 성공 시 메시지)
사용법 : 화면에서 서버로 ajax통신으로 값 전송시 사용
작성일 : 2020-07-30
작성자 : 미래전략사업팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.07.30   정다빈       최초작성
************************************************************************/
function reloadOrKill(isClose){
	//console.log("reloadOrKill 커먼 메소드 진입");
	ajaxMethod('/user/reloadOrKill.do',{"tagId":isClose});
}

/************************************************************************
함수명 : boardWriteCheck
설 명 : form 형식의 input에서 회원가입 또는 사용자 등록시 비밀번호,ID관련 유효성 검사
인 자 : form
사용법 : 로그인 회원가입, 등록 등의 입력정보 체크시 사용
작성일 : 2020-07-30
작성자 : 미래전략사업팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.07.30   정다빈       최초작성
************************************************************************/
function boardWriteCheck(form) {

	//특수문자 정규식
	var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;
	//영문+숫자 관련 정규식
	var dfExp = /[A-Za-z0-9]/; 
	
	$(form).find('span').text("");	
	
	for (var i = 0; i < form.length; i++) {
		//id의 경우 6자 이상인지
		if(form[i].name =='userId'){
			if(form[i].value.length < 6 || form[i].value.length > 10){
				alert("id 형식이 올바르지 않습니다. (6자 ~10자 이내)");
				form[i].focus();
				return false;

			}
		}
		//pw : (영문 특수문자 포함  15자 이상)
		var complexExp = /^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[~!@#$%^&*()_+|\[\]])[A-Za-z0-9~!@#$%^&*()_+|\[\]]+$/;
		
		if (form[i].name === 'userPw') {

		    var value = form[i].value.trim();

		    console.log("비밀번호 진입");
		    //해당부분은 background에서 체크
		    /*if (value.length < 8 || value.length > 15) {
		    	console.log("비밀번호 길이 체크");
		        alert("비밀번호의 길이가 유효하지 않습니다. (8~15자)");
		        form[i].focus();
		        return false;
		    }

		    if (!complexExp.test(value)) {
		    	console.log("비밀번호 정규식 체크");
		        alert("비밀번호 형식이 올바르지 않습니다. (영문+숫자+특수문자 포함)");
		        form[i].focus();
		        return false;
		    }*/
		}
		
		
	}
	//확인 비밀번호와 비밀번호가 다를 때
	if($("#userPw").val()!=$("#userPw2").val()){
		alert("비밀번호가 서로 일치하지 않습니다.");
		return false;
	}
	return true;
}

/************************************************************************
함수명 : onlyNumber
설 명 : 숫자만 입력
인 자 : 
사용법 : 
작성일 : 2020-08-25
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.08.25   정다빈       최초작성
************************************************************************/
function onlyNumber(obj){
    var numExp = /[^0-9]/g;
    
    if(numExp.test(obj.value)){
        obj.value = obj.value.replace(obj.value,''); // 공백제거
        return false;
	}
}

/************************************************************************
함수명 : removeChar
설 명 : 불필요 문자열 제거
인 자 : 
사용법 : 
작성일 : 2020-08-25
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.08.25   정다빈       최초작성
************************************************************************/
function removeChar(event) {
    event = event || window.event;
    var keyID = (event.which) ? event.which : event.keyCode;
    if ( keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39 ){
    	return;
    }else{
    	event.target.value = event.target.value.replace(/[^0-9]/g, "");
    }
}

/************************************************************************
함수명 : inputOnNum
설 명 : 숫자만 입력가능하게 제한, 범위 제한
인 자 : 
사용법 : 
작성일 : 2020-09-11
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.08.25   정다빈       최초작성
 ************************************************************************/
function inputOnNum(event,that) {
	event = event || window.event;
	var keyID = (event.which) ? event.which : event.keyCode;
	if ( keyID == 8 || keyID == 46 || keyID == 37 || keyID == 39 ){
		return;
	}else{
		event.target.value = event.target.value.replace(/[^0-9]/g, "");
	}
	if(that.name=="zoom"){
		if(parseInt($(that).val())<0 || parseInt($(that).val())>20){
			event.target.value = event.target.value.replace("");
		}else{
			return;
		}
	}else{
		if(parseInt($(that).val())<0 || parseInt($(that).val())>90){
			event.target.value = event.target.value.replace("");
		}else{
			return;
		}
	}
}

/************************************************************************
함수명 : telChk
설 명 : 전화번호 체크
인 자 : 
사용법 : 
작성일 : 2020-08-25
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.08.25   정다빈       최초작성
 ************************************************************************/
function telChk() {
	//부분 전화번호에 하나라도 값 기입시
	if($("#userPhone2").val().length>0 || $("#userPhone3").val().length>0){
		//1,2,3번째가 지정 자리수 이상일때만 값 주입
		if($("#userPhone1").val().length>1 && $("#userPhone2").val().length>2 && $("#userPhone3").val().length>3){
			var phone = $("#userPhone1").val()+"-"+$("#userPhone2").val()+"-"+$("#userPhone3").val();
			$("#userPhone").val(phone);
		}else{
			alert("전화번호 형식이 올바르지 않습니다.");
			return false;
		}
	}else{
		$("#userPhone").val("");
	}
	return true;
}

/************************************************************************
함수명 : addressChk
설 명 : api 검색 주소 + 상세주소 통합 저장시
인 자 : 
사용법 : 
작성일 : 2021-11-25
작성자 : 솔루션사업팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2021.11.25   정다빈       최초작성
 ************************************************************************/
function addressChk(ads,dv) {
	console.log(ads);
	var adds='';
	//부분 전화번호에 하나라도 값 기입시
	if(ads=="address"){
		adds = $("#addressHome1").val()+dv+$("#addressHome2").val();
	}else{
		adds = $("#"+ads+"1").val()+dv+$("#"+ads+"2").val();
	}
	return adds;
}

/************************************************************************
함수명 : addressChk
설 명 : api 검색 주소 + 상세주소 상세화면 분기시
인 자 : 
사용법 : 
작성일 : 2021-11-25
작성자 : 솔루션사업팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2021.11.25   정다빈       최초작성
 ************************************************************************/
function addressSp(ads,tagVal,dv) {
	//부분 전화번호에 하나라도 값 기입시
	$("#"+ads+"1").val(tagVal.split(dv)[0]);
	$("#"+ads+"2").val(tagVal.split(dv)[1]);
}

/************************************************************************
함수명 : spaceChk
설 명 : 공백 및 특수문자를 입력방지해주는 함수(영문,숫자,'-(하이폰)' 입력 가능)
인 자 : 
사용법 : 
작성일 : 2020-08-25
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.08.25   정다빈       최초작성
************************************************************************/
function spaceChk(obj){//공백입력방지
	var str_space = /\s/; //공백체크변수선언
	
	//특수문자 정규식
	var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\_+<>@\#$%&\\\=\(\'\"]/gi;
	
	if (str_space.exec(obj.value)){ //공백체크
        obj.focus();
        obj.value = obj.value.replace(' ',''); // 공백제거
        return false;	
	}
	//패스워드,시험코드 제외 특수문자 입력 불가
	if(!(obj.name=="userPw" || obj.name == "userPw2" || obj.name == "testCode")){
		if(regExp.test(obj.value)){
			obj.focus();
	        obj.value = obj.value.replace(obj.value,''); // 공백제거
	        return false;
		}
	}
	//이름,직급,등 (한글 입력할 항목 추가) 한글입력 가능
	if(!(obj.name == "userName" || obj.name == "userRank"
		|| obj.name == "searchValue" || obj.name == "inTel")){
		//좌우 방향키, 백스페이스, 딜리트, 탭키에 대한 예외
		if(event.keyCode == 8  || event.keyCode == 9 
		|| event.keyCode == 37 || event.keyCode == 39){
			return false;
		}
		obj.value=obj.value.replace(/[ㄱ-ㅎㅏ-ㅣ가-힣]/g,'');
	}
	
}

/************************************************************************
함수명 : chkMenu
설 명 : 메뉴 관련 항목 페이지 이동 시 메뉴 선택 보여주기 
인 자 : 
사용법 : 
작성일 : 2020-12-30
작성자 : 미래전략사업팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.12.30   정다빈       최초작성
************************************************************************/
function chkMenu(that){
	//console.log("대메뉴 항목 클릭 : "+$(that).attr('id'));
	
	//클릭한 항목 액티브
	//단일메뉴와 다중메뉴 분기
	if($(this).parent().parent().children('a').text().indexOf('혼잡도 범위 설정')!=-1){
		$(this).parent().parent().parent().parent().children('a').addClass('active');
	}else{
		$(this).parent().parent().children('a').addClass('active');
	}
}


/************************************************************************
함수명 : goMenuSite
설 명 : 메뉴 관련 항목 페이지 이동 
인 자 : 
사용법 : 
작성일 : 2020-07-30
작성자 : 미래전략사업팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.07.30   정다빈       최초작성
************************************************************************/
function goMenuSite(goUrl,thDiv){
	//기존 달력 삭제 및 재생성
	$("#ui-datepicker-div").remove();

	console.log("주소 이동 : "+goUrl);
	console.log("대상 div : "+thDiv);
	if(typeof thDiv ==="undefinded" || thDiv =='' || thDiv==null){
		thDiv="mainDiv";
	}
	var loadAjx;
	if(goUrl=="/common/firstView.do"){
		loadAjx = {
			url : goUrl,
			type : "post",
			async : false,
			success : function(json) {
				$("#"+thDiv).empty();
				$("#"+thDiv).load(json.chgUrl);
			},
			error : function() {
				alert("에러가 발생하였습니다.");
			}
		};
	}else{
		//pd,caster의경우만 maindiv 조정
		loadAjx = {
	        url: goUrl,
	        async : false,
	        success: function(json){
	        	if(json==""){//서버 단에서 에러가 없을 경우
	        		alert("접근 권한이 없습니다.");
	        		return false;
            	}else{
            		if(typeof thDiv !== "undefined"){
    					if(typeof json.chgUrl !=="undefined"){
    						$("#"+thDiv).load(json.chgUrl);
    						
    						if(json.chgUrl=="/producer/producer.do" || json.chgUrl=="/caster/caster.do"){
    							$("#mainDiv").attr("class","mainDiv2");
    						}else{
    							$("#mainDiv").attr("class","mainDiv");
    						}
    						
    					}else{
    						$("#"+thDiv).load(goUrl);
    						
    						if(goUrl=="/producer/producer.do" || goUrl=="/caster/caster.do"){
    							$("#mainDiv").attr("class","mainDiv2");
    						}else{
    							$("#mainDiv").attr("class","mainDiv");
    						}
    						
    					}
    				}else{
    					$("#mainDiv").empty();
    					if(typeof json.chgUrl !=="undefined"){
    						$("#mainDiv").load(json.chgUrl);
    						
    						if(json.chgUrl=="/producer/producer.do" || json.chgUrl=="/caster/caster.do"){
    							$("#mainDiv").attr("class","mainDiv2");
    						}else{
    							$("#mainDiv").attr("class","mainDiv");
    						}
    						
    					}else{
    						$("#mainDiv").load(goUrl);
    						
    						if(goUrl=="/producer/producer.do" || goUrl=="/caster/caster.do"){
    							$("#mainDiv").attr("class","mainDiv2");
    						}else{
    							$("#mainDiv").attr("class","mainDiv");
    						}
    						
    					}
    				}
            	}
	        },
	        error: function(res,error){
	            alert("에러가 발생했습니다."+error);
	        }
	    };
		
	}
	$.ajax(loadAjx);
	//화면이동 방지
	event.preventDefault();
    
}


/************************************************************************
함수명 : chkBoxFunc
설 명 : 테이블 내 체크박스 관련 이벤트 처리
인 자 : 테이블(this)를 받음(that으로)
사용법 : 
작성일 : 2020-08-17
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.08.17   정다빈       최초작성
************************************************************************/
function chkBoxFunc(that){
	var chkId=$(that).attr("id");
	var chkVal=$(that).val();
	var temp =[];//지금 체크한 체크박스에 관한 배열
	//console.log("tbody의 체크된 체크박스 수 : "+$('input:checkbox[name="chk"]:checked').length);
	//클릭한게 전체선택일 경우
	if(chkId=="chkAll"){
		//console.log("전체선택");
		//체크된 경우
		if ($("#chkAll").is(":checked")){
			//console.log("전체 체크박스 선택됨");
			//console.log("현재 페이지 넘버 : ");
			//하위 체크박스들도 모두 선택
			$("tbody input:checkbox").prop("checked", true);
			
		}else{//선택->취소 : 전체 체크 해지시
			//console.log("전체 체크박스 해지됨");
		    $("tbody input:checkbox").prop("checked", false);
		}
		 
	}else{//단일 선택일 경우
		//console.log("단일체크박스 클릭"+$(that).val());
		//console.log("@@@체크된 값들 길이: "+$('input:checkbox[name="chk"]:checked').length);
		//console.log("@@@tbody 길이: "+$("tbody tr").length);
		//단일선택->전체
		if($('input:checkbox[name="chk"]:checked').length==$("tbody tr").length){
    		$("#chkAll").prop("checked", true);
    	}else{//단일해지->전체해지
    		$("#chkAll").prop("checked", false);
    	}
		if($(that).is(":checked")){//체크박스가 체크된 경우
			//console.log("단일선택"+$(that).val());
		}else{//체크 해지된 경우
			//console.log("단일해지"+$(that).val());
			
		}
	}
	
}

/************************************************************************
함수명 : chkArrValiF
설 명 : 체크박스 배열내 선택 구별 함수
인 자 : 
사용법 : 
작성일 : 2020-08-19
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.08.19   정다빈       최초작성
************************************************************************/
function chkArrValiF(objArr,chkVal){
	for(var i =0;i<objArr.length;i++){
		if(chkVal==objArr[i]){
			return false;
		}
	}
	return true;
}

/************************************************************************
함수명 : schChkKey
설 명 : 검색 값 유효성 검사
인 자 : 
사용법 : 
작성일 : 2020-08-30
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.08.30   정다빈       최초작성
 ************************************************************************/
function schChkKey(that,schFlag){
	var sndUrl='';
	var dat;
	//키워드가 무엇인지 판별
	var schId= $(that).attr("name").split("_")[1];
	//텍스트에 값을 입력 안했다면
	if(schId!="empCode" && $(that).parent().children().first().val()==""){
		$(that).parent().parent().children().last().css("color","red");
		$(that).parent().parent().children().last().text("값을 입력해주세요");
	}else{
		//검색버튼이 2개이상일때는 어디로 보낼지 값이 무엇인지 분기처리
		if(schId=="userId"){
			sndUrl="/user/findUserId.do";
			dat={"userId":$(that).parent().children().first().val()};
			
			var schData=ajaxMethod(sndUrl, dat);
			//id일 경우는 값이 없을때 사용가능하고 시험코드는 값이 있을때 사용가능함
			if(schData == "" || typeof schData.data === "undefined"){//db에 값 미존재
				$(that).parent().parent().children().last().css("color","blue");
				$(that).parent().parent().children().last().text("사용 가능한 id입니다.");
				schFlag=true;
			}else{//db에 값 존재
				$(that).parent().parent().children().last().css("color","red");
				$(that).parent().parent().children().last().text("이미 사용중인 id입니다.");
				schFlag=false;
			}
		}
	}
	return schFlag;
}

/************************************************************************
함수명 : tbUpdate
설 명 : 수정 기능 함수(동적 버튼)
인 자 : that(테이블 this), paramUrl(이동할 주소)
사용법 : 
작성일 : 2020-08-24
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.08.24   정다빈       최초작성
************************************************************************/
function tbUpdate(that,paramUrl){
	var tagId;
	var cnt = $('input:checkbox[name="chk"]:checked').length;
	if($('input:checkbox[name="chk"]:checked').length==0){
		alert("수정할 항목을 선택해 주세요");
	}else if($('input:checkbox[name="chk"]:checked').length!=1){
		alert("수정할 항목을 하나만 선택해 주세요");
	}else{
		for(i=0;i<$("tbody tr").length;i++){
			if($("#chk"+i).is(":checked")){
				tagId = $("#chk"+i).val();
			}
		}
		if(paramUrl.indexOf("user")!=-1){
			$("#content").load(paramUrl,{"userId":tagId});
		}
		if(paramUrl.indexOf("terminal")!=-1){
			if(paramUrl.indexOf("directRegularStd")!=-1){
				$("#content").load(paramUrl,{"carType":tagId});
			}else{
				$("#content").load(paramUrl,{"trainNum":tagId});
			}
		}
		
		 
	}
}

/************************************************************************
함수명 : tbDelete
설 명 : 삭제 기능 함수(동적 버튼)
인 자 : that(테이블 this), paramUrl(이동할 주소)
사용법 : 
작성일 : 2020-08-24
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.08.24   정다빈       최초작성
************************************************************************/
function tbDelete(that,paramUrl,callback){
	var idArr=[]; 

	for(i=0;i<$("tbody tr").length;i++){
		if($("#chk"+i).is(":checked")){
			idArr.push($("#chk"+i).val());//배열에 아이디 값 삽입
		}
	}
	if(typeof idArr.length==="undefined" || idArr.length==0){
		alert("삭제할 항목을 선택해 주세요");
	}else{
		if(goUrl=="/statistic/dataLog.do"){
			if(confirm("선택하신 항목을 삭제하시겠습니까? \n <주의> 삭제하신 데이터는 복구할 수 없습니다!")==true){
				var url=paramUrl;
				var data = {"idArr":idArr};
				ajaxMethod(url, data, callback);
				chkArr=[];
			}
		}else{
			if(confirm("선택하신 항목을 삭제하시겠습니까?")==true){
				var url=paramUrl;
				var data = {"idArr":idArr};
				ajaxMethod(url, data, callback);
				chkArr=[];
			}
		}
	}
}


/************************************************************************
함수명 : calculDate
설 명 : 삭제 기능 함수(동적 버튼)
인 자 : idx(날짜가 존재하는 칼럼)
사용법 : 
작성일 : 2020-08-24
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.08.26   정다빈       최초작성
************************************************************************/
function calculDate(){
	//console.log("계산함수 들어옴 idx : "+iidx);
	$.fn.dataTable.ext.search.push(
        function(settings, data, dataIndex){
		////console.log("계산함수 들어옴 idx4 : "+iidx);
            var min = Date.parse($('#fromDate').val()+" 00:00:00");
            var max = Date.parse(moment($('#toDate').val()).add(1,'days').format('YYYY-MM-DD'))
            var targetDate = Date.parse(data[iidx]);

            if( (isNaN(min) && isNaN(max) ) || 
                (isNaN(min) && targetDate <= max )|| 
                ( min <= targetDate && isNaN(max) ) ||
                ( targetDate >= min && targetDate <= max) ){ 
                    return true;
            }
            return false;
        }
    )
}

function dtTbSetting(){
 
    // DataTables Default
    lang_eng = {
        "decimal" : "",
        "emptyTable" : "No data available in table",
        "info" : "Showing _START_ to _END_ of _TOTAL_ entries",
        "infoEmpty" : "Showing 0 to 0 of 0 entries",
        "infoFiltered" : "(filtered from _MAX_ total entries)",
        "infoPostFix" : "",
        "thousands" : ",",
        "lengthMenu" : "Show _MENU_ entries",
        "loadingRecords" : "Loading...",
        "processing" : "Processing...",
        "search" : "Search : ",
        "zeroRecords" : "No matching records found",
        "paginate" : {
        	 "first" : "<<",
             "last" : ">>",
             "next" : ">",
             "previous" : "<"
        },
        "aria" : {
            "sortAscending" : " :  activate to sort column ascending",
            "sortDescending" : " :  activate to sort column descending"
        }
    };
 
    // Korean
    lang_kor = {
        "decimal" : "",
        "emptyTable" : "데이터가 없습니다.",
        "info" : "_START_ - _END_ (총 _TOTAL_ 행)",
        "infoEmpty" : "0행",
        "infoFiltered" : "(전체 _MAX_ 행 중 검색결과)",
        "infoPostFix" : "",
        "thousands" : ",",
        "lengthMenu" : "_MENU_ 개씩 보기",
        "loadingRecords" : "로딩중...",
        "processing" : "처리중...",
        "search" : "검색 : ",
        "zeroRecords" : "검색된 데이터가 없습니다.",
        "paginate" : {
            "first" : "<<",
            "last" : ">>",
            "next" : ">",
            "previous" : "<"
        },
        "aria" : {
            "sortAscending" : " :  오름차순 정렬",
            "sortDescending" : " :  내림차순 정렬"
        }
    };
}


/************************************************************************
함수명 : getFormatDate
설 명 : yyyy-MM-dd 포맷으로 반환
인 자 : 
사용법 : 
작성일 : 2020-05-08
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.05.08   정다빈       최초작성
************************************************************************/
function getFormatDate(date){
    var year = date.getFullYear();              //yyyy
    var month = (1 + date.getMonth());          //M
    month = month >= 10 ? month : '0' + month;  //month 두자리로 저장
    var day = date.getDate();                   //d
    day = day >= 10 ? day : '0' + day;          //day 두자리로 저장
    var calDate =  year + '-' + month + '-' + day;
   //console.log("포맷된 날짜 : "+ calDate);
    return calDate;
}

/************************************************************************
함수명 : dateFunc
설 명 : 달력 관련 함수
인 자 : 
	   1. id1 : 시작일 input의 id값
	   2. id2 : 종료일 input의 id값 (기간이 아니라 날짜 input이 1개라면 안 넣어도 무방)
	   3. sdt : 시작일 디폴트 날짜 (안 넝어도 무방)
	   4. edt : 종료일 디폴트 날짜 (안 넝어도 무방)
	   5. format : ex 'YYYY-MM-DD' (안 넣을경우 디폴트 년월일)
	   6. callback : 날짜 초기지정 또는 날짜 변경후 실행할 callback함수(안 넣어도 무방)
사용법 : datepicker를 사용하려는 input이 포함된 jsp에서 document ready 시점에 사용
작성일 : 2020-05-08
작성자 : 솔루션사업팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.05.08   정다빈       최초작성
2024.10.22   정다빈       datetimepicker 로 변경
************************************************************************/
function dateFunc(id1,id2,sdt,edt,format,callback){

	console.log("dateFunc 실행");
	var fmt="YYYY-MM-DD";
	if(typeof format !== "undefined" && typeof format != ""){
		fmt=format;
	}
	
	// 행사관리 -> 날짜 최대 값 제한이 없어야함
	if(id1 == 'EVENT_DATE'){
		
		$('#'+id1).datetimepicker({
			 format:fmt
		}).on('dp.change',function(e){// 변경 상황 있을 때 사용   
			/*console.log("날짜변경");
			alert("날짜변경");*/
		});
		
	} else if(id1 =='endDate') { // 공지사항 -> 날짜 최대 값 제한이 없어야 함
		$('#'+id1).datetimepicker({
			 format:fmt
		}).on('dp.change',function(e){// 변경 상황 있을 때 사용   
			/*console.log("날짜변경");
			alert("날짜변경");*/
		});
	} else if(id1 =='userDate1') { // 사용자 관리 시작일
		console.log("캘린더 1 선택 진입");
		
		$('#dateText1').datetimepicker({
			 format:fmt
		}).on('dp.change',function(e){// 변경 상황 있을 때 사용   

		});
		
		
	} else if(id1 =='userDate2') { // 사용자 관리 종료일
		console.log("캘린더 2 선택 진입");
		$('#dateText2').datetimepicker({
			 format:fmt
		}).on('dp.change',function(e){
			
		});
	}
	
	else if(id1 == 'standardDate') {
		/*$('#' + id1).datetimepicker({
		    format: 'YYYY-MM',  
		    viewMode: 'months'    
		}).on('dp.change',function(e){// 변경 상황 있을 때 사용   
			console.log("날짜변경");
			alert("날짜변경");
		});*/
		
		
		// 현재 달부터는 선택 못하도록 막음
		$('#' + id1).datetimepicker({
		    format: 'YYYY-MM',  
		    viewMode: 'months',
		    maxDate: moment().subtract(1, 'months').endOf('month') 
		}).on('dp.change', function(e) {

		});
	} else if(id1 == 'excellenceDate') {
		$('#' + id1).datetimepicker({
		    format: 'YYYY',        
		    viewMode: 'years',     
		    maxDate: moment().endOf('year'),
		    defaultDate: moment()
		}).on('dp.change', function(e) {
		    
		});
	} else {
		$('#'+id1).datetimepicker({
			 format:fmt,
			 maxDate : moment()
		}).on('dp.change',function(e){// 변경 상황 있을 때 사용   
			/*console.log("날짜변경");
			alert("날짜변경");*/
		});
		
		if(typeof id2 !== "undefined" && typeof id2 != ""){
			$('#'+id2).datetimepicker({
				format:fmt,
				 maxDate : moment()
			}).on('dp.change',function(e){// 변경 상황 있을 때 사용   
				
			});
		}
		
		//별도의 지정한 날짜가 있을 경우 사용
		if(typeof sdt !== "undefined" && typeof sdt != ""){
			$("#"+id1).val(sdt);
		}else{
			$("#"+id1).val(moment().format('YYYY-MM-DD'));
		}
		if(typeof edt !== "undefined" && typeof edt != ""){
			$("#"+id2).val(edt);
		}else{
			$("#"+id2).val(moment().format('YYYY-MM-DD'));
		}
	}
}




/************************************************************************
함수명 : parseStrToDate
설 명 : 문자열을(yyyymmdd) 날짜 타입으로 변경
인 자 : str
사용법 : 
작성일 : 2020-05-10
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.05.10   정다빈       최초작성
************************************************************************/
function parseStrToDate(date,time) {
	var str = date.replace(/-/gi,"");//특수문자 삽입시 제거
    var y = str.substr(0, 4);
    var m = str.substr(4, 2);
    var d = str.substr(6, 2);
    
    if(typeof time !=="undefined"){
    	var strT = time.replace(/:/gi,"");//특수문자 삽입시 제거
    	var hh = strT.substr(0,2);
    	var mm = strT.substr(2,2);
    	var dd = strT.substr(4,2);
    	return new Date(y,m-1,d,hh,mm,dd);
    }else{
    	return new Date(y,m-1,d);
    }
}



/************************************************************************
함수명 : exportChartToPng
설 명 : 차트 svg를 이미지로 변환
인 자 : chartid
사용법 : 
작성일 : 2020-12-21
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.12.21   정다빈       최초작성
************************************************************************/

function exportChartToPng(chartID){
	//fix weird back fill
	//console.log("exportChartToPng 차트아이디: "+chartID);
	d3.select('#'+chartID).selectAll("path").attr("fill", "none");
	//fix no axes
	d3.select('#'+chartID).selectAll("path.domain").attr("stroke", "black");
	//fix no tick
	d3.select('#'+chartID).selectAll(".tick line").attr("stroke", "black");
	var svgElement = $('#'+chartID).find('svg')[0];
	saveSvgAsPng(svgElement, chartID+'.png');
}


/************************************************************************
함수명 : trainOne
설 명 : 통계-단일편성 관련
인 자 : 
사용법 : 
작성일 : 2020-12-28
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.12.28   정다빈       최초작성
************************************************************************/
function trainOne(sdate){//"2018-01-01"
	//console.log("통계 단일/전체편성 테이블 함수 : "+sdate);
	
	var ajaxData=ajaxMethod("/statistic/trainTable.ajax", {"sDate":sdate});
	var alData = ajaxData.tbList;
	var tbFirst="<tr>"
	var tbEnd="</tr>";
	var tbM;
	var firstId;
	for (var i = 0; i < alData.length; i++) {
		//맨 처음 차량 선택
		if(i==0 && alData[i].rst =='Y'){
			tbM= "<td id='"+alData[i].trainNum+"' class='tn-active'>"+alData[i].idx+"<br>"+alData[i].trainNum+"</td>";
			if($("#trainNum").attr("class")=="disabledbutton"){
				firstId=null;
			}else{
				firstId=alData[i].trainNum;
			}	
		}else{
			//배경 선택가능
			if(alData[i].rst =='Y'){
				tbM+= "<td id='"+alData[i].trainNum+"' class='tn-y'>"+alData[i].idx+"<br>"+alData[i].trainNum+"</td>";
			}else{//배경 비선택
				tbM+= "<td id='"+alData[i].trainNum+"' class='tn-n'>"+alData[i].idx+"<br>"+alData[i].trainNum+"</td>";
			}
			//7열 초과시 줄바꿈
			if(parseInt((i+1)%4)==0){
				var tbCont = tbFirst+tbM+tbEnd;
				$("#trainTbd").append(tbCont);	
				tbM="";
			}else{
				//데이터의 마지막
				if((i+1)==alData.length){
					//다찰경우 제수(나누는 수 4)에서 나머지를 뺀만큼 공백임
					//console.log("마지막열");
					var cnt = 4-parseInt((i+1)%4);
					for (var j = 0; j < cnt; j++) {
						tbM+= "<td class='ndata'></td>";
					}
					var tbCont = tbFirst+tbM+tbEnd;
					$("#trainTbd").append(tbCont);	
					tbM="";
				}
			}	
		}
	}
	if($("#trainNum").attr("class")!="disabledbutton" 
		&& $("#trainTbd tr").find(".tn-active").attr('id')===undefined){
		firstId=0;
	}	
	return firstId;
}


/************************************************************************
함수명 : stcReNew
설 명 : 통계 데이터+하단 데이터테이블 생성 또는 갱신
인 자 : 
사용법 : 
작성일 : 2020-12-28
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2020.12.28   정다빈       최초작성
 ************************************************************************/
function stcReNew(alData){
	//막대그래프에 쓰이는 배열
	titleArr=	['x'];
	rlxArr	=	['여유'];
	usArr	=	['보통'];
	careArr	=	['주의'];
	cwdArr	=	['혼잡'];
	
	$(alData).each(function(i,list) {
		titleArr.push(alData[i].title);
		
		//혼잡도 변화 건수가 0일 경우 숫자가 그래프에 안나오게 함
		if(list.rlxCnt==0){
			rlxArr.push(null);
		}else{
			rlxArr.push(alData[i].rlxCnt);	
		}
		
		if(list.usCnt==0){
			usArr.push(null);
		}else{
			usArr.push(alData[i].usCnt);
		}

		if(list.careCnt==0){
			careArr.push(null);
		}else{
			careArr.push(alData[i].careCnt);
		}

		if(list.cwdCnt==0){
			cwdArr.push(null);
		}else{
			cwdArr.push(alData[i].cwdCnt);
		}

	});
	//테이블 갱신
    $("#tbody").empty();
    var tbCont="";
    	$(alData).each(function(i,list) {
    		if(
    			list.rlxCnt!=0||list.usCnt
    			||list.careCnt!=0||list.cwdCnt
    		){
    			tbCont += 	 "<tr><td>"+list.title+"</td>"	
				+"<td>"+list.rlxCnt+"</td>"	
				+"<td>"+list.usCnt+"</td>"		
				+"<td>"+list.careCnt+"</td>"		
				+"<td>"+list.cwdCnt+"</td>"	
				+"<td>"+list.avgCwd+"</td>"	
				+"<td>"+list.minWgt+"</td>"	
				+"<td>"+list.minTnum+"</td>"		
				+"<td>"+list.minDt+"</td>"		
				+"<td>"+list.maxWgt+"</td>"	
				+"<td>"+list.maxTnum+"</td>"		
				+"<td>"+list.maxDt+"</td></tr>";
    		}
    	});
    $("#tbody").append(tbCont);
}

/************************************************************************
함수명 : filterSbT
설 명 : 데이터테이블에서 열차별로 셀렉트박스 구성
인 자 : 
사용법 : 
작성일 : 2021-12-28
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2021.12.28   정다빈       최초작성
 ************************************************************************/
function filterSbT(){
	var ajaxData = ajaxMethod("/terminal/terminalList.ajax");
	var apStr='<select id="filterSb"><option value="1" selected>전체</option>';
	for (var i = 0; i < ajaxData.data.length; i++) {
		apStr+='<option value="'+ajaxData.data[i].trainNum+'">'+ajaxData.data[i].trainNum+'</option>';
	}
	apStr+='</select>';
	return apStr;
}


//#########################  Start Airhub ############################//

/************************************************************************
함수명 : createJqgridTable
설 명 : jqgrid 방식 테이블 생성
인 자 : header(th부분), url(컨트롤러에 매핑된 주소),id(테이블 id),array(받아온 데이터) 
사용법 : 
작성일 : 2021-04-07
작성자 : 미래사업전략팀 정다빈
수정일        수정자       수정내용
----------- ------ -------------------
2021.04.07   정다빈       최초작성
 ************************************************************************/
function createJqgridTable(header,body,url,id,array){
	console.log("jqgrid 테이블 생성");
    var $grid = $("#"+id).jqGrid({
    	
    	url: url,
        //local 은 이벤트가 발생되지 않으면 실행하지 않는다. url을 바로 태우지 않겠다는 의미이다.
    	//jqGrid의 데이터 타입은 json, xml, array 가 가능
    	datatype: "local",

        colNames: header,
        colModel: body,
        //width:'auto',   
       	height:'auto',
        rowNum: 20,
        rowList:[5,10,20,30,50,100],
        pager: '#jqGridPager',
        sortname: 'default',
        sortorder: "desc",
        viewrecords: true,
        multiselect: false,
        //rownumbers  : false, 기본값 false 맨앞에 숫자 나오느냐 안나오느냐                     
//        ondblClickRow : function(rowId, iRow, iCol, e) {
//            if(iCol == 1) {
//                alert(rowId+" 째줄 입니다.");
//            }
//        },
//        
        viewrecords : true,
        //autowidth:true,
        caption:"JQGRID TABLE",
        	
     });

     for(var i in array){
          $("#"+id).jqGrid('addRowData',i+1,array[i]);
     }
     
     $(".ui-jqgrid-titlebar").hide();
		
}



/************************************************************************
함수명 : randomString
설 명 : 랜덤문자열 생성
인 자 : 
사용법 : 
작성일 : 2021-10-07
작성자 : 술루션사업팀 김민경
************************************************************************/
function randomString() {
	const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz'
	const stringLength = 6
	let randomstring = ''
    for (let i = 0; i < stringLength; i++) {
    	const rnum = Math.floor(Math.random() * chars.length)
    	randomstring += chars.substring(rnum, rnum + 1)
    }
    return randomstring
}

/************************************************************************
함수명 : clickMenu
설 명 : 
인 자 : 
사용법 : 
작성일 : 2021-10-07
작성자 : 술루션사업팀 김민경
************************************************************************/
clickMenu = function(menu) {
	var getEls = document.getElementById(menu).getElementsByTagName("LI");
	var getAgn = getEls;
	for (var i=0; i<getEls.length; i++) {
			getEls[i].onclick=function() {
				for (var x=0; x<getAgn.length; x++) {
					getAgn[x].className=getAgn[x].className.replace("click", "");
				}
				this.className+=" click";
			}
	}
}



















