﻿package akayerov.getsnils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import akayerov.getsnils.dao.MoDAO;
import akayerov.getsnils.dao.MseDAO;
import akayerov.getsnils.dao.PrgDAO;
import akayerov.getsnils.dao.Prg_rhbDAO;
import akayerov.getsnils.dao.SnilsDAO;
import akayerov.getsnils.models.Mo;
import akayerov.getsnils.models.Mse;
import akayerov.getsnils.models.Prg;
import akayerov.getsnils.models.Prg_rhb;
import akayerov.getsnils.models.Snils;
import akayerov.report.CreateFolderAllMo;
import akayerov.report.ReportRes;
import akayerov.report.VirtSnils;

public class Program {

	private static final Logger logger = Logger.getLogger(Program.class);
	private static final String DIR_COMLETE = "COMPLETE";
	private static final String DIR_ERROR = "ERROR";
	private static final String DIR_SUCCESS = "Успешно";
	private static final String DIR_FAIL = "Неудача";

	private static final int MSE_UPDATE_ID = 0;  // обновление поля id  в таблице MSE4
// 09/03/2017 + обновление поля ОГРН
	private static final int MSE_SENDER_MO = 1;  // обновление поля sender_mo  в таблице MSE4
	private static final int MSE_ENDDATE   = 3;  // обновление поля enddate в таблице MSE4

	private static final int LEN_FIELD_SENDERMO = 80;// по размеру соответствующих полей

	// режимы сброса распределения ИПРА в нераспределенные
	public static final int FREE_BY_SNILS     = 1;  // по коду SNILS с его удалением из таблицы СНИЛС
	public static final int FREE_BY_NAMEFILE =  2;  // по имени файла ИПРА без использования таблицы SNILS
													
	
	private static final int MODE_UNKNOWN = 0;
	public static final int MODE_SNILS = 1;
	public static final int MODE_MSE = 2;
	public static final int MODE_RESULT = 3;
	public static final int MODE_LISTNOTMO = 4;
	public static final int MODE_СREMOFOLDER = 5;
	public static final int MODE_SETMSEID = 6;
	public static final int MODE_SETVSNILS = 7;
	public static final int MODE_EXTRACTZIP = 8;
	private static final int MODE_TESTDIR = 9;
	private static final int MODE_IPRAFORCE = 10;
	private static final int MODE_MAKE_FREE_IPRA_FOR_SNILS = 11;
	private static final int MODE_MAKE_FREE_IPRA_FOR_FILE  = 12;
	private static final int MODE_TEST_SCANER  = 13;                // иссследовать проблемы с кодировкой UTF-8 и ANSY Входящих
	private static final int MODE_ENDDATE = 14;                     // установить поле enddate 
	private static int mode;
	private static boolean smartFolder = false;     // c версии 1.3 = true - контролирует изменения целевых папок для "умной" рассылки
	                                                // изменений по МО - + стат класс SmartFolder

	public static void main(String[] args) {

		if (args.length != 2 && args.length != 3 && args.length != 4) {
			logger.error("Usage: java  -jar ipra -<svrlf> <directory with worked file>");
			logger.error("                       -s work with SNILS set default folder SNILS)");
			logger.error("                       -v work with XML files from MSE (set default folder FROM_MSE)");
			logger.error("                       -vs smart work with XML files from MSE (set default folder FROM_MSE)");
			logger.error("                       -r work with XML files from MO set default folder RESULT)");
			logger.error("                       -rs smart work with XML files from MO set default folder RESULT)");
			logger.error(" ");
			logger.error("                       -l make list ipra witch not MO (set default folfer FROM_MSE)");
			logger.error("                       -ms set mseid from source XML (set default folder FROM_MSE");
			logger.error("                       -vs set Virtual SNILS (auto exececute in mode -v");
			logger.error("                       -f create emty folder for all MO (if it no exists)");
			logger.error("                          exec after add new MO");
			logger.error("                       -e extract from zip)");
			logger.error("                       -t test / compare MSE file ");
			logger.error("                       -mkfree <MSE_Folder> <file Snils for free><idMo>");
			logger.error("                       -force find MO by field <SenderMedOrgName> in XML MSE");
			return;
		}
		logger.info(args[0]);
		mode = MODE_UNKNOWN;
		if (args[0].equals("-s"))
			mode = MODE_SNILS;
		if (args[0].equals("-v"))
			mode = MODE_MSE;
		if (args[0].equals("-vs")) {
			mode = MODE_MSE;
			smartFolder = true;
			SmartFolder.init();
		}
		if (args[0].equals("-r"))
			mode = MODE_RESULT;
		if (args[0].equals("-rs")) {
			mode = MODE_RESULT;
			smartFolder = true;
			SmartFolder.init();
		}
		if (args[0].equals("-l"))
			mode = MODE_LISTNOTMO;
		if (args[0].equals("-ms")) 
			mode = MODE_SETMSEID;
		if (args[0].equals("-virtsnils")) 
			mode = MODE_SETVSNILS;
		if (args[0].equals("-f")) 
			mode = MODE_СREMOFOLDER;
		if (args[0].equals("-e")) 
			mode = MODE_EXTRACTZIP;
		if (args[0].equals("-t")) 
			mode = MODE_TESTDIR;
		if (args[0].equals("-force")) 
			mode = MODE_IPRAFORCE;
		if (args[0].equals("-setenddate")) 
			mode = MODE_ENDDATE;
		if (args[0].equals("-mkfrees")) 
			mode = MODE_MAKE_FREE_IPRA_FOR_SNILS;
		if (args[0].equals("-mkfreef")) 
			mode = MODE_MAKE_FREE_IPRA_FOR_FILE;
		if (args[0].equals("-testscaner")) 
			mode = MODE_TEST_SCANER;
		
		if (mode == MODE_UNKNOWN) {
			logger.error("Usage: java  -jar ipra -<svr> <directory with worked file>");
			return;

		}

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"META-INF/beans.xml");
		SnilsDAO snilsDAO = (SnilsDAO) context.getBean("storageSnils");
		MoDAO moDAO = (MoDAO) context.getBean("storageMo");
		PrgDAO prgDAO = (PrgDAO) context.getBean("storagePrg");
		Prg_rhbDAO prg_rhbDAO = (Prg_rhbDAO) context.getBean("storagePrg_rhb");
		MseDAO mse = (MseDAO) context.getBean("storageMse");


		
		ReaderSnils rd = (ReaderSnils) context.getBean("readerSnils");
//		FolderIpra folder = (FolderIpra) context.getBean("folderSnils");  другим способом определен bean -это тест для разнообразия
		FolderIpra folder = (FolderIpra) context.getBean("folder");
		IpraListNotMo listNotMo = (IpraListNotMo) context.getBean("listNotMo");

