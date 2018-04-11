package top.cellargalaxy.service;


import top.cellargalaxy.bean.dao.FilePackage;
import top.cellargalaxy.dao.FilePackageDao;
import top.cellargalaxy.util.ExceptionUtil;
import top.cellargalaxy.util.FilePackageUtil;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-8.
 */
public class BackupTask extends AbstractTaskExecute {
	public static final String AUTO_BACKUP_NAME = "auto backup";
	public static final String USER_BACKUP_NAME = "user backup";
	private final FilePackage filePackage;
	private final FilePackageDao filePackageDao;
	
	public BackupTask(String taskName, FilePackage filePackage, FilePackageDao filePackageDao) {
		super(filePackage.getFile().getAbsolutePath(), filePackage.getPathDate(), filePackage.getDescription(), taskName, null, false);
		this.filePackage = filePackage;
		this.filePackageDao = filePackageDao;
	}
	
	public BackupTask(String taskName, String id, FilePackageDao filePackageDao) {
		super(id, null, null, taskName, null, false);
		filePackage = filePackageDao.selectFilePackageInfo(new FilePackage(null, null, null, id, null, null, null, null));
		this.filePackageDao = filePackageDao;
	}
	
	public BackupTask(String taskName, String filename, Date pathDate, File driveRootFolder, DateFormat dateFormat, FilePackageDao filePackageDao) {
		super(filename, pathDate, null, taskName, null, false);
		filePackage = new FilePackage(FilePackageUtil.createFile(driveRootFolder, dateFormat, pathDate, filename), pathDate,
				null, null, null, null, null, null);
		this.filePackageDao = filePackageDao;
	}
	
	@Override
	public void executeTask() {
		try {
			if (filePackage == null) {
				setSuccess(false);
				setLog("backup info is empty");
				return;
			}
			FilePackage backup = filePackageDao.updateFilePackage(filePackage);
			if (backup != null) {
				setSuccess(true);
				setLog(backup.toString());
			} else {
				setSuccess(false);
				setLog("fail backup to database");
			}
		} catch (Exception e) {
			setSuccess(false);
			setLog(ExceptionUtil.pringException(e));
		}
	}
	
	@Override
	public String toString() {
		return "BackupTask{" +
				"filePackage=" + filePackage +
				", super=" + super.toString() +
				'}';
	}
}
