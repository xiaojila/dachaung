package com.dandelion.management.config;

import com.dandelion.management.bean.CustomRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Class description
 *
 * @author hongjianYang
 * @date 2020/10/20
 */
@Configuration
public class ShiroConfig {
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    /**
     *
     * 将自己的验证方式加入容器
     */
    @Bean
    public CustomRealm myShiroRealm() {
        CustomRealm customRealm = new CustomRealm();
        return customRealm;
    }

    /**
     *  权限管理，配置主要是Realm的管理认证
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    /**
     * Filter工厂，设置对应的过滤条件和跳转条件
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> map = new HashMap<>();
        //登出
        map.put("/logout", "logout");
        //对所有用户认证
        map.put("/**", "authc");
        map.put("/login-1.html","anon");
        map.put("/lib/**","anon");
        map.put("/js/**","anon");
        map.put("/css/**","anon");
        map.put("/images/**","anon");
        map.put("/login","anon");
        //登录
        shiroFilterFactoryBean.setLoginUrl("/login-1.html");
        //首页
        shiroFilterFactoryBean.setSuccessUrl("/index.html");
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}