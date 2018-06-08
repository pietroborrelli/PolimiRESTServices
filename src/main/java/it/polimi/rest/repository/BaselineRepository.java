package it.polimi.rest.repository;



import java.util.List;

import it.polimi.rest.domain.Baseline;


public interface BaselineRepository {
	public List<Baseline> findBaselineBySmartMeterNameAndYearGreaterThanEqualAndYearLessThanEqual(String smartMeterId, Integer startYear, Integer endYear);
	public List<Baseline> findBaselineBySmartMeterName(String smartMeterName);
}
