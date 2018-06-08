package it.polimi.rest.domain.request;

import java.util.Date;

import lombok.Data;

@Data
public class Wrapper {
	
	Date startDate;
	Date endDate;
	String meteringPointName;
	String district;

}
