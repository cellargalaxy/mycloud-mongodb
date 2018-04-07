package top.cellargalaxy.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by cellargalaxy on 18-4-7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MycloudImplTest {
	@Autowired
	private Mycloud mycloud;
	@Test
	public void addFilePackageTask() throws Exception {
		mycloud.addFilePackageTask(new File("/home/cellargalaxy/picture/GreatWallMilkyWay_Yu_1686.jpg"),new Date(),"des","jpg");
//		Thread.sleep(1000*10);
	}
	
	@Test
	public void addFilePackageTask1() throws Exception {
	}
	
	@Test
	public void removeFilePackage() throws Exception {
	}
	
	@Test
	public void removeFilePackage1() throws Exception {
	}
	
	@Test
	public void createFilePackagePages() throws Exception {
	}
	
	@Test
	public void findFilePackage() throws Exception {
	}
	
	@Test
	public void findFilePackage1() throws Exception {
	}
	
	@Test
	public void findFilePackages() throws Exception {
	}
	
	@Test
	public void createTaskPages() throws Exception {
	}
	
	@Test
	public void findTasks() throws Exception {
	}
	
}