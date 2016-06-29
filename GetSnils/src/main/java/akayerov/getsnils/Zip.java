package akayerov.getsnils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Deque;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {
	public static void directoryToZip(File directory, File zipFile) throws IOException {
        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<File>();
        queue.push(directory);
        OutputStream out = new FileOutputStream(zipFile);
        Closeable res = out;
      
        try {
            ZipOutputStream zout = new ZipOutputStream(out);
            res = zout;
            while (!queue.isEmpty()) {
                directory = queue.pop();
                for (File child : directory.listFiles()) {
                    String name = base.relativize(child.toURI()).getPath();
                    if (child.isDirectory()) {
                        queue.push(child);
                        name = name.endsWith("/") ? name : name + "/";
                        zout.putNextEntry(new ZipEntry(name));
                    } else {    // кроме zip файлов
        				int pos_end   = name.lastIndexOf(".", name.length());
        				String type = name.substring(pos_end+1).toUpperCase();
        				if(!type.equals("ZIP")) {

	                        zout.putNextEntry(new ZipEntry(name));
	 
	 
	                        InputStream in = new FileInputStream(child);
	                        try {
	                            byte[] buffer = new byte[1024];
	                            while (true) {
	                                int readCount = in.read(buffer);
	                                if (readCount < 0) {
	                                    break;
	                                }
	                                zout.write(buffer, 0, readCount);
	                            }
	                        } finally {
	                            in.close();
	                        }
	                        zout.closeEntry();
        				}   
	                 }
                }
            }
        } finally {
            res.close();
        }
    }
}
