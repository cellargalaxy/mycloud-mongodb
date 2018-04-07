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
	
	@PostMapping("/uploadFile")
	@ResponseBody
	public ReturnBean upload(@RequestParam("file") MultipartFile multipartFile,
	                         @RequestParam(value = "pathDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date pathDate,
	                         @RequestParam(value = "description", required = false) String description) {
		File tmpFile = saveFile(multipartFile);
		if (tmpFile == null) {
			return new ReturnBean(false, "空文件");
		}
		mycloud.addFilePackageTask(tmpFile, pathDate, description, multipartFile.getContentType());
		return new ReturnBean(true, "已添加到队列：" + tmpFile.getName());
	}
	
	@PostMapping("/uploadUrl")
	@ResponseBody
	public ReturnBean upload(@RequestParam(value = "httpUrl") String httpUrl,
	                         @RequestParam(value = "pathDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date pathDate,
	                         @RequestParam(value = "description", required = false) String description) {
		mycloud.addFilePackageTask(httpUrl.trim(), pathDate, description);
		return new ReturnBean(true, "已添加到队列：" + httpUrl);
	}
	
	@PostMapping("/removeById")
	@ResponseBody
	public ReturnBean remove(@RequestParam(value = "id") String id) {
		if (id == null || id.length() == 0) {
			return new ReturnBean(false, "id不能为空");
		}
		return new ReturnBean(mycloud.removeFilePackage(id), "尝试删除id=" + id);
	}
	
	@PostMapping("/removeByInfo")
	@ResponseBody
	public ReturnBean remove(
			@RequestParam(value = "pathDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date pathDate,
			@RequestParam(value = "filename") String filename) {
		if (filename == null || filename.length() == 0) {
			return new ReturnBean(false, "文件名不能为空");
		}
		return new ReturnBean(mycloud.removeFilePackage(pathDate, filename), "尝试删除pathDate=" + pathDate + ", filename=" + filename);
	}
	
	@GetMapping("/createFilePackagePages")
	@ResponseBody
	public ReturnBean createFilePackagePages(@RequestParam(value = "page") int page) {
		return new ReturnBean(true, mycloud.createFilePackagePages(page));
	}
	
	@GetMapping("/findFilePackageById")
	@ResponseBody
	public ReturnBean findFilePackage(@RequestParam(value = "id") String id) {
		return new ReturnBean(true, mycloud.findFilePackage(id));
	}
	
	@GetMapping("/findFilePackageByInfo")
	@ResponseBody
	public ReturnBean findFilePackage(
			@RequestParam(value = "pathDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date pathDate,
			@RequestParam(value = "filename") String filename) {
		return new ReturnBean(true, mycloud.findFilePackage(pathDate, filename));
	}
	
	@GetMapping("/findFilePackages")
	@ResponseBody
	public ReturnBean findFilePackages(@RequestParam(value = "page") int page) {
		return new ReturnBean(true, mycloud.findFilePackages(page));
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
