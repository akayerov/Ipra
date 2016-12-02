package akayerov.getsnils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



import akayerov.getsnils.dao.MseDAO;
import akayerov.getsnils.models.Mse;

@Configuration
public class IpraListNotMo {
	@Bean
	public IpraListNotMo listNotMo() {
		return new IpraListNotMo();
	}

	public void run(MseDAO mse, String dstfile) {
		List<Mse> lmse = mse.listNotMo();
		int i = 0;

		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(dstfile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					fileOutputStream, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bufferedWriter.write( "Код СНИЛС : Фамилия : Имя  : Отчество :  Дата рождения :  Дата программы ИПРА  :  Мед организация в документе ИПРА");
			for (Mse m : lmse) {
				bufferedWriter.write( m.getSnils() + ";" +  m.getLname() + ";" + m.getFname() + ";"
						+ m.getSname() + ";" + m.getBdate() + ";" + m.getPrgdate() + ";" + m.getSender_mo());
						
				bufferedWriter.newLine();

			}
			bufferedWriter.flush();
		} catch (IOException ex) {
			// handle exception
			ex.printStackTrace();
		} finally { // закрытие ресурсов обязательно в finally
			// Оба вызова обязательно в отдельных try-catch
			try {
				bufferedWriter.close();
			} catch (IOException ex) {
				// log here
				ex.printStackTrace();
			}

			try {
				fileOutputStream.close();
			} catch (IOException ex) {
				// log here
				ex.printStackTrace();
			}
		}
	}

}
