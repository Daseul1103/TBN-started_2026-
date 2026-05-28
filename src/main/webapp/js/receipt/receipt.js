function initReceipt(){
	reportTypeOnChange('REPORT_TYPE', 'REPORT_TYPE2');
	
	$("#AREA_CODE").val(lgnArea).prop("selected", true);
	selectAreaCodeSub();
	
	dateNTimeForReceipt();
	
	$("#arterySelect").val(lgnArea).prop("selected", true);
	listUpArtery();
	
	$("#REPORTMEAN_TYPE").val("0").prop("selected", true);
	
	//$("#missedCallList").load("/receipt/missedCallList.do?AREA_CODE="+lgnArea);
	//$('.group_tel').scrollTop($('.group_tel')[0].scrollHeight);

	$("#mapDiv").load("/map/mainMap.do");
	//이 부분에서 스크롤제어를 하면 등록시나 임시저장 시 등 스크롤 갱신되므로 주석
	//$('.group_tel').scrollTop(0);
}

//function sseTest(){
//	console.log("sseTest");
//	
//	$.ajax({
//        type : "GET",
//        url : "/sse/publish.do?message=hahahaha",
//        error : function(){
//            alert('통신실패!!');
//        },
//        success : function(data){
//            alert("통신데이터 값 : " + data) ;
//        }
//         
//    });
//	
//}

//금일접수현황 팝업
function receivedStatus() {
	var url = "/receipt/receivedStatus.do";
	var windowName = "금일접수현황";
	var popupW = 1600; // 팝업 넓이
	var popupH = 850; // 팝업 높이
	var left = (document.body.offsetWidth/2) - (popupW/2);
		left += window.screenLeft;
	var top = (document.body.offsetHeight/2) - (popupH/2);

	popObj = window
			.open(
				url,
				windowName,
					'width='+ popupW
				+ ', height='+ popupH
				+ ', left=' + left
				+ ', top=' + top
				+ ', toolbar=no, location=no, status=no, menubar=no, scrollbars=no, resizable=no');
}

//제보접수 - 시작,종료시간
function dateNTimeForReceipt(){
	var startDay = new Date();
	
	var startDate = startDay.getFullYear()+"-"+((startDay.getMonth()+1)>9? (startDay.getMonth()+1): '0'+(startDay.getMonth()+1))+"-"+(startDay.getDate() > 9 ? startDay.getDate():"0" +startDay.getDate());
	var toHH = startDay.getHours() > 9? startDay.getHours():  "0" + startDay.getHours();
	var toMM = startDay.getMinutes() > 9? startDay.getMinutes():  "0" + startDay.getMinutes();
	
	startDay.setDate(startDay.getDate()+ 1);
	startDay.setMinutes(startDay.getMinutes() + 30, 0, 0);
	
	var endHH = startDay.getHours() > 9? startDay.getHours():  "0" + startDay.getHours();
	var endMM = startDay.getMinutes() > 9? startDay.getMinutes():  "0" + startDay.getMinutes();
	var endDate = startDay.getFullYear()+"-"+( (startDay.getMonth()+1) > 9 ? (startDay.getMonth()+1) : "0" + (startDay.getMonth()+1) )+"-"+(startDay.getDate() > 9 ? startDay.getDate():"0" +startDay.getDate());
	
	$("#STARTTIMEHH").val(toHH);
	$("#STARTTIMEMI").val(toMM);
	$("#ENDTIMEHH").val(endHH);
	$("#ENDTIMEMI").val(endMM);
	
	$("#START_DAY").val(startDate);
	$("#END_DAY").val(startDate);
}

