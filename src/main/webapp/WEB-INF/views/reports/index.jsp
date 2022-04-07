<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commSearch" value="${ForwardConst.CMD_SEARCH.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>日報 一覧</h2>
        <table id="report_list">
            <tbody>
               <div>
                <form method="POST" action="<c:url value='?action=${actRep}&command=${commSearch}'/>">
                   <select id="report_progress" name="${AttributeConst.REP_PROGRESS.getValue()}">
                      <option disabled selected>進捗を選択</option>
                      <!--  <option value="0">0.すべて</option> -->
                      <option value="${AttributeConst.PRG_BEGIN.getIntegerValue()}">1.開始前</option>
                      <option value="${AttributeConst.PRG_PLAN.getIntegerValue()}">2.打合せ予定</option>
                      <option value="${AttributeConst.PRG_POST.getIntegerValue()}">3.打合せ延期</option>
                      <option value="${AttributeConst.PRG_DEC.getIntegerValue()}">4.打合せ確定</option>
                      <option value="${AttributeConst.PRG_WAIT.getIntegerValue()}">5.結果連絡待ち</option>
                      <option value="${AttributeConst.PRG_END.getIntegerValue()}">6.終了(交渉可否無関係)</option>
                      <option value="${AttributeConst.PRG_SUC.getIntegerValue()}">7.終了(交渉成功)</option>
                      <option value="${AttributeConst.PRG_FAIL.getIntegerValue()}">8.終了(交渉失敗)</option>
                      <option value="${AttributeConst.PRG_CANCEL.getIntegerValue()}">9.キャンセル</option>
                    </select>
                    <button type="submit" id="button">検索</button>
                 </form>
               </div>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_client">顧客名(取引先名)</th>
                    <th class="report_progress">進捗</th>
                    <th class="report_action">操作</th>
                </tr>
                <c:forEach var="report" items="${reports}" varStatus="status">
                    <fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />

                    <tr class="row${status.count % 2}">
                        <td class="report_name"><c:out value="${report.employee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="report_client">${report.client}</td>
                        <td class="report_progress"><c:choose>
                            <c:when test="${report.progress == AttributeConst.PRG_BEGIN.getIntegerValue()}">1.開始前</c:when>
                            <c:when test="${report.progress == AttributeConst.PRG_PLAN.getIntegerValue()}">2.打合せ予定</c:when>
                            <c:when test="${report.progress == AttributeConst.PRG_POST.getIntegerValue()}">3.打合せ延期</c:when>
                            <c:when test="${report.progress == AttributeConst.PRG_DEC.getIntegerValue()}">4.打合せ確定</c:when>
                            <c:when test="${report.progress == AttributeConst.PRG_WAIT.getIntegerValue()}">5.結果連絡待ち</c:when>
                            <c:when test="${report.progress == AttributeConst.PRG_END.getIntegerValue()}">6.終了(交渉可否無関係))</c:when>
                            <c:when test="${report.progress == AttributeConst.PRG_SUC.getIntegerValue()}">7.終了(交渉成功)</c:when>
                            <c:when test="${report.progress == AttributeConst.PRG_FAIL.getIntegerValue()}">8.終了(交渉失敗)</c:when>
                            <c:otherwise>9.キャンセル</c:otherwise>
                        </c:choose></td>
                        <td class="report_action"><a href="<c:url value='?action=${actRep}&command=${commShow}&id=${report.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div id="pagination">
            （全 ${reports_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actRep}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='?action=${actRep}&command=${commNew}' />">新規日報の登録</a></p>

    </c:param>
</c:import>