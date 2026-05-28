<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- <link rel="stylesheet" href="request.getContextPath()%>/calender/jquery-ui.css"/> -->
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>
<script>
	$(function(){

		console.log("제보접수 화면");
		clickMenu('tabs');
		firstInformerVO = null;
		secondInformerVO = null;
		thirdInformerVO = null;
		isMiss=true;
		var callMenu="pickMenu";
		
		$("#pickupCallList").load("/receipt/pickupCallList.do?AREA_CODE="+lgnArea+"&INTEL_NUM="+inTelNum);
		$("#missedCallList").hide();
		$("#pickMenu").css("color","blue");
		
		$("#receiptFrm").load("/receipt/receiptForm.do");
		
		//기존 부재중목록만 있을때 -> scrollHeight는 높이를 가져와서 아래로 보내버림...
		//따라서 0으로 맞추면 맨 위로 올라감
		
		//$('.group_tel').scrollTop($('.group_tel')[0].scrollHeight);
		$('.group_tel').scrollTop(0);
		
		//대메뉴 관련
		$(".menu li").hover(function() {
			$('ul:first', this).show();
		}, function() {
			$('ul:first', this).hide();
		});
		$(".menu>li:has(ul)>a").each(function() {
			$(this).html($(this).html() + ' &or;');
		});
		$(".menu ul li:has(ul)").find("a:first").append(
					"<p style='float:right;margin:-3px'>&#9656;</p>");
		
		document.onkeydown=function(e){
			if(e.which == 17) isCtrl = true;
			if(isCtrl) functionKeyEvent(e.which);
			if(e.which == 27) clearReceiptForm(); //ESC
		}
		document.onkeyup=function(e){
			if(e.which == 17) isCtrl = false;
		}
		//폴링 부분
		//제보접수-통신원정보
		pollingForCall = setInterval(() => checkIfPickUpInfoExists(), 2000);
		//수신전화 목록
		pollingForPickup = setInterval(() => startPickupCallPolling(), 5000);
		//pollingForMissedCall = setInterval(() => startMissedCallPolling(), 1000);
		
		$(".callmenu").on("click",function(){
			callMenu=$(this).attr("id");
			//console.log(callMenu);
			if(callMenu=='missMenu'){
				//console.log("부재중0");
				$("#pickupCallList").hide();
				$("#missedCallList").show();
				$("#pickMenu").css("color","black");
				$("#missMenu").css("color","blue");
				if(isMiss){
					clearInterval(pollingForPickup);
					isMiss=false;
					isPick=true;
					pollingForMissedCall = setInterval(() => startMissedCallPolling(), 5000);
					$("#missedCallList").load("/receipt/missedCallList.do?AREA_CODE="+lgnArea);
				}
			}else{
				//console.log("수신0");
				$("#missedCallList").hide();
				$("#pickupCallList").show();
				$("#missMenu").css("color","black");
				$("#pickMenu").css("color","blue");
				if(isPick){
					isMiss=true;
					isPick=false;
					clearInterval(pollingForMissedCall);
					pollingForPickup = setInterval(() => startPickupCallPolling(), 5000);
					$("#pickupCallList").load("/receipt/pickupCallList.do?AREA_CODE="+lgnArea+"&INTEL_NUM="+inTelNum);
				}
				
			}
		});
		
		//임시저장 체크박스와 혼용안되게 별도의 로직 구현
		/* $(" li span").on("click",function(){
			
		}); */

		if(authCode == 999 || authCode == 1 || authCode == 2  ) {
			openPopup(); // 팝업창 띄우는 함수 호출
		}
	
		// 모바일 제보접수 팝업창 열기
		function openPopup() {
		    window.open(
		        "/receipt/appStatus.do",  
		        "popupWindow",      
		        "width=1500,height=845,toolbar=no,menubar=no,scrollbars=yes,resizable=no"
		    );
		}
	});
	
</script>

<form name="receiptFrm" id="receiptFrm" action="../receipt/receiptSave.do" onsubmit="return false;">
	
</form>

