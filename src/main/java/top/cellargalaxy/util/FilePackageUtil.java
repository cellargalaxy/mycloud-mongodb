package top.cellargalaxy.util;

import top.cellargalaxy.bean.dao.FilePackage;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-6.
 */
public class FilePackageUtil {
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final FilePackage createFilePackage(File driveRootFolder, DateFormat dateFormat, File file, String urlRootPath) {
		if (driveRootFolder == null || file == null) {
			return null;
		}
		try {
			String pathDateString = file.getAbsolutePath().replaceAll(driveRootFolder.getAbsolutePath() + '/', "").replaceAll('/' + file.getName(), "");
			return new FilePackage(file, dateFormat.parse(pathDateString),
					null, null, null, null, null, createUrl(urlRootPath, pathDateString, file.getName()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static final File createPathFolder(File driveRootFolder, DateFormat dateFormat, Date pathDate) {
		return new File(driveRootFolder.getAbsolutePath() + File.separator + dateFormat.format(pathDate));
	}
	
	public static final File createFile(File driveRootFolder, DateFormat dateFormat, Date pathDate, String filename) {
		return new File(createPathFolder(driveRootFolder, dateFormat, pathDate).getAbsolutePath() + File.separator + filename);
	}
	
	public static final String createUrl(String urlRootPath, DateFormat dateFormat, Date pathDate, String filename) {
		return createUrl(urlRootPath, dateFormat.format(pathDate), filename);
	}
	
	public static final String createUrl(String urlRootPath, String pathDateString, String filename) {
		return urlRootPath + File.separator + pathDateString + File.separator + filename;
	}
	
	public static final Date createTodayPathDate() {
		return createPathDate(new Date());
	}
	
	public static final Date createPathDate(Date date) {
		try {
			return DATE_FORMAT.parse(DATE_FORMAT.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
