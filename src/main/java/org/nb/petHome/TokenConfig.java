package org.nb.petHome;

import org.nb.petHome.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/7
 **/
@Configuration
public class TokenConfig implements WebMvcConfigurer  {

    private RedisTemplate redisTemplate;

    @Autowired
    public TokenConfig(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new TokenInterceptor(redisTemplate));
        registration.addPathPatterns("/**"); //所有路径都被拦截
        registration.excludePathPatterns(    //添加不拦截路径
                "/login",                    //登录路径
                "/**/*.html",                //html静态资源
                "/**/*.js",                  //js静态资源
                "/**/*.css",                  //css静态资源
                "/getverifycode"
        );
    }
}
