package akayerov.report;

import java.io.File;
import java.util.List;

import akayerov.getsnils.Program;
import akayerov.getsnils.dao.MoDAO;
import akayerov.getsnils.models.Mo;

public class CreateFolderAllMo {
   public static void run(String basedir, MoDAO moDAO) {
     List<Mo> molist = moDAO.list();
     for( Mo m : molist) {
    	 String sdirDest = Program.constructNameFolder(basedir, m.getName());
    	 File dirDest = new File(sdirDest);    	 
   		 if (dirDest.exists() && dirDest.isDirectory()) {
 		 } else {
             System.out.println("Create folder " + dirDest);
 			 dirDest.mkdir();
 	 	 }
     }
   }
}