//전화받으면 통신원 폼에 해당 통신원 정보 반영하는 함수
function selectInformerByPhoneTest(phone_cell){
	console.log("selectInformerByPhoneTest");
	console.log("currentMonth: " + currentMonth(""));
	console.log("lastMonth: " + lastMonth(""));
	console.log("twoMonthAgo: " + twoMonthAgo(""));

	var informerVO = ajaxMethod("/receipt/selectInformerByPhone.ajax", {"phone_cell":phone_cell
																			, "month1":currentMonth("")
																			, "month2":lastMonth("")
																			, "month3":twoMonthAgo("")}).data;
	if(informerVO === "noInfoFound") {
		//view clear
		clearInformerView();

		//시민제보자일 경우 수정권한
		document.getElementById('INFORMER_NAME').readOnly = false;
		$("#INFORMER_NAME").focus();
		document.getElementById('MEMO').readOnly = true;
		document.getElementById('btn_informerSend').disabled = true;
		
		//view생성
		document.getElementById('INFORMER_NONE').checked = true;
		document.getElementById('PHONE_CELL').value = phone_cell;
		document.getElementById('INFORMER_TYPE_NM').value = "시민";
		document.getElementById('INFORMER_TYPE').value = "2";
		
		document.getElementById('LOSS_TEL').value = phone_cell;

	} else {
		//view clear
		clearInformerView();
		
		//등록된 통신원일 경우 수정권한
		document.getElementById('INFORMER_NAME').readOnly = true;
		document.getElementById('MEMO').readOnly = false;
		document.getElementById('btn_informerSend').disabled = false;
		
		//view생성
		document.getElementById('INFORMER_ID').value = informerVO.informer_ID;
		document.getElementById('ACT_ID').value = informerVO.act_ID;
		document.getElementById('INFORMER_NAME').value = informerVO.informer_NAME;
		document.getElementById('PHONE_CELL').value = informerVO.phone_CELL;
		document.getElementById('PHONE_HOME').value = informerVO.phone_HOME;
		document.getElementById('INFORMER_TYPE').value = informerVO.informer_TYPE;
		
		document.getElementById('INFORMER_TYPE_NM').value = informerVO.type_NAME;
		document.getElementById('ORG_ID').value = informerVO.org_ID;
		document.getElementById('ORG_NAME').value = informerVO.org_NAME;
		document.getElementById('ORG_SUB_ID').value = informerVO.org_ID;
		document.getElementById('ORG_SUB_NAME').value = informerVO.org_SNAME;
		
		if(!informerVO.zipcode){
			document.getElementById('ADDRESS_HOME').value = informerVO.address_HOME;
			if(!informerVO.address_HOME){
				document.getElementById('ADDRESS_HOME').value = "-";
			}
		} else{
			document.getElementById('ADDRESS_HOME').value = "("+informerVO.zipcode+") "+informerVO.address_HOME;
		}
		
		document.getElementById('STAT1').value = informerVO.stat1;
		document.getElementById('STAT2').value = informerVO.stat2;
		document.getElementById('STAT3').value = informerVO.stat3;
		
		if(informerVO.memo != undefined && informerVO.memo != ''){
			document.getElementById('MEMO').value = informerVO.memo;
		}
		if(informerVO.flag_BLACKLIST == 'Y'){
			document.getElementById('FLAG_BLACKLIST').checked = true;
		}
		//제보접수부분의 연락처 값 생성
		document.getElementById('LOSS_TEL').value = phone_cell;
	}
}

//통신원  - 메모
function saveInformerMemo(){
	console.log("saveInformerMemo");
	//var memo = document.getElementById("MEMO").value.replace("\r\n", "<br>");
	var memo = document.getElementById("MEMO").value;
	var informerID = document.getElementById("INFORMER_ID").value;
	console.log("memo: " + memo + ", informerID: " + informerID);

	var result = ajaxMethod("/receipt/saveInformerMemo.ajax", {MEMO: memo, INFORMER_ID: informerID}).result;
	if(result > 0) {
		alert("저장이 완료되었습니다.");
	} else {
		alert("잘못된 접근입니다.");
	}
}

function saveNewInformerMemo(){
	console.log("saveNewInformerMemo");
	//var memo = document.getElementById("MEMO").value.replace("\r\n", "<br>");
	var memo = document.getElementById("MEMO").value;
	var informerID = document.getElementById("INFORMER_ID").value;
	console.log("memo: " + memo + ", informerID: " + informerID);

	var result = ajaxMethod("/receipt/saveInformerMemo.ajax", {MEMO: memo, INFORMER_ID: informerID}).result;
	if(result > 0) {
		alert("저장이 완료되었습니다.");
	} else {
		alert("잘못된 접근입니다.");
	}
}

