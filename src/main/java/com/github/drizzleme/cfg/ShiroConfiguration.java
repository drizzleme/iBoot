package com.github.drizzleme.cfg;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with iBoot
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/10/9
 */
@Configuration
public class ShiroConfiguration {
    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
     Filter Chain定义说明
     1、一个URL可以配置多个Filter，使用逗号分隔
     2、当设置多个过滤器时，全部验证通过，才视为通过
     3、部分过滤器可指定参数，如perms，roles
     *
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        System.out.println("ShiroConfiguration.shiroFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //必须设置securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        //配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        //filterChainDefinitionMap.put("/logout", "logout");


        //配置记住我或认证通过可以访问的地址
        //filterChainDefinitionMap.put("/index02", "user");//对应的是页面名字
        //filterChainDefinitionMap.put("/", "user");



        //<!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/user/**", "authc");
        filterChainDefinitionMap.put("/login_phone", "anon");
        filterChainDefinitionMap.put("/login", "anon");

        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");//跳转到userController上的
        // 登录成功后要跳转的链接,第一次由他空置，以后自己控制的！
        shiroFilterFactoryBean.setSuccessUrl("/user/index02");

        //未授权界面;
        //shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;

    }

    /**
     * 身份认证realm;
     * (这个需要自己写，账号密码校验；权限等)
     * @return
     */
    @Bean
    public Realm myShiroRealm(){
        IBootShiroRealm myShiroRealm = new IBootShiroRealm();
        //myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        System.out.println("myshiro realm");
        return myShiroRealm;
    }

    /**
     * 将myShiroRealm注入到securityManager中：如果使用了doGetAuthorizationInfo()则权限控制生效
     * @return
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm
        securityManager.setRealm(myShiroRealm());

        //注入缓存管理器;
        securityManager.setCacheManager(ehCacheManager());//这个如果执行多次，也是同样的一个对象;

        //注入记住我管理器;
        //securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     *  所以我们需要修改下doGetAuthenticationInfo中的代码;
     * ）
     * @return
     */
//  @Bean
//  public HashedCredentialsMatcher hashedCredentialsMatcher(){
//      HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//      hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
//      hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
//      System.out.println("deal with");
//      return hashedCredentialsMatcher;
//  }

    /**
     *  开启shiro aop注解支持.让权限控制起作用
     *  使用代理方式;所以需要开启代码支持;
     *  没有他就没有授权这一说
     * @param securityManager
     * @return
     * Advisor： 顾问；指导教师
     * Attribute Source ：属性来源
     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor attributeSourceAdvisor(
//          SecurityManager securityManager){
//
//      AuthorizationAttributeSourceAdvisor attributeSourceAdvisor = new
//              AuthorizationAttributeSourceAdvisor();
//      attributeSourceAdvisor.setSecurityManager(securityManager);
//      return attributeSourceAdvisor;
//    }

    /**
     * shiro缓存管理器;
     * 需要注入对应的其它的实体类中：
     * 1、安全管理器：securityManager
     * 可见securityManager是整个shiro的核心；
     * @return
     */
    @Bean
    public EhCacheManager ehCacheManager(){
        System.out.println("ShiroConfiguration.getEhCacheManager()");
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
        System.out.println("has cszma ");
        return cacheManager;
    }

//    /**
//     * 记住密码
//     * cookie对象
//     */
//    @Bean
//    public SimpleCookie remembeeMecookie(){
//      System.out.println("ShiroConfiguration.rememberMeCookie()");
//      //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
//      SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
//      //<!-- 记住我cookie生效时间3天 ,单位秒;-->
//      simpleCookie.setMaxAge(259200);
//      return simpleCookie;
//
//    }
//
//    /**
//     * Cookie管理对象
//     */
//    @Bean
//    public CookieRememberMeManager rememberMeManager(){
//
//      System.out.println("ShiroConfiguration.rememberMeManager()");
//      CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
//      rememberMeManager.setCookie(remembeeMecookie());
//
//      return rememberMeManager;
//    }

//    /**
//      * Description: 指向某一个地址<br/>
//      * author: caishanzheng<br/>
//      * date: 2016年8月10日 下午12:00:25<br/>
//      * @return<br/>
//      * MyFormAuthenticationFilter
//    */
//    @Bean
//    public MyFormAuthenticationFilter authenticationFilter(){
//      MyFormAuthenticationFilter authenticationFilter = new MyFormAuthenticationFilter();
//      return authenticationFilter;
//    }
}
