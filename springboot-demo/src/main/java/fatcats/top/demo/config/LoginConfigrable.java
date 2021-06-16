package fatcats.top.demo.config;

import fatcats.top.demo.inteceptor.LoginInteceptor;
import fatcats.top.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Configuration 配置类，实现WebMvcConfigurer接口 用于拦截机制
 * @author yjh
 */
@Configuration
public class LoginConfigrable  implements WebMvcConfigurer {
    @Autowired
    UserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInteceptor(userService))
                .addPathPatterns("/**")
                .excludePathPatterns("/","/static/**","/login","/css/**","/js/**","/images/**","/fonts/**","/user/login","/regiest.html","/fnsregiest");
    }

}