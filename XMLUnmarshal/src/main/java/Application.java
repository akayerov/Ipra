import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.castor.CastorMarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Application {

    private static final String FILE_NAME = "subject.xml";
    private Settings settings = new Settings();
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    public void saveSettings() throws IOException {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(FILE_NAME);
            this.marshaller.marshal(settings, new StreamResult(os));
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    public void loadSettings() throws IOException {
        FileInputStream is = null;
        try {
            is = new FileInputStream(FILE_NAME);
            this.settings = (Settings) this.unmarshaller.unmarshal(new StreamSource(is));
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

//    public static void main(String[] args) throws IOException {
   public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
/*
    	ApplicationContext appContext =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        Application application = (Application) appContext.getBean("application");
        application.saveSettings();
        application.loadSettings();
*/
/*
	   System.out.println("first1");
    	Marshaller marshaller = new CastorMarshaller();
    	System.out.println("first2");
    	Application app = new Application();
    	System.out.println("first3");
    	app.marshaller = marshaller;
    	System.out.println("first4");
    	app.saveSettings();
 */
	   File []fList;  
	   File folderSrc = new File("C:\\Users\\a_kayerov\\Desktop\\ИПРА\\INPUT\\05_05_2016");
	   String sFolderDest = "C:\\Users\\a_kayerov\\Desktop\\ИПРА\\OUTPUT";
       File flistMo = new File(sFolderDest + "\\LIST\\mo.txt");
       //PrintWriter обеспечит возможности записи в файл
       PrintWriter fout = new PrintWriter(flistMo.getAbsoluteFile());

 
       int countFiles  = 0;
	   int countFolder = 0;
	   int countNull = 0;
	   
	   fList = folderSrc.listFiles();
	                   
	   for(int i=0; i<fList.length; i++)           
//	   for(int i=0; i<10; i++)           
	   {
		   countFiles++;
		   //Нужны только папки в место isFile() пишим isDirectory()
	        if(fList[i].isFile()) {
	            //System.out.println(String.valueOf(i) + " - " + getMO(fList[i].getName()));
	        	String smo =  getMO(fList[i].getAbsolutePath());
	        	String sSmo = smo;
//	            System.out.println(String.valueOf(i) + " - " + smo);
                if(smo != null) {
	               smo = smo.replace('\\', ' ');
	               smo = smo.replace('/', ' ');
//	               smo = smo.replaceAll(".", " ");
	               smo = smo.replace('.', ' '); 
	               smo = smo.replace('"', ' '); 
	               smo = smo.replace(',', ' ');
	               smo = smo.replace(':', ' ');
	               smo = smo.trim();
	               
                }  
                else
                	countNull++;
                   
	            System.out.println(String.valueOf(i) + " - " + smo);

	            String sfolder = sFolderDest + "\\" + smo; 
	            File dir = new File(sfolder);
	            File out = new File(sfolder + "\\" + fList[i].getName());
	            if (dir.exists() && dir.isDirectory()) {
		            System.out.println("Copy file in " + sfolder);
              	    copy(fList[i], out);
	            	
	            }
	            else {
		            System.out.println("Not found folder " + sfolder);
		            dir.mkdir();
		            copy(fList[i], out);
	            }    
	            fout.println(sSmo);
	        }    
	           
	   }
	   fout.close();
	   
       System.out.println("All found files " + countFiles);
	   File folderLst = new File(sFolderDest);
	   fList = folderLst.listFiles();
       System.out.println("All found folder " + fList.length);
       System.out.println("All found Null folder " + countNull);
	                   

       
	  // стандартный для JAVA разбор XML файла
/*
	   File f = new File("subject.xml");
       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
       DocumentBuilder builder = factory.newDocumentBuilder();
       Document document = builder.parse(f);
       Element root = document.getDocumentElement();
       // для простоты сразу берем message
       Element message = (Element) root.getElementsByTagName("SenderMedOrgName").item(0);
       if(message != null) {
         String textContent = message.getTextContent(); // тоже для упрощения
         System.out.println(textContent);
       }  
*/	   
    }
   public static void copy(File source, File dest)  {
    try {
		Files.copy(source.toPath(), dest.toPath());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
private static String getMO(String name) throws ParserConfigurationException, SAXException, IOException {
	// TODO Auto-generated method stub
	   File f = new File(name);
       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
       DocumentBuilder builder = factory.newDocumentBuilder();
       Document document = builder.parse(f);
       Element root = document.getDocumentElement();

       Element SNILS = (Element) root.getElementsByTagName("SNILS").item(0);
       if(SNILS != null) {
           String textSNILS = SNILS.getTextContent(); // тоже для упрощения
           System.out.println(textSNILS);
       }  
       
       // для простоты сразу берем message
       Element message = (Element) root.getElementsByTagName("SenderMedOrgName").item(0);
       if(message != null) {
         String textContent = message.getTextContent(); // тоже для упрощения
         //System.out.println(textContent);
         return textContent;
       }  
       else
  	     return null;
}
}