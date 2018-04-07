package top.cellargalaxy.service;

import org.springframework.data.mongodb.core.mapping.Document;
import top.cellargalaxy.bean.service.Task;

import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-7.
 */
@Document(collection = Task.COLLECTION)
public abstract class AbstractTaskExecute extends Task implements TaskExecute {
	public AbstractTaskExecute(String targetName, Date pathDate, String description, String taskName, String exception, boolean success) {
		super(targetName, pathDate, description, taskName, exception, success);
	}
}
