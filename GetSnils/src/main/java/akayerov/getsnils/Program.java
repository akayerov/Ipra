﻿package akayerov.getsnils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import akayerov.getsnils.dao.MoDAO;
import akayerov.getsnils.dao.SnilsDAO;
import akayerov.getsnils.models.Mo;
import akayerov.getsnils.models.Snils;

public class Program {

	private static final Logger logger = Logger.getLogger(Program.class);
	private static final String DIR_COMLETE = "COMPLETE";
	private static final String DIR_ERROR = "ERROR";

	private static final int MODE_UNKNOWN = 0;
	public static final int MODE_SNILS   = 1;
	public static final int MODE_MSE     = 2;
	public static final int MODE_RESULT  = 3;
    private static int mode;
	
    public static void main(String[] args) {

		if (args.length != 2) {
			logger.error("Usage: java  -jar ipra -<svr> <directory with worked file>");
			logger.error("                       -s work with SNILS");
			logger.error("                       -v work with XML files from MSE");
			logger.error("                       -r work with XML files from MO");
			return;
		}
		mode = MODE_UNKNOWN;
		if(args[0].equals("-s")) mode = MODE_SNILS;
		if(args[0].equals("-v")) mode = MODE_MSE;
		if(args[0].equals("-r")) mode = MODE_RESULT;
		if( mode == MODE_UNKNOWN) {
			logger.error("Usage: java  -jar ipra -<svr> <directory with worked file>");
			return;
			
		}
		File dirSrc = new File(args[1]);
		if (!(dirSrc.exists() && dirSrc.isDirectory())) {
			logger.info("Not found folder:" + dirSrc);
			return;
		} 
		
		
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"META-INF/beans.xml");
		SnilsDAO snils = (SnilsDAO) context.getBean("storageSnils");
		MoDAO mo = (MoDAO) context.getBean("storageMo");

		ReaderSnils rd = (ReaderSnils) context.getBean("readerSnils");
		FolderSnils folder = (FolderSnils) context.getBean("folderSnils");

		String sDirComlete = args[1] + "\\" + DIR_COMLETE;
		File dirComlete = new File(sDirComlete);
		if (dirComlete.exists() && dirComlete.isDirectory()) {
			logger.info("Обработаные файлы перемещаются в папку:" + sDirComlete);
		} else {
			logger.info("Папка не найдена, будет создана сейчас:" + sDirComlete);
			dirComlete.mkdir();
		}
		String sDirError = args[1] + "\\" + DIR_ERROR;
		File dirError = new File(sDirError);
		if (dirError.exists() && dirError.isDirectory()) {
			logger.info("Файлы с ошибками перемещаются в папку:" + sDirError);
		} else {
			logger.info("Папка не найдена, будет создана сейчас:" + sDirError);
			dirError.mkdir();
		}

		folder.setPath(args[1]);
		SnilsFile fileNameObj = folder.getNextFile(mode);

		while (fileNameObj != null) {

			if( mode == MODE_SNILS) {
				GetSnilsFunction(fileNameObj, rd, mo, sDirComlete, sDirError, snils);
			}	
			else if (mode == MODE_MSE) 
			    MSEFunction(fileNameObj, mo, sDirComlete, sDirError, snils);
			fileNameObj = folder.getNextFile(mode);
		}

