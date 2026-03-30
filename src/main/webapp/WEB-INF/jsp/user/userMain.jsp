<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>

	<!-- datetimepicker 실행 필수 -->
    <script src="<%=request.getContextPath()%>/calender/moment.js"></script>
	<script src="<%=request.getContextPath()%>/calender/mo_ko.js"></script>
	<script src="<%=request.getContextPath()%>/calender/bootstrap-datetimepicker.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/calender/no-boot-calendar-custom.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/calender/datetimepickerstyle.css" />ㄴ
<div id="contentWrap">
<!-- <div id="posi"><a href="/main.do"><img src="../images/ico_home.gif" alt="home" /></a>사용자관리 > 사용자관리</div> -->
<div id="searchDiv">
    <div id="contents">
        <h1 class='content-title'>사용자관리</h1>
    </div>
    <div class="board_list" style="margin-top: 10px;">
        <div class="rounding_wrap">
            <div class="wrap_top"></div>
                <div class="wrap_center">
                    <div id="searchBox" style="float: right;margin-right:15px ">
					    <form id="searchFrm" name="searchFrm" class="searchFrm">
					        <table class="tbSch">
					            <tr>
				 					<td style="width:70px;">
				 					<input type="checkbox" id="dchker" /> 
				 					등록일 : 
				 					</td>
				 					<td style="width:205px;">
				 						<input type="text" id="dateText1" class="dateText" name="sDate" style="text-align:center" size=10 maxlength="8" onclick="dateFunc('userDate1')">
				 						 ~ 
				 						<input type="text" id="dateText2" class="dateText" name="eDate" style="text-align:center" size=10 maxlength="8" onkeydown='return onlyNumber(event)' onkeyup='removeChar(event)' onclick="dateFunc('userDate2')">
				 					</td>
				                    <td>
				                        <select class="table_sel" id=authCode name="authCode">
				                            <option value="" ><c:out value="권한"/></option>
				                            <c:forEach var="auth" items="${authList}" varStatus="idx">
				                                <option value="${auth.authCode}" ><c:out value="${auth.authName}"/></option>
				                            </c:forEach>
				                        </select>
				                    </td>
					                <td>
					                    <select class="table_sel" id="searchType" name="searchType">
					                        <option value="userId" ><c:out value="사용자ID"/></option>
					                        <option value="userName" ><c:out value="이름"/></option>
					                    </select>
					                </td>
					                <td>
					                    <input class="input_base" type="text" id="searchValue" name="searchValue" onkeyup="spaceChk(this);" onkeydown="spaceChk(this);" />
					                </td>
					                <td>
					                	<button type="submit"><img src="../images/btn_search.gif" style="cursor:pointer"  /></button>
					                </td>
					            </tr>
					        </table>
					    </form>
				    </div>
		        </div>
		    <div class="wrap_bottom"></div>
	    </div>
    </div>
</div>
<div id="listDiv">
</div>
<div id="pageDiv" style="margin-top:50px">
    <div id="pagingBox">
        <!-- paging Box content -->
    </div>
    <div class="btnBox" align="right" style="margin-right:15px">
        <span onclick="editUser();" class="editUser"><img src="../images/btn_save1.png" alt="" style="cursor: pointer;width: 92px;height: 30px;background-size: cover; "></span>
    </div>
</div>
</div>
<script>
$(document).ready(function(){
	console.log("userMain.jsp 진입");
	
	userInit();
	//datepicker 생성 및 날짜 변경 시 관련함수 
	//파라미터 :
	/* 
	1. datepicker를 사용하는 input들의 공통된 class
	2. 시작일 input의 id값
	3. 종료일 input의 id값 (기간이 아니라 날짜 input이 1개라면 안 넣어도 무방)
	4. 날짜 초기지정 또는 날짜 변경후 실행할 callback함수(안 넣어도 무방)
	*/
	var userCalender = dateFunc("dateText1", "dateText2");
	
	//최초에는 미사용이므로 비활성화(사용자관리 한정)
	$(".dateText").attr("disabled", true);
	
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
});

function userInit(){
	console.log("listDiv에 userList 호출");
    $('#listDiv').load("/user/userList.do?MIN=0");
   //console.log("pagingBox에 userListPagenation 호출");
    //$('#pagingBox').load("/user/userListPagenation.do");
}

/**
 * 사용자 수정 팝업
 */
function editUser(str){
	console.log("function editUser");
	var param = "";
	if(str != null && str != "" && str != "undefined"){
		param = "?pr1=" + str;
	}
	var url = "/user/editUser.do" + param;
	//var url = "http://www.naver.com";
	var windowName = "사용자등록";
	var popupW = 500;  // 팝업 넓이
	var popupH = 550;  // 팝업 높이
	var left = Math.ceil((window.screen.width - popupW)/2);
	var top = Math.ceil((window.screen.height - popupH)/2);
	popObj = window.open(url, windowName, 'width=' + popupW +  ', height=' + popupH +  ', left=' + left +  ', top=' + top + ', toolbar=no, location=no, status=no, menubar=no, scrollbars=no, resizable=no');
}

/**
 * 등급관리 팝업
 */
function authorityMgmt(){
	console.log("function authorityMgmt");
	var url = "/user/authorityMgmt.do";
	var windowName = "등급관리";
	var popupW = 350;  // 팝업 넓이
    var popupH = 350;  // 팝업 높이
    var left = Math.ceil((window.screen.width - popupW)/2);
    var top = Math.ceil((window.screen.height - popupH)/2);
    popObj = window.open(url, windowName, 'width=' + popupW +  ', height=' + popupH +  ', left=' + left +  ', top=' + top + ', toolbar=no, location=no, status=no, menubar=no, scrollbars=no, resizable=no');
}

/**
 * 검색
 */
function search(){
	console.log("function search");
	let queryString = $("#searchFrm").serialize();
	console.log(queryString);
	var options = {
        url:"/user/userList.do",
        type: "POST",
		//dataType: "json",
		async : false,
        data: queryString,
        target: '#listDiv',
        success: function(json){
        	console.log("성공?");
        	$('#listDiv').html(json);
        },
        error: function(res,error){
            alert("에러가 발생했습니다."+res);
        }
    };
    $.ajax(options);
}

/**
 * 페이징
 */
 
/* 
function pagination(){
	console.log("function pagination");
    var options = {
            url: "/user/userListPagenation.do",
            target:"#pagingBox",
            type:"post"
    };
    $('#searchFrm').ajaxSubmit(options);
} */

</script>