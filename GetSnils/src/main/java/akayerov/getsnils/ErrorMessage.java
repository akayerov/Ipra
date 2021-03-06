package akayerov.getsnils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import akayerov.getsnils.models.Mo;


public class ErrorMessage {
	Mo mo;
	String fileName;
	String dirDestination;
	
	List<String> lerror = new ArrayList<String>();
	ErrorMessage() {
	};
	ErrorMessage(Mo mo, String fileName) {
		this.mo = mo;
		int pos_end   = fileName.lastIndexOf(".", fileName.length());
		if(pos_end < 0)
			this.fileName = fileName + ".err";
		else 
			this.fileName = fileName.substring(0,pos_end) + ".err";
	};
	
	public void setDirDestination(String dir) {
		this.dirDestination = dir;
	}

	
	public void add(String string1, String string2) {
		lerror.add(string1 + ":" + string2);    		
	}
	void add(String error) {
		lerror.add("Ошибка заполнения поля:" + error);
	}
	
	void print() {
		
		File fw = new File(dirDestination + "\\" + fileName);
        PrintWriter out = null;
        try {
			out = new PrintWriter(fw.getAbsoluteFile());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	    out.println("Протокол разбора файла:" + fileName );
	    out.println("От:" + mo.getName() );
	    out.println("Дата:" + new Date() );
	    out.println("Список ошибок:");
        
        for(String err:lerror) {
		   out.println(err);
        }
	    out.println("Всего ошибок:" + lerror.size() );
        
        out.close();
	}
	public int getSize() {
		return lerror.size();
	}
	
}
