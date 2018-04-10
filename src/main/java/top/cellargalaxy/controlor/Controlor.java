package top.cellargalaxy.controlor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by cellargalaxy on 18-4-10.
 */
@Controller
@RequestMapping(Controlor.CONTROLOR_URL)
public class Controlor {
	public static final String CONTROLOR_URL = "";
	
	@GetMapping("/")
	public String show() {
		return "show";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/file")
	public String file() {
		return "file";
	}
	
	@GetMapping("/log")
	public String log() {
		return "log";
	}
}
