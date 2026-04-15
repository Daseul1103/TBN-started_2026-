<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="<%= request.getContextPath() %>/DataTables/jquery.dataTables.min.css"/>
<link rel="stylesheet" href="<%= request.getContextPath() %>/DataTables/scroller.dataTables.min.css"/>
<script src="<%= request.getContextPath() %>/DataTables/jquery-3.7.1.min.js"></script>
<script src="<%= request.getContextPath() %>/DataTables/jquery.dataTables.min.js"></script>
<script src="<%= request.getContextPath() %>/DataTables/dataTables.scroller.min.js"></script>
<script src="<%= request.getContextPath() %>/DataTables/commonDatatable.js"></script>

<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>

<!-- DateTimePicker -->
<script src="<%= request.getContextPath() %>/calender/moment.js"></script>
<script src="<%= request.getContextPath() %>/calender/mo_ko.js"></script>
<script src="<%= request.getContextPath() %>/calender/bootstrap-datetimepicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/calender/no-boot-calendar-custom.css" />
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/calender/datetimepickerstyle.css" />

<style>
	.output-select-area {
	    position: relative;
	    display: inline-block;
	}
	
	/* 체크박스 레이어 */
	.select-wrap {
	    position: absolute;
	
	    bottom: 0;     
	    left: 100%;      
	    margin-left: 6px; 
	
	    width: 260px;
	    background: #fff;
	    border: 1px solid #ccc;
	    box-shadow: 0 4px 10px rgba(0,0,0,0.15);
	    z-index: 1000;
	
	    display: none;
	}
	
	.section {
	    margin-bottom: 10px;
	}
	
	.section-title {
	    font-weight: bold;
	    margin-bottom: 5px;
	    padding-left: 3px;
	}
	
	.section-body {
	    border: 1px solid #ddd;
	    padding: 6px;
	    background: #fafafa;
	}
	
	.scroll {
	    max-height: 180px;   /* 스크롤 높이 */
	    overflow-y: auto;
	}
	
	.section-body label {
	    display: block;
	    margin-bottom: 4px;
	    cursor: pointer;
	}
	
	.section-body input[type="checkbox"] {
	    margin-right: 6px;
	}
	
	#downDataSelect.on ~ .select-wrap {
	    display: block;
	}
	
	.confirm-area {
	    text-align: right;
	    margin-top: 8px;
	}
	
	.confirm-area button {
	    padding: 4px 10px;
	    font-size: 12px;
	    cursor: pointer;
	}
	
	.section-body input[disabled] {
	    cursor: default;
	    opacity: 0.7;
	}
	
	#downDataSelect {
	    padding: 6px 12px;
	    font-size: 13px;
	    border: 1px solid #bbb;
	    background: #f5f5f5;
	    color: #333;
	    cursor: pointer;
	    border-radius: 3px;
	    transition: background 0.2s, border-color 0.2s;
	}
	
	/* hover */
	#downDataSelect:hover {
	    background: #e9e9e9;
	}
	
	/* 열려있을 때 (on) */
	#downDataSelect.on {
	    background: #e0e0e0;
	    border-color: #999;
	}
	
	/* 아이콘 느낌용 (선택) */
	#downDataSelect::after {
	    content: "▼";
	    font-size: 10px;
	    margin-left: 6px;
	}
	
	/* 열려있을 때 화살표 회전 */
	#downDataSelect.on::after {
	    content: "▲";
	}
</style>

