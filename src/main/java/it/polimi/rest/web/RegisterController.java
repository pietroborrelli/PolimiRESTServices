package it.polimi.rest.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.polimi.rest.domain.Register;
import it.polimi.rest.domain.request.Wrapper;
import it.polimi.rest.domain.response.SummaryConsumption;
import it.polimi.rest.exception.InvalidDateException;
import it.polimi.rest.exception.ResourceNotFoundException;
import it.polimi.rest.service.RegisterByDistrictService;
import it.polimi.rest.service.RegisterService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/register")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class RegisterController {

	@Autowired
	RegisterService registerService;
	
	@Autowired
	RegisterByDistrictService registerByDistrictService;

	
	
	@GetMapping("/hello")
	public String hello() {
		return "index";
	}

	@PostMapping("/list")
	public List<Register> postList(@RequestBody final Wrapper wrapperRequest) throws ResourceNotFoundException, InvalidDateException {
		
		if (wrapperRequest.getStartDate() ==null)  throw new ResourceNotFoundException("startDate");
		if (wrapperRequest.getEndDate() ==null)  throw new ResourceNotFoundException("endDate");
		if (wrapperRequest.getMeteringPointName() ==null)  throw new ResourceNotFoundException("meteringPointName");
		
		if (wrapperRequest.getStartDate().after(wrapperRequest.getEndDate())) throw new InvalidDateException();
		return registerService.getRegisterList(wrapperRequest);
	}

	@PostMapping("/summary")
	public SummaryConsumption postSummaryNoDataRestriction(@RequestBody final Wrapper wrapperRequest) throws ResourceNotFoundException, InvalidDateException {
		
		if (wrapperRequest.getStartDate() ==null)  throw new ResourceNotFoundException("startDate");
		if (wrapperRequest.getEndDate() ==null)  throw new ResourceNotFoundException("endDate");
		if (wrapperRequest.getMeteringPointName() ==null)  throw new ResourceNotFoundException("meteringPointName");
		
		if (wrapperRequest.getStartDate().after(wrapperRequest.getEndDate())) throw new InvalidDateException();
		
		return registerService.getSummaryConsumptionNoDataRestriction(wrapperRequest);
	}

	@PostMapping("/summaryDistrict")
	public SummaryConsumption postSummaryDistrictNoDataRestriction(@RequestBody final Wrapper wrapperRequest) throws ResourceNotFoundException, InvalidDateException {
		
		if (wrapperRequest.getStartDate() ==null)  throw new ResourceNotFoundException("startDate");
		if (wrapperRequest.getEndDate() ==null)  throw new ResourceNotFoundException("endDate");
		if (wrapperRequest.getDistrict() ==null)  throw new ResourceNotFoundException("district");
		
		if (wrapperRequest.getStartDate().after(wrapperRequest.getEndDate())) throw new InvalidDateException();
		
		return registerByDistrictService.getSummaryConsumptionNoDataRestriction(wrapperRequest);
	}

	
	@PostMapping("/exception") public void exception() throws ResourceNotFoundException {
		  throw new ResourceNotFoundException("ciao");
		  
		  }
	 
}
