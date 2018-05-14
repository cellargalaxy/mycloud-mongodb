package top.cellargalaxy.bean.dao;

import top.cellargalaxy.util.FilePackageUtil;

import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-7.
 */
public class Task {
	private final String targetName;
	private final Date pathDate;
	private final String description;
	private String taskName;
	private String log;
	private boolean success;
	private final Date date;
	
	public Task(String targetName, Date pathDate, String description, String taskName, String log, boolean success) {
		this(targetName, pathDate, description, taskName, log, success, new Date());
	}
	
	public Task(String targetName, Date pathDate, String description, String taskName, String log, boolean success, Date date) {
		this.targetName = targetName;
		if (pathDate == null) {
			pathDate = FilePackageUtil.createTodayPathDate();
		}
		this.pathDate = pathDate;
		this.description = description;
		this.taskName = taskName;
		this.log = log;
		this.success = success;
		if (date == null) {
			date = new Date();
		}
		this.date = date;
	}
	
	public String getTargetName() {
		return targetName;
	}
	
	public Date getPathDate() {
		return pathDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getTaskName() {
		return taskName;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public String getLog() {
		return log;
	}
	
	public void setLog(String log) {
		this.log = log;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public Date getDate() {
		return date;
	}
	
	@Override
	public String toString() {
		return "Task{" +
				"targetName='" + targetName + '\'' +
				", pathDate=" + pathDate +
				", description='" + description + '\'' +
				", taskName='" + taskName + '\'' +
				", log='" + log + '\'' +
				", success=" + success +
				", date=" + date +
				'}';
	}
}
