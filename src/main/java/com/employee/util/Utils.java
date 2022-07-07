package com.employee.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Utils {

	public static String getCurrentTimeStamp() {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		return dtf.format(now);
	}
	
	public static long getDays(String createdDate) throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
	    Date date1 = sdf.parse(createdDate);
	    
	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		
	    Date date2 = sdf.parse(dtf.format(now));
	    
	    long timeDiff = date2.getTime() - date1.getTime();
	    long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
	    
	    return daysDiff;
	}
}
