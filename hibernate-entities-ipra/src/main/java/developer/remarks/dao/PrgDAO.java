package developer.remarks.dao;

import java.util.List;

import developer.remarks.models.Prg;



public interface PrgDAO {
 
    public void save(Prg p);
    public void update(int id, Prg p);
    public List<Prg> list();
    public Prg getById(int id);
    
     
}