package top.cellargalaxy.controlor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.cellargalaxy.bean.controlor.ReturnBean;
import top.cellargalaxy.service.Mycloud;

import java.io.*;
import java.util.Date;

/**
 * Created by cellargalaxy on 18-4-6.
 */
@Controller
@RequestMapping(ApiControlor.API_CONTROLOR_URL)
public class ApiControlor {
	public static final String API_CONTROLOR_URL = "/api";
	@Autowired
	private Mycloud mycloud;
	
	@PostMapping("/addFile")
	@ResponseBody
	public ReturnBean addFile(@RequestParam("file") MultipartFile multipartFile,
	                          @RequestParam(value = "pathDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date pathDate,
	                          @RequestParam(value = "description", required = false) String description) {
		File tmpFile = saveFile(multipartFile);
		if (tmpFile == null) {
			return new ReturnBean(false, "空文件");
		}
		mycloud.addFile(tmpFile, pathDate, description, multipartFile.getContentType());
		return new ReturnBean(true, "已添加到队列：" + tmpFile.getName());
	}
	
	@PostMapping("/addHttpUrl")
	@ResponseBody
	public ReturnBean addHttpUrl(@RequestParam(value = "httpUrl") String httpUrl,
	                             @RequestParam(value = "pathDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date pathDate,
	                             @RequestParam(value = "description", required = false) String description) {
		mycloud.addHttpUrl(httpUrl, pathDate, description);
		return new ReturnBean(true, "已添加到队列：" + httpUrl);
	}
	
	@PostMapping("/removeFilePackageById")
	@ResponseBody
	public ReturnBean removeFilePackageById(@RequestParam(value = "id") String id) {
		if (id == null || id.length() == 0) {
			return new ReturnBean(false, "删除id不能为空");
		}
		return new ReturnBean(mycloud.removeFilePackageById(id), "尝试删除id=" + id);
	}
	
	@PostMapping("/removeFilePackageByInfo")
	@ResponseBody
	public ReturnBean removeFilePackageByInfo(
			@RequestParam(value = "pathDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date pathDate,
			@RequestParam(value = "filename") String filename) {
		if (filename == null || filename.length() == 0) {
			return new ReturnBean(false, "删除文件名不能为空");
		}
		return new ReturnBean(mycloud.removeFilePackageByInfo(pathDate, filename), "尝试删除pathDate=" + pathDate + ", filename=" + filename);
	}
	
	@GetMapping("/createFilePackagePages")
	@ResponseBody
	public ReturnBean createFilePackagePages(@RequestParam(value = "page") int page) {
		return new ReturnBean(true, mycloud.createFilePackagePages(page));
	}
	
	@GetMapping("/findFilePackageById")
	@ResponseBody
	public ReturnBean findFilePackageById(@RequestParam(value = "id") String id) {
		return new ReturnBean(true, mycloud.findFilePackageById(id));
	}
	
	@GetMapping("/findFilePackageByInfo")
	@ResponseBody
	public ReturnBean findFilePackageByInfo(
			@RequestParam(value = "pathDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date pathDate,
			@RequestParam(value = "filename") String filename) {
		return new ReturnBean(true, mycloud.findFilePackageByInfo(pathDate, filename));
	}
	
	@GetMapping("/findFilePackages")
	@ResponseBody
	public ReturnBean findFilePackages(@RequestParam(value = "page") int page) {
		return new ReturnBean(true, mycloud.findFilePackages(page));
	}
	
	
	@PostMapping("/backupFilePackageById")
	@ResponseBody
	public ReturnBean backupFilePackageById(@RequestParam(value = "id") String id) {
		if (id == null || id.length() == 0) {
			return new ReturnBean(false, "备份id不能为空");
		}
		mycloud.backupFilePackageById(id);
		return new ReturnBean(true, "备份id已加入队列=" + id);
	}
	
	@PostMapping("/backupFilePackageByInfo")
	@ResponseBody
	public ReturnBean backupFilePackageByInfo(
			@RequestParam(value = "pathDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date pathDate,
			@RequestParam(value = "filename") String filename) {
		if (filename == null || filename.length() == 0) {
			return new ReturnBean(false, "备份文件名不能为空");
		}
		mycloud.backupFilePackageByInfo(pathDate, filename);
		return new ReturnBean(true, "备份文件已加入队列pathDate=" + pathDate + ", filename=" + filename);
	}
	
	@PostMapping("/backupAllFilePackage")
	@ResponseBody
	public ReturnBean backupAllFilePackage() {
		mycloud.backupAllFilePackage();
		return new ReturnBean(true, "全部备份文件已加入队列");
	}
	
	
	@PostMapping("/restoreFilePackageById")
	@ResponseBody
	public ReturnBean restoreFilePackageById(@RequestParam(value = "id") String id) {
		if (id == null || id.length() == 0) {
			return new ReturnBean(false, "恢复id不能为空");
		}
		mycloud.restoreFilePackageById(id);
		return new ReturnBean(true, "恢复id已加入队列=" + id);
	}
	
	@PostMapping("/restoreFilePackageByInfo")
	@ResponseBody
	public ReturnBean restoreFilePackageByInfo(
			@RequestParam(value = "pathDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date pathDate,
			@RequestParam(value = "filename") String filename) {
		if (filename == null || filename.length() == 0) {
			return new ReturnBean(false, "恢复文件名不能为空");
		}
		mycloud.restoreFilePackageByInfo(pathDate, filename);
		return new ReturnBean(true, "恢复文件已加入队列pathDate=" + pathDate + ", filename=" + filename);
	}
	
	@PostMapping("/restoreAllFilePackage")
	@ResponseBody
	public ReturnBean restoreAllFilePackage() {
		mycloud.restoreAllFilePackage();
		return new ReturnBean(true, "全部恢复文件已加入队列");
	}
	
	
	@GetMapping("/createTaskPages")
	@ResponseBody
	public ReturnBean createTaskPages(@RequestParam(value = "page") int page) {
		return new ReturnBean(true, mycloud.createTaskPages(page));
	}
	
	@GetMapping("/findTasks")
	@ResponseBody
	public ReturnBean findTasks(@RequestParam(value = "page") int page) {
		return new ReturnBean(true, mycloud.findTasks(page));
	}
	
	private final File saveFile(MultipartFile multipartFile) {
		File file = new File("mycloudTmp" + File.separator + multipartFile.getOriginalFilename());
		file.getParentFile().mkdirs();
		try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
			if (multipartFile == null || multipartFile.isEmpty()) {
				return null;
			}
			bufferedOutputStream.write(multipartFile.getBytes());
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
