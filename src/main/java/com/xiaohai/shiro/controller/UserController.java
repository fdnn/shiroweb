package com.xiaohai.shiro.controller;

import com.xiaohai.shiro.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 *
 * @Author 小海
 * @Date 17:01 2018/12/27
 **/
@Controller
    public class UserController {

    @RequestMapping(value = "/signIn", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String signIn(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),
                user.getPassword());
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            return e.getMessage();
        }

        if (subject.hasRole("user")) {
            if (subject.isPermitted("user:add")) {
                return "有user:add权限";
            }
            return "有user角色";
        }
        return "登陆成功";
    }

    /*************************************编程式授权*************************************/
    @RequestMapping(value = "/testRoleConfig", method = RequestMethod.GET)
    @ResponseBody
    public String testRoleConfig() {
        return "testRoleConfig success";
    }

    /*************************************注解式授权*************************************/
    /**
     *  @RequiresAuthentication
     *      要求当前 Subject 已经在当前的 session 中被验证通过才能被访问或调用。
     *  @RequiresGuest
     *      要求当前的 Subject 是一个"guest"，也就是说，他们必须是在之前的 session 中没有被验证或被记住才能被访问或调用。
     *  @RequiresRoles("user")
     *      要求当前的 Subject 拥有所有指定的角色。如果他们没有，则该方法将不会被执行，而且 AuthorizationException 异常将会被抛出。
     *  @RequiresPermissions("user:add")
     *      要求当前的 Subject 被允许一个或多个权限，以便执行注解的方法。
     *  @RequiresUser
     *      要求当前的 Subject 是一个应用程序用户才能被注解的类/实例/方法访问或调用。
     *      一个“应用程序用户”被定义为一个拥有已知身份，或在当前 session 中由于通过验证被确认，或者在之前 session 中的'RememberMe'服务被记住。
    */

    @RequiresGuest
    @RequestMapping(value = "/testGuest", method = RequestMethod.GET)
    @ResponseBody
    public String testGuest() {
        return "testGuest success";
    }

    @RequiresAuthentication
    @RequestMapping(value = "/testAuthe", method = RequestMethod.GET)
    @ResponseBody
    public String testAuthe() {
        return "testAuthe success";
    }

    @RequiresRoles("user")
    @RequestMapping(value = "/testRole", method = RequestMethod.GET)
    @ResponseBody
    public String testRole() {
        return "testRole success";
    }

    @RequiresPermissions("user:add")
    @RequestMapping(value = "/testPerms", method = RequestMethod.GET)
    @ResponseBody
    public String testPerms() {
        return "testPerms success";
    }

    @RequiresUser
    @RequestMapping(value = "/testUser", method = RequestMethod.GET)
    @ResponseBody
    public String testUser() {
        return "testUser success";
    }
}
