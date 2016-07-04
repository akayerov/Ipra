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
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
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

public class Program {

	private static final Logger logger = Logger.getLogger(Program.class);
	private static final String DIR_COMLETE = "COMPLETE";
	private static final String DIR_ERROR = "ERROR";
	private static final String DIR_SUCCESS = "Успешно";
	private static final String DIR_FAIL    = "Неудача";

	private static final int MODE_UNKNOWN = 0;
	public static final int MODE_SNILS = 1;
	public static final int MODE_MSE = 2;
	public static final int MODE_RESULT = 3;
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
		if (args[0].equals("-s"))
			mode = MODE_SNILS;
		if (args[0].equals("-v"))
			mode = MODE_MSE;
		if (args[0].equals("-r"))
			mode = MODE_RESULT;
		if (mode == MODE_UNKNOWN) {
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
		PrgDAO prg = (PrgDAO) context.getBean("storagePrg");
		Prg_rhbDAO prg_rhb = (Prg_rhbDAO) context.getBean("storagePrg_rhb");
		MseDAO mse = (MseDAO) context.getBean("storageMse");

		ReaderSnils rd = (ReaderSnils) context.getBean("readerSnils");
		FolderIpra folder = (FolderIpra) context.getBean("folderSnils");

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
			logger.info("Файлы с ошибками наименования перемещаются в папку:" + sDirError);
		} else {
			logger.info("Папка не найдена, будет создана сейчас:" + sDirError);
			dirError.mkdir();
		}

		folder.setPath(args[1]);
		IpraFile fileNameObj = folder.getNextFile(mode);

		while (fileNameObj != null) {

			if (mode == MODE_SNILS) {
				getSnilsFunction(fileNameObj, rd, mo, sDirComlete, sDirError,
						snils);
			} else if (mode == MODE_MSE)
				mMSEFunction(fileNameObj, mo, sDirComlete, sDirError, snils, mse);
		      else if (mode == MODE_RESULT)
		 	   resultIpraFunction(fileNameObj, mo, sDirComlete, sDirError, snils, prg, prg_rhb);
			fileNameObj = folder.getNextFile(mode);
		}

		if (mode == MODE_MSE) {
			logger.info("Создание Zip архивов:");
			CreateZIPs(sDirComlete);
		}
	}


	/*
	 * resultIpraFunction - обработка результатов ИПРА
	 */
	
	private static void resultIpraFunction(IpraFile fileNameObj, MoDAO mo,
			String sDirComlete, String sDirError, SnilsDAO snils, PrgDAO prgDAO, Prg_rhbDAO prg_rhb) {
		Mo m = null;
		if (!fileNameObj.ogrn.equals("999999999")) { // не ошибка
  
			logger.info("ИПРА результат из файла:" + fileNameObj.fullpath);
            // проверить, если запись по программе PRG, если есть , то обновить, если нет - добавить
            // PRG_EXCH _ добавлять / изменять только для своей МО, побочный эффект можно будет оследить
			// выполнение ИПРА для нескольких МО по одному СНИЛС
			m = mo.getByOgrn(fileNameObj.ogrn);
			if (m == null) {
			  logger.error("ОГРН отсуствует в списке МО:" + fileNameObj.ogrn);
			  Move(fileNameObj.fullpath, sDirError, true);
			}  
			else {  
		 	  logger.debug("Мо определена:" + m.getName());
		 	  if(ParseFieldPrg(fileNameObj, prgDAO, prg_rhb) == 0) {
			 	  logger.debug("Успешный разбор XML файла:" + fileNameObj.ogrn);
		 	  }
		 	  else {
			 	  logger.info("Обнаружены ошибки:" + fileNameObj.ogrn);
		 	  }
		 	  
//			  Move(fileNameObj.fullpath, sDirComlete, true);
			}    
		}
		else {  // плохое имя файла
		   logger.error("Неправильное имя файла:" + fileNameObj.fullpath + " .Файл перемещен в: " + sDirError);
		   Move(fileNameObj.fullpath, sDirError, true);
		}
		logger.debug("ИПРА результат обработан---------------------------");
		
	}

