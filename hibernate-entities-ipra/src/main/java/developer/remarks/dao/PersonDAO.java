package developer.remarks.dao;

import java.util.List;

import developer.remarks.models.Person;

public interface PersonDAO {
 
    public void save(Person p);
    public void update(int id, Person p);
    public List<Person> list();
    public Person getById(int id);
    
     
}