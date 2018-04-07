package top.cellargalaxy.service;


import top.cellargalaxy.bean.dao.FilePackage;
import top.cellargalaxy.dao.FilePackageDao;
import top.cellargalaxy.util.ExceptionUtil;
import top.cellargalaxy.util.FilePackageUtil;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-7.
 */
public class FileTask extends AbstractTaskExecute {
	private final static String INSERT_TASK_NAME = "新增文件";
	private final static String UPDATE_TASK_NAME = "更新文件";
	private final File tmpFile;
	private final String contentType;
	private final File driveRootFolder;
	private final DateFormat dateFormat;
	private final FilePackageDao filePackageDao;
	
	public FileTask(File tmpFile, Date pathDate, String description, String contentType, File driveRootFolder, DateFormat dateFormat, FilePackageDao filePackageDao) {
		super(tmpFile.getAbsolutePath(), pathDate, description, null, null, false);
		this.tmpFile = tmpFile;
		this.contentType = contentType;
		this.driveRootFolder = driveRootFolder;
		this.dateFormat = dateFormat;
		this.filePackageDao = filePackageDao;
	}
	
	@Override
	public void executeTask() {
		try {
			File file = FilePackageUtil.createFile(driveRootFolder, dateFormat, getPathDate(), tmpFile.getName());
			if (!tmpFile.getAbsolutePath().equals(file.getAbsolutePath())) {
				file.delete();
				file.getParentFile().mkdirs();
				tmpFile.renameTo(file);
			}
			FilePackage filePackage = new FilePackage(file, getPathDate(), getDescription(), null, null, contentType, null, null);
			if (filePackageDao.selectFilePackageInfo(filePackage) != null) {
				setTaskName(UPDATE_TASK_NAME);
				setSuccess((filePackage = filePackageDao.updateFilePackage(filePackage)) != null);
			} else {
				setTaskName(INSERT_TASK_NAME);
				setSuccess((filePackage = filePackageDao.insertFilePackage(filePackage)) != null);
			}
			if (filePackage != null) {
				setException(filePackage.toString());
			}
		} catch (Exception e) {
			setException(ExceptionUtil.pringException(e));
			setSuccess(false);
			tmpFile.delete();
		}
	}
}
