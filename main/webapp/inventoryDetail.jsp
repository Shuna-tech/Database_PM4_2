<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Character Inventory Details</title>
    
</head>
<body>
    <div class="container">
        <div class="character-info">
            <h2>Character Information</h2>
            <p>Character ID: ${character.characterID}</p>
            <p>Player ID: ${character.player.playerID}</p>
            <p>First Name: ${character.firstName}</p>
            <p>Last Name: ${character.lastName}</p>
        </div>

        <div class="inventory-section">
            <h2>Inventory Details</h2>
            <table style="text-align: center; border-spacing: 20px; border-collapse: separate;">
                <thead>
                    <tr>
                        <th>Position</th>
                        <th>Item Name</th>
                        <th>Quantity</th>
                        <th>Unit Price</th>
                        <th>Total Value</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${inventory}" var="item">
                        <tr>
                            <td>${item.stackPosition}</td>
                            <td>${item.items.itemName}</td>
                            <td>${item.stackSize}</td>
                            <td>${item.items.vendorPrice}</td>
                            <td>${item.stackSize * item.items.vendorPrice}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty inventory}">
                        <tr>
                            <td colspan="5">No items found in inventory.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
        <a href="findplayers?username=${param.searchUsername}" >Back to Search Result</a>
    </div>
</body>

</html>