<script>
$(function () {
  const $form = $('#searchFrm'); // ← 수정됨
  const nameMap = { 1:'INFORMER_ID', 2:'AREA_NAME', 3:'INFORMER_TYPE_NAME', 4:'ORG_NAME', 5:'INFORMER_NAME', 6:'PHONE_CELL', 7:'FLAG_ACT', 8:'REG_DATE' };

  // ★ 전역 노출: window.dt
window.dt = createDataTable('#informerTable', {
  order: [[8, 'desc']], // 화면 기본 정렬
  ajax: {
    url: '/infrm/datatable.do',
    type: 'POST',
    data: function (d) {
      // 정렬 파라미터 평면화(컨트롤러용)
      let sortName=null, sortDir=null;
      if (Array.isArray(d.order) && d.order.length) {
        const od = d.order[0], idx = od.column;
        sortDir  = od.dir;
        sortName = (d.columns && d.columns[idx] && d.columns[idx].name)
                    ? d.columns[idx].name
                    : nameMap[idx];
      }
	  // ★ Spring 바인딩 충돌 소스 제거
	  delete d.order;
	  delete d.columns;
	  delete d.search;

      const send = {
     	    // ★ 정렬 평면 파라미터 추가
     	    sortName: sortName,
     	    sortDir:  sortDir,

     	    // 검색 파라미터들
     	    areaCode:     $('#areaCodeSel').val(),
     	    informerType: $('#informerTypeSel').val(),
     	    orgId:        $('#orgIdSel').val(),
     	    flagAct:      $('#searchActYn').val(),
     	    searchType:   $('#searchType').val(),
     	    searchValue:  $('#searchValue').val()
     	    
      };
      if ($('#dchker').is(':checked')) {
        send.sDate = $('#sDate').val();
        send.eDate = $('#eDate').val();
      }
      return $.extend({}, d, send);
    }
  },
  columns: [
    { data:null, orderable:false, name:'CHK',
      render: d => '<input type="checkbox" name="Selection" value="'+ (d.informerId||'') +'">' },
    { data:'actId',       name:'ACT_ID' },
    { data:'areaName',         name:'AREA_NAME' },
    { data:'informerTypeName', name:'INFORMER_TYPE_NAME' },
    { data:'orgName',          name:'ORG_NAME' },
    { data:'informerName',     name:'INFORMER_NAME' },
    { data:'phoneCell',        name:'PHONE_CELL' },
    { data:'flagAct',          name:'FLAG_ACT' },
    {
    	  data: 'regDate',
    	  name: 'REG_DATE',
    	  render: function (d, t, r) {
    	      if (d === null || d === '' || d === undefined) {
    	          return '정보 없음';
    	      }
    	      return d;
    	  }
    	}
  ],
  createdRow: function(row, data){
    $(row).attr('data-informer-id', data.informerId).css('cursor','pointer');
  }
});
  // 행 클릭 팝업
  $('#informerTable tbody').on('click', 'tr', function(e){
    if ($(e.target).is('input, a, button, label, select')) return;
    const row = window.dt.row(this).data();
    if (row) editInformer(row.informerId);
  });

  // 폼 submit → search()
  $('#searchFrm').on('submit', function(e){
    e.preventDefault();
    search();
  });
});

// ★ 전역 search(): inline onclick="search()"에서도 접근 가능
function search(){
  if (!window.dt) {
    console.warn('DataTable not initialized');
    return;
  }
  // 스크롤 맨 위로
  if (window.dt.scroller && window.dt.scroller()) {
    try { window.dt.scroller().toPosition(0); } catch(e){}
  }
  window.dt.ajax.reload();
}



$('#downDataSelect').on('click', function () {
    $(this).toggleClass('on off');
});


$('#selectConfirmBtn').on('click', function () {
    $('#downDataSelect').removeClass('on').addClass('off');
});

</script>


