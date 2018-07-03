package it.polimi.rest.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
		TypedQuery<BuildingSmartMeter> query = em.createQuery("SELECT bsm FROM BuildingSmartMeter bsm WHERE bsm.districtOid = :districtOid",BuildingSmartMeter.class);
		query.setParameter("districtOid", districtOid);
		return query.getResultList();	

	}

	@Override
	public List<BuildingSmartMeter> findDistinctBuildingSmartMeterOrderBySmartMeterId() {
		TypedQuery<BuildingSmartMeter> query = em.createQuery("SELECT distinct bsm FROM BuildingSmartMeter bsm ",BuildingSmartMeter.class);
		return query.getResultList();
	}
}
