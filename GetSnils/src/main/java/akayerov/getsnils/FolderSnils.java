package akayerov.getsnils;

import java.io.File;

import akayerov.getsnils.SnilsFile;

public interface FolderSnils {
	public SnilsFile getNextFile(int mode);
	public File setPath(String path);
}
