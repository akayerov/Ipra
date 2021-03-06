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

import akayerov.getsnils.models.Prg;


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
        Prg pElem = em.find( Prg.class, id );
        return pElem;
	}

	@Override
	@Transactional
	public void update(int id, Prg s) {
		// TODO Auto-generated method stub
        Prg p = em.find( Prg.class, id );
        p.setBdate(s.getBdate());
        p.setDocdt(s.getDocdt());
        p.setDocnum(s.getDocnum());
        p.setDt(s.getDt());
        p.setFname(s.getFname());
        p.setGndr(s.getGndr());
        p.setLname(s.getLname());
        p.setMseid(s.getMseid());
        p.setNreg(s.getNreg());
        p.setOivid(s.getOivid());
        p.setOkrId(s.getOkrId());
        p.setPrg(s.getPrg());
        p.setPrgdt(s.getPrgdt());
        p.setPrgnum(s.getPrgnum());
        p.setSname(s.getSname());
        p.setSnils(s.getSnils());
        em.flush();
	}

	@Override
	public Prg getBySnils(String snils) {
		Prg pElem = null;

		try {
			pElem = (Prg) em.createQuery(
				    "select p " +
				    "from Prg p " +
				    "where p.snils = :par1")
				    .setParameter("par1",snils).getSingleResult();
		} catch (Exception e) { // Не найден
		}
        return pElem;
	}

	@Override
	public List<Prg> getBySnilsPrgNum(String snils, String prgNum) {
		List<Prg>	lPrg = em.createQuery(
				    "select p " +
				    "from Prg p " +
				    "where p.snils  = :par1 and" +
				    "      p.prgnum = :par2")
				    .setParameter("par1",snils)
				    .setParameter("par2",prgNum)
				    .getResultList();
		return lPrg;
	}
 
}