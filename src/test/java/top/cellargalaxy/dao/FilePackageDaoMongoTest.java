package top.cellargalaxy.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.cellargalaxy.bean.dao.FilePackage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FilePackageDaoMongoTest {
	@Autowired
	private FilePackageDao filePackageDao;
	
	
	@Test
	public void insertFilePackage() throws Exception {
		FilePackage filePackage = new FilePackage(
				new File("/home/cellargalaxy/picture/20170513213439.jpg"),
				new SimpleDateFormat("yyyy-MM-dd").parse("2017-09-09"),
				"des",
				null,
				null,
				"type",
				"md5",
				"url");
		FilePackage filePackage1 = filePackageDao.insertFilePackage(filePackage);
		System.out.println(filePackage1);
		Assert.assertEquals(true, filePackage1 != null);
	}
	
	@Test
	public void deleteFilePackage() throws Exception {
		FilePackage filePackage = new FilePackage(new File("/home/cellargalaxy/picture/fanart_oshino_shinobu_by_huykho192-d7mqdpp.png"),
				new Date(),
				"des",
				"5ac7491983c69f2500f9696a",
				new Date(),
				"type",
				"md5",
				"url");
		Assert.assertEquals(true, filePackageDao.deleteFilePackage(filePackage));
	}
	
	@Test
	public void selectFilePackageInfo() throws Exception {
		FilePackage filePackage = new FilePackage(
				new File("/home/cellargalaxy/picture/20170513213439.jpg"),
				new SimpleDateFormat("yyyy-MM-dd").parse("2017-09-09"),
				"des",
				null,
				null,
				"type",
				"md5",
				"url");
		System.out.println(filePackageDao.selectFilePackageInfo(filePackage));
	}
	
	@Test
	public void selectFilePackageInfos() throws Exception {
		FilePackage[] filePackages = filePackageDao.selectFilePackageInfos(0, 20);
		for (FilePackage filePackage : filePackages) {
			System.out.println(filePackage);
		}
	}
	
	@Test
	public void selectAllFilePackageInfo() throws Exception {
		FilePackage[] filePackages = filePackageDao.selectAllFilePackageInfo();
		for (FilePackage filePackage : filePackages) {
			System.out.println(filePackage);
		}
	}
	
	@Test
	public void selectFilePackageSave() throws Exception {
		FilePackage filePackage = new FilePackage(new File("/home/cellargalaxy/picture/20170513213439.jpg"),
				new Date(),
				"des2",
				"5ac897be83c69f6cbc674874",
				new Date(),
				"type1",
				"md5",
				"url2");
		FilePackage filePackage1 = filePackageDao.selectFilePackageSave(filePackage);
		System.out.println(filePackage1);
		Assert.assertEquals(true, filePackage1 != null);
	}
	
	@Test
	public void updateFilePackage() throws Exception {
		FilePackage filePackage = new FilePackage(new File("/home/cellargalaxy/picture/选区_001.png"),
				new Date(),
				"des2",
				"5ac7487083c69f24920bb41b",
				new Date(),
				"type1",
				"md5",
				"url2");
		FilePackage filePackage1 = filePackageDao.updateFilePackage(filePackage);
		System.out.println(filePackage1);
		Assert.assertEquals(true, filePackage1 != null);
	}
	
}