/*
  Разбор файла с результатами программы 
 */	
	
	private static int ParseFieldPrg(IpraFile fileNameObj, PrgDAO prgDAO, Prg_rhbDAO prg_rhbDAO) {
		
		Calendar cal;
		String ssnils = null;
   	    Prg prg = new Prg();
   	    Prg_rhb prg_rhb = new Prg_rhb();

        int num_err = 0;
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

		
		Element actualDate = (Element) root.getElementsByTagName("ActualDate").item(0);
		if (actualDate != null) {
			String s = actualDate.getTextContent();
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(s);
			} catch (ParseException e) {
// Переписать - сформировать ошибку, но не генерить останов разбора
				e.printStackTrace();
                num_err++;
			} 
			prg.setDt(date);
		} else {
			prg.setDt(null);
            num_err++;
		}
		
		Element snils = (Element) root.getElementsByTagName("Snils").item(0);
		if (snils != null && !snils.getTextContent().equals("")) {
            ssnils = snils.getTextContent();
			prg.setSnils(ssnils);
		} else {
			prg.setSnils("");
            num_err++;
		}
		
		Element lname = (Element) root.getElementsByTagName("LName").item(0);
		if (lname != null && !lname.getTextContent().equals("")) {
			prg.setLname(lname.getTextContent());
		} else {
			prg.setLname("");
            num_err++;
		}
		
		Element fname = (Element) root.getElementsByTagName("FName").item(0);
		if (fname != null  && !fname.getTextContent().equals("")) {
			prg.setFname(fname.getTextContent());
		} else {
			prg.setFname("");
            num_err++;
		}

		Element sname = (Element) root.getElementsByTagName("SName").item(0);
		if (sname != null  && !sname.getTextContent().equals("")) {
			prg.setSname(sname.getTextContent());
		} else {
			prg.setSname("");
            num_err++;
		}
		
		Element BDate = (Element) root.getElementsByTagName("BDate").item(0);
		if (BDate != null ) {
			String s = BDate.getTextContent();
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(s);
			} catch (ParseException e) {
// Переписать - сформировать ошибку, но не генерить останов разбора
				e.printStackTrace();
                num_err++;
			}
			prg.setBdate(date);
		} else {
			prg.setBdate(null);
            num_err++;
		}
		
		Element gndr = (Element) root.getElementsByTagName("Gndr").item(0);
		if (gndr != null) {
			try {
				prg.setGndr(Integer.parseInt(gndr.getTextContent()));
			} catch (NumberFormatException e) {
	            num_err++;
			} catch (DOMException e) {
	            num_err++;
			}
		} else {
			prg.setGndr(0);
            num_err++;
		}
		
		Element docNum = (Element) root.getElementsByTagName("DocNum").item(0);
		if (docNum != null  && !docNum.getTextContent().equals("")) {
			prg.setDocnum(docNum.getTextContent());
		} else {
			prg.setDocnum("");
            num_err++;
		}

		Element docDT = (Element) root.getElementsByTagName("DocDT").item(0);
		if (docDT != null) {
			String s = docDT.getTextContent();
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(s);
			} catch (ParseException e) {
// Переписать - сформировать ошибку, но не генерить останов разбора
				e.printStackTrace();
                num_err++;
			}
			prg.setDocdt(date);
		} else {
			prg.setDocdt(null);
            num_err++;
		}
		
		Element pprg = (Element) root.getElementsByTagName("Prg").item(0);
		if (pprg != null) {
			try {
				prg.setPrg(Integer.parseInt(pprg.getTextContent()));
			} catch (NumberFormatException e) {
	            num_err++;
			} catch (DOMException e) {
	            num_err++;
			}
		} else {
			prg.setPrg(0);
            num_err++;
		}

		Element prgNum = (Element) root.getElementsByTagName("PrgNum").item(0);
		if (prgNum != null) {
			prg.setPrgnum(prgNum.getTextContent());
		} else {
			prg.setPrgnum("");
            num_err++;
		}

		Element prgDT = (Element) root.getElementsByTagName("PrgDT").item(0);
		if (prgDT != null) {
			String s = prgDT.getTextContent();
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(s);
			} catch (ParseException e) {
// Переписать - сформировать ошибку, но не генерить останов разбора
				e.printStackTrace();
                num_err++;
			}
			prg.setPrgdt(date);
		} else {
			prg.setPrgdt(null);
            num_err++;
		}
		Element mseid = (Element) root.getElementsByTagName("MSEID").item(0);
		if (mseid != null) {
			prg.setMseid(mseid.getTextContent());
		} else {
			prg.setMseid("");
//            num_err++;
		}
		
