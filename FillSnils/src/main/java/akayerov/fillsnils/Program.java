package akayerov.fillsnils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import akayerov.fillsnils.dao.MoDAO;
import akayerov.fillsnils.dao.SnilsDAO;
import akayerov.fillsnils.models.Mo;
import akayerov.fillsnils.models.Snils;



public class Program {

    private static final Logger logger = Logger.getLogger(Program.class);
	private static final String DIR_COMLETE = "COMPLETE";
	private static final String DIR_ERROR   = "ERROR";

    public static void main(String[] args) {
       if(args.length < 1) {
           logger.error("Usage: java fillsnils <path with snils file>");
           return;
       }
       String sDirComlete = args + "\\" + DIR_COMLETE;
       File dirComlete = new File(sDirComlete);
       if (dirComlete.exists() && dirComlete.isDirectory()) {
           logger.info("Обработаные файлы перемещаются в папку:" + sDirComlete);
       }
       else {
           logger.info("Папка не найдена, будет создана сейчас" + sDirComlete);
           dirComlete.mkdir();
       }    
       String sDirError = args + "\\" + DIR_ERROR;
       File dirError = new File(sDirError);
       if (dirError.exists() && dirError.isDirectory()) {
           logger.info("Файлы с ошибками перемещаются в папку:" + sDirError);
       }
       else {
           logger.info("Папка не найдена, будет создана сейчас" + sDirError);
           dirComlete.mkdir();
       }    

       
       
    	ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/beans.xml");
		SnilsDAO snils = (SnilsDAO) context.getBean("storageSnils");
		MoDAO mo = (MoDAO) context.getBean("storageMo");
  
		ReaderSnils rd = (ReaderSnils) context.getBean("readerSnils"); 
    	FolderSnils folder = (FolderSnils) context.getBean("folderSnils"); 
    	
    	folder.SetPath(args[0]);
        SnilsFile fileNameObj = folder.getNextFile();
        
    	while(fileNameObj != null) {
    		if(!fileNameObj.ogrn.equals("999999999")) {
	            // проверка корректности ОГРН
 	  		  Mo m = mo.getByOgrn(fileNameObj.ogrn);
	          if( m == null) {
	             logger.error("ОГРН отсуствует в списке МО:" + fileNameObj.ogrn);
	          } 
	          else
              logger.info("ОГРН:" + fileNameObj.ogrn + "найден. Далее обработка.");
    			
    			rd.SetPath(fileNameObj.fullpath);
		        logger.info("Список СНИЛС из файла:" + fileNameObj.fullpath);
		        String s;
		
		        s = rd.getNextSnils();
		        while(!s.equals("")) {
		          logger.info(s);
	 	  		  Snils sn = snils.getById(s);
		          if( sn == null) {
	                  sn = new Snils();
		        	  sn.setSnils(s);
		              sn.setOgrn(fileNameObj.ogrn);
		              snils.save(sn);
		          } 
		          else
		             logger.info("СНИЛС уже есть базе:" + s);
		             
		          s = rd.getNextSnils();
		        }
    		}
    		else {
   	           logger.error("Error Name File:" + fileNameObj.fullpath);
   	           Move(fileNameObj.fullpath,sDirError);
   	           
    		}
    		Move(fileNameObj.fullpath,sDirComlete);
	        fileNameObj  = folder.getNextFile();
    	}      	
        
		
		
        logger.info("Список всех СНИЛС в базе:");
		for (Snils p : snils.list()) {
			logger.info(p);
		}
    }

	private static void Move(String fullpath, String sDirDistination) {
		// TODO Auto-generated method stub
	       Path movefrom = FileSystems.getDefault().getPath(fullpath);
	       Path target_dir = FileSystems.getDefault().getPath(sDirDistination);
	        try {
	            Files.move(movefrom, target_dir.resolve(movefrom.getFileName()), StandardCopyOption.REPLACE_EXISTING);
	        } catch (IOException e) {
	            System.err.println(e);
	        }
	}



}
