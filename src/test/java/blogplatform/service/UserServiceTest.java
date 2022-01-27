package blogplatform.service;

import blogplatform.entity.User;
import blogplatform.dao.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    BCryptPasswordEncoder mockBCryptPasswordEncoder;
    @Mock
    UserMapper mockUserMapper;
    @InjectMocks
    UserService userService;

    //调用userservice，验证userservice将请求发给了usermapper
    @Test
    public void signUpTest() {
        //when(mockBCryptPasswordEncoder.encode("mypassword")).thenReturn("myEncode");
        //得到的结果应该是mockUserMapper.save(myuser,mockBCryptPasswordEncoder.encode(mypassword))
        userService.signUp("myuser","mypassword");
        Mockito.verify(mockUserMapper).save("myuser",mockBCryptPasswordEncoder.encode("mypassword"));
    }

    @Test
    public void getPasswordTest() {
        userService.getUserByUsername("zhangdemao");
        Mockito.verify(mockUserMapper).getUserByName("zhangdemao");
    }

    @Test
    public void getUserByUsernameTest() {
        userService.getUserByUsername("zhangdemao");
        Mockito.verify(mockUserMapper).getUserByName("zhangdemao");
    }

    @Test
    public void getUsernameNotFoundException(){
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("zhangdemao"));
    }

    @Test
    public void getUser(){
        when(mockUserMapper.getUserByName("zhang"))
                .thenReturn(new User(1,"zhang","144444"));
        UserDetails userDetails = userService.loadUserByUsername("zhang");
        Assertions.assertEquals("zhang",userDetails.getUsername());
        Assertions.assertEquals("144444",userDetails.getPassword());
    }

}