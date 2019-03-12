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
    @GetMapping("/404")
    public String e404View() {
        return prefix + "404" + sufix;
    }
    @GetMapping("/login")
    public String loginView() {
        return prefix + "login" + sufix;
    }
    @GetMapping("/success")
    public String successView() {
        return prefix + "success" + sufix;
    }
    @GetMapping("/unauth")
    public String unauthView() {
        return prefix + "unauth" + sufix;
    }
}
