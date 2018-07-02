package it.polimi.rest.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.polimi.rest.domain.Register;
import it.polimi.rest.domain.request.Wrapper;
import it.polimi.rest.domain.response.SummaryConsumption;
import it.polimi.rest.exception.ResourceNotFoundException;
import it.polimi.rest.repository.RegisterRepository;
import it.polimi.rest.util.CustomUtil;


@Service
public class RegisterServiceImpl implements RegisterService{

	private RegisterRepository registerRepository;
	
	@Autowired 
	public RegisterServiceImpl(RegisterRepository registerRepository) {
		this.registerRepository = registerRepository;
	}
	
	@Autowired
	BaselineService baselineService;
	
	@Autowired
	RegisterByDistrictService registerByDistrict;
	
	@Override
	public String hello() {
		return "Hello Pietro!";
	}

	@Override
	public List<Register> getRegisterList(Wrapper wrapperRequest) throws ResourceNotFoundException{
		
		if (registerRepository.findTop1ByMeteringPointName(wrapperRequest.getMeteringPointName())==null)
				throw new ResourceNotFoundException("meteringPointName");

		
		return CustomUtil.convertToUnixTimestamp (
					registerRepository.findByMeteringPointNameAndTimestampGreaterThanEqualAndTimestampLessThanEqualOrderByTimestampAsc(
					wrapperRequest.getMeteringPointName(),
					wrapperRequest.getStartDate(),
					wrapperRequest.getEndDate())
				);
	}

	@Override
	public SummaryConsumption getSummaryConsumptionNoDataRestriction(Wrapper wrapperRequest) throws ResourceNotFoundException {
		
		if (registerRepository.findTop1ByMeteringPointName(wrapperRequest.getMeteringPointName())==null)
			throw new ResourceNotFoundException("meteringPointName");
		
		SummaryConsumption summaryConsumption = new SummaryConsumption();
		
		//variables for year
		LocalDate yearStartDate = wrapperRequest.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().with(TemporalAdjusters.firstDayOfYear());
		LocalDate yearEndDate = wrapperRequest.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().with(TemporalAdjusters.firstDayOfYear());
		//variables for months
		LocalDate firstDayOfStartDate = wrapperRequest.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().with ( ChronoField.DAY_OF_MONTH , 1 );
		LocalDate firstDayOfEndDate = wrapperRequest.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().with ( ChronoField.DAY_OF_MONTH , 1 );
		
		//variables for weeks
		LocalDate weekStartDate = wrapperRequest.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate weekEndDate = wrapperRequest.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		
		//AVG DAYS
		summaryConsumption.setAverageDaily(registerRepository.getAvgConsumption(
				wrapperRequest.getMeteringPointName(), 
				wrapperRequest.getStartDate(), 
				wrapperRequest.getEndDate()
				)
			);
		
		
 
		while (weekStartDate.isBefore(weekEndDate) || (weekStartDate.isEqual(weekEndDate))){
			
			Date beginningOfWeek = Date.from(weekStartDate.with(DayOfWeek.MONDAY).atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date endingOfWeek = Date.from(weekStartDate.with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
			
			summaryConsumption.getAverageWeekly().put(
					String.valueOf(weekStartDate.getYear() + "-" + weekStartDate.get(WeekFields.ISO.weekOfYear())),
					registerRepository.getAvgConsumption(
							wrapperRequest.getMeteringPointName(), 
							beginningOfWeek, 
							endingOfWeek
							)
					);
			weekStartDate=weekStartDate.plusWeeks(1);
		}
		
		//AVG MONTHS
		while (firstDayOfStartDate.isBefore(firstDayOfEndDate) || (firstDayOfStartDate.isEqual(firstDayOfEndDate))){
			
			Date beginningOfMonth = Date.from(firstDayOfStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date endingOfMonth = Date.from(firstDayOfStartDate.withDayOfMonth(firstDayOfStartDate.lengthOfMonth()).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
			
			Date beginningOfMonthOfLastYear = Date.from(firstDayOfStartDate.minusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date endingOfMonthOfLastYear = Date.from(firstDayOfStartDate.withDayOfMonth(firstDayOfStartDate.lengthOfMonth()).atTime(LocalTime.MAX).minusYears(1).atZone(ZoneId.systemDefault()).toInstant());
						
			//avg months current year
			summaryConsumption.getAverageMonthly().put(
					String.valueOf(firstDayOfStartDate.getYear()) + "-" + String.valueOf(firstDayOfStartDate.getMonth().getValue()), 
					registerRepository.getAvgConsumption(
							wrapperRequest.getMeteringPointName(), 
							beginningOfMonth, 
							endingOfMonth
							)
					);
			
			//avg months last year
			summaryConsumption.getLastYearAverageMonthly().put(
					String.valueOf(firstDayOfStartDate.getYear() - 1) + "-" + String.valueOf(firstDayOfStartDate.getMonth().getValue()), 
					registerRepository.getAvgConsumption(
							wrapperRequest.getMeteringPointName(), 
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
					registerRepository.getAvgConsumption(
							wrapperRequest.getMeteringPointName(),
							beginningOfYear,
							endingOfYear
							)
				);	
			
			yearStartDate=yearStartDate.plusYears(1);

		}
		
		//invece che calcolato a tempo di esecuzione, si è deciso per lettura da view
		//avg year last year
		/*
		summaryConsumption.setAvgBaselineLastYear(
						registerRepository.getAvgConsumption(
						wrapperRequest.getMeteringPointName(),
						lastYearBeginning,
						lastYearEnding
					)
				);
		*/
		
		//AVG last year period; preso da MYSQL
		summaryConsumption.setAvgBaselineLastYear(baselineService.getMyLastYearBaseline(wrapperRequest));
		
		summaryConsumption.setStartDate(wrapperRequest.getStartDate());
		summaryConsumption.setEndDate(wrapperRequest.getEndDate());
		summaryConsumption.setConsumption(registerRepository.getTotalConsumption(
				wrapperRequest.getMeteringPointName(),
				wrapperRequest.getStartDate(),
				wrapperRequest.getEndDate()));
		
		return CustomUtil.convertToLocalTimeZone(summaryConsumption);
	}

}
