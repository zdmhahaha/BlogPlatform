package blogplatform.dao;

import blogplatform.entity.Blog;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogDao {

    private UserMapper userMapper;
    private SqlSession session;

    @Inject
    public BlogDao(SqlSession session, UserMapper userMapper) {
        this.session = session;
        this.userMapper = userMapper;
    }

    public List<Blog> getBlogs(Integer page, Integer pageSize, Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("offset", (page - 1) * pageSize);
        params.put("limit", pageSize);
        return session.selectList("myBlogMapper.selectBlogs", params);
    }

    public int count(Integer userId) {
        return session.selectOne("myBlogMapper.countBlogs", userId);
    }

    public Blog getBlogById(Integer blogID) {
        return session.selectOne("myBlogMapper.getBlogByBlogId", blogID);
    }

    public void createNewBlog(Map<String, Object> newBlog) {
        session.insert("myBlogMapper.createBlog", newBlog);
    }

    public int getUserIdByBlogId(Integer blogId) {
        return userMapper.getUserIdByBlogId(blogId);
    }

    public void updateBlog(Map<String, Object> newBlog) {
        session.update("myBlogMapper.updateBlog", newBlog);
    }

    public void deleteBlog(Integer blogId) {
        session.delete("myBlogMapper.deleteBlog", blogId);
    }

    public List<Integer> getBlogIdByUserId(Integer userId) {
        return userMapper.getBlogIdByUserId(userId);
    }
}
