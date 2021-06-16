package fatcats.top.demo.service;

import fatcats.top.demo.domain.User;

/**
 *  * @author yjh
 */
public interface LoginService {
    /**
     * 检查登录
     * @param name 用户名
     * @param password 密码
     * @return 返回是否登录成功
     * @throws Exception 可能会出现转换异常
     */
    User checkLogin(String name , String password) throws Exception;

    /**
     * 注册用户
     * @param name 用户名
     * @param password 密码
     * @return 返回注册对象
     */
    User regiest(String name, String password);

    /**
     * 判断用户名是否重复注册
     * @param name 用户名
     * @return 返回用户名是否已经存在
     */
    boolean isExistsUser(String name);
}
