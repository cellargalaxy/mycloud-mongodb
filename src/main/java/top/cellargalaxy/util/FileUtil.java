package top.cellargalaxy.util;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 18-4-8.
 */
public class FileUtil {
	public static final List<File> getAllFileFromFolder(File folder) {
		List<File> files = new LinkedList<>();
		getAllFileFromFolder(files, folder);
		return files;
	}
	
	private static final void getAllFileFromFolder(List<File> list, File folder) {
		if (folder == null) {
			return;
		}
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					list.add(file);
				} else {
					getAllFileFromFolder(list, file);
				}
			}
		}
	}
}
