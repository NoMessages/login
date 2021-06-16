package fatcats.top.demo.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import fatcats.top.demo.domain.User;
import fatcats.top.demo.service.LoginService;
import fatcats.top.demo.service.UserService;
import fatcats.top.demo.util.CustomDateUtils;
import fatcats.top.demo.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录处理的controller
 *  * @author yjh
 */
@Slf4j
@Controller
public class UserLoginController {

    @Resource
    LoginService loginService;
    @Resource
    UserService userService;
    /**
     * 转发到时登录视图
     * @return 登录视图
     */
    @GetMapping(value = {"/","/login"})
    public String toUserLogin(){
        return "login";
    }

    /**
     * 正常是返回json数据
     * @param username 用户名
     * @param password 密码
     * @param model 转发器
     * @return 处理登录请求
     */
    @PostMapping("/user/login")
    public String userLogin(@RequestParam("username") String username
            , @RequestParam("password") String password
            , Model model , HttpServletRequest request ,
                            @RequestHeader("User-Agent") String userAgent ) throws Exception {
        //登录成功重定向 失败则返回登录界面
        Map<String , Object > map = new HashMap<>();
        //检测登录
        User loginUser = loginService.checkLogin(username, password);
        if(loginUser != null){
            log.info("登录成功！！");
            //使用JWT认证
            Map<String , String > payload = new HashMap<>();
            payload.put("name",username);
            //token中式不宜存放用户敏感信息的。
            payload.put("nickname",username);
            payload.put("role","admin");
            payload.put("regtime",loginUser.getReg_time().toString());
            payload.put("rantime",System.currentTimeMillis()+"");
            //单次登录，生成单次的token，存储token
            String token = JwtUtils.getToken(payload);
            /*
                    1.获取来自哪端的请求  userAgent
                    2.更新数据库信息
                    3.跳转到成功界面
             */
            log.error("请求头信息为：{}",userAgent);
            String ua = "";
            if(userAgent.indexOf("android") != -1 || userAgent.indexOf("Android") != -1){
                log.info("Android 设备请求");
                ua = "1#"+username;
            }else if(userAgent.indexOf("mac") != -1 || userAgent.indexOf("os") != -1 || userAgent.indexOf("Mac") != -1 || userAgent.indexOf("Os") != -1){
                log.info("iphone  ipad 请求");
                ua = "2#"+username;
            }else{
                log.info("Window phone请求");
                ua = "3#"+username;
            }
            //登记信息  在登记之前，修改token值并且传递过去
            userService.jugeInfo(ua,token);
            //存一下dbbase的token
            request.getSession().setAttribute("token",token);
            //哪端登录状态存储
            request.getSession().setAttribute("u_name",ua);
            return "redirect:/success.html";
        }
        log.error("登录失败！！");
        model.addAttribute("msg","用户名或密码错误");
        return "login";
    }

    /**
     * 注册系统
     */
    @GetMapping("/regiest.html")
    public String toRegiest(){
        return "regiest";
    }
    /**
     * 注册用户
     * 纯web 开发应当是返回User json数据
     */
    @PostMapping("/fnsregiest")
    public String regiestUser(@RequestParam("username") String username , @RequestParam("password") String password,Model model ){
        User regiest = loginService.regiest(username, password);
        if(regiest!=null){
            model.addAttribute("msg","注册成功！");
        }else{
                //重复注册不跳转，继续保持注册
            model.addAttribute("msg","重复注册！");
            return "/regiest.html";
        }
        return "redirect:/login";
    }

    /**
     * @return 跳转到成功界面
     */
    @GetMapping("/success.html")
    public String toSuccess(HttpServletRequest request , Model model) throws ParseException {
        String token = (String) request.getSession().getAttribute("token");
        String realToken = "";
        //解析jwt对象
        DecodedJWT user = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        realToken = token.substring(7);
        try {
            user = JwtUtils.verity(realToken);
            model.addAttribute("name",user.getClaim("name").asString());
            model.addAttribute("nickname",user.getClaim("nickname").asString());
            model.addAttribute("role",user.getClaim("role").asString());
            String regtime = user.getClaim("regtime").asString();
            Date parse = sdf.parse(regtime);
            String toCurrentTime = CustomDateUtils.diffTimesToNow(new Date(System.currentTimeMillis()), parse);
            model.addAttribute("regtime",toCurrentTime);
        }catch (Exception e){
        }
        //这里直接被拦截
        return "success";
    }


    @GetMapping("/user/test")
    @ResponseBody
    public String testToken(){
        return "当前端认证通过，并且可以访问~";
    }
}