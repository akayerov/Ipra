package akayerov.fillsnils.dao;

import java.util.List;

import akayerov.fillsnils.models.Mo;

public interface MoDAO {
    public void save(Mo m);
    public void update(String sid, Mo m);
    public List<Mo> list();
    public Mo getById(String sid);
	public Mo getByOgrn(String ogrn);
}