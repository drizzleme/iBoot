package com.github.drizzleme.cfg;

import com.github.drizzleme.bo.UserInfo;
import com.github.drizzleme.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

/**
 * Created with iBoot
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/10/9
 */
public class IBootShiroRealm extends AuthorizingRealm {

    @Resource
    private UserService userInfoServcie;

    /**
     * 此方法调用  hasRole,hasPermission的时候才会进行回调.
     *
     * 权限信息.(授权):
     * 1、如果用户正常退出，缓存自动清空；
     * 2、如果用户非正常退出，缓存自动清空；
     * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。
     * （需要手动编程进行实现；放在service进行调用）
     * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例，
     * 调用clearCached方法；
     * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        /*
        * 当没有使用缓存的时候，不断刷新页面的话，这个代码会不断执行，
        * 当其实没有必要每次都重新设置权限信息，所以我们需要放到缓存中进行管理；
        * 当放到缓存中时，这样的话，doGetAuthorizationInfo就只会执行一次了，
        * 缓存过期之后会再次执行。
        */
//      System.out.println("Authorization Manager--->MyShirRealm.doGetAuthorizationInfo()");
//      SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//      UserInfo suserInfo = (UserInfo) principals.getPrimaryPrincipal();
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
//       UserInfo userInfo = userInfoService.findByUsername(username)


        //权限单个添加;
        // 或者按下面这样添加
        //添加一个角色,不是配置意义上的添加,而是证明该用户拥有admin角色
//       authorizationInfo.addRole("admin");
        //添加权限
//       authorizationInfo.addStringPermission("userInfo:query");


        ///在认证成功之后返回.
        //设置角色信息.
        //支持 Set集合,
        //用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的四行可以不要
//          List<Role> roleList=user.getRoleList();
//          for (Role role : roleList) {
//              info.addStringPermissions(role.getPermissionsName());
//          }

//      for (SysRole role : suserInfo.getRoleList()) {
//          authorizationInfo.addRole(role.getRole());
//          for (SysPermission p : role.getPermissions()) {
//              authorizationInfo.addStringPermission(p.getPermission());
//          }
//      }
        //设置权限信息.
//       authorizationInfo.setStringPermissions(getStringPermissions(userInfo.getRoleList()));



        //return authorizationInfo;
        return null;
    }

    /**
     * 认证信息.(身份验证)
     * :
     * Authentication 是用来验证用户身份
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
//      第一种方式：直接获取用户名通过 token.getPrincipal()方法
//      //获取用户输入的账号(用户名)
//      String username = (String) token.getPrincipal();
        SimpleAuthenticationInfo authenticationInfo01 = new SimpleAuthenticationInfo();
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String loginMark = token.getHost();
        if("login".equals(loginMark)){
            String username = token.getUsername();
            System.out.println(token.getCredentials()+" token value"+username);
            //通过username从数据库中查找 User对象，如果找到，没找到.
            //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
            UserInfo userInfo = userInfoServcie.getUserInfoByUserName(username);
            System.out.println("login----->>userInfo="+userInfo);
            if(userInfo==null){
                return null;
            }


            //方式二：
            //UsernamePasswordToken对象用来存放提交的登录信息
            //      UsernamePasswordToken upToken = (UsernamePasswordToken)token;
            //      //logger.info("验证当前Subject时获取到token为：" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));
            //      //查出是否有此用户
            //      UserInfo userInfo = userInfoServcie.getUserInfoByUserName(upToken.getUsername());
            //      if(userInfo==null){
            //          // 若存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
            //          return null;
            //      }


           /*
            * 获取权限信息:这里没有进行实现，
            * 请自行根据UserInfo,Role,Permission进行实现；
            * 获取之后可以在前端for循环显示所有链接;
            */
            //userInfo.setPermissions(userService.findPermissions(user));


            //账号判断;

            //加密方式;
            //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
            //      SimpleAuthenticationInfo authenticationInfo  = new SimpleAuthenticationInfo(
            //              sUserInfo,//用户名
            //              sUserInfo.getPassword(),
            //              ByteSource.Util.bytes(sUserInfo.getCredentialsSalt()),//salt=username+salt
            //              getName() //realm name
            //      );

            SimpleAuthenticationInfo authenticationInfo =  new SimpleAuthenticationInfo(
                    userInfo,//用户名
                    userInfo.getUserPwd(),//密码
                    getName()//realm name

            );
            authenticationInfo01=authenticationInfo;
        }else if("login_phone".equals(loginMark)){
            String userPhone = token.getUsername();
            UserInfo userInfo = userInfoServcie.getUserInfoByUserPhone(userPhone);
            System.out.println("login_phone----->>userInfo="+userInfo);
            if(userInfo==null){
                return null;
            }

            SimpleAuthenticationInfo authenticationInfo =  new SimpleAuthenticationInfo(
                    userInfo,//用户名
                    userInfo.getUserPwd(),//密码
                    getName()//realm name

            );
            authenticationInfo01=authenticationInfo;
        }
        return authenticationInfo01;
    }
    // 清除缓存
    public void clearCached() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
    /**
     * 将权限对象中的权限code取出.
     * @param permissions
     * @return
     */
//  public Set<String> getStringPermissions(Set<SysPermission> permissions){
//     Set<String> stringPermissions = new HashSet<String>();
//     if(permissions != null){
//         for(SysPermission p : permissions) {
//            stringPermissions.add(p.getPermission());
//          }
//     }
//       return stringPermissions;
//  }

}
