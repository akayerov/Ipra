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
import akayerov.getsnils.models.Prg;
import akayerov.getsnils.models.Prg_rhb;


@Repository
@Service("storagePrg_rhb")
public class Prg_rhbDAOImpl implements Prg_rhbDAO {
	@PersistenceContext
    private EntityManager em; 

    @Override
    @Transactional
    public void save(Prg_rhb p) {
    	 em.persist(p);    
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<Prg_rhb> list() {
        return em.createQuery("from Prg_rhb", Prg_rhb.class).getResultList();
    }

	@Override
	public Prg_rhb getById(int id) {
		// TODO Auto-generated method stub
//        Person personElem = em.byId(Person.class ).load(id);
        Prg_rhb pElem = em.find( Prg_rhb.class, id );
        return pElem;
	}

	@Override
	@Transactional
	public void update(String sid, Prg_rhb s) {
		// TODO Auto-generated method stub
        Prg_rhb p = em.find( Prg_rhb.class, sid );
        p.setDicid(s.getDicid());
        p.setDt_exc(s.getDt_exc());
        p.setEvntid(s.getEvntid());
        p.setExcid(s.getExcid());
        p.setExecut(s.getExecut());
        p.setName(s.getName());
        p.setNote(s.getNote());
        p.setPar1(s.getPar1());
        p.setPar2(s.getPar2());
        p.setPar3(s.getPar3());
        p.setPrgid(s.getPrgid());
        p.setResid(s.getResid());
        p.setResult(s.getResult());
        p.setTsrid(s.getTsrid());
        p.setTypeid(s.getTypeid());
        
        em.flush();
	}

	@Override
	@Transactional
	public int delete(int idPrg, Mo mo) {
		int idmo = mo.getId();
		int deletedEntities = em.createQuery(
			    "delete Prg_rhb h " +
			    "where h.prgid = :par1 and" +
			    "      h.excid = :par2")
			.setParameter( "par1", idPrg)
			.setParameter( "par2",  idmo)
			.executeUpdate();
		return deletedEntities;
	}

 
}