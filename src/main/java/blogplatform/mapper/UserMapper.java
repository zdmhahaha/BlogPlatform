package blogplatform.mapper;

import blogplatform.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from `user` where username = #{username}")
    User getUserByName(@Param("username") String name);

    @Insert("insert into `user`" +
            "(username,encrypted_password,created_at,updated_at) " +
            "values (#{username}, #{encrypedPassword}, now(), now())")
    void save(@Param("username") String username,
              @Param("encrypedPassword") String password);
}

