import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import akayerov.getsnils.FolderIpra;
import akayerov.getsnils.FolderIpraImpl;
import akayerov.getsnils.IpraFile;
import akayerov.getsnils.Program;
import akayerov.getsnils.ReaderSnils;
import akayerov.getsnils.Zip;
import akayerov.getsnils.dao.MoDAO;
import akayerov.getsnils.dao.MseDAO;
import akayerov.getsnils.dao.PrgDAO;
import akayerov.getsnils.dao.SnilsDAO;


public class TestZip {
	private static final Logger logger = Logger.getLogger(Program.class);
	private static final String DIR_COMLETE = "COMPLETE";
	private static final String DIR_ERROR = "ERROR";

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
			logger.info("Файлы с ошибками перемещаются в папку:" + sDirError);
		} else {
			logger.info("Папка не найдена, будет создана сейчас:" + sDirError);
			dirError.mkdir();
		}

		CreateZIPs(sDirComlete);
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
		
		File zipFile = new File(sDirComlete +"\\VI_" + (year-2000) + smonth + "_IPRA.zip");

		
		try {
			Zip.directoryToZip(directory, zipFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
