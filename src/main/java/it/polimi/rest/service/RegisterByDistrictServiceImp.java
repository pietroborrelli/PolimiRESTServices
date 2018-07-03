package it.polimi.rest.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.polimi.rest.domain.request.Wrapper;
import it.polimi.rest.domain.response.SummaryConsumption;
import it.polimi.rest.exception.ResourceNotFoundException;
import it.polimi.rest.repository.RegisterByDistrictRepository;
import it.polimi.rest.util.CustomUtil;

@Service
public class RegisterByDistrictServiceImp implements RegisterByDistrictService{
	 
	private RegisterByDistrictRepository registerByDistrictRepository;
	
	@Autowired 
	public RegisterByDistrictServiceImp(RegisterByDistrictRepository registerByDistrictRepository) {
		this.registerByDistrictRepository = registerByDistrictRepository;
	}

	@Override
	public SummaryConsumption getSummaryConsumptionNoDataRestriction(Wrapper wrapperRequest) throws ResourceNotFoundException {
		
		if (registerByDistrictRepository.findTop1ByDistrictAndYear(
				wrapperRequest.getDistrict(),
				wrapperRequest.getStartDate().toInstant().atZone(ZoneId.systemDefault()).getYear())
					==null)
			throw new ResourceNotFoundException("district");
		
		SummaryConsumption summaryConsumption = new SummaryConsumption();
		
		//variables for year
		LocalDate yearStartDate = wrapperRequest.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().with(TemporalAdjusters.firstDayOfYear());
		LocalDate yearEndDate = wrapperRequest.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().with(TemporalAdjusters.firstDayOfYear());
		Date lastYearBeginning =  Date.from(yearStartDate.minusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());		
		Date lastYearEnding =  Date.from(yearStartDate.minusYears(1).with(TemporalAdjusters.lastDayOfYear()).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
		
		//variables for months
		LocalDate firstDayOfStartDate = wrapperRequest.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().with ( ChronoField.DAY_OF_MONTH , 1 );
		LocalDate firstDayOfEndDate = wrapperRequest.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().with ( ChronoField.DAY_OF_MONTH , 1 );
		
		//variables for weeks
		LocalDate weekStartDate = wrapperRequest.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate weekEndDate = wrapperRequest.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		
		//AVG DAYS
		if (wrapperRequest.getStartDate().toInstant().atZone(ZoneId.systemDefault()).getYear() == 
				wrapperRequest.getEndDate().toInstant().atZone(ZoneId.systemDefault()).getYear()) {
			summaryConsumption.setAverageDaily(registerByDistrictRepository.getAvgConsumption(
					wrapperRequest.getDistrict(), 
					wrapperRequest.getStartDate().toInstant().atZone(ZoneId.systemDefault()).getYear(),
					wrapperRequest.getStartDate(), 
					wrapperRequest.getEndDate()
					)
				);
		}else {
			//calcolo a cavallo tra piu anni
			summaryConsumption.setAverageDaily(
					straddlingYearsForAvg(wrapperRequest.getStartDate(),wrapperRequest.getEndDate(),wrapperRequest.getDistrict() 
							)
					);
			
		}
		
		
		//AVG WEEKS
		while (weekStartDate.isBefore(weekEndDate) || (weekStartDate.isEqual(weekEndDate))){
			
			Date beginningOfWeek = Date.from(weekStartDate.with(DayOfWeek.MONDAY).atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date endingOfWeek = Date.from(weekStartDate.with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
			
			if (beginningOfWeek.toInstant().atZone(ZoneId.systemDefault()).getYear() == 
					endingOfWeek.toInstant().atZone(ZoneId.systemDefault()).getYear()) {
				summaryConsumption.getAverageWeekly().put(
						String.valueOf(weekStartDate.getYear() + "-" + weekStartDate.get(WeekFields.ISO.weekOfYear())),
						registerByDistrictRepository.getAvgConsumption(
								wrapperRequest.getDistrict(), 
								beginningOfWeek.toInstant().atZone(ZoneId.systemDefault()).getYear(),
								beginningOfWeek, 
								endingOfWeek
								)
						);
		}else {
			summaryConsumption.getAverageWeekly().put(
					String.valueOf(weekStartDate.getYear() + "-" + weekStartDate.get(WeekFields.ISO.weekOfYear())),
					straddlingYearsForAvg(beginningOfWeek, endingOfWeek,wrapperRequest.getDistrict())
					);
			}
			weekStartDate=weekStartDate.plusWeeks(1);
		}
		//AVG MONTHS
		while (firstDayOfStartDate.isBefore(firstDayOfEndDate) || (firstDayOfStartDate.isEqual(firstDayOfEndDate))){
			
			Date beginningOfMonth = Date.from(firstDayOfStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date endingOfMonth = Date.from(firstDayOfStartDate.withDayOfMonth(firstDayOfStartDate.lengthOfMonth()).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
			
			Date beginningOfMonthOfLastYear = Date.from(firstDayOfStartDate.minusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date endingOfMonthOfLastYear = Date.from(firstDayOfStartDate.withDayOfMonth(firstDayOfStartDate.lengthOfMonth()).atTime(LocalTime.MAX).minusYears(1).atZone(ZoneId.systemDefault()).toInstant());
			
			//avg months current year
			summaryConsumption.getLastYearAverageMonthly().put(
					String.valueOf(firstDayOfStartDate.getYear()) + "-" + String.valueOf(firstDayOfStartDate.getMonth().getValue()), 
					registerByDistrictRepository.getAvgConsumption(
							wrapperRequest.getDistrict(),
							beginningOfMonth.toInstant().atZone(ZoneId.systemDefault()).getYear(),
							beginningOfMonth, 
							endingOfMonth
							)
					);
			
			//avg months last year
			summaryConsumption.getAverageMonthly().put(
					String.valueOf(firstDayOfStartDate.getYear() - 1) + "-" + String.valueOf(firstDayOfStartDate.getMonth().getValue()), 
					registerByDistrictRepository.getAvgConsumption(
							wrapperRequest.getDistrict(), 
							beginningOfMonth.toInstant().atZone(ZoneId.systemDefault()).getYear(),
							beginningOfMonthOfLastYear, 
							endingOfMonthOfLastYear
							)
					);
			
			firstDayOfStartDate=firstDayOfStartDate.plusMonths(1);
		}
		
		//AVG YEAR
		while (yearStartDate.isBefore(yearEndDate) || (yearStartDate.isEqual(yearEndDate))){
		
			Date beginningOfYear = Date.from(yearStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date endingOfYear = Date.from(yearStartDate.with(TemporalAdjusters.lastDayOfYear()).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
						
			//avg year
			summaryConsumption.getAverageAnnualy().put(
					String.valueOf(yearStartDate.getYear()),
					registerByDistrictRepository.getAvgConsumption(
							wrapperRequest.getDistrict(), 
							beginningOfYear.toInstant().atZone(ZoneId.systemDefault()).getYear(),
							beginningOfYear,
							endingOfYear
							)
				);	
			
			yearStartDate=yearStartDate.plusYears(1);

		}
		//avg year last year
		summaryConsumption.setAvgBaselineLastYear(
						registerByDistrictRepository.getAvgConsumption(
						wrapperRequest.getDistrict(),
						lastYearBeginning.toInstant().atZone(ZoneId.systemDefault()).getYear(),
						lastYearBeginning,
						lastYearEnding
					)
				);
		
		
		summaryConsumption.setStartDate(wrapperRequest.getStartDate());
		summaryConsumption.setEndDate(wrapperRequest.getEndDate());
		
		//total consumption
		if (wrapperRequest.getStartDate().toInstant().atZone(ZoneId.systemDefault()).getYear() == 
				wrapperRequest.getEndDate().toInstant().atZone(ZoneId.systemDefault()).getYear()) {
			summaryConsumption.setConsumption(registerByDistrictRepository.getTotalConsumption(
					wrapperRequest.getDistrict(), 
					wrapperRequest.getStartDate().toInstant().atZone(ZoneId.systemDefault()).getYear(),
					wrapperRequest.getStartDate(), 
					wrapperRequest.getEndDate()
					)
				);
		}else {
			//calcolo a cavallo tra piu anni
			summaryConsumption.setConsumption(
					straddlingYearsForSum(wrapperRequest.getStartDate(),wrapperRequest.getEndDate(),wrapperRequest.getDistrict() 
							)
					);
			
		}

		return  CustomUtil.convertToLocalTimeZone(summaryConsumption);
	}

	public Double straddlingYearsForAvg(Date startDate, Date endDate, String district) {
		Double incrementalConsumption=0.0;

		for (LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				 start.isBefore(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) ||  
				 start.isEqual(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()); 
				start=start.plusYears(1).with(TemporalAdjusters.firstDayOfYear())) {
			
			if (start.getYear() != endDate.toInstant().atZone(ZoneId.systemDefault()).getYear())
				incrementalConsumption += registerByDistrictRepository.getAvgConsumption(
						district,
						start.getYear(),
						//ATTENZIONE: PERDO INEVITABILMENTE ORA ESATTA DELLA START DATE nella conversione da localdate a date!
						Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()),
						Date.from(start.with(TemporalAdjusters.lastDayOfYear()).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant())
						);
			else
				incrementalConsumption += registerByDistrictRepository.getAvgConsumption(
						district,
						start.getYear(),
						Date.from(start.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
						endDate
						);
		}
		return incrementalConsumption;
	}
	
	
	public Double straddlingYearsForSum(Date startDate, Date endDate, String district) {
		Double incrementalConsumption=0.0;
		for (LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				 start.isBefore(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) ||  
				 start.isEqual(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()); 
				start=start.plusYears(1).with(TemporalAdjusters.firstDayOfYear())) {
			
			if (start.getYear() != endDate.toInstant().atZone(ZoneId.systemDefault()).getYear())
				incrementalConsumption += registerByDistrictRepository.getTotalConsumption(
						district,
						start.getYear(),
						//ATTENZIONE: PERDO INEVITABILMENTE ORA ESATTA DELLA START DATE nella conversione da localdate a date!
						Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()),
						Date.from(start.with(TemporalAdjusters.lastDayOfYear()).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant())
						);
			else
				incrementalConsumption += registerByDistrictRepository.getTotalConsumption(
						district,
						start.getYear(),
						Date.from(start.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
						endDate
						);
		}
		return incrementalConsumption;
	}

	@Override
	public Double getAvgNeighborhoodConsumption(Wrapper wrapperRequest) throws ResourceNotFoundException {
		Double avg = 0.0;
		
		//AVG DAYS
				if (wrapperRequest.getStartDate().toInstant().atZone(ZoneId.systemDefault()).getYear() == 
						wrapperRequest.getEndDate().toInstant().atZone(ZoneId.systemDefault()).getYear()) {
					avg = (registerByDistrictRepository.getAvgConsumption(
							wrapperRequest.getDistrict(), 
							wrapperRequest.getStartDate().toInstant().atZone(ZoneId.systemDefault()).getYear(),
							wrapperRequest.getStartDate(), 
							wrapperRequest.getEndDate()
							)
						);
				}else {
					//calcolo a cavallo tra piu anni
					avg = (
							straddlingYearsForAvg(wrapperRequest.getStartDate(),wrapperRequest.getEndDate(),wrapperRequest.getDistrict() 
									)
							);
					
				}
		
	return avg;
	}
	
}
