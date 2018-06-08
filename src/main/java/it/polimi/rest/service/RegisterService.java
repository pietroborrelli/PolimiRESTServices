package it.polimi.rest.service;

import java.util.List;

import it.polimi.rest.domain.Register;
import it.polimi.rest.domain.request.Wrapper;
import it.polimi.rest.domain.response.SummaryConsumption;
import it.polimi.rest.exception.ResourceNotFoundException;

public interface RegisterService {
	public String hello();
	public List<Register> getRegisterList(Wrapper wrapperRequest) throws ResourceNotFoundException;
	public SummaryConsumption getSummaryConsumptionNoDataRestriction(Wrapper wrapperRequest) throws ResourceNotFoundException;
}
