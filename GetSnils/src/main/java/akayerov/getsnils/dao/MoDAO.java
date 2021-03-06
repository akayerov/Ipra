package akayerov.getsnils.dao;

import java.util.List;

import akayerov.getsnils.models.Mo;

public interface MoDAO {
    public void save(Mo m);
    public void update(String sid, Mo m);
    public List<Mo> list();
    public Mo getById(int i);
	public Mo getByOgrn(String ogrn);
}