package com.isban.scf.sid.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DateUtils {
	private static final Logger logger = LogManager.getLogger(DateUtils.class);
	
	/**
	* This method parse an String input date to sqlDate. 
	* If there is an error, the method will return null.
	*
	* @param  date A String that represents a date in the next format yyyy-MM-dd
	* 
	* @return a date in sqlDate format.
	* 
	* @throws ParseException if the date is wrong or can not be parsed, the method return null.
	*/
    public static Date stringToUtilsDate(String date) throws ParseException {
    	try {
	    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date utilDate = formatter.parse(date);
	
			return new Date(utilDate.getTime());
    	} catch (ParseException e) {
    		logger.error(" ERROR in stringToUtilsDate: " + e);
    		throw e;
    	}
    }	
	
    public static Date addDays(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		Date newDate = calendar.getTime();
		
		return newDate;
    }
    
    public static boolean checkDate(String date) {
		return (date != null && date != "") ? true : false;
    }
    
    public static int daysBetween(Date startDate, Date endDate) {
    	int daysBetween;
    	
    	try {
			long diff = 
					endDate.getTime() -
					startDate.getTime();
			
			daysBetween = Math.abs((int) (diff / (1000 * 60 * 60 * 24)));
			
    	} catch (Exception e){
    		logger.warn("The numbers of day exced what int can suport in daysBetween: " + e);
    		daysBetween = -1;
		}
    	
		logger.info(" DAYS:  " + daysBetween);
		
		return daysBetween;
    }

}
