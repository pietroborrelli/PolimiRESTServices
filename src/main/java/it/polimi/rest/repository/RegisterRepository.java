package it.polimi.rest.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.polimi.rest.domain.Register;

@Repository
public interface RegisterRepository extends CassandraRepository<Register, String> {
	
	Register findTop1ByMeteringPointName(final String meteringPointName);
	
	List<Register> findByMeteringPointNameAndTimestampGreaterThanEqualAndTimestampLessThanEqualOrderByTimestampAsc(
			final String meteringPointName, final Date startDate, final Date endDate);

	@Query(value = "SELECT sum(value) from register_by_metering_point_name "
			+ "where metering_point_name=:meteringPointName and timestamp>=:startDate and timestamp<=:endDate")
	Double getTotalConsumption(
			@Param("meteringPointName") String meteringPointName, 
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
	
	@Query(value = "SELECT avg(value) from register_by_metering_point_name "
			+ "where metering_point_name=:meteringPointName and timestamp>=:startDate and timestamp<=:endDate")
	Double getAvgConsumption(
			@Param("meteringPointName") String meteringPointName, 
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
}
