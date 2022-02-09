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

<label for="${AttributeConst.REP_CLIENT.getValue()}">顧客名(取引先名)</label><br />
<textarea name="${AttributeConst.REP_CLIENT.getValue()}" rows="3" cols="20">${report.client}</textarea>
<br /><br />

<label for="${AttributeConst.REP_CLIENT.getValue()}">進捗</label><br />
<select name="下記より選択">
<option value = "開始前">開始前</option>
<option value = "打合せ予定">打合せ予定</option>
<option value = "打合せ延期">打合せ延期</option>
<option value = "打合せ確定">打合せ確定</option>
<option value = "結果連絡待ち">結果連絡待ち</option>
<option value = "終了(交渉可否無関係)">終了(交渉可否無関係)</option>
<option value = "終了(交渉成功)">終了(交渉成功)</option>
<option value = "終了(交渉失敗)">終了(交渉失敗)</option>
<option value = "キャンセル">キャンセル</option>
</select>
<br /><br />

<label for="${AttributeConst.REP_CONTENT.getValue()}">内容</label><br />
<textarea name="${AttributeConst.REP_CONTENT.getValue()}" rows="10" cols="50">${report.content}</textarea>
<br /><br />
<input type="hidden" name="${AttributeConst.REP_ID.getValue()}" value="${report.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">投稿</button>