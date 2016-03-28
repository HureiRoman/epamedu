package edu.epam.persistance;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeToText {
	
	
	public static String timeToText(Date time){
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(time);
		SimpleDateFormat datePartSdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timePartSdf = new SimpleDateFormat("HH:mm:ss");
		String datePart;
		
		if(isToday(calendar)){
			datePart = "Today";
		}else if (isYesterday(calendar) ){
			datePart = "Yesterday";
		}else{
			datePart = datePartSdf.format(time.getTime());
		}		
		String resultTimeText = datePart  + " at " + timePartSdf.format(time.getTime());
		return resultTimeText;		
	}

	private static boolean isToday(Calendar time) {
		Calendar yesterdayDate = Calendar.getInstance(); 
		return 	(yesterdayDate.get(Calendar.YEAR) == time.get(Calendar.YEAR)
				  && yesterdayDate.get(Calendar.DAY_OF_YEAR) == time.get(Calendar.DAY_OF_YEAR));
	}

	private static boolean isYesterday(Calendar time) {
		Calendar yesterdayDate = Calendar.getInstance(); 
		yesterdayDate.add(Calendar.DAY_OF_YEAR, -1); 		
		return 	(yesterdayDate.get(Calendar.YEAR) == time.get(Calendar.YEAR)
				  && yesterdayDate.get(Calendar.DAY_OF_YEAR) == time.get(Calendar.DAY_OF_YEAR));
	}
	
	
	
	

}
