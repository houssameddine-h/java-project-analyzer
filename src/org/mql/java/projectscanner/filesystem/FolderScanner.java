package org.mql.java.projectscanner.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FolderScanner {
	private List<File> files;
	private List<File> folders;

	public FolderScanner(String path) {
		files = new ArrayList<File>();
		folders = new ArrayList<File>();
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			scanFolder(file);
		}
	}

	private void scanFolder(File folder) {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				folders.add(folder);
			}
			else {
				files.add(folder);
			}
		}
	}
	
	public File[] getFiles() {
		File[] data = new File[files.size()];
		return files.toArray(data);
	}
	
	public File[] getFolders() {
		File[] data = new File[folders.size()];
		return folders.toArray(data);
	}
}
