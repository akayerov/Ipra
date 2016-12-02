package akayerov.getsnils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SmartFolder {
	private static List<String> folder = new ArrayList<String>();
    public static void init() {
    	folder.clear();
    }
    public static void add(String sFolder) {
       if(! isFolder(sFolder) )
    	   folder.add(sFolder);
 
    }
    public static boolean isFolder(String sFolder) {
        for(int i=0; i<folder.size(); i++ ) {
        	if(folder.get(i).equalsIgnoreCase(sFolder))
        		return true;
        }
        return false;	
    }
    
}
