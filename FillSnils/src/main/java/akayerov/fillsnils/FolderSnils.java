package akayerov.fillsnils;

import java.io.File;

import akayerov.fillsnils.SnilsFile;

public interface FolderSnils {
	public SnilsFile getNextFile();
	public File SetPath(String path);
}
