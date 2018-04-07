package top.cellargalaxy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.cellargalaxy.bean.controlor.Page;
import top.cellargalaxy.bean.dao.FilePackage;
import top.cellargalaxy.configuration.MycloudConfiguration;
import top.cellargalaxy.dao.FilePackageDao;
import top.cellargalaxy.dao.LogDao;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by cellargalaxy on 18-4-7.
 */
@Service
public class MycloudImpl implements Mycloud {
	@Autowired
	private FilePackageDao filePackageDao;
	@Autowired
	private LogDao logDao;
	private final MycloudConfiguration mycloudConfiguration;
	private final File driveRootFolder;
	private final DateFormat dateFormat;
	private final LinkedBlockingQueue<AbstractTaskExecute> abstractTaskExecutes;
	
	@Autowired
	public MycloudImpl(MycloudConfiguration mycloudConfiguration) {
		this.mycloudConfiguration = mycloudConfiguration;
		abstractTaskExecutes = new LinkedBlockingQueue<>();
		driveRootFolder = new File(mycloudConfiguration.getDriveRootPath());
		dateFormat = new SimpleDateFormat(mycloudConfiguration.getDateFormat());
	}
	
	@Override
	public void addFilePackageTask(File tmpFile, Date pathDate, String description, String contentType) {
		abstractTaskExecutes.offer(new FileTask(tmpFile, pathDate, description, contentType, driveRootFolder, dateFormat, filePackageDao));
	}
	
	@Override
	public void addFilePackageTask(String httpUrl, Date pathDate, String description) {
		abstractTaskExecutes.offer(
				new HttpTask(
						httpUrl,
						pathDate,
						description,
						driveRootFolder,
						dateFormat,
						mycloudConfiguration.getConnectTimeout(),
						mycloudConfiguration.getReadTimeout(),
						filePackageDao));
	}
	
	@Override
	public boolean removeFilePackage(String id) {
		return filePackageDao.deleteFilePackage(
				new FilePackage(
						null,
						null,
						null,
						id,
						null,
						null,
						null,
						null));
	}
	
	@Override
	public boolean removeFilePackage(Date pathDate, String filename) {
		return filePackageDao.deleteFilePackage(
				new FilePackage(
						new File(filename),
						pathDate,
						null,
						null,
						null,
						null,
						null,
						null));
	}
	
	@Override
	public Page[] createPages(int page) {
		return new Page[0];
	}
	
	@Override
	public FilePackage findFilePackage(String id) {
		return filePackageDao.insertFilePackage(
				new FilePackage(
						null,
						null,
						null,
						id,
						null,
						null,
						null,
						null));
	}
	
	@Override
	public FilePackage findFilePackage(Date pathDate, String filename) {
		return filePackageDao.insertFilePackage(
				new FilePackage(
						new File(filename),
						pathDate,
						null,
						null,
						null,
						null,
						null,
						null));
	}
	
	@Override
	public FilePackage[] findFilePackages(int page) {
		return new FilePackage[0];
	}
	
	@Scheduled(fixedDelay = 1000 * 60)
	private void executeTask() {
		AbstractTaskExecute abstractTaskExecute;
		while ((abstractTaskExecute = abstractTaskExecutes.poll()) != null) {
			abstractTaskExecute.executeTask();
			logDao.insertTask(abstractTaskExecute);
		}
	}
}
