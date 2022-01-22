package blogplatform.service;

import blogplatform.entity.User;
import blogplatform.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private UserMapper userMapper;
    //密码加密
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    //用户名 和 用户信息
    //private Map<String, User> storedUserMessages = new ConcurrentHashMap<>();

    @Inject
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
        //signUp("demo","demo");
    }

    //用户注册的方法
    public void signUp(String username, String password) {
        userMapper.save(username, bCryptPasswordEncoder.encode(password));
        //storedUserMessages.put(username, new User(1,username,bCryptPasswordEncoder.encode(password)));
    }

    //根据用户名从数据库中得到密码
    public String getPassword(String username) {
        return userMapper.getUserByName(username).getEncrypedPassword();
    }

    //根据用户名查询数据库，得到对应的User
    public User getUserByUsername(String username) {
        return userMapper.getUserByName(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //数据库中根据用户名查询
        User user = getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + "用户不存在");
        }
        String rightPassword = user.getEncrypedPassword();
        return new org.springframework.security.core.userdetails.User(username, rightPassword, Collections.emptyList());
    }
}