<div id="contentWrap"  style="width:1030px;">
<!-- <div id="posi"><a href="/main.do"><img src="../images/ico_home.gif" alt="home" /></a>제보자관리 > 제보자관리</div> -->
<div id="searchDiv" style="height: 100px;">
    <div id="contents">
        <h1 class='content-title'>통신원관리</h1>
    </div>
    <div class="board_list" style="margin-top: 10px; margin-bottom:100px;">
        <div class="rounding_wrap" style="width: 1030px;background: #f1f1f1;border-radius: 30px;border: 1px solid #a8b4c4;height: 50px;display: flex; align-items: center;justify-content: center;">
                <div class="wrap_center" style="    width: 1030px;background: none;border: none;display: flex;justify-content: center;align-items: center;">
				    <div id="searchBox" style="float: right;margin-right:15px ">
				        <form id="searchFrm" name="searchFrm">
				            <table class="tbSch">
					            <tr>
				 					<td style="width:70px;">
				 					<input type="checkbox" id="dchker" /> 
				 					등록일 : 
				 					</td>
				 					<td style="width:205px;">
				 						<input type="text" id="sDate" class="dateText" name="sDate" style="text-align:center" maxlength="8" size=10 onkeydown='return onlyNumber(event)' onkeyup='removeChar(event)' onclick="dateFunc">
				 						 ~ 
				 						<input type="text" id="eDate" class="dateText" name="eDate" style="text-align:center" maxlength="8" size=10 onkeydown='return onlyNumber(event)' onkeyup='removeChar(event)' onclick="dateFunc">
				 					</td>
				                    <td>
                                        <select class="table_sel"  style="width:100px;" id="areaCodeSel" name="areaCode">
                                        	 <c:choose>
                                             		<c:when test="${fn:length(informerAreaList)==1}">
                                             	
	                                             	</c:when>
	                                             	<c:otherwise>
														<option value="" selected><c:out value="소속"/></option>	
	                                             	</c:otherwise>
	                                         </c:choose>
                                             <c:forEach var="informerArea" items="${informerAreaList}">
                                                 <option value="${informerArea.areaCode}" 
                                                  <c:if test="${informerArea.areaCode eq login.regionId}">selected</c:if>>
														${informerArea.areaName}
                                                 </option>
                                             </c:forEach>
                                         </select>
				                    </td>
                                   	<td id="tdItype">
                                     	<select class="table_sel" style="width:120px;" id="informerTypeSel" name="informerType">
                                     		 <option value="" selected><c:out value="유형"/></option>
                                             <c:forEach var="informerType" items="${informerTypeList}">
                                                  <option value="${informerType.ifmId1}" <c:if test="${informerType.ifmId1 eq informerInfo.informerType}">selected</c:if>>${informerType.ifmName}</option>
                                             </c:forEach>
                                         </select>
                                     </td>
                                         <td id="tdOrgId">
											<select class="table_sel"  style="width:165px;" id="orgIdSel" name="orgId">
												<option value="" selected><c:out value="소속기관"/></option>
                                             </select>
                                         </td>
				                    <td>
				                        <select class="table_sel" id="searchActYn" name="flagAct">
				                            <option value="" ><c:out value="활동여부"/></option>
				                            <option value="Y" ><c:out value="위촉"/></option>
				                            <option value="N" ><c:out value="해촉"/></option>
				                        </select>
				                    </td>
				                    <td>
				                        <select class="table_sel" id="searchType" name="searchType">
				                        	<option value="informerName" ><c:out value="이름"/></option>
				                            <option value="actId" ><c:out value="제보자ID"/></option>
				                            <option value="phoneCell" ><c:out value="전화번호"/></option>
				                        </select>
				                    </td>
				                    <td>
				                        <input class="input_base" type="text" id="searchValue" name="searchValue"  onkeyup="if(event.keyCode == 13)search();"/>
				                    </td>
				                    <td>
				                        <img src="../images/btn_search.gif" onclick="search();" style="cursor:pointer"  />
				                    </td>
				                </tr>
				            </table>
				        </form>
				    </div>
		        </div>
	    </div>
    </div>
    
    <!-- 검색조건 영역 끝 -->
    <div style="display:flex;align-items: center;justify-content: space-between;">
		<div>
			<!-- <p id="resultListTotal" style="width: 300px;">
				<img src="../images/ico_result.gif" />
				검색결과 <span style="font-weight:700;"></span>건
			</p> -->
		</div>
		<!-- <div>
			<input type="hidden" value="0" id="showCtn">
			<select id="addOptionSelect" style="display:none;">
				<option value="none">-- 선택 --</option>
				<option value="16">16라벨 출력</option>
				<option value="24">24라벨 출력</option>
			</select>
			<button id="addDown" style="width: 100px; height: 30px; border-radius: 3px; color: white; background-color: #7b7c7d;margin-right: 4px;"><strong>주소 라벨 출력</strong></button>
			<a href="javascript:goStats('stats/informerDown.ajax');">
				<img src="../images/btn_excel_down2.gif" alt="엑셀다운로드" style="width: 90px;"/>
			</a>			
		</div> -->
	</div>
