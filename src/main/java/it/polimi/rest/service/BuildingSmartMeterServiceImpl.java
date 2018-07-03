package it.polimi.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.polimi.rest.domain.BuildingSmartMeter;
import it.polimi.rest.repository.BuildingSmartMeterRepository;

@Service
public class BuildingSmartMeterServiceImpl implements BuildingSmartMeterService{

	private BuildingSmartMeterRepository buildingSmartMeterRepository;
	
	@Autowired
	public BuildingSmartMeterServiceImpl (BuildingSmartMeterRepository buildingSmartMeterRepository) {
		this.buildingSmartMeterRepository = buildingSmartMeterRepository;
	}
	
	@Override
	public BuildingSmartMeter getBuildingSmartMeterBySmartMeterId(String smartMeterId) {
		
		return buildingSmartMeterRepository.findBuildingSmartMeterBySmartMeterId(smartMeterId);
	}

	@Override
	public List<BuildingSmartMeter> getBuildingSmartMeterByDistrictOid(Integer districtOid) {
		return buildingSmartMeterRepository.findBuildingSmartMeterByDistrictOid(districtOid);
	}

	@Override
	public List<String> getSmartMeterList() {
		List<String> smartMeterList = new ArrayList<String>();
		for (BuildingSmartMeter bsm : buildingSmartMeterRepository.findDistinctBuildingSmartMeterOrderBySmartMeterId())
			smartMeterList.add(bsm.getSmartMeterId());
		return smartMeterList;
	}

}
