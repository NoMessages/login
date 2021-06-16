package fatcats.top.demo.domain;

import java.io.Serializable;
import java.util.Date;

/**
   * @author yjh
 */
public class User implements Serializable {
    private String name;
    private String pwd;
    private Date reg_time;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Date getReg_time() {
        return reg_time;
    }

    public void setReg_time(Date reg_time) {
        this.reg_time = reg_time;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", reg_time=" + reg_time +
                '}';
    }
}
