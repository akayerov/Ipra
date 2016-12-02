package akayerov.getsnils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MoParser {
	private static int KB2      = 2;//"Клиническая больница №2" г. Ярославля                +          
	private static int KB9      = 3;//"Клиническая больница №9"                             +          
	private static int GBSEM   = 5;//"Городская больница имени Н.А. Семашко" г. Ярославля            
	private static int POL2     = 6;//поликлиника №2 г. Ярославля                           +          
	private static int BOR_CRB  = 7;//Борисоглебская ЦРБ                                    +          
	private static int DAN_CRB  = 9;//Даниловская ЦРБ                                       +          
	private static int LUB_CRB = 10;//"Любимская ЦРБ"                                                 
	private static int NEK_CRB = 11;//"Некрасовская ЦРБ"                                    +                                         
	private static int PER_CRB = 12;//"Переславская ЦРБ"                                              
	private static int ROS_CRB = 13;//"Ростовская ЦРБ"                                                
	private static int TUT_CRB = 14;//"Тутаевская ЦРБ"                                                
	private static int GB1     = 15;//"Городская больница №1"                                         
	private static int GB2     = 16;//"Городская больница №2 имени Н.И. Пирогова"                     
	private static int GB6     = 17;//"Городская больница №6"                                         
	private static int RPB     = 18;//"Рыбинская психиатрическая больница"                            
	private static int KB1     = 20;//"Клиническая больница №1" г. Ярославля                +             
	private static int KB10    = 21;//"Клиническая больница №10" г. Ярославля               +          
	private static int KB3     = 22;//"Клиническая больница №3" г. Ярославля                +          
	private static int KB5     = 23;//"Клиническая больница №5" г. Ярославля                +          
	private static int ODKB    = 24;//"Областная детская клиническая больница"                        
	private static int OKOB    = 26;//"Областная клиническая онкологическая больница"                 
	private static int OKTB    = 27;//"Областная клиническая туберкулезная больница"                  
	private static int OKPB    = 35;//"Ярославская областная клиническая психиатрическая больница"    
	private static int BOL_CRB = 40;//Большесельская ЦРБ                                              
	private static int BRE_CRB = 41;//"Брейтовская ЦРБ"                                               
	private static int GAV_CRB = 42;//Гаврилов-Ямская ЦРБ                                             
	private static int MIS_CRB = 43;//"ЦРБ им. Д.Л. Соколова" Мышкинского МР                          
	private static int NKZ_CRB = 44;//Некоузская ЦРБ                                                  
	private static int BUR_RB  = 45;//"Бурмакинская районная больница №1" Некрасовского МР            
	private static int PRE_CRB = 46;//Пречистенская ЦРБ Первомайского МР                              
	private static int POS_CRB = 47;//Пошехонская ЦРБ                                                 
	private static int RIB_CRP = 48;//"Рыбинская ЦРП"                                                 
	private static int UGL_CRB = 49;//"Угличская ЦРБ"                                                 
	private static int YAR_CRB = 50;//Ярославская ЦРБ                                                 
	private static int GB3     = 51;//городская больница №3 г. Рыбинска                               
	private static int GB4     = 52;//городская больница №4 г. Рыбинска                               
	private static int GDB     = 53;//"ГОРОДСКАЯ ДЕТСКАЯ БОЛЬНИЦА" г. Рыбинска                        
	private static int GP3     = 54;//"Городская поликлиника №3 им. Н.А. Семашко" г. Рыбинска         
	private static int B7      = 58;//больница №7 г. Ярославля                                        
	private static int DKB1    = 59;//детская клиническая больница №1 г. Ярославля                    
	private static int DP3     = 60;//"Детская поликлиника №3" г. Ярославля                           
	private static int DP5     = 61;//Детская поликлиника №5 г. Ярославля                             
	private static int KB8     = 64;//Клиническая больница №8 г. Ярославля                            

    class Parser {
		String pattern1;
    	String pattern2;
    	String pattern3;
    	int ret_code;
    	public Parser(String pattern1, String pattern2, String pattern3,int ret_code) {
			this.pattern1 = pattern1; 
			this.pattern2 = pattern2; 
			this.pattern3 = pattern3;
			this.ret_code = ret_code; 

		}
    }
    public Parser pmo[] =
    	{
    	  new Parser("НЕКРАСОВСК","ЦРБ","",NEK_CRB),
    	  new Parser("БОРИСОГЛЕБ","ЦРБ","",BOR_CRB),
    	  new Parser("ДАНИЛОВСК","ЦРБ","",DAN_CRB),
    	  new Parser("ЛЮБИМС","ЦРБ","",LUB_CRB),
    	  new Parser("ПЕРЕСЛАВС","ЦРБ","",PER_CRB),
    	  new Parser("РОСТОВСК","ЦРБ","",ROS_CRB),
    	  new Parser("ТУТАЕВСК","ЦРБ","",TUT_CRB),
    	  new Parser("БОЛЬШЕСЕЛЬСК","ЦРБ","",BOL_CRB),
    	  new Parser("БРЕЙТОВС","ЦРБ","",BRE_CRB),
    	  new Parser("ГАВРИЛОВ","ЦРБ","",GAV_CRB),
    	  new Parser("СОКОЛОВА","ЦРБ","",MIS_CRB),
    	  new Parser("НЕКОУЗС","ЦРБ","",NKZ_CRB),
    	  new Parser("БУРМАКИНС","","",BUR_RB),
    	  new Parser("ПРЕЧИСТЕНС","ЦРБ","",PRE_CRB),
    	  new Parser("ПОШЕХОНСК","ЦРБ","",POS_CRB),
    	  new Parser("РЫБИНСКАЯ","ЦРП","",RIB_CRP),
    	  new Parser("УГЛИЧЕ","ЦРБ","",UGL_CRB),
    	  new Parser("ЯРОСЛАВСКАЯ","ЦРБ","",YAR_CRB),
    	  new Parser("КЛИНИ"," Б","№1", KB1),
    	  new Parser("КЛИНИ"," Б","№2", KB2),
    	  new Parser("КЛИНИ"," Б","№3", KB3),
    	  new Parser("КЛИНИ"," Б","№5", KB5),
    	  new Parser("КЛИНИ"," Б","№8", KB8),
    	  new Parser("КЛИНИ"," Б","№9", KB9),
    	  new Parser("КЛИНИ"," Б","№10", KB10),
    	  new Parser("КЛИНИ"," Б","№10", KB10),
    	  new Parser("БОЛЬНИЦА","№7","", B7),
    	  new Parser("БОЛЬНИЦА","СЕМАШКО","ЯРОСЛАВЛ", GBSEM),
    	  new Parser("ПОЛИКЛИНИКА","№2","ЯРОСЛАВЛ", POL2),
    	  new Parser("ГОРОДСКАЯ","БОЛЬНИЦА","№1", GB1),
    	  new Parser("ГОРОДСКАЯ","БОЛЬНИЦА","№2", GB2),
    	  new Parser("ГОРОДСКАЯ","БОЛЬНИЦА","№3", GB3),
    	  new Parser("ГОРОДСКАЯ","БОЛЬНИЦА","№4", GB4),
    	  new Parser("ГОРОДСКАЯ","БОЛЬНИЦА","№6", GB6),
    	  new Parser("РЫБИНСКАЯ","ПСИХИАТРИЧ"," Б", RPB),
    	  new Parser("ОБЛАСТН","ДЕСТCК","КЛИНИ", ODKB),
    	  new Parser("ОБЛАСТН","КЛИНИЧ","ОНКОЛОГИ", OKOB),
    	  new Parser("ОБЛАСТН","КЛИНИЧ","ТУБЕРКУЛЕЗН", OKTB),
    	  new Parser("ОБЛАСТН","КЛИНИЧ","ПСИХИАТРИЧ", OKPB),
    	  new Parser("ГОРОДСКАЯ","ДЕТСКАЯ","РЫБИНСК", GDB),
    	  new Parser("ПОЛИКЛИНИКА","СЕМАШКО","№3", GP3),
    	  new Parser("ДЕТСКАЯ","КЛИНИЧЕС","№1", DKB1),
    	  new Parser("ДЕТСКАЯ","ПОЛИКЛИН","№3", DP3),
    	  new Parser("ДЕТСКАЯ","ПОЛИКЛИН","№5", DP5),       
    	  // далее дополнительная обработка, если первые объекты не выявят совпадений
    	  new Parser("ПОЛИКЛИНИКА","№2","", POL2),      
    	  new Parser("НЕКОУЗ","БОЛЬНИЦА","", NKZ_CRB),   
    	  new Parser("ГБ","№1","РЫБИНСК", GB1),   
    	  new Parser("СОКОЛОВ","БОЛЬНИЦ","", MIS_CRB ),   
    	  new Parser("ГОРОДСКАЯ","СЕМАШКО","", GB3),   
    	  new Parser("ОНКОЛОГИ","БОЛЬНИЦ","", OKOB),   
    	  new Parser("БРЕЙТОВ","БОЛЬНИЦ","", BRE_CRB),   
    	  new Parser("СЕМАШКО","РЫБИНСК","", GB3),   
    	  new Parser("СЕМАШКО","3","", GB3),   
    	  new Parser("ГБ","№1","", GB1)
    	};   
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("Hello");
		MoParser obj = new MoParser();
		try {
			obj.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

   
    private void run() throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("MoS.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintWriter out = new PrintWriter(new FileOutputStream("Mo_Res.txt"));
		
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
			out.println(line + "\t" +  Integer.toString(doParse(line)));
		}
	}

	int doParse(String line) {

		String str;
	    int res = 0;
        str = synteticNameMo(line);
	    
        for(int i=0; i < pmo.length; i++) {
        	res = parser(str, pmo[i]);
        	if( res >0 ) break;
        }
	    return res;  
	}
	
	private  String synteticNameMo(String source) {
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
	
	private int parser(String str, Parser p) {
		int id = 0;
		Matcher m1 = null;
		Matcher m2 = null;
		Matcher m3 = null;
		
        Pattern p1 = Pattern.compile(p.pattern1);
        m1 = p1.matcher(str);

        if( !p.pattern2.isEmpty()) {       
	        Pattern p2 = Pattern.compile(p.pattern2);
	        m2 = p2.matcher(str);
        }
        if( !p.pattern3.isEmpty()) {       
	        Pattern p3 = Pattern.compile(p.pattern3);
	        m3 = p3.matcher(str);
        }
        if( m1 != null && m2 != null & m3 != null) { 
            if(m1.find() && m2.find() && m3.find()) 
 		      id =  p.ret_code;
        }   
        else if( m1 != null && m2 != null & m3 == null) { 
            if(m1.find() && m2.find()) 
 		      id =  p.ret_code;
        }   
        else if( m1 != null && m2 == null & m3 == null) { 
           if(m1.find()) 
		      id =  p.ret_code;
        }   
		return id;
	}
}
