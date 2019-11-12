package myApp.controllers;

import myApp.rest.DatabaseRequests;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//delete all notes from TEST.T_DICTIONARY table
public class DeleteAction extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DatabaseRequests.truncTable("MAIN.T_DICTIONARY");
        return mapping.findForward("main");
    }
}
