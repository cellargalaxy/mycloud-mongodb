package top.cellargalaxy.service;

import top.cellargalaxy.bean.dao.FilePackage;
import top.cellargalaxy.dao.FilePackageDao;
import top.cellargalaxy.util.ExceptionUtil;

import java.io.File;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-8.
 */
public class RestoreTask extends AbstractTaskExecute {
	public static final String AUTO_RESTORE_NAME = "auto restore";
	public static final String USER_RESTORE_NAME = "user restore";
	private final FilePackage filePackage;
	private final FilePackageDao filePackageDao;
	
	public RestoreTask(String taskName, FilePackage filePackage, FilePackageDao filePackageDao) {
		super(filePackage.getFile().getAbsolutePath(), filePackage.getPathDate(), filePackage.getDescription(), taskName, null, false);
		this.filePackage = filePackage;
		this.filePackageDao = filePackageDao;
	}
	
	public RestoreTask(String taskName, String id, FilePackageDao filePackageDao) {
		super(id, null, null, taskName, null, false);
		filePackage = new FilePackage(null, null, null, id, null, null, null, null);
		this.filePackageDao = filePackageDao;
	}
	
	public RestoreTask(String taskName, String filename, Date pathDate, FilePackageDao filePackageDao) {
		super(filename, pathDate, null, taskName, null, false);
		filePackage = new FilePackage(new File(filename), pathDate, null, null, null, null, null, null);
		this.filePackageDao = filePackageDao;
	}
	
	@Override
	public void executeTask() {
		try {
			if (filePackage == null) {
				setSuccess(false);
				setLog("restore info is empty");
				return;
			}
			FilePackage restore = filePackageDao.selectFilePackageSave(filePackage);
			if (restore != null) {
				setSuccess(true);
				setLog(restore.toString());
			} else {
				setSuccess(false);
				setLog("fail restore from database");
			}
		} catch (Exception e) {
			setSuccess(false);
			setLog(ExceptionUtil.pringException(e));
		}
	}
	
	@Override
	public String toString() {
		return "RestoreTask{" +
				"filePackage=" + filePackage +
				", super=" + super.toString() +
				'}';
	}
}
