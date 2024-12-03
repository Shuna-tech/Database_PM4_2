package pm4.servlet;

import pm4.dal.*;
import pm4.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.util.StringUtils;

@WebServlet("/findplayers")
public class FindPlayers extends HttpServlet {
	protected PlayersDao playersDao;
	protected CharacterInfoDao characterInfoDao;
	protected CharacterJobsDao characterJobsDao;
   
	 @Override
	   public void init() throws ServletException {
	       playersDao = PlayersDao.getInstance();
	       characterInfoDao = CharacterInfoDao.getInstance();
	       characterJobsDao = CharacterJobsDao.getInstance();
	   }
	   
	   private String validateSortOrder(String parameter, Map<String, String> messages) {
	
		   if (!StringUtils.isNullOrEmpty(parameter)) {
			   String upperParam = parameter.toUpperCase();
		       if (upperParam.equals("ASC") || upperParam.equals("DESC")) {
		           return upperParam;
		       }
		       messages.put("sortMessage", "Invalid sort order. Please enter ASC or DESC.");
		   }    
	       return null;
	   }

	   private String validateSortColumn(String sortBy) {
	       if (sortBy == null || sortBy.trim().isEmpty()) return null;
	       String column = sortBy.toLowerCase().trim();
	       if (column.equals("name") || column.equals("player") || 
	           column.equals("job") || column.equals("hp") || 
	           column.equals("level")) {
	           return column;
	       }
	       return null;
	   }
	   
	   @Override
	   public void doGet(HttpServletRequest req, HttpServletResponse resp)
	          throws ServletException, IOException {
	      Map<String, String> messages = new HashMap<String, String>();
	      req.setAttribute("messages", messages);

	      List<Players> players = new ArrayList<Players>();
	      
	      String userName = req.getParameter("username");
	      if (userName == null || userName.trim().isEmpty()) {
	          messages.put("success", "Please enter a valid username.");
	      } else {
	          try {
	              players = playersDao.getPlayersByPartialUsername(userName);
	              messages.put("success", "Displaying results for " + userName);
	          } catch (SQLException e) {
	              e.printStackTrace();
	              throw new IOException(e);
	          }
	      }
	      req.setAttribute("players", players);
	      
	      List<CharacterInfo> characters = new ArrayList<>();
	      Map<CharacterInfo, CharacterJobs> characterInfoJobs = new HashMap<>();
	      try {
	          for (Players player : players) {
	              List<CharacterInfo> playerCharacters = characterInfoDao.getCharactersByPlayerID(player.getPlayerID());
	              for (CharacterInfo character : playerCharacters) {
	                  characters.add(character);
	                  List<CharacterJobs> jobs = characterJobsDao.getCharacterJobsByCharacter(character);
	                  for (CharacterJobs job : jobs) {
	                      if (job.isCurrentJob()) {
	                          characterInfoJobs.put(character, job);
	                          break;
	                      }
	                  }
	              }
	          }
	      } catch (SQLException e) {
	          e.printStackTrace();
	          throw new IOException(e);
	      }
	      
	      String sortBy = validateSortColumn(req.getParameter("sortBy"));
	      String sortOrder = validateSortOrder(req.getParameter("sortOrder"), messages);

	      if (sortBy != null && sortOrder != null) {
	          if (sortBy.equals("name")) {
	              Collections.sort(characters, new Comparator<CharacterInfo>() {
	                  @Override
	                  public int compare(CharacterInfo c1, CharacterInfo c2) {
	                      String name1 = c1.getFirstName() + " " + c1.getLastName();
	                      String name2 = c2.getFirstName() + " " + c2.getLastName();
	                      return sortOrder.equals("DESC") ? name2.compareTo(name1) : name1.compareTo(name2);
	                  }
	              });
	          } else if (sortBy.equals("player")) {
	              Collections.sort(characters, new Comparator<CharacterInfo>() {
	                  @Override
	                  public int compare(CharacterInfo c1, CharacterInfo c2) {
	                      return sortOrder.equals("DESC") ? 
	                          c2.getPlayer().getUserName().compareTo(c1.getPlayer().getUserName()) :
	                          c1.getPlayer().getUserName().compareTo(c2.getPlayer().getUserName());
	                  }
	              });
	          } else if (sortBy.equals("job")) {
	              Collections.sort(characters, new Comparator<CharacterInfo>() {
	                  @Override
	                  public int compare(CharacterInfo c1, CharacterInfo c2) {
	                      CharacterJobs job1 = characterInfoJobs.get(c1);
	                      CharacterJobs job2 = characterInfoJobs.get(c2);
	                      if (job1 == null || job2 == null) return 0;
	                      return sortOrder.equals("DESC") ?
	                          job2.getJob().getJobName().compareTo(job1.getJob().getJobName()) :
	                          job1.getJob().getJobName().compareTo(job2.getJob().getJobName());
	                  }
	              });
	          } else if (sortBy.equals("hp")) {
	              Collections.sort(characters, new Comparator<CharacterInfo>() {
	                  @Override
	                  public int compare(CharacterInfo c1, CharacterInfo c2) {
	                      return sortOrder.equals("DESC") ?
	                          Integer.compare(c2.getMaxHP(), c1.getMaxHP()) :
	                          Integer.compare(c1.getMaxHP(), c2.getMaxHP());
	                  }
	              });
	          } else if (sortBy.equals("level")) {
	              Collections.sort(characters, new Comparator<CharacterInfo>() {
	                  @Override
	                  public int compare(CharacterInfo c1, CharacterInfo c2) {
	                      CharacterJobs job1 = characterInfoJobs.get(c1);
	                      CharacterJobs job2 = characterInfoJobs.get(c2);
	                      if (job1 == null || job2 == null) return 0;
	                      return sortOrder.equals("DESC") ?
	                          Integer.compare(job2.getLevel(), job1.getLevel()) :
	                          Integer.compare(job1.getLevel(), job2.getLevel());
	                  }
	              });
	          }
	      }
	      
	      req.setAttribute("characters", characters);
	      req.setAttribute("characterJobs", characterInfoJobs);
	      
	      req.getRequestDispatcher("/FindPlayers.jsp").forward(req, resp);
	   }


