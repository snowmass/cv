package cv.repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cv.representation.ExperimentalSets;

public class DocumentRepository extends BaseRepository
{
    private static DocumentRepository documentRepositorySingleton = new DocumentRepository();
    
    private DocumentRepository()
    {
        return;
    }
    
    public static DocumentRepository instance()
    {
        return documentRepositorySingleton;
    }
    
    public ExperimentalSets getExperimentalSets(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        ExperimentalSets experimentalSets = (ExperimentalSets) session.getAttribute("ExperimentalSets");
        if (null == experimentalSets)
            throw new RuntimeException("No ExperimentalSets found in user's session");
        return experimentalSets;
    }

    public void setExperimentalSets(HttpServletRequest request, ExperimentalSets experimentalSets)
    {
        HttpSession session = request.getSession();
        session.setAttribute("ExperimentalSets", experimentalSets);
        return;
    }

}
