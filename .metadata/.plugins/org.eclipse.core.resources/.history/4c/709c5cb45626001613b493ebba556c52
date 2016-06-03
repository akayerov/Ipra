package developer.remarks.dao;

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

import developer.remarks.models.Person;
import developer.remarks.models.Prg;


@Repository
@Service("storagePrg")
public class PrgDAOImpl implements PrgDAO {
	@PersistenceContext
    private EntityManager em; 

    @Override
    @Transactional
    public void save(Prg p) {
    	 em.persist(p);    
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<Prg> list() {
        return em.createQuery("from Prg", Prg.class).getResultList();
    }

	@Override
	public Prg getById(int id) {
		// TODO Auto-generated method stub
//        Person personElem = em.byId(Person.class ).load(id);
        Prg prgElem = em.find( Prg.class, id );
        return prgElem;
	}

	@Override
	@Transactional
	public void update(int id, Prg p) {
		// TODO Auto-generated method stub
        Prg prgElem = em.find( Prg.class, id );
        prgElem.setFname(p.getFname());
        em.flush();
	}
 
}