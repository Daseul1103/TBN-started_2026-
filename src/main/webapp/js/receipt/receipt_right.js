//******************************************예약접수******************************************//
function sendToReceiptForm(idx){
	console.log("sendToReceiptForm");

	if(idx == 0){
		//appendInformerToView(firstInformerVO);
		selectInformerByPhone(firstInformerVO);
		var text = document.getElementById("reservationContents"+idx).value;
		var textArea = document.getElementById("CONTENT");
		textArea.value = text;

		// 25-01-16 : 예약 접수란에서 제보접수로 버튼 클릭 시 입력 값 초기화 작업
		$('#SEARCH_TEXT0').val(''); // 통신원 검색란 초기화
		$('#labelUserId0').text(''); // 통신원 ID 라벨 초기화
		$('#labelUserName0').text(''); // 통신원 이름 라벨 초기화
		$('#reservationContents0').val(''); // textArea 초기화
		
	}else if(idx == 1){
		selectInformerByPhone(secondInformerVO);
		var text = document.getElementById("reservationContents"+idx).value;
		var textArea = document.getElementById("CONTENT");
		textArea.value = text;
		
		// 25-01-16 : 예약 접수란에서 제보접수로 버튼 클릭 시 입력 값 초기화 작업
		$('#SEARCH_TEXT1').val(''); // 통신원 검색란 초기화
		$('#labelUserId1').text(''); // 통신원 ID 라벨 초기화
		$('#labelUserName1').text(''); // 통신원 이름 라벨 초기화
		$('#reservationContents1').val(''); // textArea 초기화
		
	}else if(idx ==2){
		selectInformerByPhone(thirdInformerVO);
		var text = document.getElementById("reservationContents"+idx).value;
		var textArea = document.getElementById("CONTENT");
		textArea.value = text;
		
		// 25-01-16 : 예약 접수란에서 제보접수로 버튼 클릭 시 입력 값 초기화 작업
		$('#SEARCH_TEXT2').val(''); // 통신원 검색란 초기화
		$('#labelUserId2').text(''); // 통신원 ID 라벨 초기화
		$('#labelUserName2').text(''); // 통신원 이름 라벨 초기화
		$('#reservationContents2').val(''); // textArea 초기화
	}else{
		console.log("해당 informerVO 없음");
	}
	
}



////****************************************도로명 + 노드****************************************//
function readSpotCode(){
	//console.log("readSpotCode");

	var SPOTCODE = document.getElementById("SPOTCODE");
	var value = SPOTCODE.value;
	console.log("Value: " + value);
}

function listUpArtery(){
	//console.log("도로명 불러오기");
	$("#arteryTb").empty();
	$("#nodeLinkTb").empty();
	
	var areaCode = $("#arterySelect option:selected").val();
	var ordered = $("#orderBy option:selected").val();
	
	//도로명 불러오기시 정렬순 관련 추가
	var arteryList = ajaxMethod("/receipt/listUpArtery.ajax", {AREA_CODE : areaCode, ordered:ordered}).arteryList;
		
	var arteryTb = $("#arteryTb");
	for(var i=0; i<arteryList.length; i++){
		arteryTb.append('<li onClick="listUpNodeLink(\'' 
							+ arteryList[i].artery_ID + '\', \'' + arteryList[i].area_CODE + '\', \'' + arteryList[i].artery_NAME + '\')">' 
							+ '<span>'+ arteryList[i].num + '</span>'
							+ '<span>'+ arteryList[i].artery_NAME + '</span></li>');
	}
}

