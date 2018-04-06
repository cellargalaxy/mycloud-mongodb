package top.cellargalaxy.bean.service;

import top.cellargalaxy.bean.dao.FilePackage;

/**
 * Created by cellargalaxy on 18-4-6.
 */
public class FilePackageTask {
	private final String url;
	private final FilePackage filePackage;
	
	public FilePackageTask(String url, FilePackage filePackage) {
		this.url = url;
		this.filePackage = filePackage;
	}
}
