package akayerov.getsnils;

public class IpraFile {
	public String fullpath;
	public String namefile;
	public String ogrn;
	public IpraFile(String fullpath, String ogrn) {
		this.fullpath = fullpath;
		this.ogrn = ogrn;
	}
	public IpraFile(String fullpath, String ogrn, String namefile) {
		this.fullpath = fullpath;
		this.ogrn = ogrn;
		this.namefile = namefile;
	}
}