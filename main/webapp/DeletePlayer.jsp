<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Delete a Player</title>
</head>
<body>
    <h1>Delete Player</h1>
    <form action="playerdelete" method="post">
        <p>
            <label for="username">UserName</label>
            <input id="username" name="username" value="">
        </p>
        <p>
            <input type="submit">
        </p>
 <%--              <input type="hidden" name="searchUsername" value="${searchUsername}"> --%>
    </form>
    <br/><br/>
    <p>
        <span id="successMessage"><b>${messages.success}</b></span>
    </p>
</body>
<a href="findplayers?username=${searchUsername}">Back to Search Result</a>
</html>