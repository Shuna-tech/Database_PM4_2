package pm4.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pm4.dal.*;
import pm4.model.CharacterAttributes;
import pm4.model.CharacterInfo;


@WebServlet("/attributes")
public class CharacterAttributesServlet extends HttpServlet {
		
		protected CharacterAttributesDao characterAttributesDao;
		private CharacterInfoDao characterInfoDao;
		protected AttributesDao attributesDao;

		
		
		@Override
		public void init() throws ServletException {
			characterAttributesDao = CharacterAttributesDao.getInstance();
			characterInfoDao = CharacterInfoDao.getInstance();
			attributesDao = AttributesDao.getInstance();
			
		}
		
		@Override
		public void doGet(HttpServletRequest req, HttpServletResponse resp)
		        throws ServletException, IOException {
		    Map<String, String> messages = new HashMap<String, String>();
		    req.setAttribute("messages", messages);
		    
		    List<CharacterAttributes> characterAttributes = new ArrayList<CharacterAttributes>();
		    
		    String characterIDStr = req.getParameter("characterID");
		    
		    try {
		        if (characterIDStr != null && !characterIDStr.trim().isEmpty()) {
		            int characterID = Integer.parseInt(characterIDStr);
		            characterAttributes = characterAttributesDao.getCharacterAttributes(characterID);
		            messages.put("title", "Attributes for Character ID " + characterID);
		        } else {
		            messages.put("title", "Please provide a valid Character ID");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		        throw new IOException(e);
		    }
		    
		    req.setAttribute("characterAttributes", characterAttributes);
		    req.setAttribute("username", req.getParameter("username"));
		    req.getRequestDispatcher("/Attributes.jsp").forward(req, resp);
		}
		}