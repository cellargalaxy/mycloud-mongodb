package top.cellargalaxy.service;

import org.springframework.stereotype.Service;
import top.cellargalaxy.bean.controlor.Page;
import top.cellargalaxy.bean.dao.FilePackage;
import top.cellargalaxy.bean.service.Task;

import java.io.File;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-6.
 */
@Service
public interface Mycloud {
	void addFilePackageTask(File tmpFile, Date pathDate, String description, String contentType);
	
	void addFilePackageTask(String httpUrl, Date pathDate, String description);
	
	boolean removeFilePackage(String id);
	
	boolean removeFilePackage(Date pathDate, String filename);
	
	Page[] createFilePackagePages(int page);
	
	FilePackage findFilePackage(String id);
	
	FilePackage findFilePackage(Date pathDate, String filename);
	
	FilePackage[] findFilePackages(int page);
	
	Page[] createTaskPages(int page);
	
	Task[] findTasks(int page);
}
