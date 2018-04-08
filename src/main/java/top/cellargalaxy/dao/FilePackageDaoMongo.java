package top.cellargalaxy.dao;

import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import top.cellargalaxy.bean.dao.FilePackage;
import top.cellargalaxy.configuration.MycloudConfiguration;
import top.cellargalaxy.util.FilePackageUtil;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-6.
 */
@Repository
public class FilePackageDaoMongo implements FilePackageDao {
	public static final String FILE_COLLECTION_NAME = "fs.files";
	public static final String ID_NAME = "_id";
	public static final String METADATA_NAME = "metadata";
	public static final String PATH_DATE_NAME = "pathDate";
	public static final String DESCRIPTION_NAME = "description";
	public static final String FILENAME_NAME = "filename";
	public static final String UPLOAD_DATE_NAME = "uploadDate";
	public static final String CONTENT_TYPE_NAME = "contentType";
	public static final String MD5_NAME = "md5";
	private final File driveRootFolder;
	private final String urlRootPath;
	private final DateFormat dateFormat;
	private final DB db;
	
	@Autowired
	public FilePackageDaoMongo(MycloudConfiguration mycloudConfiguration, MongoTemplate mongoTemplate) {
		driveRootFolder = new File(mycloudConfiguration.getDriveRootPath());
		urlRootPath = mycloudConfiguration.getUrlRootPath();
		dateFormat = new SimpleDateFormat(mycloudConfiguration.getDateFormat());
		db = mongoTemplate.getDb();
	}
	
	@Override
	public FilePackage insertFilePackage(FilePackage filePackage) {
		if (filePackage == null || filePackage.getFile() == null || !filePackage.getFile().exists() || filePackage.getPathDate() == null) {
			return null;
		}
		try (InputStream inputStream = new BufferedInputStream(new FileInputStream(filePackage.getFile()))) {
			GridFS gridFS = new GridFS(db);
			GridFSInputFile gridFSInputFile = gridFS.createFile(inputStream);
			DBObject dbObject = new BasicDBObject().
					append(PATH_DATE_NAME, filePackage.getPathDate()).
					append(DESCRIPTION_NAME, filePackage.getDescription());
			gridFSInputFile.setMetaData(dbObject);
			gridFSInputFile.setFilename(filePackage.getFilename());
			gridFSInputFile.setContentType(filePackage.getContentType());
			gridFSInputFile.save();
			FilePackage info = new FilePackage(
					filePackage.getFile(),
					filePackage.getPathDate(),
					filePackage.getDescription(),
					gridFSInputFile.getId() != null ? gridFSInputFile.getId().toString() : null,
					gridFSInputFile.getUploadDate(),
					gridFSInputFile.getContentType(),
					gridFSInputFile.getMD5(),
					FilePackageUtil.createUrl(urlRootPath, dateFormat, filePackage.getPathDate(), filePackage.getFilename()));
			return info;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean deleteFilePackage(FilePackage filePackage) {
		if (filePackage == null) {
			return false;
		}
		GridFS gridFS = new GridFS(db);
		if (filePackage.getId() != null) {
			gridFS.remove(new ObjectId(filePackage.getId()));
			return true;
		} else if (filePackage.getPathDate() != null && filePackage.getFilename() != null) {
			DBObject dbObject = new BasicDBObject().
					append(METADATA_NAME + '.' + PATH_DATE_NAME, filePackage.getPathDate()).
					append(FILENAME_NAME, filePackage.getFilename());
			gridFS.remove(dbObject);
			return true;
		}
		return false;
	}
	
	@Override
	public int selectFilePackageCount() {
		DBCollection collection = db.getCollection(FILE_COLLECTION_NAME);
		return (int) collection.count();
	}
	
	@Override
	public FilePackage selectFilePackageInfo(FilePackage filePackage) {
		if (filePackage == null) {
			return null;
		}
		GridFSDBFile gridFSDBFile = selectGridFSDBFile(filePackage);
		if (gridFSDBFile == null) {
			return null;
		}
		return gridFSDBFileToFilePackage(gridFSDBFile);
	}
	
	/**
	 * http://www.cnblogs.com/amosli/p/3480676.html
	 *
	 * @param off
	 * @param len
	 * @return
	 */
	@Override
	public FilePackage[] selectFilePackageInfos(int off, int len) {
		DBCollection collection = db.getCollection(FILE_COLLECTION_NAME);
		DBCursor dbCursor = collection.find().limit(len).skip(off).sort(new BasicDBObject(METADATA_NAME + '.' + PATH_DATE_NAME, -1));
		FilePackage[] filePackages = new FilePackage[dbCursor.size()];
		int i = 0;
		for (DBObject dbObject : dbCursor.toArray()) {
			filePackages[i] = dbObjectToFilePackage(dbObject);
			i++;
		}
		return filePackages;
	}
	
	@Override
	public FilePackage[] selectAllFilePackageInfo() {
		DBCollection collection = db.getCollection(FILE_COLLECTION_NAME);
		DBCursor dbCursor = collection.find();
		FilePackage[] filePackages = new FilePackage[dbCursor.size()];
		int i = 0;
		for (DBObject dbObject : dbCursor.toArray()) {
			filePackages[i] = dbObjectToFilePackage(dbObject);
			i++;
		}
		return filePackages;
	}
	
	private FilePackage dbObjectToFilePackage(DBObject dbObject) {
		DBObject metadata = (DBObject) dbObject.get(METADATA_NAME);
		Date pathDate = (Date) metadata.get(PATH_DATE_NAME);
		String description = metadata.get(DESCRIPTION_NAME) != null ? metadata.get(DESCRIPTION_NAME).toString() : null;
		String filename = dbObject.get(FILENAME_NAME) != null ? dbObject.get(FILENAME_NAME).toString() : null;
		return new FilePackage(
				FilePackageUtil.createFile(driveRootFolder, dateFormat, pathDate, filename),
				pathDate,
				description,
				dbObject.get(ID_NAME) != null ? dbObject.get(ID_NAME).toString() : null,
				(Date) dbObject.get(UPLOAD_DATE_NAME),
				dbObject.get(CONTENT_TYPE_NAME) != null ? dbObject.get(CONTENT_TYPE_NAME).toString() : null,
				dbObject.get(MD5_NAME) != null ? dbObject.get(MD5_NAME).toString() : null,
				FilePackageUtil.createUrl(urlRootPath, dateFormat, pathDate, filename));
	}
	
	@Override
	public FilePackage selectFilePackageSave(FilePackage filePackage) {
		if (filePackage == null) {
			return null;
		}
		GridFSDBFile gridFSDBFile = selectGridFSDBFile(filePackage);
		if (gridFSDBFile == null) {
			return null;
		}
		FilePackage info = gridFSDBFileToFilePackage(gridFSDBFile);
		if (info == null || info.getFile() == null) {
			return null;
		}
		try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(info.getFile()))) {
			gridFSDBFile.writeTo(outputStream);
			return info;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			info.getFile().delete();
		} catch (IOException e) {
			e.printStackTrace();
			info.getFile().delete();
		}
		return null;
	}
	
