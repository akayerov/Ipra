package akayerov.getsnils.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import akayerov.getsnils.models.Mo;


@Repository
@Service("storageMo")
public class MoDAOImpl implements MoDAO {
	@PersistenceContext
    private EntityManager em; 

    @Override
    @Transactional
    public void save(Mo p) {
    	 em.persist(p);    
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<Mo> list() {
        return em.createQuery("from Mo", Mo.class).getResultList();
    }

	@Override
	public Mo getById(int id) {
		// TODO Auto-generated method stub
//        Person personElem = em.byId(Person.class ).load(id);
        Mo moElem = em.find( Mo.class, id );
        return moElem;
	}

	@Override
	@Transactional
	public void update(String sid, Mo s) {
		// TODO Auto-generated method stub
        Mo snilsElem = em.find( Mo.class, sid );
        snilsElem.setOgrn(s.getOgrn());
        em.flush();
	}

	@Override
	public Mo getByOgrn(String sogrn) {
		Mo moElem = null;

		try {
			moElem = (Mo) em.createQuery(
				    "select m " +
				    "from Mo m " +
				    "where m.ogrn = :par1")
				    .setParameter("par1",sogrn).getSingleResult();
		} catch (Exception e) { // Не найден
		}
        return moElem;
	}
 
}