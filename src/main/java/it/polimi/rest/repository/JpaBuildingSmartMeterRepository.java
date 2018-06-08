package it.polimi.rest.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import it.polimi.rest.domain.Baseline;
import it.polimi.rest.domain.BuildingSmartMeter;


@Repository
public class JpaBuildingSmartMeterRepository implements BuildingSmartMeterRepository {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public BuildingSmartMeter findBuildingSmartMeterBySmartMeterId(String smartMeterId) {
		return em.find(BuildingSmartMeter.class, smartMeterId);
	}

	@Override
	public List<BuildingSmartMeter> findBuildingSmartMeterByDistrictOid(Integer districtOid) {
		List<BuildingSmartMeter> resultList = em.createQuery(
			    "SELECT bsm FROM BuildingSmartMeter bsm WHERE bsm.districtOid = :districtOid")
			    .setParameter("districtOid", districtOid)	    
			    .getResultList();
		return resultList;	

	}
}
