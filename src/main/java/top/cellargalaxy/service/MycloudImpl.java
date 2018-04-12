package top.cellargalaxy.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.cellargalaxy.bean.controlor.Page;
import top.cellargalaxy.bean.dao.FilePackage;
import top.cellargalaxy.bean.dao.Task;
import top.cellargalaxy.configuration.MycloudConfiguration;
import top.cellargalaxy.dao.FilePackageDao;
import top.cellargalaxy.dao.LogDao;
import top.cellargalaxy.util.FilePackageUtil;
import top.cellargalaxy.util.FileUtil;
import top.cellargalaxy.util.PageUtil;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by cellargalaxy on 18-4-7.
 */
@Service
public class MycloudImpl implements Mycloud {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private FilePackageDao filePackageDao;
	@Autowired
	private LogDao logDao;
	private final File driveRootFolder;
	private final DateFormat dateFormat;
	private final String urlRootPath;
	private final int listFileLength;
	private final int pagesLength;
	private final int connectTimeout;
	private final int readTimeout;
	private final LinkedBlockingQueue<AbstractTaskExecute> abstractTaskExecutes;
	
	@Autowired
	public MycloudImpl(MycloudConfiguration mycloudConfiguration) {
		driveRootFolder = new File(mycloudConfiguration.getDriveRootPath());
		dateFormat = new SimpleDateFormat(mycloudConfiguration.getDateFormat());
		urlRootPath = mycloudConfiguration.getUrlRootPath();
		connectTimeout = mycloudConfiguration.getConnectTimeout();
		readTimeout = mycloudConfiguration.getReadTimeout();
		listFileLength = mycloudConfiguration.getListFileLength();
		pagesLength = mycloudConfiguration.getPagesLength();
		abstractTaskExecutes = new LinkedBlockingQueue<>();
	}
	
	@Override
	public void addFile(File tmpFile, Date pathDate, String description, String contentType) {
		AbstractTaskExecute abstractTaskExecute = new FileTask(tmpFile, pathDate, description, contentType, driveRootFolder, dateFormat, filePackageDao);
		abstractTaskExecutes.offer(abstractTaskExecute);
		logger.info("addFile: " + abstractTaskExecute);
	}
	
	@Override
	public void addHttpUrl(String httpUrl, Date pathDate, String description) {
		AbstractTaskExecute abstractTaskExecute = new HttpTask(httpUrl.trim(), pathDate, description, driveRootFolder, dateFormat, connectTimeout, readTimeout, filePackageDao);
		abstractTaskExecutes.offer(abstractTaskExecute);
		logger.info("addHttpUrl: " + abstractTaskExecute);
	}
	
	@Override
	public boolean removeFilePackageById(String id) {
		FilePackage filePackage = new FilePackage(null, null, null, id, null, null, null, null);
		boolean b = filePackageDao.deleteFilePackage(filePackage);
		logger.info("removeFilePackageById: " + b + " : " + filePackage);
		return b;
	}
	
	@Override
	public boolean removeFilePackageByInfo(Date pathDate, String filename) {
		FilePackage filePackage = new FilePackage(new File(filename), pathDate, null, null, null, null, null, null);
		boolean b = filePackageDao.deleteFilePackage(filePackage);
		logger.info("removeFilePackageByInfo: " + b + " : " + filePackage);
		return b;
	}
	
	@Override
	public Page[] createFilePackagePages(int page) {
		int count = filePackageDao.selectFilePackageCount();
		int len = listFileLength;
		return PageUtil.createPages(page, count, len, pagesLength);
	}
	
	@Override
	public FilePackage findFilePackageById(String id) {
		return filePackageDao.selectFilePackageInfo(
				new FilePackage(null, null, null, id, null, null, null, null));
	}
	
	@Override
	public FilePackage findFilePackageByInfo(Date pathDate, String filename) {
		return filePackageDao.selectFilePackageInfo(
				new FilePackage(new File(filename), pathDate, null, null, null, null, null, null));
	}
	
	@Override
	public FilePackage[] findFilePackages(int page) {
		int len = listFileLength;
		int off = (page - 1) * len;
		return filePackageDao.selectFilePackageInfos(off, len);
	}
	
	@Override
	public void backupFilePackageById(String id) {
		AbstractTaskExecute abstractTaskExecute = new BackupTask(BackupTask.USER_BACKUP_NAME, id, filePackageDao);
		abstractTaskExecutes.add(abstractTaskExecute);
		logger.info("backupFilePackageById: " + abstractTaskExecute);
	}
	
