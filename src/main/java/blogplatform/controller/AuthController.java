package blogplatform.controller;

import blogplatform.entity.Result;
import blogplatform.entity.User;
import blogplatform.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {

    private AuthenticationManager authenticationManager;
    //UserService服务
    private UserService userService;

    @Inject
    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @GetMapping("/auth")
    @ResponseBody
    public Object auth() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = userService.getUserByUsername(username);
        if (loggedUser == null) {
            return Result.okStatusButNoLogin("用户未登录");
        }
        return Result.okStatusAndLogin("登录成功", userService.getUserByUsername(username));
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result post(@RequestBody Map<String, Object> usernameAndPassword) {
        String username = usernameAndPassword.get("username").toString();
        String password = usernameAndPassword.get("password").toString();

        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (Exception e) {
            return Result.failureStatus("用户不存在");
        }

        //鉴权和验权
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        try {
            authenticationManager.authenticate(token);
            //保存用户信息到context容器中
            //cookie
            SecurityContextHolder.getContext().setAuthentication(token);
            System.out.println("");
            return Result.okStatusAndLogin("登录成功", userService.getUserByUsername(username));
            //鉴权未通过，丢出异常
        } catch (BadCredentialsException e) {
            return Result.failureStatus("密码不正确");
        }
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public Result registerUser(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        if (username == null || password == null) {
            return Result.failureStatus("用户/密码不能为空");
        }
        if (username.length() > 15) {
            return Result.failureStatus("用户名长度不合法");
        }
        if (password.length() < 6 || password.length() > 16) {
            return Result.failureStatus("密码长度不合法");

        }
        //根据数据库中用户名是唯一的，所以可以直接保存
        try {
            userService.signUp(username, password);
            return new Result("ok", "注册成功", false, userService.getUserByUsername(username));
        } catch (DuplicateKeyException e) {
            return Result.failureStatus("用户已存在");
        }

    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Result logout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = userService.getUserByUsername(username);
        if (loggedUser == null) {
            return Result.failureStatus("用户未登录");
        } else {
            SecurityContextHolder.clearContext();
            return Result.okStatusButNoLogin("注销成功");
        }
    }

}
