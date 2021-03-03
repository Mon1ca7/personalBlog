package top.utopic.myblog.service;

import top.utopic.myblog.pojo.User;

public interface UserService {
    /**
     * 确认用户名和密码是否正确
     * @param username
     * @param password
     * @return
     */
    User checkUser(String username,String password);
}
