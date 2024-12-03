<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Character Attributes</title>
</head>
<body>
<h1>Character Info</h1>
<p>First Name: ${characterAttributes[0].characterInfo.firstName}</p>
<p>Last Name: ${characterAttributes[0].characterInfo.lastName}</p>
<h1>Attributes</h1>
<table border="1">
    <tr>
        <th>Attribute Name</th>
        <th>Attribute Value</th>
    </tr>
    <c:forEach items="${characterAttributes}" var="attribute" >
        <tr>
            <td><c:out value="${attribute.attributes.attributeName}" /></td>
            <td><c:out value="${attribute.attributeValue}" /></td>
        </tr>
    </c:forEach>
</table>
</body>
 <a href="findplayers?username=${param.searchUsername}" >Back to Search Result</a>
</html>