//selectBox - 제보유형
function reportTypeOnChange(thisInformetTypeId, changeInformetTypeId) {
	console.log("reportTypeOnChange");
	var selectedSelectBox = $("#" + thisInformetTypeId + " option:selected"); //상위셀렉트박스에서 선택된 값
	var firstValue = selectedSelectBox.val(); //상위셀렉트박스에서 선택된 값
	var reportTypeList = ajaxMethod("/receipt/reportTypeOnChange.ajax", {BAS_LCOD:firstValue}).data; //상위셀렉트박스의 값으로 하위 리스트를 불러온다
	
	var nextSelectBox = $("#" + changeInformetTypeId); //하위셀렉트박스 객체
	nextSelectBox.empty();
	$("#REPORT_TYPE3").empty();
	//제보접수 화면에서 충북계정이거나 
	//접수현황 팝업에서는 제보유형 대 중 나오게 
	
	if(typeof tabMenu !=="undefined"){
		console.log("셀박변경 메뉴 : "+tabMenu);
	}
	
	if(
		(typeof lgnArea !=="undefined" && lgnArea =='100')
	  ||(typeof tabMenu !=="undefined" 
		  && (tabMenu =='receivedStatus' || tabMenu =='fullReceivedHistory'
			||tabMenu =='receivedStatusSearch' || tabMenu =='fullReceivedHistorySearch')
		)
	){
		nextSelectBox.append("<option value=''>제보유형(중)</option>");
	}
	for(var i=0; i<reportTypeList.length; i++){
		nextSelectBox.append("<option value='" + reportTypeList[i].bas_SCOD + "'>" //하위셀렉트박스 객체에 옵션값을 집어넣음
							+ reportTypeList[i].bas_NAME + "</option>");
	}
}

//selectBox - 지역구분
function selectAreaCodeSub(){
	var selectBox = $("#AREA_CODE option:selected");
	var firstBoxValue = selectBox.val();
	
	var subAreaCodeList = ajaxMethod("/receipt/selectAreaCodeSub.ajax", {AREA_CODE : firstBoxValue}).data;
	var nextSelectBox = $("#AREA_CODE_SUB");
	
	nextSelectBox.empty();
	for(var i=0; i<subAreaCodeList.length; i++){
		nextSelectBox.append("<option value='" + subAreaCodeList[i].area_SUB_CODE + "'>"
							+ subAreaCodeList[i].area_SUB_NAME + "</option>");
	}
}

//예약접수 - 통신원정보 LABEL
function appendInformerLabel(informerVO, idx){
	console.log("appendInformerLabel");

	document.getElementById("labelUserId" + idx).innerHTML = informerVO.act_ID;
	document.getElementById("labelUserName" + idx).innerHTML = informerVO.informer_NAME;
	if(idx == 0){
		firstInformerVO = informerVO;
	} else if(idx == 1){
		secondInformerVO = informerVO;
	} else if(idx == 2){
		thirdInformerVO = informerVO;
	} else {
		console.log("informerVO 저장안됨");
	}
}