</div>
<form id="listFrm" name="listFrm">
<input type="hidden" name="labelType" id="labelT"> 
<div id="listDiv">

<table id="informerTable" class="display" style="width:100%">
  <thead>
  <tr>
    <th>선택</th>
    <th>ID</th>
    <th>방송국</th>
    <th>유형</th>
    <th>소속기관</th>
    <th>이름</th>
    <th>전화</th>
    <th>활동여부</th>
    <th>등록일</th>
  </tr>
  </thead>
  <tbody></tbody>
</table>

</div>
</form>
<div id="pageDiv" style="margin-top:10px">
    <div id="pagingBox">
        <!-- paging Box content -->
    </div>
    
    <div>
		<input type="hidden" value="0" id="showCtn">
		<select id="addOptionSelect" style="display:none;">
			<option value="none">-- 선택 --</option>
			<option value="16">16라벨 출력</option>
			<option value="24">24라벨 출력</option>
		</select>
		<button id="addDown" style="width: 100px; height: 30px; border-radius: 3px; color: white; background-color: #7b7c7d;margin-right: 4px;"><strong>주소 라벨 출력</strong></button>
		<a href="javascript:goStats('stats/informerDown.ajax');">
			<img src="../images/btn_excel_down2.gif" alt="엑셀다운로드" style="width: 90px;"/>
		</a>	
		
		<div class="output-select-area">	
			<button id="downDataSelect" class="off">
			출력 데이터 변경
			</button>
			<div class="select-wrap">
	
			    <!-- 기본 -->
			    <div class="section">
			        <div class="section-title">기본</div>
			        <div class="section-body scroll">
			            <label><input type="checkbox" checked disabled> 통신원 ID</label>
			            <label><input type="checkbox" checked disabled> 방송국</label>
			            <label><input type="checkbox" checked disabled> 유형</label>
			            <label><input type="checkbox" checked disabled> 소속기관</label>
			            <label><input type="checkbox" checked disabled> 이름</label>
			            <label><input type="checkbox" checked disabled> 전화번호</label>
			            <label><input type="checkbox" checked disabled> 활동여부</label>
			            <label><input type="checkbox" checked disabled> 등록일</label>
			             <label><input type="checkbox" checked disabled>우편 번호</label>
			            <label><input type="checkbox" checked disabled> 주소</label>
			        </div>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
			    </div>
			
			    <!-- 추가사항 --> 
			    <div class="section">
			        <div class="section-title">추가사항</div>
			        <div class="section-body scroll2">
			            <label><input class="datalist" type="checkbox" name="selectedCols" value="TRS_NO"> TRS</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="MEMO1"> 메모</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="HONOR"> 명예 통신원</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="BIRTHDAY"> 생일</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="FLAG_SERVICE"> 자원 봉사</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="MEMO"> 전달 사항</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="ADDRESS_OFFICE"> 회사 주소</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="PHONE_HOME"> 집 전화번호</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="CAR_NUM"> 차량 번호</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="CAR_TYPE"> 차량 종류</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="FLAG_BEST"> 최고 통신원</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="UPD_DATE"> 최종 수정일</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="LAST_SCHOOL"> 최종 학력</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="MEMO2"> 추가 메모</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="FLAG_BROAD"> 방송통신원</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="INFORMER_JOB"> 통신원 직업</label>
						<label><input class="datalist" type="checkbox" name="selectedCols" value="PHONE_OFFICE"> 회사 전화번호</label>

			        </div>
			    </div>
			    <div class="buttonContainer" style="display:flex; justify-content: space-between;">
			    	<!-- 전체 선택 버튼 -->
			    	<div class="confirm-area">
				        <button type="button" id="selectConfirmAllBtn">전체 선택</button>
				    </div>
			    	<!-- 전체 해제 버튼 -->
			    	<div class="confirm-area">
				        <button type="button" id="unChkConfirmAllBtn">전체 해제</button>
				    </div>
				    
			        <!-- 확인 버튼 -->
				    <div class="confirm-area">
				        <button type="button" id="selectConfirmBtn">확인</button>
				    </div>
			    </div>
			</div>
		</div>
	</div>
    <!--style="margin-right:15px;margin-top: 50px;"  -->
    <div class="btnBox" align="right" >
        <span onclick="editInformer();" class="editUser"><button type="button" style="border: none;"><img src="../images/btn_save1.png" alt="" style="cursor: pointer;width: 92px;height: 30px;background-size: cover; "></button></span>
    </div>
