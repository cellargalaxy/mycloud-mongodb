package top.cellargalaxy.util;

import org.junit.Test;

import java.io.*;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by cellargalaxy on 18-4-6.
 */
public class UrlDownloadUtilTest {
	@Test
	public void httpDownload() throws Exception {
//		UrlDownloadUtil.httpDownload("http://drive.cellargalaxy.top/201712/05/mycloud后台管理.png",new File("/home/cellargalaxy/picture"),1000*2,1000*2);
		URL url=new URL("http://www.baidu.com");
		System.out.println(url.toString());
	}
	
}