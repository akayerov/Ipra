package akayerov.getsnils.dao;

import java.util.List;

import akayerov.getsnils.models.Snils;

public interface SnilsDAO {
 
    public void save(Snils p);
    public void update(String sid, Snils s);
    public List<Snils> list();
    public Snils getById(String sid);       // непонятно как работает, проблемы с запонением 15 разрядного поля 14 разрядными значениями
    public Snils findBySnils(String ssnils);   // 
    public int deleteBySnils(String ssnils);    
     
}