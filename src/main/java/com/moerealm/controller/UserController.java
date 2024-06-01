package com.moerealm.controller;

import com.moerealm.pojo.ResponseData;
import com.moerealm.pojo.User;
import com.moerealm.service.EmailService;
import com.moerealm.service.Impl.UserService;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class UserController {

    private final EmailService emailService;
    private final Map<String, String> verificationCodes = new HashMap<>();

    @Autowired
    private UserService userService;

    @Autowired
    public UserController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> requestData) {
        try {
            String email = requestData.get("email");
            String verificationCode = generateVerificationCode();
            verificationCodes.put(email, verificationCode);

            emailService.sendVerificationEmail(email, "萌幻之境验证码", "your verificationCode^v^: " + verificationCode);

            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (EmailException e) {
            return ResponseEntity.status(500).body("发送出错: " + e.getMessage());
        }
    }


    @PostMapping("/users/verify")
    public ResponseEntity<?> verifyCode(@RequestParam String email, @RequestParam String code) {
        System.out.println("Received email: " + email + ", code: " + code);

        if (verificationCodes.containsKey(email) && verificationCodes.get(email).equals(code)) {
            verificationCodes.remove(email);
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(true);
            responseData.setMessage("验证码验证成功");
            return ResponseEntity.ok().body(responseData);
        } else {
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMessage("验证码验证失败");
            return ResponseEntity.ok().body(responseData);
        }
    }

    @PostMapping("/users/completeRegistration")
    public ResponseEntity<?> completeRegistration(@RequestBody User user) {
        try {
            // 检查邮箱是否已经被注册
            if (userService.isEmailInUse(user.getEmail())) {
                throw new Exception("邮箱已被注册！");
            }
            // 保存用户到数据库
            userService.registerUser(user);

            ResponseData responseData = new ResponseData();
            responseData.setSuccess(true);
            responseData.setMessage("注册成功");

            return ResponseEntity.ok().body(responseData);
        } catch (Exception e) {
            ResponseData responseData = new ResponseData();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());

            return ResponseEntity.ok().body(responseData);
        }
    }
    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }


//    登陆部分
@PostMapping("/users/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> requestData) {
    String username = requestData.get("username");
    String password = requestData.get("password");

    // 根据用户名和密码进行登录验证逻辑，这里假设使用UserService进行验证
    boolean loginSuccess = userService.login(username, password);

    if (loginSuccess) {
        return ResponseEntity.ok().body("{\"success\": true}");
    } else {
        return ResponseEntity.ok().body("{\"success\": false, \"message\": \"账号或密码错误\"}");
    }
}


}