//검색 폼을 파라미터로 받아 컨트롤러로 전송
function generateSearchVO(frm){
	console.log("generateSearchVO");

	var REPORT_TYPE = $("#REPORT_TYPE").val();
	var REPORT_TYPE2 = $("#REPORT_TYPE2").val();
	var REPORTMEAN_TYPE = $("#REPORTMEAN_TYPE").val();
	var	FLAG_SEND = $("#FLAG_SEND").val();
	var FLAG_BROD = $("#FLAG_BROD").val();
	var FLAG_IMPORTANT="";
	var FLAG_DISASTOR="";
	
	var RECEIPT_TIME = $("#RECEIPT_TIME").val();
	var RECEPTION_NAME = $("#RECEPTION_NAME").val();
	var INDIVIDUAL_NAME = $("#INDIVIDUAL_NAME").val();
	var rcptAndOr = $('input[name="rcptAndOr"]:checked').val();
	
	if($("#FLAG_IMPORTANT").val()!=null){
		FLAG_IMPORTANT = $("#FLAG_IMPORTANT").val();
	}
	if($("#FLAG_DISASTOR").val()!=null){
		FLAG_DISASTOR = $("#FLAG_DISASTOR").val();
	}
	var AREA_CODE = $("#AREA_CODE").val();
	console.log("AREA_CODE: " + AREA_CODE);
	var START_DAY;
	var END_DAY;
	if(frm === "receivedStatusSearch"){
		START_DAY = currentDate("");
		END_DAY = currentDate("");
		//START_DAY = "20211006";
		//END_DAY = "20211006";
	} else if(frm === "fullReceivedHistorySearch"){
		START_DAY = $("#START_DAY").val();
		END_DAY = $("#END_DAY").val();
	} else {
		console.log("tabMenu: " + frm);
	}
	var CONTENT1 = $("#CONTENT1").val();
	var CONTENT2 = $("#CONTENT2").val();
	
	var searchVO = {
			"REPORT_TYPE":REPORT_TYPE
			, "REPORT_TYPE2":REPORT_TYPE2
			, "REPORTMEAN_TYPE":REPORTMEAN_TYPE
			, "FLAG_SEND":FLAG_SEND
			, "FLAG_BROD":FLAG_BROD
			, "FLAG_IMPORTANT":FLAG_IMPORTANT
			, "FLAG_DISASTOR":FLAG_DISASTOR
			, "AREA_CODE":AREA_CODE
			, "START_DAY":START_DAY
			, "END_DAY":END_DAY
			, "CONTENT1":CONTENT1
			, "CONTENT2":CONTENT2
			,'RECEIPT_TIME':RECEIPT_TIME 
			,'INDIVIDUAL_NAME':INDIVIDUAL_NAME 
			,'RECEPTION_NAME':RECEPTION_NAME 
			,'rcptAndOr':rcptAndOr
	};
	return searchVO;
}

function generateSearchURL(frm, crtPage){
	console.log("generateSearchURL");

	var REPORT_TYPE = $("#REPORT_TYPE").val();
	var REPORT_TYPE2 = $("#REPORT_TYPE2").val();
	var REPORTMEAN_TYPE = $("#REPORTMEAN_TYPE").val();
	var	FLAG_SEND = $("#FLAG_SEND").val();
	var FLAG_BROD = $("#FLAG_BROD").val();
	var FLAG_IMPORTANT = $("#FLAG_IMPORTANT").val();
	var FLAG_DISASTOR = $("#FLAG_DISASTOR").val();
	var AREA_CODE = $("#AREA_CODE").val();
	
	var RECEIPT_TIME = $("#RECEIPT_TIME").val();
	var INDIVIDUAL_NAME = $("#INDIVIDUAL_NAME").val();
	var RECEPTION_NAME = $("#RECEPTION_NAME").val();
	var rcptAndOr = $('input[name="rcptAndOr"]:checked').val();
	
	var START_DAY;
	var END_DAY;
	
	var semiUrl;
	
	if(frm === "receivedStatusSearch"){
		START_DAY = currentDate("");
		END_DAY = currentDate("");
		semiUrl="/receipt/searchFullStatus.do?SEARCH_TAB=";
	} else if(frm === "fullReceivedHistorySearch"){
		START_DAY = $("#START_DAY").val();
		END_DAY = $("#END_DAY").val();
		semiUrl="/receipt/searchFullStatusFR.do?SEARCH_TAB=";
	} else{
		semiUrl="/receipt/searchFullStatus.do?SEARCH_TAB=";
		console.log("tabMenu: " + frm);
	}
	var CONTENT1 = $("#CONTENT1").val();
	var CONTENT2 = $("#CONTENT2").val();
	
	var url = semiUrl +frm +"&REPORT_TYPE="+REPORT_TYPE
											+'&REPORT_TYPE2='+REPORT_TYPE2 +'&REPORTMEAN_TYPE='+REPORTMEAN_TYPE
											+'&FLAG_SEND='+FLAG_SEND 
											+'&FLAG_BROD='+FLAG_BROD
											+'&FLAG_IMPORTANT='+FLAG_IMPORTANT
											+'&FLAG_DISASTOR='+FLAG_DISASTOR
											+'&AREA_CODE='+AREA_CODE +'&START_DAY='+START_DAY
											+'&END_DAY='+END_DAY + '&startRow='+crtPage
											+'&CONTENT1='+CONTENT1 
											+'&CONTENT2='+CONTENT2 
											+'&RECEIPT_TIME='+RECEIPT_TIME 
											+'&INDIVIDUAL_NAME='+INDIVIDUAL_NAME 
											+'&RECEPTION_NAME='+RECEPTION_NAME 
											+'&rcptAndOr='+rcptAndOr;
	return url;
}

