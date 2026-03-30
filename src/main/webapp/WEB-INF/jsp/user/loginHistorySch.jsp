<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>
<form id="searchFrm" name="/user/loginHistoryList.do">
	<table>
		<tr>
			<td style="width:70px;">
				접속일자 : 
			</td>
			<td style="width:205px;">
				<input type="text" id="dateText1" class="dateText" name="sDate" style="text-align:center" size="10" readonly="readonly" onclick="dateFunc">
				 ~ 
				<input type="text" id="dateText2" class="dateText" name="eDate" style="text-align:center" size="10" readonly="readonly" onclick="dateFunc">
			</td>
			<td><select class="table_sel" id=authCode name="authCode">
					<option value=""><c:out value="권한" /></option>
					<c:forEach var="auth" items="${authList}" varStatus="idx">
						<option value="${auth.authCode}"><c:out
								value="${auth.authName}" /></option>
					</c:forEach>
			</select></td>
			<td><select class="table_sel" id="searchType" name="searchType">
					<option value="userId"><c:out value="사용자ID" /></option>
					<option value="userName"><c:out value="이름" /></option>
			</select></td>
			<td><input class="input_base" type="text" id="searchValue" name="searchValue" onkeyup="spaceChk(this);" onkeydown="spaceChk(this);" /></td>
			<td>
				<button type="submit">
					<img src="../images/btn_admin_search.gif" style="cursor: pointer" />
				</button> 
			</td>
		</tr>
	</table>
</form>
<script>
$(document).ready(function(){
	console.log("userMain.jsp 진입");
	
	$('.input_base').keydown(function(e) {
		if(e.keyCode==13){
			//기존 엔터버튼 누를 시 화면누락 방지 및 추가 및 수정 버튼클릭과 동일한 효과를 줌(트리거)
			e.preventDefault();
			search();
		}
	});
	
	var hisCalender = dateFunc("dateText","dateText1","dateText2",search);
	
	$('#searchFrm').submit( function(event){
		//화면이동 방지
		event.preventDefault();
		search();
	});

	/**
	 * 검색
	 */
	function search(){
		console.log("function search");
		let queryString = $("#searchFrm").serialize();
		var thUrl = hisUrl;
		var options = {
	        url:thUrl,
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
	            alert("에러가 발생했습니다."+error);
	        }
	    };
		//화면이동 방지
		event.preventDefault();
	    $.ajax(options);
	}
});

</script>