package top.cellargalaxy.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.cellargalaxy.bean.service.Task;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by cellargalaxy on 18-4-7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LogDaoMongoTest {
	@Autowired
	private LogDao logDao;
	
	@Test
	public void insertTask() throws Exception {
		for (int i = 0; i < 10; i++) {
			Task task=new Task("target"+i,new Date(),"des"+i,"insert"+i,"exc"+i,true);
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