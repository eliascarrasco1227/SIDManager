package com.isban.scf.sid.controller;

import java.text.ParseException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.isban.scf.sid.service.DocumentService;
import com.isban.scf.sid.utils.DateRange;
import com.isban.scf.sid.utils.DateUtils;

@Controller
public class DocumentController {
	
	private static final Logger logger = LogManager.getLogger(DocumentController.class);

	@Autowired
    private DocumentService documentService;
	
	/**
	* This method add to spring boot model a list of documents 
	* and return a view (HTML template) with the documents.
	*
	* @param  model A Model from spring boot used to storage the document list
	* 
	* @return a view (HTML template) in String format with the documents in a data table.
	*/

    @GetMapping()
    public String getAllDocuments(Model model) {
    	logger.debug(" ----- Entering getAllDocuments ----- ");
        model.addAttribute("documents", documentService.getAllDocuments());
        logger.debug(" ----- Exiting getAllDocuments ----- ");
        logger.debug(" ____________________________________ ");
        
        return "documentsView"; // View name (documentsView.html)
    }
  
    
	/**
	* This method add to spring boot model a list of documents 
	* filtered with start and end dates and return a view (HTML template)
	* with the documents.
	* If one date is not valid return all documents.
	*
	* @param  startDate A Date which filters removing dates before itself.
	* @param  endDate A Date which filters removing dates after itself.
	* @param  model A Model from spring boot used to storage the document list
	* 
	* @return a view (HTML template) in String format with the documents in a data table.
	 * @throws Exception 
	*/
	
    @GetMapping("/filter")
    public String getFilteredDocuments(@RequestParam("start") String startDate,
							    	   @RequestParam("end") String endDate,
							    	   Model model) throws Exception {
    	
    	logger.debug(" ----- Entering getFilteredDocuments ----- ");
    	try {
    		Date sqlStartDate = DateUtils.stringToUtilsDate(startDate);
    		Date sqlEndDate = DateUtils.stringToUtilsDate(endDate);
    		
    		if(sqlStartDate != null && sqlEndDate != null) {
    			model.addAttribute("documents", documentService.getFilteredDocuments(sqlStartDate,sqlEndDate));
    			logger.info(" Returning FILTERED documents ");
    		} else {
    			//If there is a no valid date, return all documents
    			model.addAttribute("documents", documentService.getAllDocuments());
    			logger.info(" Returning ALL documents ");
    		}
    		
    	} catch (ParseException e) {
    		logger.error(" ERROR in getFilteredDocuments: " + e);
    	}
    	logger.debug(" ----- Exiting getFilteredDocuments ----- ");
    	logger.debug(" ____________________________________ ");
        
        return "documentsView"; // View name (documentsView.html)
    }
    
    
    @GetMapping("/not-received")
    public String getNotReceivedDocuments(Model model) {
        logger.debug(" ----- Entering getNotReceivedDocuments ----- ");
        model.addAttribute("documents", documentService.getNotReceivedDocuments());
        model.addAttribute("isShowingUnsaveds", "Yes");
        logger.debug(" ----- Exiting getNotReceivedDocuments ----- ");
        logger.debug(" ____________________________________ ");
        
        return "documentsView"; // Nombre de la vista (documentsView.html)
    }
    
    /**
	* This method add to spring boot model a list of documents 
	* filtered with start and end dates and return a view (HTML template)
	* with the documents.
	* If one date is not valid return last 10 days.
	*
	* @param  startDate A Date which filters removing dates before itself.
	* @param  endDate A Date which filters removing dates after itself.
	* @param  model A Model from spring boot used to storage the document list
	* 
	* @return a view (HTML template) in String format with the documents in a data table.
     * @throws Exception 
	*/
    @GetMapping("/list")
    public String listDocuments(
    		@RequestParam(value = "min-date",  required = false) String startDate,
			@RequestParam(value = "max-date", required = false) String endDate,
			Model model) throws Exception {
    	logger.debug(" ----- Entering listDocuments ----- ");
    	
    	DateRange dateRange = documentService.checkDates(startDate, endDate, model);
    	
    	model.addAttribute("documents", 
    			documentService.getFilteredDocuments(
    					dateRange.getStartDate(),
    					dateRange.getEndDate()
    					));    	
    	model.addAttribute("startDate", dateRange.getStartDate());
		model.addAttribute("endDate", dateRange.getEndDate());
		model.addAttribute("isShowingUnsaveds", "No");
    	
    	logger.debug(" ----- Exiting listDocuments ----- ");
    	logger.debug(" ____________________________________ ");
	        
        return "documentsView";
    }

}