//메모(제보내용)가 없을 경우 내용 조합
function sentenceForReceiptContent(){
	console.log("sentenceForReceiptContent");

	var str = "";
	
	//긴급체크
/*	var FLAG_IMPORTANT = $("input:checkbox[id=FLAG_EMERGENCY]");
	if(FLAG_IMPORTANT.is(":checked")){
		str += "<긴급>";
	}
	var FLAG_DISASTOR = $("input:checkbox[id=FLAG_DISASTOR]");
	if(FLAG_DISASTOR.is(":checked")){
		str += "<재난>";
	}
*/	
	//도로명
	var ARTERY_NAME = $("#ARTERY_NAME").val();
	if(ARTERY_NAME != ""){
		str += ARTERY_NAME;
		str += " ";
	}
	//차선
	var LANE = $("#LANE").val();
	if(LANE != ""){
		str += LANE;
		str += "차로 ";
	}
	//F노드
	var F_NODE_NAME = $("#F_NODE_NAME").val();
	if(F_NODE_NAME != ""){
		str += $("#F_NODE_NAME").val();
		str += "에서 ";
	}
	//T노드
	var T_NODE_NAME = $("#T_NODE_NAME").val();
	if(T_NODE_NAME != ""){
		str += T_NODE_NAME;
		str += "방향 ";
	}
	//세부사항(subType)
	var REPORT_TYPE2 = $("#REPORT_TYPE2 option:selected");
	if(REPORT_TYPE2.val() != ""){
		str += REPORT_TYPE2.text();
		str += " ";
	}
	//후미상황
	var REPORT_TYPE3 = $("#REPORT_TYPE3").val();
	if(REPORT_TYPE3 != ""){
		str += REPORT_TYPE3;
		str += " ";
	}
	return str;
}

function appendToReceiptContent(){
	console.log("appendToReceiptContent");

	var str = sentenceForReceiptContent();
	$("#CONTENT").val(str);
}


// 꺽쇠 추가 함수(재난,긴급 등)
function memoAddSentnece() {
	var changeStr = "";  // 기본 값 변수
	
	// 긴급이 체크 된 경우
	var FLAG_IMPORTANT = $("input:checkbox[id=FLAG_EMERGENCY]");
	if(FLAG_IMPORTANT.is(":checked")){
		changeStr += "<긴급>";
	}
	var FLAG_DISASTOR = $("input:checkbox[id=FLAG_DISASTOR]");
	if(FLAG_DISASTOR.is(":checked")){
		changeStr += "<재난>";
	}
	var FLAG_DMB = $("input:checkbox[id=FLAG_DMB_SEND]");
	if(FLAG_DMB.is(":checked")){
		changeStr += "<사진/영상>";
	}
	
	
	return changeStr;
	
	/*var REPORT_TYPE3 = $("#REPORT_TYPE3").val();
	var REPORT_TYPE3_STR = "";
	
	if(REPORT_TYPE3 != ""){
		REPORT_TYPE3_STR += " ";
		REPORT_TYPE3_STR += REPORT_TYPE3;
		
		return changeStr + str + REPORT_TYPE3_STR; // 최종 값 반환
	} else {
		return changeStr + str;
	}*/
}


