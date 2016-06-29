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

import akayerov.getsnils.models.Mse;


@Repository
@Service("storageMse")
public class MseDAOImpl implements MseDAO {
	@PersistenceContext
    private EntityManager em; 

    @Override
    @Transactional
    public void save(Mse p) {
    	 em.persist(p);    
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<Mse> list() {
        return em.createQuery("from Mse", Mse.class).getResultList();
    }

	@Override
	public Mse getById(int id) {
		// TODO Auto-generated method stub
//        Person personElem = em.byId(Person.class ).load(id);
        Mse pElem = em.find( Mse.class, id );
        return pElem;
	}

	@Override
	@Transactional
	public void update(String sid, Mse s) {
		// TODO Auto-generated method stub
        Mse p = em.find( Mse.class, sid );
        p.setBdate(s.getBdate());
        p.setDt(s.getDt());
        p.setFname(s.getFname());
        p.setLname(s.getLname());
        p.setSname(s.getSname());
        p.setSnils(s.getSnils());
        p.setIdMo(s.getIdMo());
        p.setNameFile(s.getNameFile());
        em.flush();
	}

	@Override
	public Mse getBySnils(String snils) {
		Mse pElem = null;

		try {
			pElem = (Mse) em.createQuery(
				    "select p " +
				    "from Mse p " +
				    "where p.snils = :par1")
				    .setParameter("par1",snils).getSingleResult();
		} catch (Exception e) { // Не найден
		}
        return pElem;
	}

	@Override
	public Mse getByNameFile(String namefile) {
		Mse pElem = null;
		try {
			pElem = (Mse) em.createQuery(
				    "select m " +
				    "from Mse m " +
				    "where m.nmfile =:par1")
				    .setParameter("par1",namefile).getSingleResult();
		} catch (Exception e) {
		}
        return pElem;
	}
 
}