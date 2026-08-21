// 제보접수 시 필수항목 체크
// 6/8 충북요청사항 원활일 경우 좌표없이도 등록가능
function isProperInput(form){
	var startHour, startMin, endHour, endMin;
	console.log("제보접수 등록 시 필수항목체크");
	var xyOk=0;//좌표 없이도 등록가능하게 하는 flag 변수
	var dolbalChk = $("form").find("[name=REPORT_TYPE]").val();
	
	//2209 지역취합 : 돌발유형 제외 좌표,시작끝점 안찍게
	if(dolbalChk !=''
		&&dolbalChk!='A02'//공사
		&&dolbalChk!='A03'//행사
		&&dolbalChk!='A04'//사고
		&&dolbalChk!='A05'//기상
			){
		xyOk=1;
	}
	
	for(var i=0; i<form.length; i++){
		if(authCode != '5') {
			if(form[i].name == "ARTERY_NAME"){
				var objName = "도로명을";
				if(isBlank(form[i], objName)) return;
			}
			
			if(form[i].name == "F_NODE_NAME"){
				var objName = "시작지점을";
				//2209 지역취합 : 돌발유형 제외 좌표,시작끝점 안찍게
				if(xyOk!=1){//돌발유형 제외하고 좌표체크
					if(isBlank(form[i], objName)) return;
				}
			}
			
			if(form[i].name == "T_NODE_NAME"){
				var objName = "종료지점을";
				//2209 지역취합 : 돌발유형 제외 좌표,시작끝점 안찍게
				if(xyOk!=1){//돌발유형 제외하고 좌표체크
					if(isBlank(form[i], objName)) return;
				}
			}
		}
		
		if(form[i].name == "REPORT_TYPE"){
			var objName = "제보유형(대)를";
			if(notSelected(form[i], objName)) return;
		}
		
		if(form[i].name == "REPORT_TYPE2"){
			var objName = "제보유형(중)을";
			if(notSelected(form[i], objName)) return;
		}
		
		if(authCode != '5') {
			if($("#INFORMER_NONE").is(":checked")==false){//시민제보자 아닐경우
				if(form[i].name == "R_TEL" || form[i].name =="PHONE_CELL"){
					var objName = "통신원정보(연락처)를";
					if(isBlank(form[i], objName)) return;
				}
			}else{//시민제보자일경우
				if(form[i].name == "R_TEL"){
					var objName = "연락처를";
					if(isBlank(form[i], objName)) return;
				}
			}
			
			
			if(form[i].name == "X_COORDINATE" || form[i].name =="Y_COORDINATE"){
				var objName = "좌표를";
				//220608 충북 : 원활만 지도안찍게
				//2209 지역취합 : 돌발유형 제외 좌표,시작끝점 안찍게
				if(xyOk!=1){//돌발유형 제외하고 좌표체크
					if(isBlank(form[i], objName)) return;
				}
			}
		}		
		
		if(form[i].name == "STARTTIMEHH"){
			startHour = form[i].value;
			if(isNotProperHourFormat(form[i], "시간 형식이")) return;
		}
		
		if(form[i].name == "STARTTIMEMI"){
			startMin = form[i].value;
			if(isNotProperMinFormat(form[i], "시간 형식이")) return;
		}

		if(form[i].name == "ENDTIMEHH"){
			endHour = form[i].value;
			if(isNotProperHourFormat(form[i], "시간 형식이")) return;
		}
		
		if(form[i].name == "ENDTIMEMI"){
			endMin = form[i].value;
			if(isNotProperMinFormat(form[i], "시간 형식이")) return;
		}
		
		if(isNotProperTimeGap(startHour, startMin, endHour, endMin)) return;
	}
	return true;
}

function lengthCheck(obj, len){
	if(obj.value.length > len){
		alert("작성 가능한 최대 글자수를 초과하였습니다.");
		obj.value = obj.value.substr(0, len);
	}
}

function isBlank(obj, objName){
	if(obj.value.length == 0 || obj.value == "" || obj.value == undefined){
		alert(objName + " 입력해주세요.");
		obj.focus();
		return true;
	}
}

function notSelected(obj, objName){
	if(obj.value == "" || obj.value == undefined){
		alert(objName + " 선택해주세요.");
		obj.focus();
		return true;
	}
}

function isNotProperHourFormat(obj, objName){
	var pattern = /^([1-9]|[01][0-9]|2[0-3])$/;
	
	if(!pattern.test(obj.value)){
		alert(objName + " 올바르지 않습니다.");
		obj.focus();
        return true;
	}
}

function isNotProperMinFormat(obj, objName){
	var pattern = /^([0-5][0-9])$/;
	
	if(!pattern.test(obj.value)){
		alert(objName + " 올바르지 않습니다.");
		obj.focus();
        return true;
	}
}

function isNotProperTimeGap(startHour, startMin, endHour, endMin){
	var startTime = startHour + startMin;
	var endTime = endHour + endMin;
	
	if(startTime > endTime){
		alert("종료시간이 시작시간보다 빠를 수 없습니다.");
		document.getElementById("STARTTIMEHH").focus();
		return true;
	}
}

function checkHourLength(){
	var hourValue = document.getElementById("STARTTIMEHH").value;
	console.log("hourLength: " + hourValue.length);
	
	if(hourValue.length == 2){
		hourValue = "0" + hourValue;
	}
}

