function saveReceipt(){
	if(authCode != '2'){
		alert("권한이 없습니다.");
		return;
	}
	console.log("saveReceipt");

	var form = document.getElementById("receiptFrm");
	if(!isProperInput(form)){
		return;
	}
	
	
	var disastor_flag = $("input:checkbox[id=FLAG_DISASTOR]");
	var dmb_flag = $("input:checkbox[id=FLAG_DMB_SEND]");
	
	if(disastor_flag.is(":checked") && dmb_flag.is(":checked")) {
		alert("재난 제보와 사진/영상은 중복 체크할 수 업습니다. (택1)");
		return false;
	}
	
	
	//제보내용 객체 생성
	var str="";
	//긴급,중요 꺽쇠표시
	str = memoAddSentnece();
	
	var MEMO = $("#CONTENT");
	//메모가 없을 경우 
	//하위 유형,도로 등을 조합하여 내용 생성
	if((MEMO.val() == "")){ 
		str += sentenceForReceiptContent();
	}else{
		str +=$("#CONTENT").val();
	}
	MEMO.val(str);
	
	/*else { //2024-09-10 : 메모를 작성하고 긴급 제보가 체크 된 경우
		console.log("<긴급> 메모 추가 함수 실행");
		
		var changeMemo = memoAddSentnece(MEMO.val()); // 메모 빈값이 아닌 경우 꺽쇠 추가 ( 함수 미완료 )
		
		console.log("바뀌고 난 후 메모 값 : " + changeMemo);
		MEMO.val(changeMemo); // 함수 실행 후 꺽쇠 추가 된 값 저장
	}*/
	
	var INFORMER_NONE = $("input:checkbox[id=INFORMER_NONE]");
	var INFORMER_NAME = $("input:checkbox[id=INFORMER_NAME]").val();
	
	//등록되지 않은 시민을 제보할 경우 (22.03 충북요청사항)
	if(INFORMER_NONE.is(":checked")){
		console.log("등록되지 않은 시민제보");
		//시민에 이름을 등록하고 싶은경우 
		if( !($("#INFORMER_NAME").val()!='' && $("#INFORMER_NAME").val()!='시민') ){
			$("#INFORMER_NAME").val("시민");
		}
		$("#INFORMER_TYPE").val("2");
		
		$("#STAT1").val("0");
		$("#STAT2").val("0");
		$("#STAT3").val("0");
		
	}
	
	var data = $("#receiptFrm").serialize();
	
	//체크박스에 대한 예외처리
	var FLAG_IMPORTANT = $("input:checkbox[id=FLAG_EMERGENCY]");
	(FLAG_IMPORTANT.is(":checked")) ? console.log("Y") : data = data + "&FLAG_IMPORTANT=N";
	
	var FLAG_DISASTOR = $("input:checkbox[id=FLAG_DISASTOR]");
	(FLAG_DISASTOR.is(":checked")) ? console.log("Y") : data = data + "&FLAG_DISASTOR=N";
	
	var FLAG_SEND = $("input:checkbox[id=FLAG_REQUEST]");
	(FLAG_SEND.is(":checked")) ? console.log("Y") : data = data + "&FLAG_SEND=N";
	
	var FLAG_BLACKLIST = $("input:checkbox[id=FLAG_BLACKLIST]");
	(FLAG_BLACKLIST.is(":checked")) ? console.log("Y") : data = data + "&FLAG_BLACKLIST=N";
	
	console.log("data: " + data);
	
	var sttUpdateResult;
	var receiptSaveesult;
	var statUpdateResult;
	if($("input:checkbox[id=FLAG_STT]").is(":checked")){ //만약 STT내용을 등록한다면 : STT UPDATE -> RECEIPT SAVE
		sttUpdateResult = updateFlagSTT();
		if(sttUpdateResult == 1){
			receiptSaveesult = ajaxMethod("/receipt/insertReceipt.do", data).result;
			//등록되지 않은 시민 제외하고 월별 제보건수 업데이트
			//if(receiptSaveesult == 1 && !($("#INFORMER_NONE").is(":checked"))){
				statUpdateResult = ajaxMethod("/receipt/updateMonthlyStat.ajax", data).result;
			//}
		} else{
			alert("Error: STT저장실패");
		}
	} else{
		receiptSaveesult = ajaxMethod("/receipt/insertReceipt.do", data).result; //STT가 아닐 시 바로 RECEIPT SAVE
		//if(receiptSaveesult == 1 && !($("#INFORMER_NONE").is(":checked"))){
			statUpdateResult = ajaxMethod("/receipt/updateMonthlyStat.ajax", data).result;
		//}
	}
	
	//RECEIPT SAVE에 대한 결과값
	//시민제보에 따라 분기
	if($("#INFORMER_NONE").is(":checked")){
		if(receiptSaveesult >= 1){
			//alert("등록 되었습니다.");
			$("#receiptFrm").load("/receipt/receiptForm.do");
		} else{
			alert("저장에 실패하였습니다.");
		}
	}else{
		if(statUpdateResult >= 1){
			//alert("등록 되었습니다.");
			$("#receiptFrm").load("/receipt/receiptForm.do");
		} else{
			alert("저장에 실패하였습니다.");
		}
	}
}

