package it.polimi.rest.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Data;

@Data
@Table("register_by_district")
public class RegisterByDistrict implements Serializable {

	

	private static final long serialVersionUID = 1L;

	@PrimaryKeyColumn(name = "district", ordinal=1, type=PrimaryKeyType.PARTITIONED)
	private String district;
	@PrimaryKeyColumn(name = "year", ordinal=0, type=PrimaryKeyType.PARTITIONED)
	private Integer year;
	
	@PrimaryKeyColumn
    private Date timestamp;
	
	@Column("metering_point_id")
	private String meteringPointId;
	
	@PrimaryKeyColumn(name="metering_point_name")
    private String meteringPointName;
    private String status;
    private String unit;
    private float value;
    
    
    
    public RegisterByDistrict() {
    	
    }

	public RegisterByDistrict(String district, Integer year, Date timestamp, String meteringPointId,
			String meteringPointName, String status, String unit, float value) {
		super();
		this.district = district;
		this.year = year;
		this.timestamp = timestamp;
		this.meteringPointId = meteringPointId;
		this.meteringPointName = meteringPointName;
		this.status = status;
		this.unit = unit;
		this.value = value;
	}

}
