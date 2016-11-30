package akayerov.report;

import akayerov.getsnils.dao.MseDAO;

public class VirtSnils {
   public static void run(MseDAO mse) {
       System.out.println("Set Virtual Snils...");
	   mse.setVSnils();
       System.out.println("Done");
	   
   }
}