function listUpNodeLink(arteryId, areaCode, arteryName){
	
	// 26-08-19 : 소방청 신규 계정 및 권한 생성에 따른 분기 처리
	if(authCode != '5'){
		//appendClassOn(this);
		//clickMenu("artery");
		$("#F_NODE_NAME").val("");
		$("#T_NODE_NAME").val("");
		$("#ARTERY_NAME").val(arteryName);
		$("#ARTERY_ID").val(arteryId);
		$("#nodeLinkTb").empty();
		var ordered = $("#orderBy option:selected").val();
		var nodeLinkList = ajaxMethod("/receipt/listUpNodeLink.ajax", {ARTERY_ID : arteryId, AREA_CODE : areaCode, ordered:ordered}).nodeLinkList;
			
		var nodeLinkTb = $("#nodeLinkTb");
		for(var i=0; i<nodeLinkList.length; i++){
			nodeLinkTb.append('<li onClick="getNodeValue(\'' + nodeLinkList[i].l_NODE_NAME +'\', \'' + nodeLinkList[i].nodelink_ID + '\')">' 
					+ '<span>'+ nodeLinkList[i].num + '</span>'
					+ '<span>' + nodeLinkList[i].l_NODE_NAME + '</span></li>');
		}
		//22.06.09 충북요청사항 : 도로 클릭시 맵이동
		//console.log("도로선택시 맵이동 : "+arteryName);
		searchPlaces(arteryName);
	} else {
		alert("해당 계정은 사용할 수 없는 기능 입니다.");
	}
	
}

function getNodeValue(nodeName, nodeId){
	var f_node_name = document.getElementById("F_NODE_NAME");
	var t_node_name = document.getElementById("T_NODE_NAME");
	var f_link_id = document.getElementById("F_LINK_ID");
	var t_link_id = document.getElementById("T_LINK_ID");
	
//	if(f_node_name.select()){
//		f_node_name.value = nodeName;
//	}
//	if(t_node_name.select()){
//		t_node_name.value = nodeName;
//	}
	
	if(f_node_name.value != ""){
		t_node_name.value = nodeName;
		t_link_id.value = nodeId;
	} else {
		f_node_name.value = nodeName;
		f_link_id.value = nodeId;
	}
	
//	
//	if(t_node_name == document.activeElement){
//		alert(nodeName);
//	}
}

//function appendClassOn(elementId){
//	console.log("elemntId: " + elementId);
//	//document.getElementById(elementId).className += " on";
//	$(elementId).addClass("on");
//	//console.log();
//}



//******************************************부재중목록*****************************************//
function insertMissedCall(){
	console.log("insertMissedCall함수 실행");
	var missedCallId = randomString();
	var missedCallTime = currentDate("-") + " " + currentTimeBig();
	var missedCallTel = document.getElementById("PHONE_CELL").value;
	var informerName = document.getElementById("INFORMER_NAME").value;
	var missedCallObj = {MISSED_CALL_ID: missedCallId
					, MISSED_CALL_TIME: missedCallTime
					, MISSED_CALL_TEL: missedCallTel
					, INFORMER_NAME: informerName
					, STT_CONTENT: missedCallId};
	console.log("missedCallVO : " + missedCallObj);
	var result = ajaxMethod("/receipt/insertMissedCall.ajax", missedCallObj).result;
	console.log("result: "+result);
	if(result > 0){
		var	html = '<li onclick="getSttContent(\''+missedCallId+'\')" style="cursor: pointer;">';
			html += '<span class="stt" style="width:140px;">'+missedCallTime+'</span>';
			html += '<span class="stt" style="width:100px;">'+missedCallTel+'</span>';
			html += '<span class="stt" style="width:60px;">'+informerName+'</span>';
			html += '</li>';
		$("#missedCallList").append(html);
	}
	$('.group_tel').scrollTop($('.group_tel')[0].scrollHeight);
}

//function getSttContent(sttContent, informerId, telNum, missedCallId){
//	//var sttContent = ajaxMethod("/receipt/getSttContent.ajax", {MISSED_CALL_ID: missed_call_id}).data;
//	var informerVO = ajaxMethod("/receipt/selectInformerByID.ajax", {"INFORMER_ID" : informerId
//																		, "month1":currentMonth("")
//																		, "month2":lastMonth("")
//																		, "month3":twoMonthAgo("")}).data;
//	if(!informerVO){
//		informerVO = "noInfoFound";
//	}
//	selectInformerByPhone(informerVO, telNum);
//	var contentTextArea = document.getElementById("CONTENT");
//	contentTextArea.value = sttContent;
//	
//	document.getElementById("FLAG_STT").checked = true;
//	document.getElementById("MISSED_CALL_ID").value = missedCallId;
//}


