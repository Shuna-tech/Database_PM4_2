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

@WebServlet("/characterjobupdate")
public class CharacterJobUpdate extends HttpServlet {
    protected CharacterJobsDao characterJobsDao;
    protected CharacterInfoDao characterInfoDao;
    protected JobsDao jobsDao;

    @Override
    public void init() throws ServletException {
        characterJobsDao = CharacterJobsDao.getInstance();
        characterInfoDao = CharacterInfoDao.getInstance();
        jobsDao = JobsDao.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);
        req.getRequestDispatcher("/CharacterJobUpdate.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        String characterId = req.getParameter("characterId");
        String jobId = req.getParameter("jobId");
        String newLevel = req.getParameter("newLevel");
        String newExp = req.getParameter("newExp");

        if (characterId == null || characterId.trim().isEmpty() ||
                jobId == null || jobId.trim().isEmpty() ||
                newLevel == null || newLevel.trim().isEmpty() ||
                newExp == null || newExp.trim().isEmpty()) {
            messages.put("success", "Please fill in all fields");
        } else {
            try {
                CharacterInfo character = characterInfoDao.getCharactersByCharacterID(Integer.parseInt(characterId));
                if (character == null) {
                    messages.put("success", "Character does not exist");
                    return;
                }

                Jobs job = jobsDao.getJobById(Integer.parseInt(jobId));
                if (job == null) {
                    messages.put("success", "Job does not exist");
                    return;
                }

                CharacterJobs characterJob = characterJobsDao.getCharacterJobByCharacterAndJob(character, job);
                if (characterJob == null) {
                    messages.put("success", "Character job combination does not exist");
                } else {
                    characterJob = characterJobsDao.updateLevelAndExperience(
                            characterJob,
                            Integer.parseInt(newLevel),
                            Integer.parseInt(newExp));

                    if (characterJob != null) {
                        messages.put("success", "Successfully updated character job level and experience");
                    } else {
                        messages.put("success", "Failed to update character job");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        }

        req.getRequestDispatcher("/CharacterJobUpdate.jsp").forward(req, resp);
    }
}