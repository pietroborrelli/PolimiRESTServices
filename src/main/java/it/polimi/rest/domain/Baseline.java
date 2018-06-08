package it.polimi.rest.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="baseline")
public class Baseline {
	
	@Id Integer oid;
	@Column(name="smart_meter_id")
	String smartMeterName;
    Double totalConsumption;
    Integer year;

    public Baseline() {
    	
    }
    
}
