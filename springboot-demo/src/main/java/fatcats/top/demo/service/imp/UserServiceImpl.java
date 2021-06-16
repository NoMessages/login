package fatcats.top.demo.service.imp;

import fatcats.top.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     *      主要功能
     *      1.获取该用户Id
     *      2.判断当前端是否登录，如果登录，更新数据
     *      3.如果没有记录，可以插入记录，记载当前数据
     *
     * @param username
     * @param token
     */
    @Override
    public void jugeInfo(String username,String token) {
        String name = username.split("#")[1];
        String sel_uid = "SELECT id FROM u_info WHERE NAME = ? ";
        Integer uid = jdbcTemplate.queryForObject(sel_uid, Integer.class, name);
        String sql = "SELECT STATUS FROM db_status WHERE uid = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, uid.intValue());
        boolean isInsert = false;
        if(maps.size()>0){
            boolean flag = false;
            for (Map<String, Object> map : maps) {
                Set<String> strings = map.keySet();
                for (String s : strings) {
                    if(username.equals(map.get(s))){
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    break;
                }
            }
            //如果没有执行插入，如果有，执行更新
            if(flag){
                //更新
                String upt_usr_sql = "UPDATE db_status SET token = ? WHERE uid = ? and STATUS = ?";
                jdbcTemplate.update(upt_usr_sql,token,uid,username);
            }else{
                //插入
                isInsert = true;
            }
        }else{
            isInsert = true;
        }
        if(isInsert){
            //更新操作，往表中插入数据，并且返回false
            String upt_sql = "INSERT db_status VALUES(?,?,?)";
            jdbcTemplate.update(upt_sql,uid.intValue(),username,token);
        }
    }

    @Override
    public String getToken(String username) {
        String sql = "SELECT token FROM db_status WHERE STATUS = ?";
        String db_token = jdbcTemplate.queryForObject(sql, String.class, username);
        return db_token;
    }

}