package top.cellargalaxy.bean.service;

import org.springframework.data.mongodb.core.mapping.Document;

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
	private String exception;
	private boolean success;
	
	public Task() {
	}
	
	public Task(String targetName, Date pathDate, String description, String taskName, String exception, boolean success) {
		this.targetName = targetName;
		this.pathDate = pathDate;
		this.description = description;
		this.taskName = taskName;
		this.exception = exception;
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
	
	public String getException() {
		return exception;
	}
	
	public void setException(String exception) {
		this.exception = exception;
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
				", exception='" + exception + '\'' +
				", success=" + success +
				'}';
	}
}
