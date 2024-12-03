<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Update a Player</title>
</head>
<body>
    <h1>Update Player</h1>
    <form action="playerupdate" method="post">
        <p>
            <label for="oldUsername">Current UserName</label>
            <input id="oldUsername" name="oldUsername" value="">
        </p>
        <p>
            <label for="newUsername">New UserName</label>
            <input id="newUsername" name="newUsername" value="">
        </p>
        <p>
            <input type="submit">
        </p>
    </form>
    <br/><br/>
    <p>
        <span id="successMessage"><b>${messages.success}</b></span>
    </p>
</body>
 <a href="findplayers?username=${param.searchUsername}" >Back to Search Result</a>
</html>