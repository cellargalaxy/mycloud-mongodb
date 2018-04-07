package top.cellargalaxy.util;


import java.io.File;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-6.
 */
public class FilePackageUtil {
	public static final File createPathFolder(File driveRootFolder, DateFormat dateFormat, Date pathDate) {
		return new File(driveRootFolder.getAbsolutePath() + File.separator + dateFormat.format(pathDate));
	}
	
	public static final File createFile(File driveRootFolder, DateFormat dateFormat, Date pathDate, String filename) {
		return new File(createPathFolder(driveRootFolder, dateFormat, pathDate).getAbsolutePath() + File.separator + filename);
	}
	
	public static final String createUrl(String urlRootPath, DateFormat dateFormat, Date pathDate, String filename) {
		return urlRootPath + File.separator + dateFormat.format(pathDate) + File.separator + filename;
	}
	
}
