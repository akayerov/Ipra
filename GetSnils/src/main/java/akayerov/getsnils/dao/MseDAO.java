package akayerov.getsnils.dao;

import java.util.List;

import akayerov.getsnils.models.Mse;

public interface MseDAO {
    public void save(Mse m);
    public void update(String sid, Mse m);
    public List<Mse> list();
    public Mse getById(int i);
	public Mse getBySnils(String snils);
	public Mse getByNameFile(String namefile);
}