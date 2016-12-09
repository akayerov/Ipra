package akayerov.getsnils.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import akayerov.getsnils.models.Prg;
import akayerov.getsnils.models.Snils;


@Repository
@Service("storageSnils")
public class SnilsDAOImpl implements SnilsDAO {
	@PersistenceContext
    private EntityManager em; 

    @Override
    @Transactional
    public void save(Snils p) {
    	 em.persist(p);    
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<Snils> list() {
        return em.createQuery("from Snils", Snils.class).getResultList();
    }

	@Override
	public Snils getById(String sid) {
		// TODO Auto-generated method stub
//        Person personElem = em.byId(Person.class ).load(id);
        Snils snilsElem = em.find( Snils.class, sid );
        return snilsElem;
	}

	@Override
	@Transactional
	public void update(String sid, Snils s) {
		// TODO Auto-generated method stub
        Snils snilsElem = em.find( Snils.class, sid );
        snilsElem.setOgrn(s.getOgrn());
        em.flush();
	}

	@Override
	public Snils findBySnils(String ssnils) {
		// TODO Auto-generated method stub
	    Snils snilsElem = null;

		try {
			snilsElem = (Snils) em.createQuery(
				    "select s " +
				    "from Snils s " +
				    "where s.snils = :par1")
				    .setParameter("par1",ssnils).getSingleResult();
		} catch (Exception e) { // Не найден
		}
        return snilsElem;
	}

	@Override
	@Transactional
	public int deleteBySnils(String ssnils) {
	    Snils snilsElem = null;
	    int deletedEntities = 0;
		try {
			deletedEntities = em.createQuery("delete Snils s where snils = :par1" )
				.setParameter( "par1", ssnils )
				.executeUpdate();
		} catch (Exception e) { // Не найден
		}
		return deletedEntities;
	}


}