package cn.blmdz.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpecialController {
	
	private static final String prefix = "/";
	private static final String sufix = ".html";

	@GetMapping({"/index", "/"})
	public String indexView() {
		return prefix + "index" + sufix;
	}
}
