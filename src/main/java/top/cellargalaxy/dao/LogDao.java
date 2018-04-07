package top.cellargalaxy.dao;

import org.springframework.stereotype.Repository;
import top.cellargalaxy.bean.service.Task;

/**
 * Created by cellargalaxy on 18-4-7.
 */
@Repository
public interface LogDao {
	Task insertTask(Task task);
	
	int selectTaskCount();
	
	Task[] selectTasks(int off, int len);
}
