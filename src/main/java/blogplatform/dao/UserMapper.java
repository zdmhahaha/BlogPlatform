package blogplatform.dao;

import blogplatform.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from `user` where username = #{username}")
    User getUserByName(@Param("username") String name);

    @Insert("insert into `user`" +
            "(username,encrypted_password,created_at,updated_at) " +
            "values (#{username}, #{encrypedPassword}, now(), now())")
    void save(@Param("username") String username,
              @Param("encrypedPassword") String password);

    @Select("select user_id from blog where id = #{blogId}")
    int getUserIdByBlogId(@Param("blogId") Integer blogId);

    @Select("select id from blog where user_id = #{userId}")
    List<Integer> getBlogIdByUserId(@Param("userId") Integer userId);
}

