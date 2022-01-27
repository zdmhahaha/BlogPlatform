package blogplatform.controller;

import blogplatform.entity.BlogResult;
import blogplatform.entity.User;
import blogplatform.service.BlogService;
import blogplatform.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class BlogController {

    private UserService userService;
    private BlogService blogService;

    @Inject
    public BlogController(UserService userService, BlogService blogService) {
        this.userService = userService;
        this.blogService = blogService;
    }

    @RequestMapping(value = "/blog")
    @ResponseBody
    public BlogResult getBlogList(@RequestParam("page") Integer page,
                                  @RequestParam(value = "userId", required = false) Integer userId) {
        //page: 页码，不传默认 page 为1。如果设置该参数则获取博客列表的第 page 页博客列表
        if (page == null || page < 1) {
            page = 1;
        }
        return blogService.getBlogsFromDao(page, 10, userId);
    }


    @RequestMapping(value = "/blog/{blogId}", method = RequestMethod.GET)
    @ResponseBody
    public BlogResult getBlogbyId(@PathVariable("blogId") Integer blogId) {
        return blogService.getBlogById(blogId);

        //return Result.failureStatus("系统异常");
    }


    @PostMapping("/blog")
    @ResponseBody
    public BlogResult creatBlog(@RequestBody Map<String, Object> newBlog) {
        User loggedUser = getUserByCookieName();
        //查询是否登录
        if (loggedUser != null) {
            //登录成功 规定博客规范
            int titleLength = newBlog.get("title").toString().length();
            int contentLength = newBlog.get("content").toString().length();
            int descriptionLength = newBlog.get("description").toString().length();
            if (titleLength <= 0 || titleLength > 100) {
                return BlogResult.failureBlogStyle("标题不能为空/不能超过100字符");
            }
            if (contentLength <= 0 || titleLength > 100000) {
                return BlogResult.failureBlogStyle("内容不能为空/不能超过100000字符");
            }
            if (descriptionLength <= 0 || titleLength > 200) {
                return BlogResult.failureBlogStyle("简介不能为空/不能超过200字符");
            }

            newBlog.put("userId", loggedUser.getId());
            blogService.insertBlog(newBlog);

            return blogService.getLastOneBlog(loggedUser.getId());
        } else {
            return BlogResult.failure("登录后才能操作");
        }

    }

    @PatchMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResult patchBlog(@PathVariable("blogId") Integer blogId,
                                @RequestBody Map<String, Object> updateBlog) {
        User loggedUser = getUserByCookieName();
        //查询是否登录
        if (loggedUser != null) {
            updateBlog.put("blogId", blogId);
            return blogService.patchBlog(loggedUser, updateBlog);

        } else {
            return BlogResult.failure("登录后才能操作");
        }
    }

    @DeleteMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResult patchBlog(@PathVariable("blogId") Integer blogId) {
        User loggedUser = getUserByCookieName();
        //查询是否登录
        if (loggedUser != null) {
            return blogService.deleteBlog(loggedUser, blogId);
        } else {
            return BlogResult.failure("登录后才能操作");
        }
    }


    //根据cookie 从数据库中得到对应的User
    public User getUserByCookieName() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByUsername(username);
    }
}
