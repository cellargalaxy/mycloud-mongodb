package top.cellargalaxy.dao;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import top.cellargalaxy.bean.dao.Task;

import java.util.Date;


/**
 * Created by cellargalaxy on 18-4-7.
 */
@Repository
public class LogDaoMongo implements LogDao {
	public static final String COLLECTION_NAME = "log";
	private static final String TARGET_NAME_NAME = "targetName";
	private static final String PATH_DATE_NAME = "pathDate";
	private static final String DESCRIPTION_NAME = "description";
	private static final String TASK_NAME_NAME = "taskName";
	private static final String LOG_NAME = "log";
	private static final String SUCCESS_NAME = "success";
	private static final String DATE_NAME = "date";
	private final DB db;
	
	@Autowired
	public LogDaoMongo(MongoTemplate mongoTemplate) {
		db = mongoTemplate.getDb();
	}
	
	@Override
	public Task insertTask(Task task) {
		DBObject dbObject = new BasicDBObject().
				append(TARGET_NAME_NAME, task.getTargetName()).
				append(PATH_DATE_NAME, task.getPathDate()).
				append(DESCRIPTION_NAME, task.getDescription()).
				append(TASK_NAME_NAME, task.getTaskName()).
				append(LOG_NAME, task.getLog()).
				append(DATE_NAME, task.getDate()).
				append(SUCCESS_NAME, task.isSuccess());
		DBCollection collection = db.getCollection(COLLECTION_NAME);
		collection.insert(dbObject);
		return task;
	}
	
	@Override
	public int selectTaskCount() {
		DBCollection collection = db.getCollection(COLLECTION_NAME);
		return (int) collection.count();
	}
	
	@Override
	public Task[] selectTasks(int off, int len) {
		DBCollection collection = db.getCollection(COLLECTION_NAME);
		DBCursor dbCursor = collection.find().limit(len).skip(off).sort(new BasicDBObject(DATE_NAME, -1));
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
				dbObject.get(TARGET_NAME_NAME) != null ? dbObject.get(TARGET_NAME_NAME).toString() : null,
				(Date) dbObject.get(PATH_DATE_NAME),
				dbObject.get(DESCRIPTION_NAME) != null ? dbObject.get(DESCRIPTION_NAME).toString() : null,
				dbObject.get(TASK_NAME_NAME) != null ? dbObject.get(TASK_NAME_NAME).toString() : null,
				dbObject.get(LOG_NAME) != null ? dbObject.get(LOG_NAME).toString() : null,
				(Boolean) dbObject.get(SUCCESS_NAME),
				(Date) dbObject.get(DATE_NAME));
	}
}
