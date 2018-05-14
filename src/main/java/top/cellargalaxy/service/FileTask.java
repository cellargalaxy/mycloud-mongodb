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
	public final static String INSERT_TASK_NAME = "insert file";
	public final static String UPDATE_TASK_NAME = "update file";
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
		File file = null;
		try {
			if (!tmpFile.exists()) {
				setLog("tmpFile not exists:" + tmpFile.getAbsolutePath());
				setSuccess(false);
				return;
			}
			file = FilePackageUtil.createFile(driveRootFolder, dateFormat, getPathDate(), tmpFile.getName());
			if (!tmpFile.getAbsolutePath().equals(file.getAbsolutePath())) {
				file.delete();
				file.getParentFile().mkdirs();
				tmpFile.renameTo(file);
			}
			if (!file.exists()) {
				setLog("fail remove tmpFile to:" + file.getAbsolutePath());
				setSuccess(false);
				return;
			}
			FilePackage filePackage = new FilePackage(file, getPathDate(), getDescription(), null, null, contentType, null, null);
			if (filePackageDao.selectFilePackageInfo(filePackage) != null) {
				setTaskName(UPDATE_TASK_NAME);
				filePackage = filePackageDao.updateFilePackage(filePackage);
			} else {
				setTaskName(INSERT_TASK_NAME);
				filePackage = filePackageDao.insertFilePackage(filePackage);
			}
			if (filePackage != null) {
				setSuccess(true);
				setLog(filePackage.toString());
			} else {
				setSuccess(false);
				setLog("fail upload to database");
			}
		} catch (Exception e) {
			setSuccess(false);
			setLog(ExceptionUtil.pringException(e));
			if (file != null) {
				file.delete();
			}
		} finally {
			if (tmpFile != null) {
				tmpFile.delete();
			}
		}
	}
	
	@Override
	public String toString() {
		return "FileTask{" +
				"tmpFile=" + tmpFile +
				", contentType='" + contentType + '\'' +
				", driveRootFolder=" + driveRootFolder +
				", dateFormat=" + dateFormat +
				", super=" + super.toString() +
				'}';
	}
}
