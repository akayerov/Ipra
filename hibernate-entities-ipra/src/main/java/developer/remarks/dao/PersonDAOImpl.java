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


@Repository
@Service("storagePerson")
public class PersonDAOImpl implements PersonDAO {
	@PersistenceContext
    private EntityManager em; 

    @Override
    @Transactional
    public void save(Person p) {
    	 em.persist(p);    
    }
 
    @SuppressWarnings("unchecked")
    @Override
    public List<Person> list() {
        return em.createQuery("from Person", Person.class).getResultList();
    }

	@Override
	public Person getById(int id) {
		// TODO Auto-generated method stub
//        Person personElem = em.byId(Person.class ).load(id);
        Person personElem = em.find( Person.class, id );
        return personElem;
	}

	@Override
	@Transactional
	public void update(int id, Person p) {
		// TODO Auto-generated method stub
        Person personElem = em.find( Person.class, id );
        personElem.setName(p.getName());
        em.flush();
	}
 
}