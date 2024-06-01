package com.moerealm.mapper;

import com.moerealm.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> selectAll();
    User selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}

