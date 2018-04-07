package top.cellargalaxy.dao;

import org.springframework.stereotype.Repository;
import top.cellargalaxy.bean.dao.FilePackage;

/**
 * Created by cellargalaxy on 18-4-6.
 */
@Repository
public interface FilePackageDao {
	FilePackage insertFilePackage(FilePackage filePackage);
	
	boolean deleteFilePackage(FilePackage filePackage);
	
	FilePackage selectFilePackageInfo(FilePackage filePackage);
	
	FilePackage[] selectFilePackageInfos(int off, int len);
	
	FilePackage[] selectAllFilePackageInfo();
	
	FilePackage selectFilePackageSave(FilePackage filePackage);
	
	FilePackage updateFilePackage(FilePackage filePackage);
}
