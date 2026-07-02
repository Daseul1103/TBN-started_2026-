<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- DateTimePicker -->
<script src="<%= request.getContextPath() %>/calender/moment.js"></script>
<script src="<%= request.getContextPath() %>/calender/mo_ko.js"></script>
<script src="<%= request.getContextPath() %>/calender/bootstrap-datetimepicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/calender/no-boot-calendar-custom.css" />
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/calender/datetimepickerstyle.css" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
 	table, th, td {
	  border: 1px solid black;
	  border-collapse: collapse;
	}
	
	table {
		width : 100%;
		height : auto;
	}
	
	
	th, td {
		text-align: center;
		font-size : 16px;
		line-height : 40px;
	}
	
	th {
		background-color : #dededf;
	}
	
</style>
<script>
/*  	$(document).ready(function(){

 	}); */
	 
	// 페이지 이동
	function changePage(url){
		
		if(url == 'select') {
			$.ajax
			(
				{
					type : "post" ,
					url : "/informer/excellenceIfrm/excellenceIfrmMain.do",
					dataType : "html" ,
					cache : false ,
					success:function(html){
						$('#contentWrap').html(html);
					},
		
					error:function(data,error){
						alert("시스템에 문제가 생겼습니다." + data);
					}
				}
			);
		} else if(url == 'list'){
			$.ajax
			(
				{
					type : "post" ,
					url : "/excellenceIfrm/excellenceUserMain.do" ,
					dataType : "html" ,
					cache : false ,
					success:function(html){
						$('#contentWrap').html(html);
					} ,
					error:function(data,error){
						alert("시스템에 문제가 생겼습니다." + data);
					}
				}
			);
		} else {
			$.ajax
			(
				{
					type : "post" ,
					url : "/excellenceIfrm/standardMain.do" ,
					dataType : "html" ,
					cache : false ,
					success:function(html){
						$('#contentWrap').html(html);
					} ,
					error:function(data,error){
						alert("시스템에 문제가 생겼습니다." + data);
					}
		
				}
			);
		}
	}	
	
</script>
</head>
<body>
<div id="contentWrap">
	<form id="searchFrm" name="searchFrm">
			<div id="contents">
				<h1 class='content-title'>선정 기준</h1>
				<!-- board_list -->
				<div class="board_list">
					<!-- 서브메뉴 탭영역 시작 -->
					<div class="gnb_tab">
						<ul class="lst_tab">
							<li><a href="javascript:changePage('select')">우수제보자 선정</a></li>
							<li class="ns"></li>
							<li><a href="javascript:changePage('list')">우수제보자 조회</a></li>
							<li class="ns"></li>
							<li class="on">선정 기준</li>
						</ul>
					</div>
					
					<div id="listDiv">
						<table>
							<tr>
								<th>선정 항목
								</th>
								<th>비율
								</th>
							</tr>
							<tr>
								<td>당월 주요제보건수(사고, 공사, 행사 등) 건수</td>
								<td>60~80%</td>
							</tr>
							<tr>
								<td>당월 제보건수 전체 건수</td>
								<td>10~30%</td>
							</tr>
							<tr>
								<td>전월 제보건수 전체</td>
								<td>10~20%</td>
							</tr>
						</table>
					
					</div>
			</div>
		</div>
		
	</form>
</div>
</body>
</html>