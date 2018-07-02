package it.polimi.rest.service;

import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.polimi.rest.domain.Baseline;
import it.polimi.rest.domain.BuildingSmartMeter;
import it.polimi.rest.domain.request.Wrapper;
import it.polimi.rest.exception.ResourceNotFoundException;
import it.polimi.rest.repository.BaselineRepository;

@Service
public class BaselineServiceImpl implements BaselineService {

	static final int ORDER_MAGNITUDE = 1000;

	private BaselineRepository baselineRepository;

	@Autowired
	public BaselineServiceImpl(BaselineRepository baselineRepository) {
		this.baselineRepository = baselineRepository;
	}

	@Autowired 
	BuildingSmartMeterService buildingSmartMeterService;
	
	@Autowired
	RegisterByDistrictService registerByDistrictService;
	
	@Override
	public Double getMyLastYearBaseline(Wrapper wrapperRequest) {
		
		List <Baseline> baselines = baselineRepository.findBaselineBySmartMeterNameAndYearGreaterThanEqualAndYearLessThanEqual(
				wrapperRequest.getMeteringPointName(),
				wrapperRequest.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear()-1,
				wrapperRequest.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear()-1
				);
		
		Double avg = computeAvgOfBaselines(baselines);
		return avg * ORDER_MAGNITUDE;

	}

	public Double computeAvgOfBaselines(List<Baseline> baselines) {
		int conta = 0;
		Double somma = 0.0;
		
		if (baselines.isEmpty())
			return somma;
		
		for (Baseline baseline : baselines) {
			somma += baseline.getTotalConsumption();
			conta++;
		}
		
		return somma / conta ;
	}

	@Override
	public Double getDistrictBaseline(Wrapper wrapperRequest) throws ResourceNotFoundException {
		
		BuildingSmartMeter bsm = buildingSmartMeterService.getBuildingSmartMeterBySmartMeterId(wrapperRequest.getMeteringPointName());
		if (bsm == null || bsm.getDistrictOid() == null)
			throw new ResourceNotFoundException("district");
		else {
			wrapperRequest.setDistrict(bsm.getDistrictOid().toString());
			//nearest integer
			return (double) Math.round(registerByDistrictService.getAvgNeighborhoodConsumption(wrapperRequest) * ORDER_MAGNITUDE);
		}
		
	}
}

	