		/*
		 * logger.info("Список всех СНИЛС в базе:"); for (Snils p :
		 * snils.list()) { logger.info(p); }
		 */
	}

	
	/* MSEFunction - функция обрабатывает очередной XML-файл из МСЭ
	 * */
	private static void MSEFunction(SnilsFile fileNameObj, 
			MoDAO mo, String sDirComlete, String sDirError, SnilsDAO snils) {
		int newSnils = 0;
		int oldSnils = 0;
		String sSnils = null;
		if (!fileNameObj.ogrn.equals("999999999")) { // не ошибка

			logger.info("ИПРА выписка из файла:" + fileNameObj.fullpath);
			String s;
            // Есть ли СНИЛС в выписке?
			try {
				sSnils = getSnils(fileNameObj.fullpath);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!sSnils.isEmpty()) {
				logger.info("Найден СНИЛС в файле ИПРА:" + sSnils);
				// СНИЛС  в базе есть?  Если есть -  тогда файла перемещаем в папку для соотвествующей организации
				// и последующей отправки в МО
                Snils sn = snils.getById(sSnils);
                if(sn != null) {
    				logger.info("Найден СНИЛС в базе данных:" + sSnils);
    				Mo m = mo.getByOgrn(sn.getOgrn());
    				if (m == null) {
    					logger.error("ОГРН отсуствует в списке МО:" + sn.getOgrn());
    				} else {
    					logger.info("ОГРН:" +sn.getOgrn() + ":"
    							+ m.getName().trim());
    					String nameFolder = constructNameFolder(sDirComlete, m.getName().trim());
    					logger.info("Перемещаем докумен в папку:" + nameFolder);
    					
    				}	
                	
                }
				
				
			}
			else {
				logger.info("НЕ Найден СНИЛС в файле ИПРА:" + sSnils);
			}
			
//			Move(fileNameObj.fullpath, sDirComlete);
		} else {
			logger.error("Error Name File:" + fileNameObj.fullpath);
//			Move(fileNameObj.fullpath, sDirError);

		}
		logger.info("ИПРА выписка обработана:");

		
	}

	private static String constructNameFolder(String sDaseDir, String sMo) {
        if(sMo != null) {
           sMo = sMo.toUpperCase().replace("ГОСУДАРСТВЕННОЕ", "");
           sMo = sMo.replace("УЧРЕЖДЕНИЕ", "");
           sMo = sMo.replace("ЯРОСЛАВСКОЙ", "");
           sMo = sMo.replace("ОБЛАСТИ", "");
           sMo = sMo.replace("ЗДРАВООХРАНЕНИЯ", "");
           sMo = sMo.replace('\\', ' ');
           sMo = sMo.replace('/', ' ');
           sMo = sMo.replace('.', ' '); 
           sMo = sMo.replace('"', ' '); 
           sMo = sMo.replace(',', ' ');
           sMo = sMo.replace(':', ' ');
           sMo = sMo.trim();
           
           return  sDaseDir + "\\" + sMo;  
        }  
		return null;
	}


	private static String getSnils(String fullpath) throws ParserConfigurationException, SAXException, IOException {
	   File f = new File(fullpath);
       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
       DocumentBuilder builder = factory.newDocumentBuilder();
       Document document = builder.parse(f);
       Element root = document.getDocumentElement();

       Element SNILS = (Element) root.getElementsByTagName("SNILS").item(0);
       if(SNILS != null) {
           String textSNILS = SNILS.getTextContent(); // тоже для упрощения
           return textSNILS;
       }  
       else {
           return "";
       }
	}


	/* GetSnilsFunction - функция обрабатывает очередной СНИЛС файл
	 * */
	private static void GetSnilsFunction(SnilsFile fileNameObj, ReaderSnils rd,
			MoDAO mo, String sDirComlete, String sDirError, SnilsDAO snils) {
		int newSnils = 0;
		int oldSnils = 0;
		if (!fileNameObj.ogrn.equals("999999999")) { // не ошибка
			rd.setPath(fileNameObj.fullpath);
			logger.info("Список СНИЛС из файла:" + fileNameObj.fullpath);
			String s;
			s = rd.getNextSnils();
			// проверка корректности ОГРН
			Mo m = mo.getByOgrn(fileNameObj.ogrn);
			// Mo m1 = mo.getById(2);

			if (m == null) {
				logger.error("ОГРН отсуствует в списке МО:" + fileNameObj.ogrn);
				rd.close();
				Move(fileNameObj.fullpath, sDirError);
			} else {
				logger.info("ОГРН:" + fileNameObj.ogrn + ":"
						+ m.getName().trim());

				newSnils = 0;
				oldSnils = 0;
				while (!s.equals("")) {
					// logger.info(s);
					Snils sn = snils.getById(s);
					if (sn == null) {
						sn = new Snils();
						sn.setSnils(s);
						sn.setOgrn(fileNameObj.ogrn);
						snils.save(sn);
						newSnils++;
					} else
						oldSnils++;
					// logger.info("СНИЛС уже есть базе:" + s);

					s = rd.getNextSnils();
				}
			}
			rd.close();
			Move(fileNameObj.fullpath, sDirComlete);
		} else {
			logger.error("Error Name File:" + fileNameObj.fullpath);
			rd.close();
			Move(fileNameObj.fullpath, sDirError);

		}
		logger.info("Всего СНИЛС:" + (newSnils + oldSnils) + " в т.ч новых:"
				+ newSnils);

	}

	private static void Move(String fullpath, String sDirDistination) {
		// TODO Auto-generated method stub
		Path movefrom = FileSystems.getDefault().getPath(fullpath);
		Path target_dir = FileSystems.getDefault().getPath(sDirDistination);
		try {
			Files.move(movefrom, target_dir.resolve(movefrom.getFileName()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}
