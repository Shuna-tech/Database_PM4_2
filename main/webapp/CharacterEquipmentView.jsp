<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="pm4.model.CharacterEquipments" %>
<%@ page import="pm4.model.CharacterInfo" %>
<%@ page import="pm4.model.EquipmentSlots" %>
<%@ page import="pm4.model.EquippableItems" %>
<html>
<head>
    <title>Character Equipment</title>
</head>
<body>
    <h1>Character Equipment</h1>

    <h2>Character Info</h2>
    <%
        CharacterInfo character = (CharacterInfo) request.getAttribute("character");
        List<EquipmentSlots> allSlots = (List<EquipmentSlots>) request.getAttribute("allSlots");
        List<CharacterEquipments> equipmentList = (List<CharacterEquipments>) request.getAttribute("equipmentList");
    %>
    <p>First Name: <%= character != null ? character.getFirstName() : "Not Found" %></p>
    <p>Last Name: <%= character != null ? character.getLastName() : "Not Found" %></p>
    

    <h2>Equipment</h2>
    <table border="1">
        <thead>
            <tr>
                <th>Slot</th>
                <th>Item</th>
            </tr>
        </thead>
        <tbody>
        <%
            if (allSlots != null && !allSlots.isEmpty()) {
                for (EquipmentSlots slot : allSlots) {
                    EquippableItems item = null;

                    // Check if there is equipment for this slot
                    if (equipmentList != null) {
                        for (CharacterEquipments equipment : equipmentList) {
                            if (equipment != null && equipment.getSlot() != null &&
                                equipment.getSlot().getSlotID() == slot.getSlotID()) {
                                item = equipment.getItem();
                                break; // Found equipment, no need to continue
                            }
                        }
                    }
        %>
                    <tr>
                        <td><%= slot.getSlotName() %></td>
                        <td><%= item != null ? item.getItemName() : "" %></td>
                    </tr>
        <%
                }
            } else {
        %>
                <tr>
                    <td colspan="2">No slots available.</td>
                </tr>
        <%
            }
        %>
        </tbody>
    </table>
</body>
 <a href="findplayers?username=${param.searchUsername}" >Back to Search Result</a>
</html>