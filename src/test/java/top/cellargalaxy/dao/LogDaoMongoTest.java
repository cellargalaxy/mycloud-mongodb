package top.cellargalaxy.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.cellargalaxy.bean.dao.Task;
import top.cellargalaxy.service.FileTask;

import java.io.File;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LogDaoMongoTest {
	
	
	
	@Test
	public void selectTaskCount() throws Exception {
		System.out.println(logDao.selectTaskCount());
	}
	
	@Autowired
	private LogDao logDao;
	
	@Test
	public void insertTask() throws Exception {
		for (int i = 0; i < 1; i++) {
			Task task=new FileTask(new File(i+".jpg"),new Date(),"des"+i,"type"+i,null,null,null);
			System.out.println(logDao.insertTask(task));
		}
	}
	
	@Test
	public void selectTasks() throws Exception {
		for (Task task : logDao.selectTasks(2, 3)) {
			System.out.println(task);
		}
	}
	
}