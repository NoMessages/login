package fatcats.top.demo.inteceptor;

import fatcats.top.demo.service.LoginService;
import fatcats.top.demo.service.UserService;
import fatcats.top.demo.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录检查
 * * @author yjh
 */
@Slf4j
public class LoginInteceptor implements HandlerInterceptor {
    @Autowired
    public LoginService loginService;

    public UserService userService;
    public LoginInteceptor(UserService userService) {
        this.userService = userService;
    }

    /**
     *  核心认证部分
     * @param request 请求域
     * @param response 响应域
     * @param handler 处理域
     * @return 是否放行
     * @throws Exception 异常处理
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        log.info("拦截的路径为：{}",requestUri);
        Map<String ,String > map = new HashMap<>();
        //token 附带在请求头中的验证令牌
        String token = (String) request.getSession().getAttribute("token");
        //哪端登录+用户名
        String u_name = (String) request.getSession().getAttribute("u_name");
        log.info("session的信息:{}",token);
        log.info("目前登录的状态信息:{}",u_name);
        String errMsg = "";
        String realToken = "";
        //我们自己约定了请求头的前半部分为Custom 我们自己定义的规则
        try {
            if(token == null){
                errMsg = "您还未登录，请先登录！";
                throw new RuntimeException(errMsg);
            }
                if(!token.startsWith("custom ")){
                    errMsg = "非法仿制token登录！！";
                    throw new RuntimeException(errMsg);
                }
                 if(!userService.getToken(u_name).equals(token)){
                    errMsg = "您已经在另一处登录，请重新登录！";
                    throw new RuntimeException(errMsg);
                }
                realToken = token.substring(7);
                //验证token是否正确
                JwtUtils.verity(realToken);
            return true;
        }catch (Exception e){
            log.error("错误信息是：{}",e.toString());
            request.setAttribute("msg",errMsg);
            request.getRequestDispatcher("/").forward(request,response);
        }
        return false;
    }

}