// Установка общих константных полей для ЯО
		prg.setOivid(1);    // Сфера охраны здоровья
		prg.setOkrId(1);    // ЦФО
		prg.setNreg(76);    // ЯО
		
		

		if(num_err > 0)
			return num_err;
		
		prgDAO.save(prg);
	
		prg =  prgDAO.getBySnils(ssnils);

		
		Element res = (Element) root.getElementsByTagName("ListPRG_RHB").item(0);
		Element resprg;
		Element typeid;
		Element evntid;
		Element dicid;
		Element tsrid;
		Element excid;
		
		
		
		if (res != null) {
			int numprg = root.getElementsByTagName("PRG_RHB").getLength();
			Element result;
			for(int i=0; i<numprg; i++) {
			   prg_rhb.setPrgid(prg.getId());

			   typeid = (Element) root.getElementsByTagName("TypeId").item(i);
			   prg_rhb.setTypeid(Integer.parseInt(typeid.getTextContent()));

			   evntid = (Element) root.getElementsByTagName("EvntId").item(i);
			   prg_rhb.setEvntid(Integer.parseInt(evntid.getTextContent()));

			   dicid = (Element) root.getElementsByTagName("DicId").item(i);
			   prg_rhb.setDicid(Integer.parseInt(dicid.getTextContent()));
			   
			   tsrid = (Element) root.getElementsByTagName("TsrId").item(i);
			   prg_rhb.setTsrid(Integer.parseInt(tsrid.getTextContent()));
				
			   excid = (Element) root.getElementsByTagName("Ex_Id").item(i);
			   prg_rhb.setExcid(Integer.parseInt(excid.getTextContent()));

			   result = (Element) root.getElementsByTagName("Result").item(i);
			   prg_rhb.setResult(result.getTextContent());
  			   prg_rhbDAO.save(prg_rhb);
			}
		} else {
		}

		
		
		
		
		
		return num_err;
		
	}


	/*
	 * MSEFunction - функция обрабатывает очередной XML-файл из МСЭ
	 */
	private static void mMSEFunction(IpraFile fileNameObj, MoDAO mo,
			String sDirComlete, String sDirError, SnilsDAO snils, MseDAO mse) {
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
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Snils sn = null;
			Mo m = null;

			if (!sSnils.isEmpty()) {
				logger.info("Найден СНИЛС в файле ИПРА:" + sSnils);
				// СНИЛС в базе есть? Если есть - тогда файла перемещаем в папку
				// для соотвествующей организации
				// и последующей отправки в МО
				sn = snils.getById(sSnils);
				if (sn != null) {
					logger.info("Найден СНИЛС в базе данных:" + sSnils);
					m = mo.getByOgrn(sn.getOgrn());
					if (m == null) {
						logger.error("ОГРН отсуствует в списке МО:"
								+ sn.getOgrn());
					} else {
						logger.info("ОГРН:" + sn.getOgrn() + ":"
								+ m.getName().trim());
						String nameFolder = constructNameFolder(sDirComlete, m
								.getName().trim());

						if (!isMSE(mse,fileNameObj.namefile)) 
						    AddRecordMSE(fileNameObj, sn, m, mse);
						else
							logger.info("ИПРА выписка из файла была ранее разнесена:" + fileNameObj.fullpath);
				
						logger.info("Перемещаем документ в папку:" + nameFolder);
						Move(fileNameObj.fullpath, nameFolder, true);
					}

				}

			} else {
				if (!isMSE(mse,fileNameObj.namefile)) 
				    AddRecordMSE(fileNameObj, sn, m, mse);
				else
					logger.info("ИПРА выписка из файла была ранее разнесена:" + fileNameObj.fullpath);
				logger.info("НЕ Найден СНИЛС в файле ИПРА:" + sSnils);
			}
		} else {
			logger.error("Неправильное имя файла:" + fileNameObj.fullpath);
			Move(fileNameObj.fullpath, sDirError, true);
		}
		logger.info("ИПРА выписка обработана---------------------------");

	}

	// проверка ранее занесенной записи, для предотвращения повторного ввода
	
	private static boolean isMSE(MseDAO mse, String namefile) {
		Mse m = mse.getByNameFile(namefile);
		if( m != null)
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
			saveFieldsMse(fileNameObj, sn, mo, mseDAO );
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
		Element FNAME = (Element) root.getElementsByTagName("ct:FirstName").item(0);
		if (FNAME != null) {
			mse.setFname(FNAME.getTextContent());
		} else {
			mse.setFname("");
		}
		Element SNAME = (Element) root.getElementsByTagName("ct:SecondName").item(0);
		if (SNAME != null) {
			mse.setSname(SNAME.getTextContent());
		} else {
			mse.setSname("");
		}
		Element LNAME = (Element) root.getElementsByTagName("ct:LastName").item(0);
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
				date = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(s);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			logger.info("Дата рождения:" + date);
			mse.setBdate(date);
		} else {
			mse.setBdate(null);
		}
		
		mse.setIdMo(mo.getId());
		mse.setDt(new Date());
		mse.setNameFile(fileNameObj.namefile);
		
		mseDAO.save(mse);

		
	}

	private static String constructNameFolder(String sDaseDir, String sMo) {
		if (sMo != null) {
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

			return sDaseDir + "\\" + sMo;
		}
		return null;
	}

	
	private static void CreateZIPs(String sDirComlete) {
		FolderIpra folder = new FolderIpraImpl();
		folder.setPath(sDirComlete);
		IpraFile fileNameObj = folder.getNextDir();

		while (fileNameObj != null) {
			logger.info("Папка c МО найдена:" + fileNameObj.fullpath);
			CreateZIP(fileNameObj.fullpath);
			fileNameObj = folder.getNextDir();
		}
		
		
	}

	private static void CreateZIP(String sDirComlete) {
		File directory = new File(sDirComlete);
		java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
		int year = calendar.get(java.util.Calendar.YEAR);
		int month = calendar.get(java.util.Calendar.MONTH)+1;
		String smonth;
		if (month<10)
			smonth = "0" + month;
		else
			smonth = "" + month;
		
		File zipFile = new File(sDirComlete +"\\IR_" + (year-2000) + smonth + ".zip");

		
		try {
			Zip.directoryToZip(directory, zipFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	
	private static String getSnils(String fullpath)
			throws ParserConfigurationException, SAXException, IOException {
		File f = new File(fullpath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(f);
		Element root = document.getDocumentElement();

		Element SNILS = (Element) root.getElementsByTagName("SNILS").item(0);
		if (SNILS != null) {
			String textSNILS = SNILS.getTextContent(); // тоже для упрощения
			return textSNILS;
		} else {
			return "";
		}
	}

	/*
	 * GetSnilsFunction - функция обрабатывает очередной СНИЛС файл
	 */
	private static void getSnilsFunction(IpraFile fileNameObj, ReaderSnils rd,
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
		Path movefrom = FileSystems.getDefault().getPath(fullpath);
		Path target_dir = FileSystems.getDefault().getPath(sDirDistination);
		try {
			Files.move(movefrom, target_dir.resolve(movefrom.getFileName()),
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
	
	private static void CreateFolder(String sDirDistination) {
			File dirDest = new File(sDirDistination);
			if (dirDest.exists() && dirDest.isDirectory()) {
			} else {
				dirDest.mkdir();
			}

	}

}
