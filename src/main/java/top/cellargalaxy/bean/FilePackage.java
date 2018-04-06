package top.cellargalaxy.bean;

import java.io.File;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-6.
 */
public class FilePackage {
	private final File file;
	private final Date pathDate;
	private final String description;
	private final String id;
	private final Date uploadDate;
	private final String contentType;
	private final String md5;
	private final String url;
	
	public FilePackage(File file, Date pathDate, String description, String id, Date uploadDate, String contentType, String md5, String url) {
		this.file = file;
		this.pathDate = pathDate;
		this.description = description;
		this.id = id;
		this.uploadDate = uploadDate;
		this.contentType = contentType;
		this.md5 = md5;
		this.url = url;
	}
	
	public String getFilename() {
		return file.getName();
	}
	
	public long getFileLength() {
		return file.length();
	}
	
	public File getFile() {
		return file;
	}
	
	public Date getPathDate() {
		return pathDate;
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
				", filename='" + file.getName() + '\'' +
				", fileLength=" + file.length() +
				", pathDate=" + pathDate +
				", description='" + description + '\'' +
				", id='" + id + '\'' +
				", uploadDate=" + uploadDate +
				", contentType='" + contentType + '\'' +
				", md5='" + md5 + '\'' +
				", url='" + url + '\'' +
				'}';
	}
}