package akayerov.getsnils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service("readerSnils")
public class ReaderSnilsImpl implements ReaderSnils {
	Scanner in = null;
    int idx = 0;
	public ReaderSnilsImpl() {
    	
    }
	@Override
	public String getNextSnils() {
		// TODO Auto-generated method stub
		if( in != null ) {
			String s;
			while (in.hasNextLine()) {
			  s = in.nextLine();
           	  idx++;
              if(!s.equals(""))
            	  if(idx==0) 
            		  s = CodePage.run(s);
            	  return s;
			}	  
        }
		return "";
	}

	@Override
	public int setPath(String path) {
		// TODO Auto-generated method stub
		try {
			in = new Scanner(new File(path),"UTF-8");
            idx = 0;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub
        if(in != null)
		  in.close();
		return;
	}

}
