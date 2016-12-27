package akayerov.getsnils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;

import akayerov.getsnils.dao.MoDAO;
import akayerov.getsnils.dao.MseDAO;
import akayerov.getsnils.dao.SnilsDAO;
import akayerov.getsnils.models.Mo;
import akayerov.getsnils.models.Mse;
import akayerov.getsnils.models.Snils;

public class FreeSnils {
	private static final Logger logger = Logger.getLogger(Program.class);

	public static void run(int mode, MseDAO mse, String sDirComplete, String namefile, String sMoId, SnilsDAO snilsDAO, MoDAO moDAO, BeanFactory context) {
		// TODO Auto-generated method stub
		Scanner in = null;
		int idx = 0;
		int count = 0;
		int success = 0;
		int srcMoId = Integer.parseInt(sMoId);
		logger.info("Workfile is:" + namefile);
		try {
			in = new Scanner(new File(namefile),"UTF-8");   /* передаем только в ANSY кодировке!!! Иначе поиск не будет работать */
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if( in != null ) {
			String strin;
			while (in.hasNextLine()) {
			  strin = in.nextLine();
			  if(idx==0)
				  strin = CodePage.run(strin); 
              if(!strin.equals("")) {
          		  logger.info("DataIn:" + strin);
          		  if(proc_freeSnils(mode,mse,sDirComplete,strin,snilsDAO,moDAO,context, srcMoId))
          			 success++;
                  count++;
              }	  
              idx++;
			}	  
        }

        if(in != null)
  		  in.close();
		logger.info("End Made:" + success + " success in " + count + " Files");
        if( success != count)
    		logger.info("Found Problem!");
		return;
  	}

	private static boolean proc_freeSnils(int mode, MseDAO mseDAO, String sDirDestination,
			String ssnils, SnilsDAO snilsDAO, MoDAO moDAO, BeanFactory context, int srcMoId) {
      
		Snils sn = null;
		Mse mse = null;
		Mo mo = null;
		String ogrn = "";
		String nmfile = ssnils.trim();

		if( mode == Program.FREE_BY_SNILS) {
			sn = snilsDAO.findBySnils(ssnils);
	        if( sn == null) {
	            logger.info("snils not found");
	    		return false;		
	        }	
	     	logger.info("found snils:" + sn.getSnils() + " ogrn:" + sn.getOgrn());
	    	ogrn =  sn.getOgrn();
	        mse =  mseDAO.getBySnils(ssnils);
		} else if( mode == Program.FREE_BY_NAMEFILE) {   
	        mse =  mseDAO.getByNameFile(nmfile);
	        int idmo = mse.getIdMo();
	        if( idmo==0 )
	        	return false;
	        	
	        Mo mo1 = moDAO.getById(idmo);
	        ogrn = mo1.getOgrn();
		}
        if( mse == null ) {
           logger.info("mse not found");
   		   return false;		
        }

        mo = moDAO.getByOgrn(ogrn);
        if( mo == null || mo.getId() != srcMoId ) {
            logger.info("Mo not found or Mo is not correct");
    		return false;		
        }	
        
        mse.setIdMo(0);                      // открепить
    	mse.setAutoSelect(false);            // убрать тэг автозакрепления
    	mseDAO.update(mse.getId(), mse);
    	// переместить ИПРА в корневую папку
        moveFile(sDirDestination,mo,mse);

        // удалить snils из таблицы sils
        if( mode == Program.FREE_BY_SNILS ) {
          int deleted = snilsDAO.deleteBySnils(ssnils);
          logger.info("Num deleted=" + String.valueOf(deleted));
        }  
        
    	return true;
	}

	private static void moveFile(String sDirDestination, Mo mo, Mse mse) {
 	    File folderComplete = new File(sDirDestination +  "\\COMPLETE");
    	String nameFolder = Program.constructNameFolder(folderComplete.getAbsolutePath(), mo.getName().trim()); 
    	String nameFile = nameFolder + "\\" + mse.getNameFile().trim();
     	logger.info("Move file:" + nameFile + " To:" + sDirDestination);
     	Program.Move(nameFile, sDirDestination);
	}

}
