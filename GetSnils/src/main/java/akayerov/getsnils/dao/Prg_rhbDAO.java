package akayerov.getsnils.dao;

import java.util.List;

import akayerov.getsnils.models.Mo;
import akayerov.getsnils.models.Prg_rhb;

public interface Prg_rhbDAO {
    public void save(Prg_rhb p);
    public void update(String sid, Prg_rhb p);
    public List<Prg_rhb> list();
    public Prg_rhb getById(int i);
	public int delete(int idPrg, Mo mo);

}