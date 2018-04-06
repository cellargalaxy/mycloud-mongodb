package top.cellargalaxy.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-6.
 */
@Component
public class FilePackageUtil {
	public File createFile(Date pathDate, String filename) {
		return new File("/home/cellargalaxy/picture/"+pathDate.toString() + filename+".jpg");
	}
	
	public String createUrl(Date pathDate, String filename) {
		return pathDate.toString() + filename;
	}
}
