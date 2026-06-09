<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script>
	$(document).ready(function(){
		console.log("receivedStatusList");
	});
</script>
<c:forEach var="toadysListVO" items="${receivedStatusList}" varStatus="num">
		<%-- <c:choose>
		  <c:when test="${fn:startsWith(toadysListVO.RECEIPT_ID, 'APP_')}">
		    <li id="resultList${toadysListVO.RECEIPT_ID}"
		        style="background-color: aliceblue;">
		  </c:when>
		  <c:otherwise>
		    <li id="resultList${toadysListVO.RECEIPT_ID}"
		        ondblclick="getEditPage('${toadysListVO.RECEIPT_ID}','${toadysListVO.RNUM}')">
		  </c:otherwise>
		</c:choose> --%>
		
		<!-- 26/06/08 : APP도 더블 클릭 시 정보 수정 가능 -->
		<c:choose>
		    <c:when test="${toadysListVO.REPORTER_TYPE == 'APP'}">
		        <li id="resultList${toadysListVO.RECEIPT_ID}"
		            style="background-color: aliceblue;"
		            ondblclick="getEditPage('${toadysListVO.RECEIPT_ID}','${toadysListVO.RNUM}')">
		    </c:when>
		    <c:otherwise>
		        <li id="resultList${toadysListVO.RECEIPT_ID}"
		            ondblclick="getEditPage('${toadysListVO.RECEIPT_ID}','${toadysListVO.RNUM}')">
		    </c:otherwise>
		</c:choose>
		
		<span style="width:50px;">${toadysListVO.RNUM }</span>
		<span style="width:85px;">${toadysListVO.RECEIPT_DAY }</span>
		<span style="width:30px;">
			<c:choose>
       	   		<c:when test="${toadysListVO.FLAG_SEND == 'Y' }">
       				<img src="../images/ico_send.png" alt="전송여부" />
       	   		</c:when>
       	   		<c:otherwise>
       	   			<img src="../images/ico_no2.png" alt="전송여부" />
       	   		</c:otherwise>
       		</c:choose>
		</span>
		<span style="width:30px;">
			<c:choose>
				<c:when test="${toadysListVO.FLAG_BROD eq 'Y'}">
					<img src="../images/btn_pd2.png" alt="방송상태" />
				</c:when>
				<c:when test="${toadysListVO.FLAG_BROD eq 'C'}">
					<img src="../images/btn_caster2.png" alt="방송상태" />
				</c:when>
				<c:otherwise>
					<img src="../images/ico_no2.png" alt="방송상태" />
				</c:otherwise>
			</c:choose>
		</span>
		<span style="width:52px;">${toadysListVO.RECEIPT_TIME }</span>
		<span style="width:52px;">${toadysListVO.BROAD_TIME }</span>
		<span style="width:60px; font-weight:700;
					color:${toadysListVO.f_COLOR }; background-color:${toadysListVO.b_COLOR };">
				${toadysListVO.REPORT_TYPE }
		</span>
		<span style="width:150px;">${toadysListVO.REPORT_TYPE2 }</span>
		<span style="width:120px;">${toadysListVO.INDIVIDUAL_NAME }</span>
		<span style="width:40px;">${toadysListVO.TYPE_NAME }</span>
		<span style="width:80px;">${toadysListVO.RECEPTION_NAME }</span>
		<!-- 긴급제보의 경우 노란바탕에 빨간글씨 -->
		<c:choose>
   	   		<c:when test="${fn:contains(toadysListVO.FLAG_IMPORTANT, 'Y')  && toadysListVO.FLAG_BROD == 'N'}">
   				<span style="width:481px;font-weight:700;color:#FF0000;background-color:#FFFF00;">${toadysListVO.MEMO }</span>
   	   		</c:when>
   	   		<c:otherwise>
   	   			<span style="width:481px;">${toadysListVO.MEMO }</span>
   	   		</c:otherwise>
   		</c:choose>
		
		<span style="width:60px;">${toadysListVO.REPORTER_TYPE }</span>
		<span>${toadysListVO.REGION_NAME }</span>
	</li>
</c:forEach>