</div>
</div>

<script>

$(document).ready(function(){
	console.log("informerMain.jsp 진입");
                                                                          
	
	/* init(); */
	
	
	var fromDate = new Date(new Date().setMonth(new Date().getMonth() - 1));
	fromDate = getFormatDate(fromDate);
	var toDate = getFormatDate(new Date());
	
	console.log("fromDate :" + fromDate);
	console.log("toDate :" + toDate);
	//var infrmCalender = dateFunc("dateText","dateText1","dateText2",search,fromDate,toDate);
	dateFunc('sDate','eDate',fromDate,toDate);
	
	
	//최초에는 미사용이므로 비활성화
	$(".dateText").attr("disabled", true);
	//$('#dchker').prop('checked',true);
	
	//최초 조회시는 위촉만
	$('#searchFrm').submit( function(event){
		event.preventDefault();
		search();
	});
	
	$('#dchker').on("click", function(){
		if($(this).is(":checked")){
			$(".dateText").attr("disabled", false);
		}else{
			$(".dateText").attr("disabled", true);
		}
	});
	
	
	// 출력 데이터 변경 전체 선택 기능
	$('#selectConfirmAllBtn').on('click', function() {
		$('.section-body.scroll2 input[type="checkbox"]').prop('checked', true);
	});
	
	$('#unChkConfirmAllBtn').on('click', function() {
		$('.section-body.scroll2 input[type="checkbox"]').prop('checked', false);
	});
	
	
	//$('#listDiv').load('/informer/informerList.do?sDate='+fromDate+'&eDate='+toDate+''+'&eDate='+flagAct='+'Y');
	//$('#listDiv').load('/informer/informerList.do;');
	
	//소속방송국,통신원유형 클릭 시 -> id랑 소속기관 소속기관 세부 갱신
    $("#areaCodeSel , #informerTypeSel").on("change",function(){
    	$("#orgIdSel option").remove();
    	$("#orgSubIdSel option").remove();
    	if($("#informerTypeSel").val()==""){
    		$("#orgIdSel").append("<option value=''>소속기관</option>");
    	}else{
    		var ajaxData = ajaxMethod
        	("/informer/reSel.do",
    			{
    				areaCode:$("#areaCodeSel option:selected").val()
    				,informerType:$("#informerTypeSel option:selected").val()
    				,sflag:$(this).attr("id")	
    			}
    		);
        	
        	//소속방송국 경우 ->actid 변경
        	if(typeof ajaxData.newActId !="undefined"){
        		$("#actId").val(ajaxData.newActId);
        	}
        	//지역코드값과 통신원유형으로 2,3을 불러옴
        	
        	var alData1 = ajaxData.sList1;
        	var alData2 = ajaxData.sList2;
        	
        	//셀렉트박스 갱신 하위영역들 2,3
        	if (alData1.length==0) {
        		console.log("alData1.length==0");
        		$("#orgIdSel").append("<option value=''>무소속</option>");
    		}else{
    			console.log("alData1.length!=0");
    			$("#orgIdSel").append("<option value=''>소속기관</option>");
    			for (var i = 0; i < alData1.length; i++) {
    	    		$("#orgIdSel").append("<option value='"+alData1[i].ifmId2+"'>"+alData1[i].ifmName+"</option>");	
    			}	
    		}
    		if (alData2.length==0) {
    			console.log("alData2.length==0");
    			$("#orgSubIdSel").append("<option value=''>무소속</option>");
    		}else{
    			console.log("alData2.length!=0");
    			for (var i = 0; i < alData2.length; i++) {
    	    		$("#orgSubIdSel").append("<option value='"+alData2[i].ifmId3+"'>"+alData2[i].ifmName+"</option>");
    			}	
    			$("#orgSubIdSel").append("<option value=''>무소속</option>");
    		}
    	}
    	
		
    });
	
});

