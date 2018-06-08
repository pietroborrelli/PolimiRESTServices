package it.polimi.rest.util;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import it.polimi.rest.domain.Register;
import it.polimi.rest.domain.response.SummaryConsumption;

public final class CustomUtil {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	private static TimeZone tz = TimeZone.getDefault(); ;
	

	/*
	 * Convert dates to local timezone : Register
	 * 
	 */
	public static List<Register> convertToLocalTimeZone(List<Register> registers){
		
		sdf.setTimeZone(tz);
		
		for (Register register : registers) {
			
			register.setLocalTimestamp(sdf.format(register.getTimestamp()));
		  
		}
		
		return registers;
	}
	
	/*
	 * Convert dates to local timezone : Register
	 * 
	 */
	public static SummaryConsumption convertToLocalTimeZone(SummaryConsumption summaryConsumption){
		
		sdf.setTimeZone(tz);
		
		summaryConsumption.setLocalStartDate(sdf.format(summaryConsumption.getStartDate()));
		summaryConsumption.setLocalEndDate(sdf.format(summaryConsumption.getEndDate()));
		
		return summaryConsumption;
	}
	
	/*
	 * Convert dates to unix timestamp : Register
	 * 
	 */
	public static List<Register> convertToUnixTimestamp(List<Register> registers){
		
		
		for (Register register : registers) {
			
			register.setUnixTimestamp(register.getTimestamp().getTime());
		  
		}
		
		return registers;
	}
}
