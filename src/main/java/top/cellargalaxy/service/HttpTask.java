package top.cellargalaxy.service;

import top.cellargalaxy.bean.dao.FilePackage;
import top.cellargalaxy.dao.FilePackageDao;
import top.cellargalaxy.util.ExceptionUtil;
import top.cellargalaxy.util.FilePackageUtil;
import top.cellargalaxy.util.UrlDownloadUtil;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-7.
 */
public class HttpTask extends AbstractTaskExecute {
	private final static String INSERT_TASK_NAME = "新增链接";
	private final static String UPDATE_TASK_NAME = "更新链接";
	private final String httpUrl;
	private final File driveRootFolder;
	private final DateFormat dateFormat;
	private final int connectTimeout;
	private final int readTimeout;
	private final FilePackageDao filePackageDao;
	
	public HttpTask(String httpUrl, Date pathDate, String description, File driveRootFolder, DateFormat dateFormat, int connectTimeout, int readTimeout, FilePackageDao filePackageDao) {
		super(httpUrl, pathDate, description, null, null, false);
		this.httpUrl = httpUrl;
		this.driveRootFolder = driveRootFolder;
		this.dateFormat = dateFormat;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.filePackageDao = filePackageDao;
	}
	
	@Override
	public void executeTask() {
		File file = null;
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(connectTimeout);
			httpURLConnection.setReadTimeout(readTimeout);
			File pathFolder = FilePackageUtil.createPathFolder(driveRootFolder, dateFormat, getPathDate());
			file = UrlDownloadUtil.downloadHttp(httpURLConnection, pathFolder);
			FilePackage filePackage = new FilePackage(file, getPathDate(), getDescription(), null, null, httpURLConnection.getContentType(), null, null);
			if (filePackageDao.selectFilePackageInfo(filePackage) != null) {
				setTaskName(UPDATE_TASK_NAME);
				setSuccess((filePackage = filePackageDao.updateFilePackage(filePackage)) != null);
			} else {
				setTaskName(INSERT_TASK_NAME);
				setSuccess((filePackage = filePackageDao.insertFilePackage(filePackage)) != null);
			}
			setException(filePackage.toString());
		} catch (Exception e) {
			setException(ExceptionUtil.pringException(e));
			setSuccess(false);
			if (file != null) {
				file.delete();
			}
		}
	}
}
