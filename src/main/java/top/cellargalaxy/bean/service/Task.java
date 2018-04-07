package top.cellargalaxy.bean.service;

import org.springframework.data.mongodb.core.mapping.Document;
import top.cellargalaxy.bean.dao.FilePackage;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-7.
 */
@Document(collection = Task.COLLECTION)
public class Task {
	public static final String COLLECTION ="log";
	private String targetName;
	private Date pathDate;
	private String description;
	private String taskName;
	private String log;
	private boolean success;
	
	public Task() {
	}
	
	public Task(String targetName, Date pathDate, String description, String taskName, String log, boolean success) {
		this.targetName = targetName;
		if (pathDate == null) {
			try {
				pathDate = FilePackage.DATE_FORMAT.parse(FilePackage.DATE_FORMAT.format(new Date()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		this.pathDate = pathDate;
		this.description = description;
		this.taskName = taskName;
		this.log = log;
		this.success = success;
	}
	
	public String getTargetName() {
		return targetName;
	}
	
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	
	public Date getPathDate() {
		return pathDate;
	}
	
	public void setPathDate(Date pathDate) {
		this.pathDate = pathDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
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
	
	@Override
	public String toString() {
		return "Task{" +
				"targetName='" + targetName + '\'' +
				", pathDate=" + pathDate +
				", description='" + description + '\'' +
				", taskName='" + taskName + '\'' +
				", log='" + log + '\'' +
				", success=" + success +
				'}';
	}
}
