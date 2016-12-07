package akayerov.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;



import akayerov.getsnils.ErrorMessage;
import akayerov.getsnils.Program;
import akayerov.getsnils.dao.MoDAO;
import akayerov.getsnils.dao.MseDAO;
import akayerov.getsnils.models.Mo;
import akayerov.getsnils.models.Mse;

public class ReportRes {
    private static BufferedWriter bufferedWriter;
    private static FileOutputStream fileOutputStream;
    
	public static void run(MseDAO mseDAO, MoDAO moDAO, String sDirComlete) {
    	fileOutputStream = null;
    	bufferedWriter = null;
    	
    	List<Mse> mseList = mseDAO.listOrderedMo();
    	int idmo = 0;
    	for(Mse mse: mseList) {
    		if(idmo != mse.getIdMo() && mse.getIdMo() > 0) {
        		idmo = mse.getIdMo();
        		System.out.println("Медицинская оганизация. Код= " + mse.getIdMo());
        		if( createFileAndHeader(mse.getIdMo(),moDAO,sDirComlete) == 0)
        			continue;
    			
    		}
			try {
				bufferedWriter.write(mse.getSnils() +  ";" + mse.getLname() + ";" + mse.getFname() + ";"
						+ mse.getSname() + ";" + mse.getBdate() + ";"
						 + mse.getPrgdate() + ";" + mse.isComplete());
				bufferedWriter.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();   эти исключения встечаются, пока не знаю почему, поскольку при этом работает
			}

//    		System.out.println(mse.getLname() + " " + mse.getFname() + mse.getSname() + mse.getIdMo());
    	}
    	close_file();
    }

	private static int createFileAndHeader(Integer idMo, MoDAO moDAO, String sDirComlete) {
        int rc = 0;

		close_file();
		Mo mo = moDAO.getById(idMo);
		String dirDestinationComlete = Program.constructNameFolder(sDirComlete, mo.getName().trim());
	    if(create_file(dirDestinationComlete) == 0)
	    	return 0;
		try {
			bufferedWriter.write(mo.getName());
			bufferedWriter.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return 1;
	}

	private static int create_file(String dirDestinationComplete) {
		String pref = "IPRA_RESULT";
        int rc = 1;
		
		java.util.Calendar calendar = java.util.Calendar.getInstance(
				java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
		int year = calendar.get(java.util.Calendar.YEAR);
		int month = calendar.get(java.util.Calendar.MONTH) + 1;
		String smonth;
		if (month < 10)
			smonth = "0" + month;
		else
			smonth = "" + month;

		try {
			fileOutputStream = new FileOutputStream(dirDestinationComplete + File.separatorChar + pref + "_" + (year - 2000) + smonth
					+ ".txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			rc = 0;
		}
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					fileOutputStream, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    return rc;	
	}

	private static void close_file() {
        if(bufferedWriter != null) { 
			try {
				bufferedWriter.flush();
			} catch (IOException e) {
	//			e.printStackTrace();
			}
			try {
				bufferedWriter.close();
			} catch (IOException e) {
	//			e.printStackTrace();
			}
			try {
				fileOutputStream.close();
			} catch (IOException ex) {
				// log here
	//			ex.printStackTrace();
			}
        }
	}
}



