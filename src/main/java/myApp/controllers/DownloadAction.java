package myApp.controllers;

import myApp.model.DataPacket;
import myApp.model.DbConfiguration;
import myApp.rest.DatabaseRequests;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//download from the directory specified in the config file
public class DownloadAction extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        DatabaseRequests.createSchemaAndTableDictionary("MAIN");
        if (DataPacket.readFilesToDictionary("MAIN",
                                             DbConfiguration.getInputDirectory(),
                                             DbConfiguration.getOutputDirectory()))
            return mapping.findForward("success");
        else
            return mapping.findForward("failure");
    }
}
