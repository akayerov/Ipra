package akayerov.getsnils.dao;

import java.util.List;

import akayerov.getsnils.models.Prg;

public interface PrgDAO {
    public void save(Prg p);
    public void update(int id, Prg p);
    public List<Prg> list();
    public Prg getById(int i);
	public Prg getBySnils(String snils);
	public List<Prg> getBySnilsPrgNum(String snils,String prgNum);
}