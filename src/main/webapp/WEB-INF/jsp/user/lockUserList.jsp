<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="board_list">
    <div class="list_result_sc">
        <table summary="계정잠금사용자목록"
               border="0" cellpadding="0" cellspacing="0"
               class="list01" id="user_table"
               style="display:table; width:100%; table-layout:fixed;">

            <colgroup>
                <col style="width:6%;">
                <col style="width:13%;">
                <col style="width:18%;">
                <col style="width:13%;">
                <col style="width:13%;">
                <col style="width:18%;">
                <col style="width:19%;">
            </colgroup>

            <thead style="display:table-header-group; float:none;">
                <tr style="display:table-row; float:none;">
                    <th scope="col" style="display:table-cell; float:none; width:auto; box-sizing:border-box; text-align:center;">선택</th>
                    <th scope="col" style="display:table-cell; float:none; width:auto; box-sizing:border-box; text-align:center;">ID</th>
                    <th scope="col" style="display:table-cell; float:none; width:auto; box-sizing:border-box; text-align:center;">소속</th>
                    <th scope="col" style="display:table-cell; float:none; width:auto; box-sizing:border-box; text-align:center;">이름</th>
                    <th scope="col" style="display:table-cell; float:none; width:auto; box-sizing:border-box; text-align:center;">권한</th>
                    <th scope="col" style="display:table-cell; float:none; width:auto; box-sizing:border-box; text-align:center;">전화번호</th>
                    <th scope="col" style="display:table-cell; float:none; width:auto; box-sizing:border-box; text-align:center;">잠금일자</th>
                </tr>
            </thead>

            <tbody style="display:table-row-group; float:none; height:auto; overflow:visible;">
                <c:choose>
                    <c:when test="${fn:length(userList)==0}">
                        <tr style="display:table-row; float:none;">
                            <td colspan="7"
                                style="display:table-cell; float:none; width:auto; text-align:center;">
                                해당하는 데이터를 조회하지 못했습니다.
                            </td>
                        </tr>
                    </c:when>

                    <c:otherwise>
                        <c:forEach var="user" items="${userList}" varStatus="idx">
                            <tr style="display:table-row; float:none;">
                                <td style="display:table-cell; float:none; width:auto; box-sizing:border-box; text-align:center;">
                                    <input type="checkbox"
                                           name="userIds"
                                           value="<c:out value='${user.userId}'/>">
                                </td>
                                <td style="display:table-cell; float:none; width:auto; box-sizing:border-box; text-align:center;">
                                    <c:out value="${user.userId}"/>
                                </td>
                                <td style="display:table-cell; float:none; width:auto; box-sizing:border-box; text-align:center;">
                                    <c:out value="${user.regionName}"/>
                                </td>
                                <td style="display:table-cell; float:none; width:auto; box-sizing:border-box; text-align:center;">
                                    <c:out value="${user.userName}"/>
                                </td>
                                <td style="display:table-cell; float:none; width:auto; box-sizing:border-box; text-align:center;">
                                    <c:out value="${user.authName}"/>
                                </td>
                                <td style="display:table-cell; float:none; width:auto; box-sizing:border-box; text-align:center;">
                                    <c:out value="${user.userPhone}"/>
                                </td>
                                <td style="display:table-cell; float:none; width:auto; box-sizing:border-box; text-align:center; white-space:nowrap;">
                                    <c:out value="${fn:substring(user.accountLockDt, 0, 19)}"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>