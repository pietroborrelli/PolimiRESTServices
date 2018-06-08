package it.polimi.rest.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Table("register_by_metering_point_name")
public class Register implements Serializable {

	private static final long serialVersionUID = 1L;	
	@JsonIgnore
	@PrimaryKeyColumn(name = "metering_point_id", type=PrimaryKeyType.PARTITIONED)
	String meteringPointId;
	
	@JsonIgnore
	@PrimaryKeyColumn
    Date timestamp;
	
	@JsonIgnore
	@Column("metering_point_name")
    String meteringPointName;
    
	float value;
    Long unixTimestamp;
    
	@JsonIgnore
	String status;
    
	@JsonIgnore
	String unit;
   
	@JsonIgnore
	@Column("trasformer_ratio")
    Float transformerRatio;

    @JsonIgnore
    String localTimestamp;
	
}