//부재중목록 + 수신전화 리스트 클릭시 좌측 통신원 정보에 값 기입됨
function getSttContent(informerId, telNum, missedCallId){
  //var sttContent = ajaxMethod("/receipt/getSttContent.ajax", {MISSED_CALL_ID: missed_call_id}).data;
	var informerVO = ajaxMethod("/receipt/selectInformerByID.ajax", {"INFORMER_ID" : informerId
																		, "month1":currentMonth("")
																		, "month2":lastMonth("")
																		, "month3":twoMonthAgo("")}).data;
	var missedCallVO = ajaxMethod("/recipet/getMissedCallInfoById.ajax", {"MISSED_CALL_ID":missedCallId}).data;
	
	if(!informerVO){
		informerVO = "noInfoFound";
	}
	selectInformerByPhone(informerVO, telNum);
	var contentTextArea = document.getElementById("CONTENT");
	contentTextArea.value = missedCallVO.stt_CONTENT;
	
	document.getElementById("FLAG_STT").checked = true;
	document.getElementById("MISSED_CALL_ID").value = missedCallId;
}

//******************************************통신원검색*****************************************//
function searchInformer(idx){
	console.log("searchInformer");

	var searchText = $("#SEARCH_TEXT" + idx).val();
	var nowUserArea = $('#nowUserArea').val(); 
	if (searchText == ''){
		alert("검색어를 입력해주세요.");
		return;
	}
	
	var url = '/receipt/popUpInformerList.do?SEARCH_TEXT=' 
				+ escape(encodeURIComponent(searchText))
				+ "&searchType=" + $("#SEARCHTYPE"+ idx +" option:selected").val()
				+ "&idx=" + idx
				+ "&nowUserArea=" + nowUserArea;
	var windowName = "통신원정보";
	var popupW = 800; // 팝업 넓이
	var popupH = 730; // 팝업 높이
	var left = Math.ceil((window.screen.width - popupW) / 2);
	var top = Math.ceil((window.screen.height - popupH) / 2);
	
	// 26-08-19 : 소방청 신규 계정 및 권한 생성에 따른 분기 처리
	if(authCode != '5') {
		var popObj = window
		.open(
				url,
				windowName,
				'width='
						+ popupW
						+ ', height='
						+ popupH
						+ ', left='
						+ left
						+ ', top='
						+ top
						+ ', toolbar=no, location=no, status=no, menubar=no, scrollbars=no, resizable=no');
	} else {
		alert("해당 계정은 권한이 없습니다.");
	}
	
}

function appendInformerResultList(){
	console.log("appendInformerResultList");

	var tbdoy = $("#informerList");
	
	var informerList = ajaxMethod("/receipt/popUpInformerList.do");
	
	for(var i=0; i<reportTypeList.length; i++){
		tbdoy.append("<option value='" + reportTypeList[i].bas_SCOD + "'>" //하위셀렉트박스 객체에 옵션값을 집어넣음
							+ reportTypeList[i].bas_NAME + "</option>");
	}
}

