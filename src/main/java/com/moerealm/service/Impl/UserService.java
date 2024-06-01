package com.moerealm.service.Impl;

import com.moerealm.pojo.User;
import com.moerealm.mapper.UserMapper;
import com.moerealm.repository.UserRepository;
import com.moerealm.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserMapper userMapper;

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean isEmailInUse(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void registerUser(User user) throws Exception {
        if (isEmailInUse(user.getEmail())) {
            throw new Exception("邮箱已被注册！");
        }
        userRepository.save(user);
    }


    public boolean login(String username, String password) {
        User user = userMapper.selectByUsernameAndPassword(username, password);
        return user != null;
    }

}

