package akayerov.fillsnils;


import java.io.File;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import akayerov.fillsnils.SnilsFile;

@Repository
@Service("folderSnils")
public class FolderSnilsImpl implements FolderSnils {
	File folder;
	private File[] fList;
	private int idx;
	public FolderSnilsImpl() {
    	
    }
	@Override
	public SnilsFile getNextFile() {
      if(idx < fList.length) {
    	String sogrn;
		if(fList[idx].isFile()) { 
			
			sogrn = fList[idx].getName();
			int pos_start = sogrn.lastIndexOf("_", sogrn.length());
			int pos_end   = sogrn.lastIndexOf(".", sogrn.length());
			if(pos_start< 0 || pos_end < 0)
				sogrn="999999999";
    	    sogrn = sogrn.substring(pos_start+1,pos_end);
			return new SnilsFile(fList[idx++].getAbsolutePath(),sogrn);
		}		  
    	idx++;
      }
	  return null;
	}

	@Override
	public File SetPath(String path) {
	  folder = new File(path);
      fList = folder.listFiles();
      idx = 0;
      return folder;
	}

}
