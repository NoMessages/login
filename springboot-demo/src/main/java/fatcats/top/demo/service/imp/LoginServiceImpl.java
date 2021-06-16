package fatcats.top.demo.service.imp;

import fatcats.top.demo.domain.User;
import fatcats.top.demo.service.LoginService;
import fatcats.top.demo.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *  * @author yjh
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    /* 加密匙 */
    MD5Util encoderMd5 = new MD5Util(MD5Util.MD5_SALT, "MD5");
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public User checkLogin(String name, String password) throws Exception {
        User user = null;
        boolean existsUser = isExistsUser(name);
        if(existsUser){
            //登录密码md5加密
            password = encoderMd5.encode(password);
            String sql  = "SELECT name , pwd , reg_time FROM u_info where name = ?";
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), name);
            if(!password.equals(user.getPwd())){
                user=null;
            }
        }else{
            user = null;
        }
        return user;
    }

    @Override
    public User regiest(String name, String password) {
        //查询数据库是否已经存在用户名？
        User user = null;
        boolean existsUser = isExistsUser(name);
        if(!existsUser){
            try {
                user = new User();
                //实现密码md5加密
                String sql = "insert into u_info(name,pwd) values(?,?)";
                password = encoderMd5.encode(password);

                jdbcTemplate.update(sql, name, password);
                user.setName(name);
                user.setPwd(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    @Override
    public boolean isExistsUser(String name) {
        String sql  = "select count(*) from u_info where name = ?";
        Long aLong = jdbcTemplate.queryForObject(sql, Long.class, name);
        boolean flag = false;
        if(aLong>=1){
            flag = true;
        }
        return flag;
    }

}
