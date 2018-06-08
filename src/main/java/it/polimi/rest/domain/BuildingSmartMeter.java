package it.polimi.rest.domain;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Table (name="building_smart_meter")
public class BuildingSmartMeter {
	@Id String smartMeterId;
	Integer districtOid;
	
	public BuildingSmartMeter() {
		
	}
}
