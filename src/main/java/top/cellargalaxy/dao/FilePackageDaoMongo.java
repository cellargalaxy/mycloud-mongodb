package top.cellargalaxy.dao;

import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
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
	public static final String COLLECTION_NAME = "fs.files";
	public static final String ID_NAME = "_id";
	public static final String METADATA_NAME = "metadata";
	public static final String PATH_DATE_NAME = "pathDate";
	public static final String DESCRIPTION_NAME = "description";
	public static final String FILENAME_NAME = "filename";
	public static final String UPLOAD_DATE_NAME = "uploadDate";
	public static final String CONTENT_TYPE_NAME = "contentType";
	public static final String MD5_NAME = "md5";
	@Autowired
	private MongoDbFactory mongodbfactory;
	@Autowired
	private MycloudConfiguration mycloudConfiguration;
	private volatile GridFS gridFS;
	
	public GridFS getGridFS() {
		if (gridFS == null) {
			synchronized (this) {
				if (gridFS == null) {
					gridFS = new GridFS(mongodbfactory.getDb());
				}
			}
		}
		return gridFS;
	}
	
	@Override
	public FilePackage insertFilePackage(FilePackage filePackage) {
		if (filePackage == null || filePackage.getFile() == null || !filePackage.getFile().exists() || filePackage.getPathDate() == null) {
			return null;
		}
		try (InputStream inputStream = new BufferedInputStream(new FileInputStream(filePackage.getFile()))) {
			GridFS gridFS = getGridFS();
			GridFSInputFile gridFSInputFile = gridFS.createFile(inputStream);
			DBObject dbObject = new BasicDBObject();
			dbObject.put(PATH_DATE_NAME, filePackage.getPathDate());
			dbObject.put(DESCRIPTION_NAME, filePackage.getDescription());
			gridFSInputFile.setMetaData(dbObject);
			gridFSInputFile.setFilename(filePackage.getFilename());
			gridFSInputFile.setContentType(filePackage.getContentType());
			gridFSInputFile.save();
			FilePackage info = new FilePackage(
					filePackage.getFile(),
					filePackage.getPathDate(),
					filePackage.getDescription(),
					gridFSInputFile.getId().toString(),
					gridFSInputFile.getUploadDate(),
					gridFSInputFile.getContentType(),
					gridFSInputFile.getMD5(),
					FilePackageUtil.createUrl(mycloudConfiguration.getUrlRootPath(), new SimpleDateFormat(mycloudConfiguration.getDateFormat()), filePackage.getPathDate(), filePackage.getFilename()));
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
		} else if (filePackage.getId() != null) {
			GridFS gridFS = getGridFS();
			gridFS.remove(new ObjectId(filePackage.getId()));
			return true;
		} else if (filePackage.getPathDate() != null && filePackage.getFilename() != null) {
			GridFS gridFS = getGridFS();
			DBObject dbObject = new BasicDBObject();
			dbObject.put(METADATA_NAME+'.'+PATH_DATE_NAME, filePackage.getPathDate());
			dbObject.put(FILENAME_NAME, filePackage.getFilename());
			gridFS.remove(dbObject);
			return true;
		}
		return true;
	}
	
	@Override
	public int selectFilePackageCount() {
		DB db = getGridFS().getDB();
		DBCollection collection = db.getCollection(COLLECTION_NAME);
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
		DB db = getGridFS().getDB();
		DBCollection collection = db.getCollection(COLLECTION_NAME);
		DBCursor dbCursor = collection.find().limit(len).skip(off).sort(new BasicDBObject(METADATA_NAME+'.'+PATH_DATE_NAME, -1));
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
		DB db = getGridFS().getDB();
		DBCollection collection = db.getCollection(COLLECTION_NAME);
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
		String description = metadata.get(DESCRIPTION_NAME).toString();
		String filename = dbObject.get(FILENAME_NAME).toString();
		DateFormat dateFormat = new SimpleDateFormat(mycloudConfiguration.getDateFormat());
		return new FilePackage(
				FilePackageUtil.createFile(new File(mycloudConfiguration.getDriveRootPath()), dateFormat, pathDate, filename),
				pathDate,
				description,
				dbObject.get(ID_NAME).toString(),
				(Date) dbObject.get(UPLOAD_DATE_NAME),
				dbObject.get(CONTENT_TYPE_NAME).toString(),
				dbObject.get(MD5_NAME).toString(),
				FilePackageUtil.createUrl(mycloudConfiguration.getUrlRootPath(), dateFormat, pathDate, filename));
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}
	
	private GridFSDBFile selectGridFSDBFile(FilePackage filePackage) {
		if (filePackage == null) {
			return null;
		}
		GridFS gridFS = getGridFS();
		GridFSDBFile gridFSDBFile = null;
		if (filePackage.getId() != null) {
			gridFSDBFile = gridFS.findOne(new BasicDBObject(ID_NAME, new ObjectId(filePackage.getId())));
		} else if (filePackage.getPathDate() != null && filePackage.getFilename() != null) {
			DBObject dbObject = new BasicDBObject();
			dbObject.put(METADATA_NAME+'.'+PATH_DATE_NAME, filePackage.getPathDate());
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
		DateFormat dateFormat = new SimpleDateFormat(mycloudConfiguration.getDateFormat());
		FilePackage info = new FilePackage(
				FilePackageUtil.createFile(new File(mycloudConfiguration.getDriveRootPath()), dateFormat, pathDate, filename),
				pathDate,
				gridFSDBFile.getMetaData().get(DESCRIPTION_NAME).toString(),
				gridFSDBFile.getId().toString(),
				gridFSDBFile.getUploadDate(),
				gridFSDBFile.getContentType(),
				gridFSDBFile.getMD5(),
				FilePackageUtil.createUrl(mycloudConfiguration.getUrlRootPath(), dateFormat, pathDate, filename));
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
