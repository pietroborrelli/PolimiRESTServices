package it.polimi.rest.service;

import java.util.List;

import it.polimi.rest.domain.BuildingSmartMeter;

public interface BuildingSmartMeterService {
	public BuildingSmartMeter getBuildingSmartMeterBySmartMeterId(String smartMeterId);
	public List <BuildingSmartMeter> getBuildingSmartMeterByDistrictOid(Integer districtOid);
}
