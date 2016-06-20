package akayerov.fillsnils;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import akayerov.fillsnils.dao.SnilsDAO;
import akayerov.fillsnils.models.Snils;



public class Program {

    private static final Logger logger = Logger.getLogger(Program.class);

    public static void main(String[] args) {
       if(args.length < 1) {
           logger.error("Usage: java fillsnils <path with snils file>");
           return;
       }
    	
    	ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/beans.xml");
		SnilsDAO snils = (SnilsDAO) context.getBean("storageSnils");
    	ReaderSnils rd = (ReaderSnils) context.getBean("readerSnils"); 
    	FolderSnils folder = (FolderSnils) context.getBean("folderSnils"); 
    	
    	folder.SetPath(args[0]);
        SnilsFile fileNameObj = folder.getNextFile();
        
    	while(fileNameObj != null) {
    		if(!fileNameObj.ogrn.equals("999999999")) {
		     	rd.SetPath(fileNameObj.fullpath);
		        logger.info("Список СНИЛС из файла:");
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
   	           logger.error("Error Name file:" + fileNameObj.fullpath);
    		}
	        fileNameObj  = folder.getNextFile();
    	}      	
        
		
		
        logger.info("Список всех СНИЛС в базе:");
		for (Snils p : snils.list()) {
			logger.info(p);
		}
    }



}
