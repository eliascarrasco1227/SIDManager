package com.isban.scf.sid.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.isban.scf.sid.model.Document;
import com.isban.scf.sid.service.DocumentService;
import com.isban.scf.sid.utils.DateRange;
import com.isban.scf.sid.utils.DateUtils;

@RequestMapping("/rest")
@Controller
public class DocumentRestController {
	
	private static final Logger logger = LogManager.getLogger(DocumentRestController.class);

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
    public ResponseEntity<List<Document>> getAllDocuments(Model model) {
    	logger.debug(" ----- Entering getAllDocuments ----- ");
    	
        return new ResponseEntity<List<Document>>
			(documentService.getAllDocuments(), HttpStatus.OK); 
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
    public ResponseEntity<List<Document>> getFilteredDocuments(@RequestParam("start") String startDate,
							    	   @RequestParam("end") String endDate,
							    	   Model model) throws Exception {
    	ResponseEntity<List<Document>> documents = new ResponseEntity<List<Document>>
			(documentService.getAllDocuments(), HttpStatus.OK); 
    	
    	logger.debug(" ----- Entering getFilteredDocuments ----- ");
    	
    	try {
    		Date sqlStartDate = DateUtils.stringToUtilsDate(startDate);
    		Date sqlEndDate = DateUtils.stringToUtilsDate(endDate);
    		
    		if(sqlStartDate != null && sqlEndDate != null) {
    			documents = new ResponseEntity<List<Document>>
    				(documentService.getFilteredDocuments(sqlStartDate,sqlEndDate), HttpStatus.OK);
    			logger.info(" Returning FILTERED documents ");
    		} else {
    			//If there is a no valid date, return all documents
    			documents = new ResponseEntity<List<Document>>
    				(documentService.getAllDocuments(), HttpStatus.OK);
    			logger.info(" Returning ALL documents ");
    		}
    	} catch (ParseException e) {
    		logger.error(" ERROR in getFilteredDocuments: " + e);
    	}
    	
    	logger.debug(" ----- Exiting getFilteredDocuments ----- ");
    	logger.debug(" ____________________________________ ");
        
        return documents; // View name (documentsView.html)
    }
    
    @GetMapping("/not-received")
    public String getNotReceivedDocuments(Model model) {
        logger.debug(" ----- Entering getNotReceivedDocuments ----- ");
        model.addAttribute("documents", documentService.getNotReceivedDocuments());
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
	*/
    /*
    @GetMapping("/list")
    public String listDocuments(
    		@RequestParam(value = "min-date",  required = false) String startDate,
			@RequestParam(value = "max-date", required = false) String endDate,
			Model model) {
    	logger.debug(" ----- Entering listDocuments ----- ");
    	
    	DateRange dateRange = documentService.checkDates(startDate, endDate, model);
    	
    	model.addAttribute("documents", 
    			documentService.getFilteredDocuments(
    					dateRange.getStartDate(),
    					dateRange.getEndDate()
    					));    	
    	model.addAttribute("startDate", dateRange.getStartDate());
		model.addAttribute("endDate", dateRange.getEndDate());
    	
    	logger.debug(" ----- Exiting listDocuments ----- ");
    	logger.debug(" ____________________________________ ");
    	//List<Document> documents = 
    	
    	 //ResponseEntity<List<Document>> documents = new ResponseEntity<List<Document>>(documentService.getAllDocuments(), HttpStatus.OK);
	        
        return "documentsView";
    }
    */
}


