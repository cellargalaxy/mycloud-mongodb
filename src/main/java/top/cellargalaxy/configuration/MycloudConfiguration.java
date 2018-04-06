package top.cellargalaxy.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by cellargalaxy on 18-4-6.
 */
@Component
public class MycloudConfiguration {
	@Value("${token:mycloud}")
	private String token;
	@Value("${listFileLength:10}")
	private int listFileLength;
	@Value("${pagesLength:6}")
	private int pagesLength;
	@Value("${driveRootPath}")
	private String driveRootPath;
	@Value("${urlRootPath}")
	private String urlRootPath;
	@Value("${dataFormat:yyyyMM/dd}")
	private String dataFormat;
	@Value("${connectTimeout:5000}")
	private int connectTimeout;
	@Value("${readTimeout:10000}")
	private int readTimeout;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
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
	
	public String getDataFormat() {
		return dataFormat;
	}
	
	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
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