	private GridFSDBFile selectGridFSDBFile(FilePackage filePackage) {
		if (filePackage == null) {
			return null;
		}
		GridFS gridFS = new GridFS(db);
		GridFSDBFile gridFSDBFile = null;
		if (filePackage.getId() != null) {
			gridFSDBFile = gridFS.findOne(new BasicDBObject(ID_NAME, new ObjectId(filePackage.getId())));
		} else if (filePackage.getPathDate() != null && filePackage.getFilename() != null) {
			DBObject dbObject = new BasicDBObject();
			dbObject.put(METADATA_NAME + '.' + PATH_DATE_NAME, filePackage.getPathDate());
			dbObject.put(FILENAME_NAME, filePackage.getFilename());
			gridFSDBFile = gridFS.findOne(dbObject);
		}
		return gridFSDBFile;
	}
	
	private FilePackage gridFSDBFileToFilePackage(GridFSDBFile gridFSDBFile) {
		if (gridFSDBFile == null) {
			return null;
		}
		Date pathDate = (Date) gridFSDBFile.getMetaData().get(PATH_DATE_NAME);
		String filename = gridFSDBFile.getFilename();
		FilePackage info = new FilePackage(
				FilePackageUtil.createFile(driveRootFolder, dateFormat, pathDate, filename),
				pathDate,
				gridFSDBFile.getMetaData().get(DESCRIPTION_NAME) != null ? gridFSDBFile.getMetaData().get(DESCRIPTION_NAME).toString() : null,
				gridFSDBFile.getId() != null ? gridFSDBFile.getId().toString() : null,
				gridFSDBFile.getUploadDate(),
				gridFSDBFile.getContentType(),
				gridFSDBFile.getMD5(),
				FilePackageUtil.createUrl(urlRootPath, dateFormat, pathDate, filename));
		return info;
	}
	
	@Override
	public FilePackage updateFilePackage(FilePackage filePackage) {
		if (filePackage == null) {
			return null;
		}
		return deleteFilePackage(filePackage) ? insertFilePackage(filePackage) : null;
	}
}
