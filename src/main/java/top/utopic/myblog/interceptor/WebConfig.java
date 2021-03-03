package top.utopic.myblog.interceptor;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration //定义为配置文件
public class WebConfig implements WebMvcConfigurer {
    //相当于之前mvc的xml配置文件中的 mvc:interceptors
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")   //拦截admin下所有请求，只有登录了才能访问
                .excludePathPatterns("/admin")  //放行
                .excludePathPatterns("/admin/login"); //放行登陆页面
    }
}
