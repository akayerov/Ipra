package akayerov.getsnils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import akayerov.getsnils.dao.MoDAO;
import akayerov.getsnils.dao.MseDAO;
import akayerov.getsnils.dao.SnilsDAO;
import akayerov.getsnils.models.Snils;

public class TestScaner {
	private static final Logger logger = Logger.getLogger(Program.class);

	public static void run(int mode, MseDAO mse, SnilsDAO snilsDAO, MoDAO moDAO, ApplicationContext context) {
      String[] arr = { "sAnsy.txt", "sUTF8.txt"};
	  int count =0;
	  int succ = 0;
	  for(int i=0; i< arr.length; i++) {
		  succ += test(arr[i], mse, snilsDAO, moDAO, context);
		  count++;
	  }
	  
      logger.info("Result:");
      logger.info("Success " + String.valueOf(succ) + " from " + String.valueOf(count));
	  
	}

	public static int test(String nameFile, MseDAO mse, SnilsDAO snilsDAO, MoDAO moDAO, ApplicationContext context) {
        int ret = 0;
		Scanner in = null;
		int count = 0;
		int success = 0;
		logger.info("Workfile is:" + nameFile);
		try {
			in = new Scanner(new File(nameFile),"UTF-8");   /* передаем только в ANSY кодировке!!! Иначе поиск не будет работать */
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if( in != null ) {
			String strin;
			int idx = 0;
			while (in.hasNextLine()) {
			  strin = in.nextLine();
              if(!strin.equals("")) {
            	  if(idx==0) 
            		  strin = correctCodePage(strin);
            	  for(int i=0; i< strin.length(); i++)
              		  logger.info("Char[" + String.valueOf(i) + "]:" + strin.charAt(i) + ":" + strin.codePointAt(i));
            		   
          		  logger.info("DataIn:" + strin);
          		  if( testSnils(strin,mse,strin,snilsDAO,moDAO,context))
          			 success++;
                  count++;
                  idx++; 
              }	  
			}	  
        }

        if(in != null)
  		  in.close();
		logger.info("End Made:" + success + " success in " + count + " Files");
        if( success != count) {
    		logger.info("Found Problem!");
    		ret = 0;
        }	
        else
        	ret = 1;
		return ret;		
		
	}

	private static String correctCodePage(String strin) {
		// TODO Auto-generated method stub
		if( strin.charAt(0)> 64000) 
			strin = strin.substring(1);
		return strin;
	}

	private static boolean testSnils(String ssnils, MseDAO mse,
			String strin, SnilsDAO snilsDAO, MoDAO moDAO,
			ApplicationContext context) {
		Snils sn = snilsDAO.findBySnils(ssnils);
        if( sn == null) {
            logger.info("snils not found");
            return false;		
        }	
        logger.info("snils found");
		return true;
	}

	
}
