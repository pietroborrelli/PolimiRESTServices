package it.polimi.rest.domain.response;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class SummaryConsumption {
	
	@JsonIgnore
	Date startDate;
	@JsonIgnore
	Date endDate;
	String localStartDate;
	String localEndDate;
	Double consumption;
	Double averageDaily;
	Map<String,Double> averageWeekly = new HashMap<String,Double>();
	Map<String,Double> averageMonthly = new HashMap<String,Double>();
	Map<String,Double> averageAnnualy = new HashMap<String,Double>();
	Map<String,Double> lastYearAverageMonthly = new HashMap<String,Double>();;
	Double avgBaselineLastYear;
	
	
}
