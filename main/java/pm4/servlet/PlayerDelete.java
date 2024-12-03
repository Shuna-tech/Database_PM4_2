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

@WebServlet("/playerdelete")
public class PlayerDelete extends HttpServlet {
    protected PlayersDao playersDao;

    @Override
    public void init() throws ServletException {
        playersDao = PlayersDao.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<>();
        req.setAttribute("messages", messages);
        // Preserve the search parameter
        String searchUsername = req.getParameter("searchUsername");
        req.setAttribute("searchUsername", searchUsername);
        req.getRequestDispatcher("/DeletePlayer.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<>();
        req.setAttribute("messages", messages);
        // Preserve the search parameter 
        String searchUsername = req.getParameter("searchUsername");
        req.setAttribute("searchUsername", searchUsername);

        String username = req.getParameter("username");
        if (username == null || username.trim().isEmpty()) {
            messages.put("success", "Invalid Username");
        } else {
            try {
                Players player = playersDao.getPlayerFromUserName(username);
                if (player == null) {
                    messages.put("success", "Player " + username + " does not exist");
                } else {
                    Players result = playersDao.delete(player);
                    if (result == null) {
                        messages.put("success", "Successfully deleted " + username);
                    } else {
                        messages.put("success", "Failed to delete " + username);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        }
        req.getRequestDispatcher("/DeletePlayer.jsp").forward(req, resp);
    }
}


//@WebServlet("/playerdelete")
//public class PlayerDelete extends HttpServlet {
//    protected PlayersDao playersDao;
//
//    @Override
//    public void init() throws ServletException {
//        playersDao = PlayersDao.getInstance();
//    }
//    
//
//
//    @Override
//    public void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//        Map<String, String> messages = new HashMap<String, String>();
//        req.setAttribute("messages", messages);
//        req.getRequestDispatcher("/DeletePlayer.jsp").forward(req, resp);
//    }
//
//    @Override
//    public void doPost(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//        Map<String, String> messages = new HashMap<String, String>();
//        req.setAttribute("messages", messages);
//
//        String username = req.getParameter("username");
//        String searchUsername = req.getParameter("searchUsername");
//        if (username == null || username.trim().isEmpty()) {
//            messages.put("success", "Invalid Username");
//        } else {
//            try {
//                Players player = playersDao.getPlayerFromUserName(username);
//                if (player == null) {
//                    messages.put("success", "Player " + username + " does not exist");
//                } else {
//                    Players result = playersDao.delete(player);
//                    if (result == null) {
//                        messages.put("success", "Successfully deleted " + username);
//                    } else {
//                        messages.put("success", "Failed to delete " + username);
//                    }
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                throw new IOException(e);
//            }
//        }
//
//        req.getRequestDispatcher("/DeletePlayer.jsp").forward(req, resp);
//    }
//}
