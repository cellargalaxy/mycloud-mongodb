package top.cellargalaxy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.cellargalaxy.bean.controlor.Page;
import top.cellargalaxy.bean.dao.FilePackage;
import top.cellargalaxy.bean.service.Task;
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
	public Page[] createFilePackagePages(int page) {
		int count = filePackageDao.selectFilePackageCount();
		int len = mycloudConfiguration.getListFileLength();
		return createPages(page, count, len);
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
		int len = mycloudConfiguration.getListFileLength();
		int off = (page - 1) * len;
		return filePackageDao.selectFilePackageInfos(off, len);
	}
	
	@Override
	public Page[] createTaskPages(int page) {
		int count = logDao.selectTaskCount();
		int len = mycloudConfiguration.getListFileLength();
		return createPages(page, count, len);
	}
	
	@Override
	public Task[] findTasks(int page) {
		int len = mycloudConfiguration.getListFileLength();
		int off = (page - 1) * len;
		return logDao.selectTasks(off, len);
	}
	
	@Scheduled(fixedDelay = 1)
	private void executeTask() {
		AbstractTaskExecute abstractTaskExecute;
		while ((abstractTaskExecute = abstractTaskExecutes.poll()) != null) {
			abstractTaskExecute.executeTask();
//			logDao.insertTask(abstractTaskExecute);
			System.out.println(abstractTaskExecute);
		}
	}
	
	private final Page[] createPages(int page, int count, int len) {
		int pageCount;
		if (count % len == 0) {
			pageCount = count / len;
		} else {
			pageCount = (count / len) + 1;
		}
		int pagesLength = mycloudConfiguration.getPagesLength();
		int start = page - (pagesLength / 2);
		int end = page + (pagesLength / 2);
		if (start < 1) {
			start = 1;
		}
		if (pageCount < 1) {
			pageCount = 1;
		}
		if (end > pageCount) {
			end = pageCount;
		}
		Page[] pages = new Page[end - start + 3];
		pages[0] = new Page("首页", "1", page == 1);
		pages[pages.length - 1] = new Page("尾页", pageCount + "", page == pageCount);
		for (int i = 1; i < pages.length - 1; i++) {
			pages[i] = new Page((start + i - 1) + "", (start + i - 1) + "", page == (start + i - 1));
		}
		return pages;
	}
}
