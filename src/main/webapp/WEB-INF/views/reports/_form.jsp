<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>

<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>

    </div>
</c:if>
<fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />
<label for="${AttributeConst.REP_DATE.getValue()}">日付</label><br />
<input type="date" name="${AttributeConst.REP_DATE.getValue()}" value="<fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' />" />
<br /><br />

<label for="name">氏名</label><br />
<c:out value="${sessionScope.login_employee.name}" />
<br /><br />

<label for="${AttributeConst.REP_TITLE.getValue()}">タイトル</label><br />
<input type="text" name="${AttributeConst.REP_TITLE.getValue()}" value="${report.title}" />
<br /><br />

<label for="client">顧客名(取引先名)</label><br />
<!--  <select name="${ResuestParam.client.id}" >
<option disabled selected>下記より顧客を選択</option>
<c:forEach var="client" items="${clients}" varStatus="status">
<option value="${AttributeConst.CLI_ID.getIntegerValue()}" <c:if test="${ResuestParam.client.id == report.client.id}">selected</c:if>>${report.client.name}</option>
</c:forEach>
</select> -->
<br /><br />

<label for="${AttributeConst.REP_PROGRESS.getValue()}">進捗</label><br />
<select name="${AttributeConst.REP_PROGRESS.getValue()}" >
<option disabled selected>進捗を選択</option>
<option value="${AttributeConst.PRG_BEGIN.getIntegerValue()}"<c:if test="${report.progress == AttributeConst.PRG_BEGIN.getIntegerValue()}"> selected</c:if>>1.開始前</option>
<option value="${AttributeConst.PRG_PLAN.getIntegerValue()}"<c:if test="${report.progress == AttributeConst.PRG_PLAN.getIntegerValue()}"> selected</c:if>>2.打合せ予定</option>
<option value="${AttributeConst.PRG_POST.getIntegerValue()}"<c:if test="${report.progress == AttributeConst.PRG_POST.getIntegerValue()}"> selected</c:if>>3.打合せ延期</option>
<option value="${AttributeConst.PRG_DEC.getIntegerValue()}"<c:if test="${report.progress == AttributeConst.PRG_DEC.getIntegerValue()}"> selected</c:if>>4.打合せ確定</option>
<option value="${AttributeConst.PRG_WAIT.getIntegerValue()}"<c:if test="${report.progress == AttributeConst.PRG_WAIT.getIntegerValue()}"> selected</c:if>>5.結果連絡待ち</option>
<option value="${AttributeConst.PRG_END.getIntegerValue()}"<c:if test="${report.progress == AttributeConst.PRG_END.getIntegerValue()}"> selected</c:if>>6.終了(交渉可否無関係)</option>
<option value="${AttributeConst.PRG_SUC.getIntegerValue()}"<c:if test="${report.progress == AttributeConst.PRG_SUC.getIntegerValue()}"> selected</c:if>>7.終了(交渉成功)</option>
<option value="${AttributeConst.PRG_FAIL.getIntegerValue()}"<c:if test="${report.progress == AttributeConst.PRG_FAIL.getIntegerValue()}"> selected</c:if>>8.終了(交渉失敗)</option>
<option value="${AttributeConst.PRG_CANCEL.getIntegerValue()}"<c:if test="${report.progress == AttributeConst.PRG_CANCEL.getIntegerValue()}"> selected</c:if>>9.キャンセル</option>
</select>
<br /><br />

<label for="${AttributeConst.REP_CONTENT.getValue()}">内容</label><br />
<textarea name="${AttributeConst.REP_CONTENT.getValue()}" rows="10" cols="50">${report.content}</textarea>
<br /><br />
<input type="hidden" name="${AttributeConst.REP_ID.getValue()}" value="${report.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">投稿</button>