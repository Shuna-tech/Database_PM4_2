package pm4.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pm4.dal.*;
import pm4.model.*;

@WebServlet("/inventoryDetail")
public class InventoryDetailServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   
   protected CharacterInfoDao characterInfoDao;
   protected InventoryPositionsDao inventoryPositionsDao;
   protected ItemsDao itemsDao;
   protected ConnectionManager connectionManager;
   
   @Override
   public void init() throws ServletException {
       characterInfoDao = CharacterInfoDao.getInstance();
       inventoryPositionsDao = InventoryPositionsDao.getInstance();
       itemsDao = ItemsDao.getInstance();
       connectionManager = new ConnectionManager();
   }
   
   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp)
           throws ServletException, IOException {
       // Get character ID from request parameters
       String characterIdStr = req.getParameter("characterID");
       
       if (characterIdStr == null || characterIdStr.trim().isEmpty()) {
           resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Character ID is required.");
           return;
       }
       
       try {
           int characterId = Integer.parseInt(characterIdStr);
           
           // Get character info
           CharacterInfo character = characterInfoDao.getCharactersByCharacterID(characterId);
           if (character == null) {
               resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Character not found.");
               return;
           }
           
           // Get inventory positions using existing methods
           List<InventoryPositions> allInventory = new ArrayList<>();
           List<Integer> characterItemIds = getCharacterItemIds(characterId);
           for (Integer itemId : characterItemIds) {
               List<InventoryPositions> itemInventory = inventoryPositionsDao.getInventoryPositions(itemId);
               // Filter for only this character's positions
               for (InventoryPositions pos : itemInventory) {
                   if (pos.getCharacterInfo().getCharacterID() == characterId) {
                       allInventory.add(pos);
                   }
               }
           }
           
           // Set attributes for JSP
           req.setAttribute("character", character);
           req.setAttribute("inventory", allInventory);
           
           // Forward to detail.jsp
           req.getRequestDispatcher("/inventoryDetail.jsp").forward(req, resp);
           
       } catch (NumberFormatException e) {
           resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid character ID format.");
       } catch (SQLException e) {
           e.printStackTrace();
           resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error accessing database.");
       }
   }
   
   private List<Integer> getCharacterItemIds(int characterId) throws SQLException {
       List<Integer> itemIds = new ArrayList<>();
       String selectQuery = "SELECT DISTINCT itemID FROM InventoryPositions WHERE characterID = ?";
       
       Connection connection = null;
       PreparedStatement selectStmt = null;
       ResultSet results = null;
       
       try {
           connection = connectionManager.getConnection();
           selectStmt = connection.prepareStatement(selectQuery);
           selectStmt.setInt(1, characterId);
           results = selectStmt.executeQuery();
           
           while(results.next()) {
               itemIds.add(results.getInt("itemID"));
           }
       } finally {
           if(results != null) results.close();
           if(selectStmt != null) selectStmt.close();
           if(connection != null) connection.close();
       }
       return itemIds;
   }
}