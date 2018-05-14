package top.cellargalaxy.service;

import top.cellargalaxy.bean.dao.Task;

import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-7.
 */
public abstract class AbstractTaskExecute extends Task implements TaskExecute {
	
	public AbstractTaskExecute(String targetName, Date pathDate, String description, String taskName, String log, boolean success) {
		super(targetName, pathDate, description, taskName, log, success);
	}
}
