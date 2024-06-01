package com.moerealm.service;

import com.moerealm.pojo.User;
import java.util.List;

public interface IUserService {
    List<User> getAllUsers();
    boolean isEmailInUse(String email);
    void registerUser(User user) throws Exception;
    public boolean login(String username, String password);
}
