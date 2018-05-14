package top.cellargalaxy.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.MultipartConfigElement;

/**
 * Created by cellargalaxy on 18-4-6.
 */
@Component
public class MycloudConfiguration {
	@Value("${username:mycloud}")
	private String username;
	@Value("${password:mycloud}")
	private String password;
	@Value("${listFileLength:10}")
	private int listFileLength;
	@Value("${pagesLength:6}")
	private int pagesLength;
	@Value("${driveRootPath}")
	private String driveRootPath;
	@Value("${urlRootPath}")
	private String urlRootPath;
	@Value("${dateFormat:yyyyMM/dd}")
	private String dateFormat;
	@Value("${connectTimeout:5000}")
	private int connectTimeout;
	@Value("${readTimeout:10000}")
	private int readTimeout;

	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setLocation("/tmp");
		return factory.createMultipartConfig();
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getListFileLength() {
		return listFileLength;
	}
	
	public void setListFileLength(int listFileLength) {
		this.listFileLength = listFileLength;
	}
	
	public int getPagesLength() {
		return pagesLength;
	}
	
	public void setPagesLength(int pagesLength) {
		this.pagesLength = pagesLength;
	}
	
	public String getDriveRootPath() {
		return driveRootPath;
	}
	
	public void setDriveRootPath(String driveRootPath) {
		this.driveRootPath = driveRootPath;
	}
	
	public String getUrlRootPath() {
		return urlRootPath;
	}
	
	public void setUrlRootPath(String urlRootPath) {
		this.urlRootPath = urlRootPath;
	}
	
	public String getDateFormat() {
		return dateFormat;
	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public int getConnectTimeout() {
		return connectTimeout;
	}
	
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	
	public int getReadTimeout() {
		return readTimeout;
	}
	
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
}
