package it.polimi.rest.service;

import it.polimi.rest.domain.request.Wrapper;
import it.polimi.rest.exception.ResourceNotFoundException;

public interface BaselineService {

	public Double getMyLastYearBaseline(Wrapper wrapperRequest);
	public Double getDistrictBaseline(Wrapper wrapperRequest) throws ResourceNotFoundException;
}
