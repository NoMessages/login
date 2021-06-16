package fatcats.top.demo;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@SpringBootTest
class SpringbootDemoApplicationTests {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() {
        String sel_uid = "SELECT id FROM db_user WHERE username = ? ";
        Integer uid = jdbcTemplate.queryForObject(sel_uid, Integer.class, "admin");
        String sql = "SELECT STATUS FROM db_status WHERE uid = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, uid.intValue());
        Map<String, Object> map = maps.get(0);
        String username = "";
        String token = "";
       if(map.size()>0){
           boolean flag = false;
           Set<String> strings = map.keySet();
           for (String s : strings) {
               if(username.equals(s)){
                   flag = true;
                   break;
               }
           }
           //如果没有执行插入，如果有，执行更新
           if(flag){
               //更新
            String upt_usr_sql = "UPDATE db_status SET STATUS = ? , token = ? WHERE uid = ?";
            jdbcTemplate.update(upt_usr_sql,username,token,uid);
           }else{
               //插入
               String upt_sql = "INSERT db_statusVALUES(?,?,?)";
               jdbcTemplate.update(upt_sql,uid.intValue(),username,token);
           }
           //如果当前端已经登录，则返回true
       }else{
           //更新操作，往表中插入数据，并且返回false
           String upt_sql = "INSERT db_statusVALUES(?,?,?)";
           jdbcTemplate.update(upt_sql,uid.intValue(),username,token);
       }
    }
}