		if( mode == MODE_TEST_SCANER) {
			TestScaner.run(mode,  mse, snilsDAO, moDAO, context);
			logger.info("Done");
			return;
		}

		
		
		File dirSrc = new File(args[1]);
		if (!(dirSrc.exists() && dirSrc.isDirectory())) {
			logger.info("Not found folder:" + dirSrc);
			return;
		}
		String sDirComplete = args[1] + "\\" + DIR_COMLETE;
		File dirComplete = new File(sDirComplete);
		if (dirComplete.exists() && dirComplete.isDirectory()) {
			logger.info("Обработаные файлы перемещаются в папку:" + sDirComplete);
		} else {
			logger.info("Папка не найдена, будет создана сейчас:" + sDirComplete);
			dirComplete.mkdir();
		}
		String sDirError = args[1] + "\\" + DIR_ERROR;
		File dirError = new File(sDirError);
		if (dirError.exists() && dirError.isDirectory()) {
			logger.info("Файлы с ошибками наименования перемещаются в папку:"
					+ sDirError);
		} else {
			logger.info("Папка не найдена, будет создана сейчас:" + sDirError);
			dirError.mkdir();
		}

		
		if( mode == MODE_SETMSEID) {
			setMSEdata(MSE_UPDATE_ID, args[1], mse);
			logger.info("Done");
			return;
		}
		if( mode == MODE_ENDDATE) {
			setMSEdata(MSE_ENDDATE, args[1], mse);
			logger.info("Done");
			return;
		}
		else if( mode == MODE_IPRAFORCE) {
			setMSEdata(MSE_SENDER_MO, args[1], mse);
			logger.info("Done");
			return;
		}

		else if( mode == MODE_TESTDIR) {
			TestBaseIpra.start(args[1], mse, moDAO);
			logger.info("Done");
			return;
		}
		else if( mode == MODE_SETVSNILS) {
			VirtSnils.run(mse);
			logger.info("Done");
			return;
		}
		else if( mode == MODE_MAKE_FREE_IPRA_FOR_SNILS) {
			FreeSnils.run(FREE_BY_SNILS,mse,args[1],args[2], args[3], snilsDAO, moDAO, context);
			logger.info("Done");
			return;
		}
		else if( mode == MODE_MAKE_FREE_IPRA_FOR_FILE) {
			FreeSnils.run(FREE_BY_NAMEFILE,mse,args[1],args[2], args[3], snilsDAO, moDAO, context);
			logger.info("Done");
			return;
		}
		else if( mode == MODE_СREMOFOLDER || mode == MODE_MSE || mode == MODE_IPRAFORCE) {
			CreateFolderAllMo.run(sDirComplete, moDAO);
			logger.info("Done");
		    if(  mode == MODE_СREMOFOLDER )
			  return;
		}
		else if( mode == MODE_EXTRACTZIP || mode == MODE_RESULT || mode == MODE_SNILS) {
			Zip.ZipExtract(args[1]);
			logger.info("Done");
		    if(  mode == MODE_EXTRACTZIP )
			   return;
		}
 
			
		folder.setPath(args[1]);
		IpraFile fileNameObj = folder.getNextFile(mode);
        
		while (fileNameObj != null) {

			if (mode == MODE_SNILS) {
				getSnilsFunction(fileNameObj, rd, moDAO, sDirComplete, sDirError,
						snilsDAO, context);
			} else if (mode == MODE_MSE)
				mSEFunction(fileNameObj, moDAO, sDirComplete, sDirError, snilsDAO, mse);
			else if (mode == MODE_RESULT)
				resultIpraFunction(fileNameObj, moDAO, sDirComplete, sDirError,
						snilsDAO, prgDAO, prg_rhbDAO);
			fileNameObj = folder.getNextFile(mode);
		}

