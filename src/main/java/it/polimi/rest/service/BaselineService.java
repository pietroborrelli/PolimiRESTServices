package it.polimi.rest.service;

import it.polimi.rest.domain.request.Wrapper;

public interface BaselineService {

	public Double getMyLastYearBaseline(Wrapper wrapperRequest);
	public Double getDistrictBaseline(Wrapper wrapperRequest);
}
