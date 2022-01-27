package blogplatform.service;

import blogplatform.dao.BlogDao;
import blogplatform.entity.Blog;
import blogplatform.entity.BlogResult;
import blogplatform.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
public class BlogService {

    private BlogDao blogDao;

    @Inject
    public BlogService(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    //page当前页数 pageSize每页博客数 userId用户id
    public BlogResult getBlogsFromDao(Integer page, Integer pageSize, Integer userId) {
        try {
            //根据用户名查出所有博客数
            int count = blogDao.count(userId);
            //总页数
            int totalPageNum = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            List<Blog> blogDaoBlogs = blogDao.getBlogs(page, pageSize, userId);
            return BlogResult.allBlogs(count, page, totalPageNum, blogDaoBlogs);
        } catch (Exception e) {
            return BlogResult.failure("系统异常");
        }
    }

    public BlogResult getBlogById(Integer blogId) {
        try {
            return BlogResult.allBlogs(1, 1, 1, blogDao.getBlogById(blogId));
        } catch (Exception e) {
            return BlogResult.failure("系统异常");
        }
    }

    public void insertBlog(Map<String, Object> newBlog) {
        blogDao.createNewBlog(newBlog);
    }

    public BlogResult getLastOneBlog(Integer useID) {
        //查出userId对应的所有的博客id
        List<Integer> blogIds = blogDao.getBlogIdByUserId(useID);
        //根据blogid查出 最后一个博客id
        int finalBlogId = blogIds.get(blogIds.size() - 1);
        return BlogResult.Success(10, "创建成功", 1, 10, blogDao.getBlogById(finalBlogId));
    }

    public BlogResult patchBlog(User loggedUser, Map<String, Object> updateBlog) {
        //根据blogId查找对应的userId ， 判断是否相等
        Integer blogId = (Integer) updateBlog.get("blogId");
        if (loggedUser.getId() != blogDao.getUserIdByBlogId(blogId)) {
            return BlogResult.failure("无法修改别人的博客");
        }
        //数据库中更新博客
        blogDao.updateBlog(updateBlog);
        return BlogResult.Success(88, "修改成功", 1, 10, blogDao.getBlogById(blogId));
    }

    public BlogResult deleteBlog(User loggedUser, Integer blogId) {
        //根据blogId查找对应的userId ， 判断是否相等
        if (loggedUser.getId() != blogDao.getUserIdByBlogId(blogId)) {
            return BlogResult.failure("无法删除别人的博客");
        }
        //数据库中更新博客
        blogDao.deleteBlog(blogId);
        return BlogResult.Success(88, "删除成功", 0, 0, null);
    }


}