function updateFlagSTT(){
	console.log("updateFlagSTT");

	var missedCallId = $("#MISSED_CALL_ID").val();
	console.log("MISSED_CALL_ID: " + missedCallId);
	var result = ajaxMethod("/receipt/updateFlagSTT.ajax", {"MISSED_CALL_ID": missedCallId}).result;
	return result;
}

function clearReceiptForm(){
	console.log("clearReceiptForm");

	var doubleCheck = confirm("초기화 하시겠습니까?");
	if(doubleCheck){
		$("#receiptFrm").load("/receipt/receiptForm.do");
	}
}
	
function clearInformerView(){
	console.log("clearInformerView");

	document.getElementById('ACT_ID').value = "";
	document.getElementById('INFORMER_NAME').value = "";
	document.getElementById('PHONE_CELL').value = "";
	document.getElementById('PHONE_HOME').value = "";
	document.getElementById('ADDRESS_HOME').value = "";
	document.getElementById('INFORMER_TYPE_NM').value = "";
	document.getElementById('ORG_ID').value = "";
	document.getElementById('ORG_NAME').value = "";
	document.getElementById('ORG_SUB_ID').value = "";
	document.getElementById('ORG_SUB_NAME').value = "";
	document.getElementById('STAT1').value = "";
	document.getElementById('STAT2').value = "";
	document.getElementById('STAT3').value = "";
	document.getElementById('MEMO').value = "";
	
	document.getElementById('FLAG_BLACKLIST').checked = false;
	document.getElementById('INFORMER_NONE').checked = false;
}

function openPersonalMemo(){
	console.log("openPersonalMemo");

	var url = "/receipt/personalMemo.do";
	var windowName = "개인메모창";
	var popupW = 700; // 팝업 넓이
	var popupH = 830; // 팝업 높이
	var left = (document.body.offsetWidth/2) - (popupW/2);
		left += window.screenLeft;
	var top = (document.body.offsetHeight/2) - (popupH/2);

	popObj = window
			.open(
				url,
				windowName,
					'width='+ popupW
				+ ', height='+ popupH
				+ ', left=' + left
				+ ', top=' + top
				+ ', toolbar=no, location=no, status=no, menubar=no, scrollbars=no, resizable=no');
}

