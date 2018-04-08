package top.cellargalaxy.service;

import org.springframework.stereotype.Service;
import top.cellargalaxy.bean.controlor.Page;
import top.cellargalaxy.bean.dao.FilePackage;
import top.cellargalaxy.bean.dao.Task;

import java.io.File;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-6.
 */
@Service
public interface Mycloud {
	void addFile(File tmpFile, Date pathDate, String description, String contentType);
	
	void addHttpUrl(String httpUrl, Date pathDate, String description);
	
	boolean removeFilePackageById(String id);
	
	boolean removeFilePackageByInfo(Date pathDate, String filename);
	
	Page[] createFilePackagePages(int page);
	
	FilePackage findFilePackageById(String id);
	
	FilePackage findFilePackageByInfo(Date pathDate, String filename);
	
	FilePackage[] findFilePackages(int page);
	
	void backupFilePackageById(String id);
	
	void backupFilePackageByInfo(Date pathDate, String filename);
	
	void backupAllFilePackage();
	
	void restoreFilePackageById(String id);
	
	void restoreFilePackageByInfo(Date pathDate, String filename);
	
	void restoreAllFilePackage();
	
	Page[] createTaskPages(int page);
	
	Task[] findTasks(int page);
}
