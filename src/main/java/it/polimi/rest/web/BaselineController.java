package it.polimi.rest.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.polimi.rest.domain.Register;
import it.polimi.rest.domain.request.Wrapper;
import it.polimi.rest.exception.InvalidDateException;
import it.polimi.rest.exception.ResourceNotFoundException;
import it.polimi.rest.service.BaselineService;

@RestController
@RequestMapping("/baseline")
@CrossOrigin(origins = "http://localhost:8080")
public class BaselineController {
	
	@Autowired
	BaselineService baselineService;

	/* Used in smartg20 app
	 * Return a double, avg of baseline of last year
	 */
	@PostMapping("/lastYear")
	public Double postMyLastYearBaseline(@RequestBody final Wrapper wrapperRequest) throws ResourceNotFoundException, InvalidDateException {
		
		if (wrapperRequest.getStartDate() ==null)  throw new ResourceNotFoundException("startDate");
		if (wrapperRequest.getEndDate() ==null)  throw new ResourceNotFoundException("endDate");
		if (wrapperRequest.getMeteringPointName() ==null)  throw new ResourceNotFoundException("meteringPointName");
		
		if (wrapperRequest.getStartDate().after(wrapperRequest.getEndDate())) throw new InvalidDateException();
		
		return baselineService.getMyLastYearBaseline(wrapperRequest);
	}
	
	/*
	 * used in smarth2o app
	 * returns total consumption of specified district's smart meter in a period of time
	 * 
	 */
	@PostMapping("/myNeighborhood")
	public Double postDistrictBaseline(@RequestBody final Wrapper wrapperRequest) throws ResourceNotFoundException, InvalidDateException {
		
		if (wrapperRequest.getStartDate() ==null)  throw new ResourceNotFoundException("startDate");
		if (wrapperRequest.getEndDate() ==null)  throw new ResourceNotFoundException("endDate");
		if (wrapperRequest.getMeteringPointName() ==null)  throw new ResourceNotFoundException("meteringPointName");
		
		if (wrapperRequest.getStartDate().after(wrapperRequest.getEndDate())) throw new InvalidDateException();
		
		return baselineService.getDistrictBaseline(wrapperRequest);
	}
}
