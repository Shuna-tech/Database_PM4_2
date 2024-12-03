package pm4.servlet;

import pm4.dal.*;
import pm4.model.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/playercreate")
public class PlayerCreate extends HttpServlet {
    protected PlayersDao playersDao;
    
    @Override
    public void init() throws ServletException {
        playersDao = PlayersDao.getInstance();
    }
    
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);
        req.getRequestDispatcher("/CreatePlayer.jsp").forward(req, resp);
    }
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        
        if (username == null || username.trim().isEmpty()) {
            messages.put("success", "Invalid Username");
        } else {
            try {
                Players existingPlayer = playersDao.getPlayerByEmail(email);
                if (existingPlayer != null) {
                    messages.put("success", "Creation failed - Email " + email + " already exists");
                } else {
                    Players player = new Players(username, email);
                    player = playersDao.create(player);
                    messages.put("success", "Successfully created " + username);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        }
        
        req.getRequestDispatcher("/CreatePlayer.jsp").forward(req, resp);
    }
}