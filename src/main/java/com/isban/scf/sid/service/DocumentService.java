package com.isban.scf.sid.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.isban.scf.sid.enumerated.ResponseEnum;
import com.isban.scf.sid.model.Document;
import com.isban.scf.sid.repository.DocumentRepository;
import com.isban.scf.sid.utils.DateRange;
import com.isban.scf.sid.utils.DateUtils;

import java.text.ParseException;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@Service
public class DocumentService {
	
	private static final Logger logger = LogManager.getLogger(DocumentService.class);

	@Autowired
    private DocumentRepository documentRepository;

	/**
	* This method return a list of documents calling findAllDocuments.
	* 
	* @return a list of documents of the database in List Document format.
	*/
    
    public List<Document> getAllDocuments() {
    	logger.debug(" Entering findAllDocuments ");
        return documentRepository.findAll();
    }
    
    
	/**
	* This method return a list of documents filtered with
	* start and end dates calling findFilteredDocuments.
	*
	* @param  startDate A Date which filters removing dates before itself.
	* @param  endDate A Date which filters removing dates after itself.
	* 
	* @return a list of documents of the database in List Document format.
	*/
    public List<Document> getFilteredDocuments(Date startDate, Date endDate) {
    	logger.debug(" Entering getFilteredDocuments ");
        return documentRepository.findFilteredDocuments(startDate, endDate);
    }
    
    public List<Document> getNotReceivedDocuments() {
        logger.debug(" Entering getNotReceivedDocuments ");
        return documentRepository.findByReceivedAtIsNull();
    }
    
    public DateRange checkDates(String stringStartDate, String stringEndDate, Model model) {
		final int daysRange = 10;  
		DateRange dateRange;
		boolean isCorrectRange = true;
		
		try {
			if (DateUtils.checkDate(stringStartDate) && DateUtils.checkDate(stringEndDate)) {
				Date startDate = DateUtils.stringToUtilsDate(stringStartDate);
				Date endDate = DateUtils.stringToUtilsDate(stringEndDate);
				
	    		int daysBetween = DateUtils.daysBetween(startDate, endDate);
	    		
	    		if(daysBetween != -1 && daysBetween <= daysRange) {
		    		dateRange = new DateRange(startDate, endDate);
		    		logger.info(" Returning FILTERED documents ");
	    		} else {
	    			dateRange = new DateRange(-daysRange);
	    			
	    			logger.warn(" Exceded maximum day range ( " +
				    			daysBetween + 
				    			" ) with: " + 
				    			daysRange);
	    			logger.warn(" Returning last 10 days ");
	    			
	    			isCorrectRange = false;
	    		}
			} else if (DateUtils.checkDate(stringEndDate)) {
				dateRange = new DateRange(DateUtils.stringToUtilsDate(stringEndDate), -daysRange);
				isCorrectRange = false;
			} else if (DateUtils.checkDate(stringStartDate)) {
				dateRange = new DateRange(DateUtils.stringToUtilsDate(stringStartDate), daysRange);
				isCorrectRange = false;
			} else {
	    		dateRange = new DateRange(-daysRange);
	    	}
		} catch (ParseException e) {
    		logger.error(" ERROR in getFilteredDocuments: " + e);
    		dateRange = new DateRange(-daysRange);			
    	}
		
		if (!isCorrectRange) {
			model.addAttribute("responseMessage", ResponseEnum.LIST_WARNING_1.getMessageKey());
			model.addAttribute("isAWarningMessage", true);
		}
		
		return dateRange;
	}

}
