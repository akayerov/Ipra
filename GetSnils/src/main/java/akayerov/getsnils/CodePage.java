package akayerov.getsnils;

public class CodePage {
// обрубить кодовую страницу  в начале тектового файла, которая вписывается в читаемую строку
// и не дает работать поиску	
	public static String run(String s) {
		if( s.charAt(0)> 64000) 
			s = s.substring(1);
		return s;
	}

}
