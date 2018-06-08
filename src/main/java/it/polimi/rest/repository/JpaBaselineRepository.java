
package it.polimi.rest.repository;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import it.polimi.rest.domain.Baseline;

@Repository
public class JpaBaselineRepository implements BaselineRepository{

	@PersistenceContext
	 private EntityManager em;
	
	
	@Override
	public List<Baseline> findBaselineBySmartMeterNameAndYearGreaterThanEqualAndYearLessThanEqual(String smartMeterName,
			Integer startYear, Integer endYear) {
		List<Baseline> resultList = em.createQuery(
					    "SELECT b FROM Baseline b WHERE b.smartMeterName LIKE :smartMeterName AND b.year BETWEEN :startYear AND :endYear")
					    .setParameter("smartMeterName", smartMeterName)
					    .setParameter("startYear", startYear)
					    .setParameter("endYear", endYear)			    
					    .getResultList();
		return resultList;	
	}


	@Override
	public List<Baseline> findBaselineBySmartMeterName(String smartMeterName) {

		TypedQuery<Baseline> typedQuery = em.createQuery("SELECT b FROM baseline b where b.smartMeterName = :smartMeterName", Baseline.class);
		return typedQuery.getResultList();
	}
	
	
}
