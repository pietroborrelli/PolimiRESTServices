package it.polimi.rest.service;

import it.polimi.rest.domain.request.Wrapper;
import it.polimi.rest.domain.response.SummaryConsumption;
import it.polimi.rest.exception.ResourceNotFoundException;


public interface RegisterByDistrictService {
	public SummaryConsumption getSummaryConsumptionNoDataRestriction(Wrapper wrapperRequest) throws ResourceNotFoundException;
}