/**
 * 통신원 수정 팝업
 */
function editInformer(str){
    var param = "";
    var popupW = 1100;  // 팝업 넓이
    var popupH = 900;  // 팝업 높이
    
    
    // 업데이트 인 경우
    if(str != null && str != "" && str != "undefined"){
        param = "?pr1=" + str;
    }
    console.log("변수 :" + param);
    
    var url = "/informer/editInformer.do" + param;
    var windowName = "통신원등록";
    var left = Math.ceil((window.screen.width - popupW)/2);
    var top = Math.ceil((window.screen.height - popupH)/2);
    popObj = window.open(url, windowName, 'width=' + popupW +  ', height=' + popupH +  ', left=' + left +  ', top=' + top + ', toolbar=no, location=no, status=no, menubar=no, scrollbars=no, resizable=no');
}

/**
 * 통신원 이력 팝업
 */
function showInformerHistory(str){
	var param = "";
	if(str != null && str != "" && str != "undefined"){
        param = "?informerId=" + str;
    }
    var url = "/informer/showInformerHistory.do" + param;
    var windowName = "통신원이력";
    var popupW = 800;  // 팝업 넓이
    var popupH = 700;  // 팝업 높이
    var left = Math.ceil((window.screen.width - popupW)/2);
    var top = Math.ceil((window.screen.height - popupH)/2);
    popObj = window.open(url, windowName, 'width=' + popupW +  ', height=' + popupH +  ', left=' + left +  ', top=' + top + ', toolbar=no, location=no, status=no, menubar=no, scrollbars=no, resizable=no');
}



/**
 * 페이징
 */
function pagination(){
   /*  var options = {
            url: "/informer/informerListPagenation.do",
            target:"#pagingBox",
            type:"post"
    };
    $('#searchFrm').ajaxSubmit(options); */
}
/* function goStats(url){
	console.log("통계 서브밋?");
    
 	rkFlag = true;
	searchFrm.action = '<c:url value="/"/>'+url;
	searchFrm.submit();
	rkFlag = true; 
} */


function goStats(url){
    console.log("통계 서브밋?");

    $('#searchFrm input[name="selectedCols"]').remove();

    $('.datalist:checked').each(function () {
        $('<input>')
            .attr('type', 'hidden')
            .attr('name', 'selectedCols')
            .val(this.value)
            .appendTo('#searchFrm');
    });

    rkFlag = true;
    searchFrm.action = '<c:url value="/"/>' + url;
    searchFrm.submit();
    rkFlag = true;
}


// 주소 라벨 출력 버튼 클릭 시 실행 함수
$('#addDown').on('click', function(){
	var showCtn = $('#showCtn').val();
	
	if(showCtn == 0) { //select box 보이게 하기
		$('#addOptionSelect').show();
		$('#showCtn').val(1);
	} else {
		$('#addOptionSelect').hide();
		$('#showCtn').val(0); // 초기화 하기
	}
	
});

// 라벨 타입 선택 시 실행 함수
$('#addOptionSelect').on('change', function() {
	var nowVal = $('#addOptionSelect').val();
	var url = 'stat/addDownload.do';
	
	
	if(nowVal == 'none') { 
		return false;
	} else {
		$('#labelT').val(nowVal);
		
		var valCheck = labelYes();
		
		if(valCheck) {
			rkFlag = true;
			listFrm.action = '<c:url value="/"/>'+url;
			listFrm.submit();
			rkFlag = true; 
		} else {
			return false;
		}
		
	}
})

// 라벨 출력 유효성 검사
function labelYes() {
	
	if ($("input[name='Selection']:checked").length === 0) {
        alert("통신원을 1명 이상 선택해주세요");
        return false; 
 } else {
	 return true;
 } 
	
}





</script>