   @Override
   public void doPost(HttpServletRequest req, HttpServletResponse resp)
           throws ServletException, IOException {
       Map<String, String> messages = new HashMap<String, String>();
       req.setAttribute("messages", messages);

       List<Players> players = new ArrayList<Players>();
       
       String userName = req.getParameter("username");
       if (userName == null || userName.trim().isEmpty()) {
           messages.put("success", "Please enter a valid username.");
       } else {
           try {
               players = playersDao.getPlayersByPartialUsername(userName);
               messages.put("success", "Displaying results for " + userName);
           } catch (SQLException e) {
               e.printStackTrace();
               throw new IOException(e);
           }
       }
       req.setAttribute("players", players);
       
       List<CharacterInfo> characters = new ArrayList<>();
       Map<CharacterInfo, CharacterJobs> characterInfoJobs = new HashMap<>();
       try {
           for (Players player : players) {
               List<CharacterInfo> playerCharacters = characterInfoDao.getCharactersByPlayerID(player.getPlayerID());
               for (CharacterInfo character : playerCharacters) {
                   characters.add(character);
                   List<CharacterJobs> jobs = characterJobsDao.getCharacterJobsByCharacter(character);
                   for (CharacterJobs job : jobs) {
                       if (job.isCurrentJob()) {
                           characterInfoJobs.put(character, job);
                           break;
                       }
                   }
               }
           }
       } catch (SQLException e) {
           e.printStackTrace();
           throw new IOException(e);
       }
       req.setAttribute("characters", characters);
       req.setAttribute("characterJobs", characterInfoJobs);
       
       req.getRequestDispatcher("/FindPlayers.jsp").forward(req, resp);
   }
}