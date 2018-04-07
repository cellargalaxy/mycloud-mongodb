package top.cellargalaxy.dao;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Repository;
import top.cellargalaxy.bean.service.Task;

import java.util.Date;


/**
 * Created by cellargalaxy on 18-4-7.
 */
@Repository
public class LogDaoMongo implements LogDao {
	private static final String TARGET_NAME_NAME = "targetName";
	private static final String PATH_DATE_NAME = "pathDate";
	private static final String DESCRIPTION_NAME = "description";
	private static final String TASK_NAME_NAME = "taskName";
	private static final String LOG_NAME = "log";
	private static final String SUCCESS_NAME = "success";
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public Task insertTask(Task task) {
		DBObject dbObject = new BasicDBObject().
				append(TARGET_NAME_NAME, task.getTargetName()).
				append(PATH_DATE_NAME, task.getPathDate()).
				append(DESCRIPTION_NAME, task.getDescription()).
				append(TASK_NAME_NAME, task.getTaskName()).
				append(LOG_NAME, task.getLog()).
				append(SUCCESS_NAME, task.isSuccess());
		DB db = mongoTemplate.getDb();
		DBCollection collection = db.getCollection(Task.class.getAnnotation(org.springframework.data.mongodb.core.mapping.Document.class).collection());
		collection.insert(dbObject);
		return task;
	}
	
	@Override
	public int selectTaskCount() {
		DB db = mongoTemplate.getDb();
		DBCollection collection = db.getCollection(Task.class.getAnnotation(org.springframework.data.mongodb.core.mapping.Document.class).collection());
		return (int) collection.count();
	}
	
	@Override
	public Task[] selectTasks(int off, int len) {
		DB db = mongoTemplate.getDb();
		DBCollection collection = db.getCollection(Task.class.getAnnotation(org.springframework.data.mongodb.core.mapping.Document.class).collection());
		DBCursor dbCursor = collection.find().limit(len).skip(off).sort(new BasicDBObject(PATH_DATE_NAME, -1));
		Task[] tasks = new Task[dbCursor.size()];
		int i = 0;
		for (DBObject dbObject : dbCursor.toArray()) {
			tasks[i] = dbObjectToTask(dbObject);
			i++;
		}
		return tasks;
	}
	
	private final Task dbObjectToTask(DBObject dbObject) {
		return new Task(
				dbObject.get(TARGET_NAME_NAME).toString(),
				(Date) dbObject.get(PATH_DATE_NAME),
				dbObject.get(DESCRIPTION_NAME) != null ? dbObject.get(DESCRIPTION_NAME).toString() : null,
				dbObject.get(TASK_NAME_NAME) != null ? dbObject.get(TASK_NAME_NAME).toString() : null,
				dbObject.get(LOG_NAME) != null ? dbObject.get(LOG_NAME).toString() : null,
				(Boolean) dbObject.get(SUCCESS_NAME));
	}
}
