package top.cellargalaxy.bean.dao;

import top.cellargalaxy.util.FilePackageUtil;

import java.io.File;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-6.
 */
public class FilePackage {
	private final File file;
	private final Date pathDate;
	private final String filename;
	private final String description;
	private final String id;
	private final Date uploadDate;
	private final long length;
	private final String contentType;
	private final String md5;
	private final String url;
	
	public FilePackage(File file, Date pathDate, String description, String id, Date uploadDate, String contentType, String md5, String url) {
		this.file = file;
		if (pathDate == null) {
			pathDate= FilePackageUtil.createTodayPathDate();
		}
		this.pathDate = pathDate;
		this.description = description;
		this.id = id;
		this.uploadDate = uploadDate;
		this.contentType = contentType;
		this.md5 = md5;
		this.url = url;
		filename = file != null ? file.getName() : null;
		length = file != null ? file.length() : 0;
	}
	
	public File getFile() {
		return file;
	}
	
	public Date getPathDate() {
		return pathDate;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getId() {
		return id;
	}
	
	public Date getUploadDate() {
		return uploadDate;
	}
	
	public long getLength() {
		return length;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public String getMd5() {
		return md5;
	}
	
	public String getUrl() {
		return url;
	}
	
	@Override
	public String toString() {
		return "FilePackage{" +
				"file=" + file +
				", pathDate=" + pathDate +
				", filename='" + filename + '\'' +
				", description='" + description + '\'' +
				", id='" + id + '\'' +
				", uploadDate=" + uploadDate +
				", length=" + length +
				", contentType='" + contentType + '\'' +
				", md5='" + md5 + '\'' +
				", url='" + url + '\'' +
				'}';
	}
}
