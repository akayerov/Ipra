package akayerov.getsnils;

import java.io.File;
import java.io.FileFilter;
import java.util.List;




import akayerov.getsnils.dao.MoDAO;
import akayerov.getsnils.dao.MseDAO;
import akayerov.getsnils.models.Mo;
import akayerov.getsnils.models.Mse;

public class TestBaseIpra {
	int i;
	private static File folder;
	private static File[] fList;
	
	public static void run(int idmo, String path, String pathError, MseDAO mse) {
          int idir = 0;   
          int imse = 0;   
		 
		  folder = new File(path);
		  FileFilter fileFilter = new FileFilter() {
				@Override
				public boolean accept(File pathname) {
	        	    String s =  pathname.getName().toLowerCase();
	        	    if( s.endsWith(".xml"))
	        	    	return true;
	        	    else
					   return false;
				}
		  };
	      fList = folder.listFiles(fileFilter);

//	      System.out.println("START from Dir");

	      for(File f : fList) {
        	  String fname = f.getName();
        	  Mse m = mse.getByNameFile(fname);
        	  if(m == null) {
            	  System.out.println("Dir=" + fname + " MSE= null");
        	  }
        	  else {
        		  if(m.getIdMo() != idmo) {
                	  System.out.println("Error MO Dir=" + fname);
        			  Program.Move(f.getAbsolutePath(), pathError);
        		  }
        		  imse++;
        	  }	  
        	  idir++;
          }
    	  System.out.println("RESULT: Count Dir=" + imse + " Count Mse=" + imse);
//    	  System.out.println("START revers from table MSE4");

	      fList = folder.listFiles(fileFilter);
    	  
    	  idir = 0;
	      imse = 0;
    	  List<Mse> lmse = mse.list();
    	  for (Mse m: lmse) {
    		  if(m.getIdMo() == idmo) {
    			  String fmse = m.getNameFile().toLowerCase().trim();
    		      boolean res = false;
            	  for(File f : fList) {
    	        	  String fname = f.getName().toLowerCase().trim();
                      if(fname.equalsIgnoreCase(fmse)) {
                    	  idir++;
                    	  res = true;
                    	  break;
                      }
    		      }	  
            	  if(!res) 
                	  System.out.println("MSE =" + fmse + "  DIR = null");
                  imse++;
    		  }
    	  }
    	  System.out.println("RESULT: Count Mse=" + imse + " Count Dir=" + idir);
          
	};
	
	public static void start(String startpath, MseDAO mse, MoDAO mo) {
	    System.out.println("Нераспределенные");
        String pathError = startpath + "\\ERROR";
		run(0, startpath, pathError, mse);
  	    File folderComplete = new File(startpath +  "\\COMPLETE");

//  	    System.out.println(folderComplete);
	    File[] fListComplete = folderComplete.listFiles();
        List<Mo> mlist = mo.list(); 
	    
	    for(Mo m: mlist) {
		    System.out.println(m.getScode());
	    	String nameFolder = Program.constructNameFolder(folderComplete.getAbsolutePath(), m.getName().trim()); 
//         	System.out.println("FDir =" + nameFolder);

         	for(File f : fListComplete) {
		    	if( f.isDirectory() && f.getAbsolutePath().equalsIgnoreCase(nameFolder)) {
//	           	  System.out.println("Found Dir =" + f.getName());
  	    		  run(m.getId(), f.getAbsolutePath(), pathError, mse);
		    		
	           	  break;
		    	}
		    }
	    }	    
		
	}

}
