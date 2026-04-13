<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script>
	$(document).ready(function(){

	});
</script>
<c:forEach var="toadysListVO" items="${appStatusList}" varStatus="num">
	<li id="resultList${toadysListVO.RECEIPT_ID}"
    <c:choose>
        <c:when test="${toadysListVO.FLAG_SITU_ED eq '2'}">
            style="background-color: #efefef;"
        </c:when>
        <c:when test="${toadysListVO.FLAG_CHK eq 'Y'}">
            style="background-color: aliceblue;"
        </c:when>
    </c:choose>>
		<span style="width : 60px;">
			<c:choose>
		        <c:when test="${toadysListVO.FLAG_CHK eq 'N'}">
		            <input type="checkbox" name="Selection" value="${toadysListVO.RECEIPT_ID}"/>
		        </c:when>
		        <c:otherwise>
		            
		        </c:otherwise>
		    </c:choose>
		</span>
		<span style="width:60px;">${toadysListVO.RNUM }</span>
		<span style="width:100px;">${toadysListVO.RECEIPT_DAY }</span>
		<span style="width:100px;">${toadysListVO.RECEIPT_TIME }</span>
		<span style="width:100px;">${toadysListVO.REPORT_TYPE}</span>
		<span style="width:150px;">${toadysListVO.INDIVIDUAL_NAME }</span>
		<c:choose>
        	<c:when test="${toadysListVO.FLAG_SITU_ED eq '3'}">
				<span style="width:516px;">
					<div style="display : flex; justify-content: center;">
						[오류제보]<p style="color :lightgray;">${fn:replace(toadysListVO.MEMO, '[오류제보]', '')}</p>
					</div>		
				</span>	
			</c:when>	
			<c:otherwise>
				<span style="width:516px;">${toadysListVO.MEMO }</span>	
			</c:otherwise>			
		</c:choose>
		<span style="width:80px;">${toadysListVO.REPORTER_TYPE }</span>
		<span>${toadysListVO.REGION_NAME }</span>
	</li>
</c:forEach>