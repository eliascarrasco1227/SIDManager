package com.isban.scf.sid.utils;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DateRange {
	private static final Logger logger = LogManager.getLogger(DateRange.class);
	
    private final Date startDate;
    private final  Date endDate;

    public DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateRange(int daysAgo) {
    	this.startDate = DateUtils.addDays(new Date(), daysAgo);
        this.endDate = new Date();
        logger.info(" Returning FILTERED documents for last " + daysAgo + " days");
	}

	public DateRange(Date date, int daysDifference) {
		if (daysDifference < 0) {
			this.endDate = date;
			this.startDate = DateUtils.addDays(date, daysDifference);
			logger.info(" startDate: " + this.startDate + " .");
		} else {
			this.startDate = date;
			this.endDate = DateUtils.addDays(date, daysDifference);
		}
	}

	public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}

