package cn.blmdz.admin.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.blmdz.admin.services.shiro.AuthorizationLevelDescriptionAdvisor;

@RestController
@RequestMapping(name="测试类权限识别", value="/test")
public class TestShiroController {
	
	@Autowired AuthorizationLevelDescriptionAdvisor authorizationLevelDescriptionAdvisor;
    
    @RequiresUser
    @GetMapping(value="/t01", name="测试权限识别名称-t01")
    public String test01() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        methodName = "enter in method " + methodName + " ...";
        System.out.println(methodName);
        return methodName;
    }

    @RequiresAuthentication
    @GetMapping("/t02")
    public String test02() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        methodName = "enter in method " + methodName + " ...";
        System.out.println(methodName);
        return methodName;
    }

    @RequiresGuest
    @GetMapping(value="/t03", name="测试权限识别名称-t03")
    public String test03() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        methodName = "enter in method " + methodName + " ...";
        System.out.println(methodName);
        return methodName;
    }

    @RequiresPermissions(value = { "test01" })
    @GetMapping("/t04")
    public String test04() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        methodName = "enter in method " + methodName + " ...";
        System.out.println(methodName);
        return methodName;
    }

    @RequiresPermissions(value = { "test02" })
    @GetMapping("/t07")
    public String test07() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        methodName = "enter in method " + methodName + " ...";
        System.out.println(methodName);
        return methodName;
    }

    @RequiresRoles(value = { "login01" })
    @GetMapping("/t05")
    public String test05() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        methodName = "enter in method " + methodName + " ...";
        System.out.println(methodName);
        return methodName;
    }
    
    @RequiresRoles(value = { "login02" })
    @PostMapping("/t05")
    public String test0501() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        methodName = "enter in method " + methodName + " ...";
        System.out.println(methodName);
        return methodName;
    }

    @RequiresRoles(value = { "login02" })
    @GetMapping("/t06")
    public String test06() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        methodName = "enter in method " + methodName + " ...";
        System.out.println(methodName);
        return methodName;
    }

    @GetMapping("/t88")
    public String test88() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        methodName = "enter in method " + methodName + " ...";
        System.out.println(methodName);
        System.out.println(JSON.toJSONString(authorizationLevelDescriptionAdvisor.getResourceClasses()));
        return methodName;
    }
    


    @GetMapping("/login01")
    public String login01() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        methodName = "enter in method " + methodName + " ...";
        System.out.println(methodName);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("login01", "login01");
        subject.login(token);
        
        return methodName;
    }
    
    @GetMapping("/login02")
    public String login02() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        methodName = "enter in method " + methodName + " ...";
        System.out.println(methodName);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("login02", "login02");
        subject.login(token);
        
        return methodName;
    }
    
    @GetMapping("/loginout")
    public String loginout() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        methodName = "enter in method " + methodName + " ...";
        System.out.println(methodName);

        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        
        return methodName;
    }
    
}