	@Override
	public void backupFilePackageByInfo(Date pathDate, String filename) {
		AbstractTaskExecute abstractTaskExecute = new BackupTask(BackupTask.USER_BACKUP_NAME, filename, pathDate, driveRootFolder, dateFormat, filePackageDao);
		abstractTaskExecutes.add(abstractTaskExecute);
		logger.info("backupFilePackageByInfo: " + abstractTaskExecute);
	}
	
	@Override
	public void backupAllFilePackage() {
		List<File> files = FileUtil.getAllFileFromFolder(driveRootFolder);
		if (files != null) {
			for (File file : files) {
				AbstractTaskExecute abstractTaskExecute = new BackupTask(BackupTask.USER_BACKUP_NAME, FilePackageUtil.createFilePackage(driveRootFolder, dateFormat, file, urlRootPath), filePackageDao);
				abstractTaskExecutes.add(abstractTaskExecute);
				logger.info("backupAllFilePackage: " + abstractTaskExecute);
			}
		}
	}
	
	@Override
	public void restoreFilePackageById(String id) {
		AbstractTaskExecute abstractTaskExecute = new RestoreTask(RestoreTask.USER_RESTORE_NAME, id, filePackageDao);
		abstractTaskExecutes.add(abstractTaskExecute);
		logger.info("restoreFilePackageById: " + abstractTaskExecute);
	}
	
	@Override
	public void restoreFilePackageByInfo(Date pathDate, String filename) {
		AbstractTaskExecute abstractTaskExecute = new RestoreTask(RestoreTask.USER_RESTORE_NAME, filename, pathDate, filePackageDao);
		abstractTaskExecutes.add(abstractTaskExecute);
		logger.info("restoreFilePackageByInfo: " + abstractTaskExecute);
	}
	
	@Override
	public void restoreAllFilePackage() {
		FilePackage[] filePackages = filePackageDao.selectAllFilePackageInfo();
		for (FilePackage filePackage : filePackages) {
			AbstractTaskExecute abstractTaskExecute = new RestoreTask(RestoreTask.USER_RESTORE_NAME, filePackage, filePackageDao);
			abstractTaskExecutes.add(abstractTaskExecute);
			logger.info("restoreAllFilePackage: " + abstractTaskExecute);
		}
	}
	
	@Override
	public Page[] createTaskPages(int page) {
		int count = logDao.selectTaskCount();
		int len = listFileLength;
		return PageUtil.createPages(page, count, len, pagesLength);
	}
	
	@Override
	public Task[] findTasks(int page) {
		int len = listFileLength;
		int off = (page - 1) * len;
		return logDao.selectTasks(off, len);
	}
	
	@Scheduled(fixedDelay = 1)
	public void executeTask() {
		AbstractTaskExecute abstractTaskExecute;
		while ((abstractTaskExecute = abstractTaskExecutes.poll()) != null) {
			abstractTaskExecute.executeTask();
			logDao.insertTask(abstractTaskExecute);
			logger.info("executeTask: " + abstractTaskExecute);
		}
	}
	
	@Scheduled(initialDelay = 1000 * 60 * 60, fixedDelay = 1000 * 60 * 60 * 3)
	public void synchronize() {
		FilePackage[] filePackages = filePackageDao.selectAllFilePackageInfo();
		if (filePackages == null) {
			return;
		}
		List<File> files = FileUtil.getAllFileFromFolder(driveRootFolder);
		for (FilePackage filePackage : filePackages) {
			if (filePackage.getFile() != null && filePackage.getFile().exists()) {
				try (InputStream inputStream = new BufferedInputStream(new FileInputStream(filePackage.getFile()))) {
					String md5 = DigestUtils.md5Hex(inputStream);
					if (md5 != null && md5.equals(filePackage.getMd5())) {
						continue;
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			AbstractTaskExecute abstractTaskExecute = new RestoreTask(RestoreTask.AUTO_RESTORE_NAME, filePackage, filePackageDao);
			abstractTaskExecutes.add(abstractTaskExecute);
			logger.info("synchronize restore: " + abstractTaskExecute);
		}
		if (files == null) {
			return;
		}
		main:
		for (File file : files) {
			for (FilePackage filePackage : filePackages) {
				if (filePackage.getFile() != null && filePackage.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
					continue main;
				}
			}
			file.delete();
			logger.info("synchronize delete: " + file);
		}
	}
}