<!-- 24-11-12 : 공지사항 모달창 -->
<div id="input_notice">

</div>

<!-- rightWrap -->
<div id="rightWrap">
	<!-- tab menu -->
	<div id="tabs">
		<ul>
	    	<!-- 예약접수 -->
			<li class="outer one">
				<!-- <div> -->
				<div>
					<ul class="on_bbs" >
					
				        <li class="txt_leftb padTop20">
							<select id="SEARCHTYPE0" name="SEARCHTYPE0" class="r_table_sel" style="width:70px;">
								<option value="1">이름</option>
								<option value="2">ID</option>
								<option value="3">전화번호</option>
							</select> 
							<input type="text" class="input_sel" onkeyup="if(event.keyCode == 13)searchInformer(0)" name="SEARCH_TEXT0" id="SEARCH_TEXT0" 
									style="width:100px; height:22px;" placeholder="통신원검색" />
							<img id="rSearch_btn" src="../images/map/btn_sch.png" onclick="searchInformer(0)" />
							<!-- <input type="button" src="../images/map/btn_sch.png" class="rSearch_btn" value="검색" onclick="searchInformer(0)" /> -->
				&nbsp;&nbsp;<label id="labelUserId0"></label>&nbsp;&nbsp;
							<label id="labelUserName0"></label>
							<input type="hidden" class="input_sel" name="reservationUserName0" id="reservationUserName0" style="width:50px;" />
							<input type="hidden" class="input_sel" name="reservationUserId0" id="reservationUserId0" style="width:50px;" />
						</li>
						
						<li class="pabt15">
							<img src="../images/btn_move.gif" alt="제보등록폼으로 이동" class="imgt" 
									style="cursor:pointer; padding:2px 0 0 2px; height:78px; width:24px;" onclick="sendToReceiptForm(0)"/>
				  			<textarea class="r_table_sel" name="reservationContents0" id="reservationContents0" 
				  					cols="50" rows="8" style="width:286px; height:70px; resize: none;"></textarea>
						</li>
						
						<li class="txt_leftb pat3">
							<select id="SEARCHTYPE1" name="SEARCHTYPE1" class="r_table_sel" style="width:70px ">
								<option value="1">이름</option>
								<option value="2">ID</option>
								<option value="3">전화번호</option>
							</select> 
							<input type="text" class="input_sel" onkeyup="if(event.keyCode == 13)searchInformer(1)" name="SEARCH_TEXT1" id="SEARCH_TEXT1" 
									style="width:100px; height:22px;" placeholder="통신원검색" />
							<img id="rSearch_btn" src="../images/map/btn_sch.png" onclick="searchInformer(1)" />
							<!-- <input type="button" class="rSearch_btn" value="검색" onclick="searchInformer(1)" /> -->
				&nbsp;&nbsp;<label id="labelUserId1"></label>&nbsp;&nbsp;
							<label id="labelUserName1"></label>
							<input type="hidden" class="input_sel" name="reservationUserName1" id="reservationUserName1" style="width:50px;" />
							<input type="hidden" class="input_sel" name="reservationUserId1" id="reservationUserId1" style="width:50px;" />
						</li>
						<li class="pabt15">
							<img src="../images/btn_move.gif" alt="제보등록폼으로 이동" class="imgt" 
									style="cursor:pointer; padding:2px 0 0 2px; height:78px; width:24px;" onclick="sendToReceiptForm(1)"/>
							<textarea class="r_table_sel" name="reservationContents1" id="reservationContents1" 
									cols="50" rows="8" style="width:286px; height:70px; resize: none;"></textarea>
						</li>
						
						<li class="txt_leftb pat3">
							<select id="SEARCHTYPE2" name="SEARCHTYPE2" class="r_table_sel" style="width:70px ">
								<option value="1">이름</option>
								<option value="2">ID</option>
								<option value="3">전화번호</option>
							</select> 
				   			<input type="text" class="input_sel" onkeyup="if(event.keyCode == 13)searchInformer(2)" name="SEARCH_TEXT2" id="SEARCH_TEXT2" 
				   					style="width:100px; height:22px;" placeholder="통신원검색" />
				   			<img id="rSearch_btn" src="../images/map/btn_sch.png" onclick="searchInformer(2)" />
							<!-- <input type="button" class="rSearch_btn" value="검색" onclick="searchInformer(2)" /> -->
				&nbsp;&nbsp;<label id="labelUserId2"></label>&nbsp;&nbsp;
							<label id="labelUserName2"></label>
							<input type="hidden" class="input_sel" name="reservationUserName2" id="reservationUserName2" style="width:50px;" />
							<input type="hidden" class="input_sel" name="reservationUserId2" id="reservationUserId2" style="width:50px;" />
						</li>
						<li class="pabt15">
							<img src="../images/btn_move.gif" alt="제보등록폼으로 이동" class="imgt" 
									style="cursor:pointer; padding:2px 0 0 2px; height:78px; width:24px;" onclick="sendToReceiptForm(2)"/>
						  	<textarea class="r_table_sel" name="reservationContents2" id="reservationContents2"
						  			cols="50" rows="8" style="width:286px; height:70px; resize: none;"></textarea>
						</li>
					</ul>
				</div>
			</li>
	
			<!-- utic (도로명) -->
			<li class="outer two click">
	    		<div id="arteryNodeLink" class="tab_center">
	    			<div id="atry_area" class="atry_area" style="margin-top: -50px;">
	    				<span>방송국 : </span> 
						<select id="arterySelect" onChange="listUpArtery()">
							<c:forEach var="areaCodeVO" items="${areaCodeList}">
								<option value=${areaCodeVO.AREA_CODE }>${areaCodeVO.AREA_NAME }</option>
							</c:forEach>
						</select>
						<span>정렬순 : </span>
						<select id="orderBy" class="orderBy" onChange="listUpArtery()">
							<option value="1">가나다순</option>
							<option value="2">등록순</option>
						</select>
	    			</div>
					
					<div id="slt_atry_node" class="slt_atry_node" style="top: 4px;">
						<ul id="arteryTb"></ul>
						<ul id="nodeLinkTb"></ul>
					</div>
				</div>
			</li>
	
			<!-- 임시저장 -->
			<li class="outer three" onclick="tempsaveList()">
	    		<div id="tempSave" class="tab_center">
		    		<ul id="tempSaveTb"></ul>
					<button type="button" class="tmps_btn" style="width:100px;height:30px;POSITION: absolute;top: 335px;left: 219px;" onclick="deleteTemp();">
						임시저장 삭제
					</button>
				</div>
			</li>
		</ul>
	</div>	

	<!-- 부재중 전화 수신목록 -->
	<div class="callSecDiv">
		<h2>
			<img src="../images/ico_missed_call2.png" id="I3" alt="부재중 전화 수신목록" style="width:65px;">
		</h2>
		<div>
            <label id="pickMenu" class="callmenu">수신전화 목록</label>    
        </div>
		<div>
		    <label id="missMenu" class="callmenu">부재중 전화 목록</label>    
		</div>
	</div>
	<!-- <button type="button" class="scrollDown" onclick="$('.group_tel').scrollTop($('.group_tel')[0].scrollHeight)">▼맨아래로</button> -->
	<!-- <img src="../images/scrollDown.png" class="scrollDown" onclick="$('.group_tel').scrollTop($('.group_tel')[0].scrollHeight)" /> -->
	<div class="group_tel" style="margin-top:0px;" id="setConnectedDiv" >
		<ul id="pickupCallList" class='rcvCallList'><!-- pickupCallList.jsp --></ul>
		<ul id="missedCallList" class='rcvCallList'><!-- missedCallList.jsp --></ul>
	</div>
	
</div>
<!-- //rightWrap -->

<div id="mapDiv"  class="mapDiv"></div>

<!--  -->
<script>
	jQuery(window).load(function() {
		console.log("스크롤탑");
		$('.group_tel').scrollTop(0);
	});
</script>