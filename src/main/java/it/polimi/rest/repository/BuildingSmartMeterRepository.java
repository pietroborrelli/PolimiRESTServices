package it.polimi.rest.repository;

import java.util.List;

import it.polimi.rest.domain.BuildingSmartMeter;

public interface BuildingSmartMeterRepository {
	public BuildingSmartMeter findBuildingSmartMeterBySmartMeterId(String smartMeterId);
	public List<BuildingSmartMeter> findBuildingSmartMeterByDistrictOid(Integer districtOid);
	public List<BuildingSmartMeter> findDistinctBuildingSmartMeterOrderBySmartMeterId();
}
