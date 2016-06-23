package akayerov.getsnils.dao;

import java.util.List;

import akayerov.getsnils.models.Snils;

public interface SnilsDAO {
 
    public void save(Snils p);
    public void update(String sid, Snils s);
    public List<Snils> list();
    public Snils getById(String sid);
    
     
}