///////////////////////////////////////TEST용 코드////////////////////////////////////////////
function telTest(){
	selectInformerByPhone("noInfoFound", "01087269203");
}
//통신원 - 검색:연락처(IP-PBX용)
function selectInformerByPhone(informerVO, phone_cell){
	console.log("selectInformerByPhone-----------??");

	clearInformerView();
	if(informerVO === "noInfoFound") {
		//시민제보자일 경우 수정권한
		document.getElementById('INFORMER_NAME').readOnly = false;
		$("#INFORMER_NAME").focus();
		document.getElementById('MEMO').readOnly = true;
		document.getElementById('btn_informerSend').disabled = true;
		//view생성
		document.getElementById('INFORMER_NONE').checked = true;
		document.getElementById('PHONE_CELL').value = phone_cell;
		document.getElementById('INFORMER_TYPE_NM').value = "시민";
		document.getElementById('INFORMER_TYPE').value = "2";
		//제보접수부분의 연락처 값 생성
		document.getElementById('LOSS_TEL').value = phone_cell;
	} else {
		//등록된 통신원일 경우 수정권한
		document.getElementById('INFORMER_NAME').readOnly = true;
		document.getElementById('MEMO').readOnly = false;
		document.getElementById('btn_informerSend').disabled = false;
		//view생성
		document.getElementById('INFORMER_ID').value = informerVO.informer_ID;
		document.getElementById('ACT_ID').value = informerVO.act_ID;
		document.getElementById('INFORMER_NAME').value = informerVO.informer_NAME;
		document.getElementById('PHONE_CELL').value = informerVO.phone_CELL;
		document.getElementById('PHONE_HOME').value = informerVO.phone_HOME;
		document.getElementById('INFORMER_TYPE').value = informerVO.informer_TYPE;
		
		document.getElementById('INFORMER_TYPE_NM').value = informerVO.type_NAME;
		document.getElementById('ORG_ID').value = informerVO.org_ID;
		document.getElementById('ORG_NAME').value = informerVO.org_NAME;
		document.getElementById('ORG_SUB_ID').value = informerVO.org_ID;
		document.getElementById('ORG_SUB_NAME').value = informerVO.org_SNAME;
		
		if(!informerVO.zipcode){
			document.getElementById('ADDRESS_HOME').value = informerVO.address_HOME;
			if(!informerVO.address_HOME){
				document.getElementById('ADDRESS_HOME').value = "-";
			}
		} else{
			document.getElementById('ADDRESS_HOME').value = "("+informerVO.zipcode+") "+informerVO.address_HOME;
		}
		
		document.getElementById('STAT1').value = informerVO.stat1;
		document.getElementById('STAT2').value = informerVO.stat2;
		document.getElementById('STAT3').value = informerVO.stat3;
		
		if(informerVO.memo != undefined && informerVO.memo != ''){
			document.getElementById('MEMO').value = informerVO.memo;
		}
		if(informerVO.flag_BLACKLIST == 'Y'){
			document.getElementById('FLAG_BLACKLIST').checked = true;
		}
		//제보접수부분의 연락처 값 생성
		document.getElementById('LOSS_TEL').value = informerVO.phone_CELL;
		
	}
}

//임시저장 
function tempsave(){
	console.log("임시저장");
	var form = document.getElementById("receiptFrm");
	//통신원 정보가 기입됬을 경우에만 임시저장 가능하도록
	var lsTel = $("#LOSS_TEL").val();
	if(lsTel==''||typeof lsTel ==="undefined"){
		alert("통신원 정보를 입력하셔야 임시저장이 가능합니다.");
	}else{
		//통신원이름이 없을때
		if($("#INFORMER_NAME").val()==''){
			//시민제보자 체크했을경우에는 통신원이름 시민으로
			if($("#INFORMER_NONE").is(":checked")){
				$("#INFORMER_NAME").val('시민');
			}
		}
		var data = $("#receiptFrm").serialize();
		ajaxMethod("/receipt/tempSave.do", data).result;
		tempsaveList();
		$("#receiptFrm").load("/receipt/receiptForm.do");
	}

}



/*25-12-01 : 모바일 앱 제보접수 팝업창 띄우는 함수*/
function openAppreceipt() {
	console.log("팝업창 버튼 클릭 성공");
	
	if(authCode == 999 || authCode == 1 || authCode == 2) {
		window.open(
		        "/receipt/appStatus.do",  
		        "popupWindow",      
		        "width=1500,height=845,toolbar=no,menubar=no,scrollbars=yes,resizable=no"
		    );
	} else {
		alert("현재는 관리자 및 접수자만 열람할 수 있습니다.");
	}
}







