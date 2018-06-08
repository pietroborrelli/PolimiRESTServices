package it.polimi.rest.repository;

import java.util.Date;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.polimi.rest.domain.RegisterByDistrict;

@Repository
public interface RegisterByDistrictRepository extends CassandraRepository<RegisterByDistrict,String>{
	
	@Query(value = "SELECT avg(value) from register_by_district "
			+ "where district=:district and year=:year and timestamp>=:startDate and timestamp<=:endDate")
	Double getAvgConsumption(
			@Param("district") String district, 
			@Param("year") Integer year,
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
	
	@Query(value = "SELECT sum(value) from register_by_district "
			+ "where district=:district and year=:year and timestamp>=:startDate and timestamp<=:endDate")
	Double getTotalConsumption(
			@Param("district") String district, 
			@Param("year") Integer year,
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate);

	RegisterByDistrict findTop1ByDistrictAndYear(final String district, final int year);
}