		if (mode == MODE_MSE) {
			VirtSnils.run(mse);                   //  автоматический запуск запроса на создание вирт снилс
		//  авто запуск формированиния списка неразобранных ИПРА выписок
			logger.info("Create List IPRA not MO:");
            String sFile = sDirComplete + "\\" + "IPRA_NOT_MO.txt";
			listNotMo.run(mse,sFile);
			CopyFileInFOlder(sFile, sDirComplete, context);
			logger.info("Done");
		//
			logger.info("Создание Zip архивов:");
			CreateZIPs(sDirComplete,"IR", context);
			logger.info("End!");
		}
		else if (mode == MODE_LISTNOTMO) {
			logger.info("Create List IPRA not MO:");
            String sFile = args[1] + "\\" + "IPRA_NOT_MO.txt";
			listNotMo.run(mse,sFile);
			CopyFileInFOlder(sFile, sDirComplete, context);
			logger.info("Done");
		}
		else if (mode == MODE_RESULT) {
			ReportRes.run(mse,moDAO,sDirComplete);
			logger.info("Создание Zip архивов:");
			CreateZIPs(sDirComplete,"IPRA_RESULT", context);	
			logger.info("Done");
		}
		else {	
			logger.info("End List File");
		}
	}

	private static void setMSEdata(int mode, String path, MseDAO mseDAO) {
    // для однократного выполнения - расставляем для уже обработанных файлов *. xml код MSEID в таблицу mse4    
        // определяем объект для каталога
        Mse mse;
		File dir = new File(path);
        // если объект представляет каталог
        if(dir.isDirectory())
        {
            // получаем все вложенные объекты в каталоге
            for(File item : dir.listFiles()){
                 if(item.isDirectory()){
                     System.out.println("каталог:" + item.getName() + "\n");
                     for(File item1 : item.listFiles()){
              			 String s = item1.getName(); 
            			 int pos_end   = s.lastIndexOf(".", s.length());
            			 String type = s.substring(pos_end+1).toUpperCase();
            			 if(type.equals("XML")) {
            					File f = new File(item1.getAbsolutePath());
                                System.out.println("файл1:"+ item1.getAbsolutePath() + "\n");

            					mse = mseDAO.getByNameFile(item1.getName());
            					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            					DocumentBuilder builder = null;
            					try {
            						builder = factory.newDocumentBuilder();
            					} catch (ParserConfigurationException e) {
            						// TODO Auto-generated catch block
            						e.printStackTrace();
            					}
            					Document document = null;
            					try {
            						document = builder.parse(f);
            					} catch (SAXException e) {
            						// TODO Auto-generated catch block
            						e.printStackTrace();
            					} catch (IOException e) {
            						// TODO Auto-generated catch block
            						e.printStackTrace();
            					}
            					Element root = document.getDocumentElement();
                                if( mode == MSE_UPDATE_ID) {
	            					Element MSEID = (Element) root.getElementsByTagName("Id").item(0);
	            					if (MSEID != null) {
	            						mse.setMseid(MSEID.getTextContent().toLowerCase());
	            						mseDAO.update(mse.getId(),mse);
	            					} else {
	            						mse.setMseid("");
	            					}
                                }	
                                else if( mode == MSE_SENDER_MO) {
	            					Element MSSENDERMO = (Element) root.getElementsByTagName("SenderMedOrgName").item(0);
	            					if (MSSENDERMO != null) {
	            						String ss =  MSSENDERMO.getTextContent();
	            						if( ss.length() > LEN_FIELD_SENDERMO){
    	            						ss = ss.substring(0,LEN_FIELD_SENDERMO-1);
                                        }
	            								
//	            						System.out.println("SenderMO = " + ss + " mse:" + mse);
                                        if(mse != null) { 
	                                      mse.setSender_mo(ss);
	            						  mseDAO.update(mse.getId(),mse);
                                        }  
	            					} else {
	                                    System.out.println("SenderMO = NOT FOUND");
	            					}    
	            					Element MSSENDERMO2 = (Element) root.getElementsByTagName("SentOrgName").item(0);
	            					if (MSSENDERMO2 != null) {
	            						String ss =  MSSENDERMO.getTextContent();
	            						if( ss.length() > LEN_FIELD_SENDERMO){
    	            						ss = ss.substring(0,LEN_FIELD_SENDERMO-1);
                                        }
                                        if(mse != null) { 
	                                      if( ss.length() > 0) {
	                                          mse.setSender_mo(ss);
		            						  mseDAO.update(mse.getId(),mse);
	                                      }	  
                                        }  
	            					} else {
	                                    System.out.println("SenderMO2017 = NOT FOUND");
	            					}
	            					Element MSEOGRN = (Element) root.getElementsByTagName("SentOrgOgrn").item(0);
	            					if (MSEOGRN != null) {
	            						String ss =  MSEOGRN.getTextContent();
                                        if(mse != null) { 
	                                      mse.setOgrn(ss);
	            						  mseDAO.update(mse.getId(),mse);
                                        }  
	            					} else {
	                                    System.out.println("SenderMO2017 OGRN = NOT FOUND");
	            					}
                                }	
                                else if( mode == MSE_ENDDATE) {
                            		Element ENDDATE = (Element) root.getElementsByTagName("EndDate").item(0);
                            		// 26/12/2016 дата окончания
                    				if (ENDDATE != null) {
                    					String s1 = ENDDATE.getTextContent();
                    					Date date = null;
                    					try {
                    						date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    								.parse(s1);
                    					} catch (ParseException e) {
                    						// TODO Auto-generated catch block
                    						e.printStackTrace();
                    					}
                    				    logger.trace("Дата окончания:" + date);
                                        if(mse != null) { 
                                          mse.setEnddate(date);
  	            						  mseDAO.update(mse.getId(),mse);
                                        }  
                    				}
                                }	
                                
            			 }
                     }
                     
                 }
                 else{
                     System.out.println(item.getName() + "\tфайл");
                 }
             }
        }
    }



	/*
	 * resultIpraFunction - обработка результатов ИПРА
	 */
	private static void resultIpraFunction(IpraFile fileNameObj, MoDAO mo,
			String sDirComplete, String sDirError, SnilsDAO snils,
			PrgDAO prgDAO, Prg_rhbDAO prg_rhb) {
		Mo m = null;

		if (!fileNameObj.ogrn.equals("999999999")) { // не ошибка
			logger.info("ИПРА результат из файла:" + fileNameObj.fullpath);
			// проверить, если запись по программе PRG, если есть , то обновить,
			// если нет - добавить
			// PRG_EXCH _ добавлять / изменять только для своей МО, побочный
			// эффект можно будет оследить
			// выполнение ИПРА для нескольких МО по одному СНИЛС
			m = mo.getByOgrn(fileNameObj.ogrn);
			if (m == null) {
				logger.error("ОГРН отсуствует в списке МО:" + fileNameObj.ogrn);
				Move(fileNameObj.fullpath, sDirError, true);
			} else {
				logger.debug("Мо определена:" + m.getName());
				ErrorMessage err = new ErrorMessage(m, fileNameObj.namefile);

				String dirDestinationComlpete = constructNameFolder(sDirComplete, m
						.getName().trim());
				CreateFolder(dirDestinationComlpete);

				
				if (ParseFieldPrg(err, fileNameObj, prgDAO, prg_rhb, m).lerror
						.size() == 0) {
					logger.info("Успешный разбор XML файла:"
							+ fileNameObj.ogrn);
					Move(fileNameObj.fullpath, dirDestinationComlpete, true);
				} else {
					logger.info("Обнаружены ошибки:" + fileNameObj.namefile);
					String dirDestinationError = constructNameFolder(sDirError, m
							.getName().trim());
			//		err.setDirDestination(dirDestinationError);
			//		CreateFolder(dirDestinationError);
					err.setDirDestination(dirDestinationComlpete);
					CreateFolder(dirDestinationComlpete);

					err.print();
			//		Move(fileNameObj.fullpath, dirDestinationError, true);
					Move(fileNameObj.fullpath, dirDestinationComlpete, true);
				}
			}
		} else { // плохое имя файла
			logger.error("Неправильное имя файла:" + fileNameObj.fullpath
					+ " .Файл перемещен в: " + sDirError);
			Move(fileNameObj.fullpath, sDirError, true);
		}
//		logger.debug("ИПРА результат обработан---------------------------");

	}

	/*
	 * Разбор файла с результатами программы
	 */

	private static ErrorMessage ParseFieldPrg(ErrorMessage err,
			IpraFile fileNameObj, PrgDAO prgDAO, Prg_rhbDAO prg_rhbDAO, Mo m) {

		Calendar cal;
		String ssnils = "";
		String snumprg = "";
		Prg prg = new Prg();

		File f = new File(fileNameObj.fullpath);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document document = null;
		try {
			document = builder.parse(f);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = document.getDocumentElement();

		Element actualDate = (Element) root.getElementsByTagName("ActualDate")
				.item(0);
		if (actualDate != null) {
			String s = actualDate.getTextContent();
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
						.parse(s);
			} catch (ParseException e) {
				// Переписать - сформировать ошибку, но не генерить останов
				// разбора
				// e.printStackTrace();
				err.add("Ошибка разбора поля", "actualDate");
			}
			prg.setDt(date);
		} else {
			prg.setDt(null);
			err.add("actualDate");
		}

		Element snils = (Element) root.getElementsByTagName("Snils").item(0);
		if (snils != null && !snils.getTextContent().equals("")) {
			ssnils = snils.getTextContent();
			prg.setSnils(ssnils);
		} else {
			prg.setSnils("");
// Разрешаю вводить результат без СНИЛС, в дальнейшем запись буду индентифицироыввть как
// Фамилия + Дара рождения + Дата программы ИПРА			
//			err.add("Snils");
		}
		Element prgNum = (Element) root.getElementsByTagName("PrgNum").item(0);
		if (prgNum != null) {
			snumprg = prgNum.getTextContent();
			prg.setPrgnum(snumprg);
		} else {
			prg.setPrgnum("");
			err.add("PrgNum");
		}
		// snils и prgnum - ключи для проверки есть ли в базе подобная запись
		// использование одного поля snuils считаю ненадежным - может быть у
		// человека более одной программы?
		List<Prg> lPrg = prgDAO.getBySnilsPrgNum(ssnils, snumprg);
			Element lname = (Element) root.getElementsByTagName("LName")
					.item(0);
			if (lname != null && !lname.getTextContent().equals("")) {
				prg.setLname(lname.getTextContent());
			} else {
				prg.setLname("");
				err.add("LName");
			}

			Element fname = (Element) root.getElementsByTagName("FName")
					.item(0);
			if (fname != null && !fname.getTextContent().equals("")) {
				prg.setFname(fname.getTextContent());
			} else {
				prg.setFname("");
				err.add("FName");
			}

			Element sname = (Element) root.getElementsByTagName("SName")
					.item(0);
			if (sname != null && !sname.getTextContent().equals("")) {
				prg.setSname(sname.getTextContent());
			} else {
				prg.setSname("");
				err.add("SName");
			}

			Element BDate = (Element) root.getElementsByTagName("BDate")
					.item(0);
			if (BDate != null) {
				String s = BDate.getTextContent();
				Date date = null;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
							.parse(s);
				} catch (ParseException e) {
					// Переписать - сформировать ошибку, но не генерить останов
					// разбора
					err.add("Ошибка разбора поля", "BDate");
				}
				prg.setBdate(date);
			} else {
				prg.setBdate(null);
				err.add("BDate");
			}

			Element gndr = (Element) root.getElementsByTagName("Gndr").item(0);
			if (gndr != null) {
				try {
					prg.setGndr(Integer.parseInt(gndr.getTextContent()));
				} catch (NumberFormatException e) {
					err.add("Ошибка формата поля", "Gndr");
				} catch (DOMException e) {
					err.add("Ошибка поля", "Gndr");
				}
			} else {
				prg.setGndr(0);
				err.add("Gndr");
			}

			Element docNum = (Element) root.getElementsByTagName("DocNum")
					.item(0);
			if (docNum != null && !docNum.getTextContent().equals("")) {
				prg.setDocnum(docNum.getTextContent());
			} else {
				prg.setDocnum("");
				err.add("DocNum");
			}

			Element docDT = (Element) root.getElementsByTagName("DocDT")
					.item(0);
			if (docDT != null) {
				String s = docDT.getTextContent();
				Date date = null;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
							.parse(s);
				} catch (ParseException e) {
					// Переписать - сформировать ошибку, но не генерить останов
					// разбора
					// e.printStackTrace();
					err.add("Ошибка разбора поля", "DocDT");
				}
				prg.setDocdt(date);
			} else {
				prg.setDocdt(null);
				err.add("DocDT");
			}

			Element pprg = (Element) root.getElementsByTagName("Prg").item(0);
			if (pprg != null) {
				try {
					prg.setPrg(Integer.parseInt(pprg.getTextContent()));
				} catch (NumberFormatException e) {
					err.add("Ошибка формата поля", "Prg");
				} catch (DOMException e) {
					err.add("Ошибка поля", "Prg");
				}
			} else {
				prg.setPrg(0);
				err.add("Prg");
			}

			Element prgNum1 = (Element) root.getElementsByTagName("PrgNum")
					.item(0);
			if (prgNum1 != null) {
				prg.setPrgnum(prgNum1.getTextContent());
			} else {
				prg.setPrgnum("");
				err.add("PrgNum");
			}

			Element prgDT = (Element) root.getElementsByTagName("PrgDT")
					.item(0);
			if (prgDT != null) {
				String s = prgDT.getTextContent();
				Date date = null;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
							.parse(s);
				} catch (ParseException e) {
					// Переписать - сформировать ошибку, но не генерить останов
					// разбора
					// e.printStackTrace();
					err.add("Ошибка разбора поля", "PrgDT");
				}
				prg.setPrgdt(date);
			} else {
				prg.setPrgdt(null);
				err.add("PrgDT");
			}
			Element mseid = (Element) root.getElementsByTagName("MSEID")
					.item(0);
			if (mseid != null) {
				prg.setMseid(mseid.getTextContent().toLowerCase());
			} else {
				prg.setMseid("");
				err.add("MSEID"); //Уже не так --- Сейчас я не буду считать это ошибкой!!!
				// - слишком жестоко и неправильно требовать заполнения с
				// пользователей!
			}

			// Установка общих константных полей для ЯО
			prg.setOivid(1); // Сфера охраны здоровья
			prg.setOkrId(1); // ЦФО
			prg.setNreg(76); // ЯО

			if (err.getSize() > 0)
				return err;
			if (lPrg.size() == 0) {
				prgDAO.save(prg);
			} else { // сохранение
				
				prgDAO.update(lPrg.get(0).getId(),prg);

			}	
		lPrg = prgDAO.getBySnilsPrgNum(ssnils, snumprg);
		if (lPrg.size() > 0) { // почему то не сохранилось
			prg = lPrg.get(0);
			Element res = (Element) root.getElementsByTagName("ListPRG_RHB")
					.item(0);
			Element resprg;
			Element typeid;
			Element evntid;
			Element dicid;
			Element tsrid;
			Element excid;
			if (res != null) {
				// удалим из базы prg_rhb все рузультаты по данной программе для
				// конкретной организации
				// записи о выполнении других организаций оставим!!!
				int num = prg_rhbDAO.delete(prg.getId(), m);
				logger.debug("Удалено " + num + " записей из prg_rhb");

				int numprg = root.getElementsByTagName("PRG_RHB").getLength();
				Element result;
				for (int i = 0; i < numprg; i++) {
					Prg_rhb prg_rhb = new Prg_rhb();
					prg_rhb.setPrgid(prg.getId());

					typeid = (Element) root.getElementsByTagName("TypeId")
							.item(i);
					if (typeid != null) {
						try {
							prg_rhb.setTypeid(Integer.parseInt(typeid
									.getTextContent()));
						} catch (NumberFormatException e) {
							err.add("Ошибка формата поля", "TypeId");
						} catch (DOMException e) {
							err.add("Ошибка поля", "TypeId");
						}
					} else {
						prg_rhb.setTypeid(0);
						err.add("TypeId");
					}

					evntid = (Element) root.getElementsByTagName("EvntId")
							.item(i);
					if (evntid != null) {
						try {
							prg_rhb.setEvntid(Integer.parseInt(evntid
									.getTextContent()));
						} catch (NumberFormatException e) {
							err.add("Ошибка формата поля", "EvntId");
						} catch (DOMException e) {
							err.add("Ошибка поля", "EvntId");
						}
					} else {
						prg_rhb.setEvntid(0);
						err.add("EvntId");
					}

					dicid = (Element) root.getElementsByTagName("DicId")
							.item(i);
					boolean isdicid = false;
					if (dicid != null) {
						try {
							String s = dicid.getTextContent();
							if (!s.equals("")) {
								prg_rhb.setDicid(Integer.parseInt(dicid
										.getTextContent()));
								isdicid = true;
							}
						} catch (NumberFormatException e) {
							err.add("Ошибка формата поля", "DicId");
						} catch (DOMException e) {
							err.add("Ошибка поля", "DicId");
						}
					} else {
						prg_rhb.setDicid(0);
						// err.add("DicId");
						isdicid = false;
					}
					Element name = (Element) root.getElementsByTagName("Name")
							.item(i);
					if (name != null) {
						try {
							String s = name.getTextContent();
							int len = s.length();
							if( len > 128) len = 128;
							s = s.substring(0, len);
							if (!s.equals("")) {
								prg_rhb.setName(s);
							}
							else {
								if (!isdicid)
									err.add("Name");
							}
						} catch (NumberFormatException e) {
							err.add("Ошибка формата поля", "Name");
						} catch (DOMException e) {
							err.add("Ошибка поля", "Name");
						}
					} else {
						prg_rhb.setName("");
						if (!isdicid)
							err.add("Name");
					}

					Element dt_exc = (Element) root.getElementsByTagName("Dt_Exc")
							.item(0);
					if (dt_exc != null) {
						String s = dt_exc.getTextContent();
						Date date = null;
						try {
							date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
									.parse(s);
						} catch (ParseException e) {
							// Переписать - сформировать ошибку, но не генерить останов
							// разбора
							// e.printStackTrace();
							err.add("Ошибка разбора поля", "Dt_Exc");
						}
						prg_rhb.setDt_exc(date);
					} else {
						prg_rhb.setDt_exc(null);
						err.add("Dt_Exc");
					}


					tsrid = (Element) root.getElementsByTagName("TsrId")
							.item(i);
					if (tsrid != null) {
						try {
							String s = tsrid.getTextContent();
							if (!s.equals(""))
								prg_rhb.setTsrid(Integer.parseInt(tsrid
										.getTextContent()));
						} catch (NumberFormatException e) {
							err.add("Ошибка формата поля", "TsrId");
						} catch (DOMException e) {
							err.add("Ошибка поля", "TsrId");
						}
					} else {
						prg_rhb.setTsrid(0);
					}

					excid = (Element) root.getElementsByTagName("Ex_Id")
							.item(i);
					if (excid != null) {
						try {
							String s = excid.getTextContent();
							if (!s.equals(""))
								prg_rhb.setExcid(Integer.parseInt(excid
										.getTextContent()));
						} catch (NumberFormatException e) {
							err.add("Ошибка формата поля", "Ex_Id");
						} catch (DOMException e) {
							err.add("Ошибка поля", "Ex_Id");
						}
					} else {
						prg_rhb.setExcid(0);
						err.add("Ex_Id");
					}
					Element execut = (Element) root.getElementsByTagName(
							"Execut").item(i);
					if (execut != null) {
						try {
							String s = execut.getTextContent();
							if (!s.equals("")) {
								prg_rhb.setExecut(s);
							}
						} catch (NumberFormatException e) {
							err.add("Ошибка формата поля", "Execut");
						} catch (DOMException e) {
							err.add("Ошибка поля", "Execut");
						}
					} else {
						prg_rhb.setExecut("");
					}

					Element resid = (Element) root
							.getElementsByTagName("ResId").item(i);
					boolean isresid = false;
					if (resid != null) {
						try {
							String s = resid.getTextContent();
							if (!s.equals("")) {
								prg_rhb.setResid(Integer.parseInt(resid
										.getTextContent()));
								isresid = true;
							}
						} catch (NumberFormatException e) {
							err.add("Ошибка формата поля", "ResId");
						} catch (DOMException e) {
							err.add("Ошибка поля", "ResId");
						}
					} else {
						prg_rhb.setResid(0);
						// err.add("DicId");
						isresid = false;
					}
					result = (Element) root.getElementsByTagName("Result")
							.item(i);
					if (result != null) {
						try {
							String s = result.getTextContent();
							if (!s.equals("")) {
								prg_rhb.setResult(s);
							}
							else {
								if (!isresid)
									err.add("Result");
							}

						} catch (NumberFormatException e) {
							err.add("Ошибка формата поля", "Result");
						} catch (DOMException e) {
							err.add("Ошибка поля", "Result");
						}
					} else {
						prg_rhb.setResult("");
						if (!isresid)
							err.add("Result");
					}
					Element par1 = (Element) root.getElementsByTagName("Par1")
							.item(i);
					if (par1 != null) {
						try {
							String s = par1.getTextContent();
							if (!s.equals(""))
								prg_rhb.setPar1(Integer.parseInt(par1
										.getTextContent()));
						} catch (NumberFormatException e) {
							err.add("Ошибка формата поля", "Par1");
						} catch (DOMException e) {
							err.add("Ошибка поля", "Par1");
						}
					} else {
						prg_rhb.setPar1(0);
					}
					Element par2 = (Element) root.getElementsByTagName("Par2")
							.item(i);
					if (par2 != null) {
						try {
							String s = par2.getTextContent();
							if (!s.equals(""))
								prg_rhb.setPar2(Integer.parseInt(par2
										.getTextContent()));
						} catch (NumberFormatException e) {
							err.add("Ошибка формата поля", "Par2");
						} catch (DOMException e) {
							err.add("Ошибка поля", "Par2");
						}
					} else {
						prg_rhb.setPar2(0);
					}
					Element par3 = (Element) root.getElementsByTagName("Par3")
							.item(i);
					if (par3 != null) {
						try {
							String s = par3.getTextContent();
							if (!s.equals(""))
								prg_rhb.setPar3(Integer.parseInt(par3
										.getTextContent()));
						} catch (NumberFormatException e) {
							err.add("Ошибка формата поля", "Par3");
						} catch (DOMException e) {
							err.add("Ошибка поля", "Par3");
						}
					} else {
						prg_rhb.setPar3(0);
					}
					Element note = (Element) root.getElementsByTagName("Note")
							.item(i);
					if (result != null) {
						try {
							String s = result.getTextContent();
							if (!s.equals("")) {
								prg_rhb.setNote(s);
							}

						} catch (NumberFormatException e) {
							err.add("Ошибка формата поля", "Note");
						} catch (DOMException e) {
							err.add("Ошибка поля", "Note");
						}
					} 

					if (err.getSize() == 0)
						prg_rhbDAO.save(prg_rhb);
				}
			}
		} else {
			logger.error("Неизвестная ошибка при сохранении программы prg");
		}

		return err;

	}

	/*
	 * MSEFunction - функция обрабатывает очередной XML-файл из МСЭ
	 */
	private static void mSEFunction(IpraFile fileNameObj, MoDAO mo,
			String sDirComlete, String sDirError, SnilsDAO snils, MseDAO mse)  {
		int newSnils = 0;
		int oldSnils = 0;
		String sSnils = null;
		
		if (!fileNameObj.ogrn.equals("999999999")) { // не ошибка

			logger.info("ИПРА выписка из файла:" + fileNameObj.fullpath);
			String s;
			// Есть ли СНИЛС в выписке?
			try {
                // Эту надо отключить позднее после проверки  pleload делает аналогично
				sSnils = getSnils(fileNameObj.fullpath, fileNameObj.namefile, mse);
				// 02/12/2026 Для получения id организации по строке SenderMedOrgnName в исходном XML документе
				// 09/03/2017 Для получения id организации по строке SentOrgOgrn (c 01/2017).
				preloadXML(fileNameObj.fullpath, fileNameObj.namefile, mse, mo); 
				// тестирование совпадений для проверки preloadXML
	//			if( !sSnils.equals(PreviewXML.snils)) {
	//				 System.out.println("Error sSnils=" + sSnils + "  PreviewXML.snils=" + PreviewXML.snils);
	//			     Error ref = new Error(); // создаем экземпляр
	//			     throw ref; 
	//			}
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Snils sn = null;
			Mo m = null;

			if (!sSnils.isEmpty()) {
				logger.info("поле СНИЛС в файле ИПРА:" + sSnils);
				// СНИЛС в базе есть? Если есть - тогда файла перемещаем в папку
				// для соотвествующей организации
				// и последующей отправки в МО
				// + 5.12.2016 отбор по строке SenderMO
				
				sn = snils.getById(sSnils);
				if (sn != null || PreviewXML.id_mo > 0) {
					if (sn != null) {
						logger.info("Найден СНИЛС в базе данных:" + sSnils);
						m = mo.getByOgrn(sn.getOgrn());
					}
					else {  // по строке мед организация в файле XML или по ОГРН в XML
						m = mo.getById(PreviewXML.id_mo);
						logger.info("СНИЛС в базе не найден, распределение ОГРН или по строке Sender_MO" 
				                 + Integer.toString(PreviewXML.id_mo) + ":" + m.getName());
					}
                     
					if (m == null) {
						logger.error("ОГРН отсуствует в списке МО:"
								+ sn.getOgrn());
					} else {
						logger.info("ОГРН:" + m.getOgrn() + ":"
								+ m.getName().trim());
						String nameFolder = constructNameFolder(sDirComlete, m
								.getName().trim());

						if (!isMSE(mse, fileNameObj.namefile))
							AddRecordMSE(fileNameObj, sn, m, mse);
						else {
							UpdateRecordMSE(fileNameObj, sn, m, mse);
							logger.info("ИПРА выписка из файла была ранее разнесена:"
									+ fileNameObj.fullpath);
						}
						logger.info("Перемещаем документ в папку:" + nameFolder);
						Move(fileNameObj.fullpath, nameFolder, true);
					}
				}
				else  { // 02/08/2016 СНИЛС в таблице не найден - все равно запись в базе создаем!! - так удобнее считать файлы выписок
					if (!isMSE(mse, fileNameObj.namefile))
						AddRecordMSE(fileNameObj, sn, m, mse);
					else {
                        
						logger.info("ИПРА выписка из файла была ранее разнесена:"
								+ fileNameObj.fullpath);
					}	
					logger.trace("Не найден СНИЛС в таблице SNILS:" + sSnils);
					
				}

			} else {
				if (!isMSE(mse, fileNameObj.namefile))
					AddRecordMSE(fileNameObj, sn, m, mse);
				else
					logger.info("ИПРА выписка из файла была ранее разнесена:"
							+ fileNameObj.fullpath);
				logger.info("Не найден СНИЛС в файле ИПРА:" + sSnils);
			}
		} else {
			logger.error("Неправильное имя файла:" + fileNameObj.fullpath);
			Move(fileNameObj.fullpath, sDirError, true);
		}
//		logger.info("ИПРА выписка обработана---------------------------");

	}

	private static void preloadXML(String fullpath, String namefile, MseDAO mse, MoDAO mo) throws ParserConfigurationException, SAXException, IOException {
		File f = new File(fullpath);
		MoParser mp = new MoParser();
		PreviewXML.id_mo = 0;
		PreviewXML.snils = "";
		Mo m = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(f);
		Element root = document.getDocumentElement();
//  c 2017 основной механизм ОГРН в ИПРА файле
		Element MSSENDERMO2 = (Element) root.getElementsByTagName("SentOrgOgrn").item(0);
		if (MSSENDERMO2 != null) {
			String ogrn =  MSSENDERMO2.getTextContent();
			m = mo.getByOgrn(ogrn);
			if( m !=  null) {
			  PreviewXML.id_mo = m.getId();
  			  System.out.println("preloadXML: found by ogrn: " + m.getName());
  			  return;
			}  
		} 

		Element SNILS = (Element) root.getElementsByTagName("SNILS").item(0);
		if (SNILS != null) {
			String textSNILS = SNILS.getTextContent(); // тоже для упрощения
			if( !textSNILS.isEmpty())
			   PreviewXML.snils = textSNILS;
			else
		  	   PreviewXML.snils = findVirtSnils(namefile,mse);	
		}
		else {
		  PreviewXML.snils = findVirtSnils(namefile,mse);	
	    }
		Element MSSENDERMO = (Element) root.getElementsByTagName("SenderMedOrgName").item(0);
		if (MSSENDERMO != null) {
/* 12.12.2017 Отключаю механизм автоматического поиска МО по подстроке в ИПРА программе
			String ss =  MSSENDERMO.getTextContent();
			if( ss.length() > LEN_FIELD_SENDERMO){
				ss = ss.substring(0,LEN_FIELD_SENDERMO-1);
            }
			PreviewXML.id_mo = mp.doParse(ss);					
*/
		} 
//		System.out.println("preloadXML: snils="  + PreviewXML.snils + " id_mo=" + Integer.toString(PreviewXML.id_mo) );
		
	}


// update MSE - установка МО
	private static void UpdateRecordMSE(IpraFile fileNameObj, Snils sn, Mo m,
			MseDAO mseDAO) {
		Mse mse = mseDAO.getByNameFile(fileNameObj.namefile);
		if (m != null) {
			mse.setIdMo(m.getId());
			if(sn == null)
			   mse.setAutoSelect(true);
			else
			   mse.setAutoSelect(false);
			mseDAO.update(mse.getId(), mse);
		}
		
		
	}

	// проверка ранее занесенной записи, для предотвращения повторного ввода

	private static boolean isMSE(MseDAO mse, String namefile) {
		Mse m = mse.getByNameFile(namefile);
		if (m != null)
			return true;
		else
			return false;
	}

	// Добавление MSE - выписка моя таблица - для контроля выписок
	// решил добавить таблицу MSE, потому что PRG таблица предназначена для
	// результатов и сложно по ней считать выписки,
	// особенно выделять - выписки с найдеными МО или еще необработанные

	private static void AddRecordMSE(IpraFile fileNameObj, Snils sn, Mo mo,
			MseDAO mseDAO) {
		Mse mse = mseDAO.getByNameFile(fileNameObj.namefile);
		if (mse == null) {
			saveFieldsMse(fileNameObj, sn, mo, mseDAO);
		}

	}

	private static void saveFieldsMse(IpraFile fileNameObj, Snils sn, Mo mo,
			MseDAO mseDAO) {

		Mse mse = new Mse();
		File f = new File(fileNameObj.fullpath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document document = null;
		try {
			document = builder.parse(f);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = document.getDocumentElement();

		Element SNILS = (Element) root.getElementsByTagName("SNILS").item(0);
		if (SNILS != null) {
			mse.setSnils(SNILS.getTextContent());
		} else {
			mse.setSnils("");
		}
		Element FNAME = (Element) root.getElementsByTagName("ct:FirstName")
				.item(0);
		if (FNAME != null) {
			mse.setFname(FNAME.getTextContent());
		} else {
			mse.setFname("");
		}
		Element SNAME = (Element) root.getElementsByTagName("ct:SecondName")
				.item(0);
		if (SNAME != null) {
			mse.setSname(SNAME.getTextContent());
		} else {
			mse.setSname("");
		}
		Element LNAME = (Element) root.getElementsByTagName("ct:LastName")
				.item(0);
		if (LNAME != null) {
			mse.setLname(LNAME.getTextContent());
		} else {
			mse.setLname("");
		}

		Element BDATE = (Element) root.getElementsByTagName("BirthDate").item(0);

		if (BDATE != null) {
			String s = BDATE.getTextContent();
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
						.parse(s);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// logger.info("Дата рождения:" + date);
			mse.setBdate(date);
		} else {
			mse.setBdate(null);
		}

		Element PRGDATE = (Element) root.getElementsByTagName("ProtocolDate").item(0);

		if (PRGDATE != null) {
			String s = PRGDATE.getTextContent();
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
						.parse(s);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    logger.trace("Дата протокола:" + date);
			mse.setPrgdate(date);
		} else {
			mse.setPrgdate(null);
		}
		Element ENDDATE = (Element) root.getElementsByTagName("EndDate").item(0);
// 26/12/2016 дата окончания
		if (ENDDATE != null) {
			String s = ENDDATE.getTextContent();
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
						.parse(s);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    logger.trace("Дата окончания:" + date);
			mse.setEnddate(date);
		} else {
			mse.setEnddate(null);
		}
		
		Element MSEID = (Element) root.getElementsByTagName("Id").item(0);
		if (MSEID != null) {
			mse.setMseid(MSEID.getTextContent().toLowerCase());
		} else {
			mse.setMseid("");
		}
// В программах до 2017 года
		Element MSSENDERMO = (Element) root.getElementsByTagName("SenderMedOrgName").item(0);
		if (MSSENDERMO != null) {
			String s = MSSENDERMO.getTextContent();
			int pos = s.length() - LEN_FIELD_SENDERMO;
			if( pos > 0) s = s.substring(pos);
			mse.setSender_mo(s);
		} else {
			mse.setSender_mo("");
		}
// В программах с 2017 года
		Element MSSENDERMO2 = (Element) root.getElementsByTagName("SentOrgName").item(0);
		if (MSSENDERMO2 != null) {
			String s = MSSENDERMO2.getTextContent();
			int pos = s.length() - LEN_FIELD_SENDERMO;
			if( pos > 0) s = s.substring(pos);
			mse.setSender_mo(s);
		} else {
			mse.setSender_mo("");
		}
		Element SOGRN = (Element) root.getElementsByTagName("SentOrgOgrn")
				.item(0);
		if (SOGRN != null) {
			mse.setOgrn(SOGRN.getTextContent());
		} else {
			mse.setOgrn("");
		}
// --- 2017
		
		if(mo != null)
		   mse.setIdMo(mo.getId());
		else
 	       mse.setIdMo(0);
// 01/12/2016  Установка флага auto , если это автоматическое распределение		
		if(sn == null && mo != null)
		   mse.setAutoSelect(true);

		mse.setDt(new Date());
		mse.setNameFile(fileNameObj.namefile);

		mse.setComplete(false);
	
		mseDAO.save(mse);

	}

	public static String constructNameFolder(String sDaseDir, String sMo) {
		if (sMo != null) {
			sMo = sMo.toUpperCase().replace("ГОСУДАРСТВЕННОЕ", "");
			sMo = sMo.replace("БЮДЖЕТНОЕ", "");
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

			return sDaseDir + "\\" + sMo;
		}
		return null;
	}

	public static String synteticNameMo(String source) {
		if (source != null) {
			source = source.toUpperCase().replace("ГОСУДАРСТВЕННОЕ", "");
			source = source.replace("БЮДЖЕТНОЕ", "");
			source = source.replace("УЧРЕЖДЕНИЕ", "");
			source = source.replace("ЯРОСЛАВСКОЙ", "");
			source = source.replace("ОБЛАСТИ", "");
			source = source.replace("ЗДРАВООХРАНЕНИЯ", "");
			source = source.replace('\\', ' ');
			source = source.replace('/', ' ');
			source = source.replace('.', ' ');
			source = source.replace('"', ' ');
			source = source.replace(',', ' ');
			source = source.replace(':', ' ');
			source = source.replace("ГБУЗ", "");
			source = source.replace("БУЗ", "");
			source = source.replace("ЯО", "");
			source = source.replace("ГАУЗ", "");
			source = source.replace("ГУЗ", "");
			source = source.replace("№ ", "№");
			source = source.trim();

			return source;
		}
		return null;
	}
	
	
	private static void CreateZIPs(String sDirComplete, String pref, BeanFactory context) {
//		FolderIpra folder = new FolderIpraImpl();
		FolderIpra folder =  (FolderIpra) context.getBean("folder");
		folder.setPath(sDirComplete);
		IpraFile fileNameObj = folder.getNextDir();

		while (fileNameObj != null) {
			CreateZIP(fileNameObj.fullpath, pref);
			fileNameObj = folder.getNextDir();
		}

	}
	
// копировать файл по все поддиректории для папки 	
	private static void CopyFileInFOlder(String sFile, String sDirComplete, BeanFactory context) {
		FolderIpra folder =  (FolderIpra) context.getBean("folder");
		folder.setPath(sDirComplete);
		IpraFile fileNameObj = folder.getNextDir();
		while (fileNameObj != null) {
			logger.info("Folder MO:" + fileNameObj.fullpath);
			Copy(sFile, fileNameObj.fullpath);
			fileNameObj = folder.getNextDir();
		}

	}

	private static void CreateZIP(String sDirComplete, String pref) {
        if( !smartFolder || SmartFolder.isFolder(sDirComplete)) {
			logger.info("Folder MO:" + sDirComplete);
			File directory = new File(sDirComplete);
			java.util.Calendar calendar = java.util.Calendar.getInstance(
					java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
			int year = calendar.get(java.util.Calendar.YEAR);
			int month = calendar.get(java.util.Calendar.MONTH) + 1;
			String smonth;
			if (month < 10)
				smonth = "0" + month;
			else
				smonth = "" + month;
	
			File zipFile = new File(sDirComplete + "\\" + pref + "_" + (year - 2000) + smonth
					+ ".zip");
	
			try {
				Zip.directoryToZip(directory, zipFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } 
	}

	private static String getSnils(String fullpath, String namefile, MseDAO mse)
			throws ParserConfigurationException, SAXException, IOException {
		File f = new File(fullpath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(f);
		Element root = document.getDocumentElement();

		Element SNILS = (Element) root.getElementsByTagName("SNILS").item(0);
		if (SNILS != null) {
			String textSNILS = SNILS.getTextContent(); // тоже для упрощения
			if( !textSNILS.isEmpty())
			   return textSNILS;
			else
			   return findVirtSnils(namefile,mse);	
		} else {
	      return findVirtSnils(namefile,mse);	
	    }
	}


	/* 07.10.2016 прежде чем выдавать пустое значение - по имени файла поищем в списке mse4 вирт снилс - может он уже занесен
	 * тогда возвратим его и файл распределится как нормальные */
	
	private static String findVirtSnils(String namefile, MseDAO mse) {
		Mse m = mse.getByNameFile(namefile);
		if( m != null) {
			return m.getSnils().trim();
		}
		else
		  return "";
	}



	/*
	 * GetSnilsFunction - функция обрабатывает очередной СНИЛС файл
	 */
	private static void getSnilsFunction(IpraFile fileNameObj, ReaderSnils rd,
			MoDAO mo, String sDirComlete, String sDirError, SnilsDAO snils, BeanFactory context) {
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
						//sn = new Snils();
						sn = (Snils) context.getBean(Snils.class);

						sn.setSnils(s);
						sn.setOgrn(fileNameObj.ogrn);
						snils.save(sn);
						newSnils++;
					} else { 
						Snils su = (Snils) context.getBean(Snils.class);
                        if(!fileNameObj.ogrn.trim().equalsIgnoreCase(sn.getOgrn().trim())) {
            			   logger.info("Found update snils:" + sn.getOgrn().trim() + ":" + fileNameObj.ogrn.trim());
						   su.setSnils(s);
						   su.setOgrn(fileNameObj.ogrn);
						   snils.update(sn.getSnils(),su);
						   
                        }   
                        
						oldSnils++;
					}	
					// logger.info("СНИЛС уже есть базе:" + s);

					s = rd.getNextSnils();
				}
				rd.close();
				Move(fileNameObj.fullpath, sDirComlete);
			}
		} else {
			logger.error("Error Name File:" + fileNameObj.fullpath);
			Move(fileNameObj.fullpath, sDirError);

		}
		logger.info("Всего СНИЛС:" + (newSnils + oldSnils) + " в т.ч новых:"
				+ newSnils);

	}

	public static void Move(String fullpath, String sDirDestination) {
        if( smartFolder )
        	SmartFolder.add(sDirDestination);
		Path movefrom = FileSystems.getDefault().getPath(fullpath);
		Path target_dir = FileSystems.getDefault().getPath(sDirDestination);
		try {
			Files.move(movefrom, target_dir.resolve(movefrom.getFileName()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	private static void Copy(String fullpath, String sDirDistination) {
		Path copyfile = FileSystems.getDefault().getPath(fullpath);
		Path target_dir = FileSystems.getDefault().getPath(sDirDistination);
		try {
			Files.copy(copyfile, target_dir.resolve(copyfile.getFileName()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	private static void Move(String fullpath, String sDirDistination,
			boolean createFolder) {
		if (createFolder) {
			File dirDest = new File(sDirDistination);
			if (dirDest.exists() && dirDest.isDirectory()) {
			} else {
				dirDest.mkdir();
			}
		}
		Move(fullpath, sDirDistination);
	}

	public static void CreateFolder(String sDirDistination) {
		File dirDest = new File(sDirDistination);
		if (dirDest.exists() && dirDest.isDirectory()) {
		} else {
			dirDest.mkdir();
		}

	}

}
