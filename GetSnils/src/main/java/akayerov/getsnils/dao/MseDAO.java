package akayerov.getsnils.dao;

import java.util.List;

import akayerov.getsnils.models.Mse;
import akayerov.getsnils.models.Prg;

public interface MseDAO {
    public void save(Mse m);
    public void update(int id, Mse m);
    public List<Mse> list();
    public Mse getById(int i);
	public Mse getBySnils(String snils);
	public Mse getByNameFile(String namefile);
    public List<Mse> listNotMo();
}