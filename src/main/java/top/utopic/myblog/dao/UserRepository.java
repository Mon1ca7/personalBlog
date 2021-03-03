package top.utopic.myblog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.utopic.myblog.pojo.User;

public interface UserRepository extends JpaRepository<User,Long> {
    //向数据库中查询用户名和密码
    User findByUsernameAndPassword(String username,String password);
}
