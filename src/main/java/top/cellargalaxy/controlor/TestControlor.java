package top.cellargalaxy.controlor;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cellargalaxy on 18-4-6.
 */
@RestController
public class TestControlor {
	@Autowired
	private MongoDbFactory mongodbfactory;
	
	@PostMapping("/upload")
	@ResponseBody
	public Object upload(@RequestParam(value = "file") MultipartFile file) throws IOException {
		InputStream in = file.getInputStream();
		String name = file.getOriginalFilename();
		
		GridFS gridFS = new GridFS(mongodbfactory.getDb());
		GridFSInputFile gridFSInputFile = gridFS.createFile(in);
		gridFSInputFile.setFilename(name);
		gridFSInputFile.setContentType(file.getContentType());
		gridFSInputFile.setMetaData(new BasicDBObject("data", new Date()));
		gridFSInputFile.save();
		
		Map<String, Object> dt = new HashMap<String, Object>();
		dt.put("id", gridFSInputFile.getId().toString());
		dt.put("md5", gridFSInputFile.getMD5());
		dt.put("name", gridFSInputFile.getFilename());
		dt.put("length", gridFSInputFile.getLength());
		dt.put("contentType", gridFSInputFile.getContentType());
		dt.put("uploadDate", gridFSInputFile.getUploadDate());
		
		return dt;
	}
	
	@PostMapping("/remove")
	@ResponseBody
	public String remove(String id) {
		GridFS gridFS = new GridFS(mongodbfactory.getDb());
		gridFS.remove(new ObjectId(id));
		return "delete 了";
	}
	
	@GetMapping("/download")
	@ResponseBody
	public void download(String id, HttpServletResponse response) throws IOException {
		GridFS gridFS = new GridFS(mongodbfactory.getDb());
		GridFSDBFile gridFSDBFile = gridFS.findOne(new BasicDBObject("_id", new ObjectId(id)));
		if (gridFSDBFile == null) {
			System.out.println("404 了");
			return;
		}
		System.out.println("data: "+gridFSDBFile.getMetaData().get("data"));
		OutputStream os = response.getOutputStream();
		response.addHeader("Content-Disposition", "attachment;filename=" + gridFSDBFile.getFilename());
		response.addHeader("Content-Length", "" + gridFSDBFile.getLength());
		response.setContentType(gridFSDBFile.getContentType());
		gridFSDBFile.writeTo(os);
		os.flush();
		os.close();
	}
}
