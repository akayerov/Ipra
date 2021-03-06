package akayerov.getsnils;


import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import akayerov.getsnils.IpraFile;
// Тест объекта сделать по другому способу через configuration и bean
//@Repository
//@Service("folderSnils")
@Configuration
public class FolderIpraImpl implements FolderIpra {
	File folder;
	private File[] fList;
	private int idx;
	public FolderIpraImpl() {
    	
    }
// тестовая функция см выше для создания bean
	@Bean
	public FolderIpraImpl folder() {
		return new FolderIpraImpl();
	}
// конец	
	
	@Override
	public IpraFile getNextFile(int mode) {
      while(idx < fList.length) {
    	String sogrn;
		if(fList[idx].isFile()) { 
			sogrn = fList[idx].getName();
            String namefile = sogrn;
			if( mode == Program.MODE_SNILS) {
				String type = sogrn.substring(0,2).toUpperCase();
				if( type.equals("SN")) {
		            int pos_start = sogrn.lastIndexOf("_", sogrn.length());
					int pos_end   = sogrn.lastIndexOf(".", sogrn.length());
					if(pos_start< 0 || pos_end < 0)
						sogrn="999999999";                                    // код ошибки записываем в поле ОГРН
					else
					    sogrn = sogrn.substring(pos_start+1,pos_end);
				}
				else
					sogrn="999999999";
		    	return new IpraFile(fList[idx++].getAbsolutePath(),sogrn);
            }
            else if( mode == Program.MODE_MSE ) {
            	 // тестировать имя выписки
				int pos_end   = sogrn.lastIndexOf(".", sogrn.length());
				String type = sogrn.substring(pos_end+1).toUpperCase();
				if(!type.equals("XML")) {
					sogrn="999999999";
				}
		    	return new IpraFile(fList[idx++].getAbsolutePath(),sogrn,namefile);
            }
            else if( mode == Program.MODE_RESULT ) {
				String type = sogrn.substring(0,2).toUpperCase();
				if( type.equals("RE")) {
		            int pos_start = sogrn.lastIndexOf("_", sogrn.length());
		            int pos_start2= sogrn.lastIndexOf("_", pos_start-1);
					int pos_end   = sogrn.lastIndexOf(".", sogrn.length());
					if(pos_start < 0 || pos_start2 < 0 || pos_end < 0)
						sogrn="999999999";                                    // код ошибки записываем в поле ОГРН
					else {
						type = sogrn.substring(pos_end+1).toUpperCase();
						if(!type.equals("XML")) {
							sogrn="999999999";
						}
						else {
						    sogrn = sogrn.substring(pos_start2+1,pos_start);
						}
					}
				}
				else
					sogrn="999999999";
		    	return new IpraFile(fList[idx++].getAbsolutePath(),sogrn,namefile);
            	
            }
		}		  
    	idx++;
      }
	  return null;
	}

	@Override
	public File setPath(String path) {
	  folder = new File(path);
      fList = folder.listFiles();
      idx = 0;
      return folder;
	}

	@Override
	public IpraFile getNextDir() {
	      while(idx < fList.length) {
	      	String sogrn = null;
	  		if(!fList[idx].isFile()) { 
	  			    sogrn = fList[idx].getName();
	  		        String namefile = sogrn;
	  		    	return new IpraFile(fList[idx++].getAbsolutePath(),sogrn,namefile);
	  		}	
	    	idx++;
	      } 
		return null;
	}

}