//임시저장된 목록 불러오기
function tempsaveList(){
	//console.log("임시저장 목록");
	$("#tempSaveTb").empty();
	var tsList=null;
	var receiptId = $("#RECEPTION_ID").val();
	if(receiptId=='' || typeof receiptId ==="undefined"){
		alert("세션이 만료되었습니다. 새로고침(F5)을 눌러 재 로그인 해주세요");
		$("#changeBody").load("/common/login.do");
	}else{
		tsList = ajaxMethod("/receipt/tempsaveList.ajax", {RECEPTION_ID : receiptId}).tsList;
	}

	var tempSaveTb = $("#tempSaveTb");
	for(var i=0; i<tsList.length; i++){
		tempSaveTb.append('<li onClick="tempSavePush(\''+ tsList[i].TS_TIME + '\', \'' + tsList[i].RECEPTION_ID +'\')">' 
							+ '<span><input type="checkbox" id="'+tsList[i].TS_TIME+'" maxlength="7" name="temp_chk" class="temp_check" value="'+tsList[i].TS_TIME+'"></span>'
							+ '<span>'+ tsList[i].TS_TIME + '</span>'
							+ '<span>'+ tsList[i].INDIVIDUAL_NAME + '</span>'
							+ '</li>'
							);
	}
	
}
//선택한 임시저장 리스트를 제보접수폼에 반영
function tempSavePush(time,rcptId){
	console.log("임시저장 푸시");
	var tsvo = ajaxMethod("/receipt/tempsavePush.ajax"
			, {"TS_TIME" : time, "RECEPTION_ID":rcptId}).tsvo;
	
	//반 영 시 작
	document.getElementsByName("RECEPTION_ID")[0].value=tsvo.RECEPTION_ID;//접수자 id
	document.getElementsByName("RECEPTION_NAME")[0].value=tsvo.RECEPTION_NAME;//접수자이름
	if(tsvo.FLAG_IMPORTANT=='Y'){//긴급접수
		$("#FLAG_EMERGENCY").prop("checked",true);
	}else{
		$("#FLAG_EMERGENCY").prop("checked",false);
	}
	if(tsvo.FLAG_DISASTOR=='Y'){//재난제보
		$("#FLAG_DISASTOR").prop("checked",true);
	}else{
		$("#FLAG_DISASTOR").prop("checked",false);
	}
	if(tsvo.FLAG_SEND=='Y'){//방송요청
		$("#FLAG_REQUEST").prop("checked",true);
	}else{
		$("#FLAG_REQUEST").prop("checked",false);
	}
	if(tsvo.DLS_TEXT=='Y'){//문자제보
		$("#DLS_TEXT").prop("checked",true);
	}else{
		$("#DLS_TEXT").prop("checked",false);
	}
	if(tsvo.FLAG_DMB_SEND !=null && tsvo.FLAG_DMB_SEND.includes('Y')){//사진/영상
		$("#FLAG_DMB_SEND").prop("checked",true);
	}else{
		$("#FLAG_DMB_SEND").prop("checked",false);
	}
	if(tsvo.FLAG_WEB !=null && tsvo.FLAG_WEB.includes('Y')){//홈페이지
		$("#FLAG_WEB").prop("checked",true);
	}else{
		$("#FLAG_WEB").prop("checked",false);
	}
	
	document.getElementsByName("MEMO")[0].value=tsvo.MEMO;
	document.getElementsByName("ARTERY_NAME")[0].value=tsvo.ARTERY_NAME;
	document.getElementsByName("LANE")[0].value=tsvo.LANE;
	document.getElementsByName("F_NODE_NAME")[0].value=tsvo.F_NODE_NAME;
	document.getElementsByName("T_NODE_NAME")[0].value=tsvo.T_NODE_NAME;
	//제보유형 대
	if(tsvo.REPORT_TYPE!=null||typeof tsvo.REPORT_TYPE !=="undefined"){
		$("#REPORT_TYPE").val(tsvo.REPORT_TYPE).trigger('change');
	}
	//제보유형 중
	if(tsvo.REPORT_TYPE2!=null||typeof tsvo.REPORT_TYPE2 !=="undefined"){
		$("#REPORT_TYPE2").val(tsvo.REPORT_TYPE2).trigger('change');
	}
	//제보유형 소
	if(tsvo.REPORT_TYPE3!=null||typeof tsvo.REPORT_TYPE3 !=="undefined"){
		$("#REPORT_TYPE3").val(tsvo.REPORT_TYPE3).trigger('change');
	}
	document.getElementsByName("R_TEL")[0].value=tsvo.R_TEL;
	//지역
	if(tsvo.REGION_ID!=null||typeof tsvo.REGION_ID !=="undefined"){
		$("#AREA_CODE").val(tsvo.REGION_ID).trigger('change');
	}
	//세부지역
	if(tsvo.AREA_ID!=null||typeof tsvo.AREA_ID !=="undefined"){
		$("#AREA_CODE_SUB").val(tsvo.AREA_ID).trigger('change');
	}
	//제보수단
	if(tsvo.REPORTER_TYPE!=null||typeof tsvo.REPORTER_TYPE !=="undefined"){
		$("#REPORTMEAN_TYPE").val(tsvo.REPORTER_TYPE).trigger('change');
	}
	
	document.getElementsByName("X_COORDINATE")[0].value=tsvo.X_COORDINATE;
	document.getElementsByName("Y_COORDINATE")[0].value=tsvo.Y_COORDINATE;
	document.getElementsByName("START_DAY")[0].value=tsvo.START_DAY;
	document.getElementsByName("STARTTIMEHH")[0].value=tsvo.STARTTIMEHH;
	document.getElementsByName("STARTTIMEMI")[0].value=tsvo.STARTTIMEMI;
	document.getElementsByName("END_DAY")[0].value=tsvo.END_DAY;
	document.getElementsByName("ENDTIMEHH")[0].value=tsvo.ENDTIMEHH;
	document.getElementsByName("ENDTIMEMI")[0].value=tsvo.ENDTIMEMI;
	
	//통신원 검색방법
	if(tsvo.SEARCHTYPE3!=null||typeof tsvo.SEARCHTYPE3 !=="undefined"){
		$("#SEARCHTYPE3").val(tsvo.SEARCHTYPE3).trigger('change');
	}
	
	document.getElementsByName("SEARCH_TEXT3")[0].value=tsvo.SEARCH_TEXT3;
	document.getElementsByName("INDIVIDUAL_ID")[0].value=tsvo.INDIVIDUAL_ID;
	document.getElementsByName("ACT_ID")[0].value=tsvo.ACT_ID;
	
	//document.getElementsByName("INFORMER_NONE")[0].value=tsvo.INFORMER_NONE;
	if(tsvo.INFORMER_NONE=='Y'){//방송요청
		$("#INFORMER_NONE").prop("checked",true);
	}else{
		$("#INFORMER_NONE").prop("checked",false);
	}
	
	document.getElementsByName("INDIVIDUAL_NAME")[0].value=tsvo.INDIVIDUAL_NAME;
	document.getElementsByName("PHONE_CELL")[0].value=tsvo.PHONE_CELL;
	document.getElementsByName("PHONE_HOME")[0].value=tsvo.PHONE_HOME;
	document.getElementsByName("ADDRESS_HOME")[0].value=tsvo.ADDRESS_HOME;
	document.getElementsByName("INDIVIDUAL_TYPE")[0].value=tsvo.INDIVIDUAL_TYPE;
	document.getElementsByName("INFORMER_TYPE_NM")[0].value=tsvo.INFORMER_TYPE_NM;
	document.getElementsByName("ORG_ID")[0].value=tsvo.ORG_ID;
	document.getElementsByName("ORG_SUB_ID")[0].value=tsvo.ORG_SUB_ID;
	document.getElementsByName("ORG_NAME")[0].value=tsvo.ORG_NAME;
	document.getElementsByName("ORG_SUB_NAME")[0].value=tsvo.ORG_SUB_NAME;
	document.getElementsByName("STAT1")[0].value=tsvo.STAT1;
	document.getElementsByName("STAT2")[0].value=tsvo.STAT2;
	document.getElementsByName("STAT3")[0].value=tsvo.STAT3;
	document.getElementsByName("FLAG_BLACKLIST")[0].value=tsvo.FLAG_BLACKLIST;
	document.getElementsByName("MEMO_INFORMER")[0].value=tsvo.MEMO_INFORMER;
	document.getElementsByName("MISSED_CALL_ID")[0].value=tsvo.MISSED_CALL_ID;
	document.getElementsByName("ARTERY_ID")[0].value=tsvo.ARTERY_ID;
	document.getElementsByName("F_LINK_ID")[0].value=tsvo.F_LINK_ID;
	document.getElementsByName("T_LINK_ID")[0].value=tsvo.T_LINK_ID;
	
}

//임시저장 삭제
function tempSaveDelete(time,id){
	ajaxMethod("/receipt/tempSaveDelete.ajax", {TS_TIME:time,RECEPTION_ID : id});
}

//시상자 제외(수상자 선정 화면으로 통신원 보내기)
function deleteTemp(){
	var chklist=[];
	
	if($("input:checkbox[name=temp_chk]:checked").length!=0){
		if(!confirm("체크된 임시저장 데이터를 삭제 하시겠습니까?" )){
			return;
		}else{
			$('input:checkbox[name=temp_chk]').each(function (index) {
				if($(this).is(":checked")==true){
					chklist.push($(this).val());
			    }
			});
			$.ajax
			(
				{
					type : "post" ,
					url : "/receipt/tempSaveDelete.ajax" ,
					dataType : "json" ,
					data : {chkArr:chklist},
					cache : false ,
					success:function(data){
						//임시저장 목록 갱신
						tempsaveList();
					},
					error:function(data,error){
						alert("시스템에 문제가 생겼습니다.");
					}
				}
			);
			
		}
		
	}else{
		alert("삭제할 임시저장목록을 선택해주세요");
	}
	
}












