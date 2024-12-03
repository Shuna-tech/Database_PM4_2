package pm4.servlet;

import pm4.dal.CharacterEquipmentsDao;
import pm4.dal.CharacterInfoDao;
import pm4.dal.EquipmentSlotsDao;
import pm4.model.CharacterEquipments;
import pm4.model.CharacterInfo;
import pm4.model.EquipmentSlots;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CharacterEquipmentServlet")
public class CharacterEquipmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CharacterEquipmentsDao characterEquipmentsDao;
    private CharacterInfoDao characterInfoDao;
    private EquipmentSlotsDao equipmentSlotsDao;

    @Override
    public void init() throws ServletException {
        characterEquipmentsDao = CharacterEquipmentsDao.getInstance();
        characterInfoDao = CharacterInfoDao.getInstance();
        equipmentSlotsDao = EquipmentSlotsDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//for testing: http://localhost:8080/Milestone4/CharacterEquipmentServlet?characterID=1
        int characterId;
        try {
            characterId = Integer.parseInt(request.getParameter("characterID"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid character ID");
            return;
        }

        try {
            // Retrieve the character
            CharacterInfo character = characterInfoDao.getCharactersByCharacterID(characterId);
            if (character == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Character not found");
                return;
            }

            // Retrieve all slots
            List<EquipmentSlots> allSlots = equipmentSlotsDao.getAllSlots();

            // Retrieve character equipment for all slots
            List<CharacterEquipments> equipmentList = new ArrayList<>();
            for (EquipmentSlots slot : allSlots) {
                CharacterEquipments equipment = characterEquipmentsDao.getCharacterEquipmentByCharacterAndSlot(character, slot);
                equipmentList.add(equipment);
            }

            // Pass data to the JSP
            request.setAttribute("character", character);
            request.setAttribute("allSlots", allSlots);
            request.setAttribute("equipmentList", equipmentList);
            request.getRequestDispatcher("/CharacterEquipmentView.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving data");
        }

    }
}