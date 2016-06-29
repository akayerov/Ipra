package akayerov.getsnils;

import java.io.File;

import akayerov.getsnils.IpraFile;

public interface FolderIpra {
	public IpraFile getNextFile(int mode);
	public File setPath(String path);
	public IpraFile getNextDir();
}
