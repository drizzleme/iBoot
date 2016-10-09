package com.github.drizzleme.controller;

import com.github.drizzleme.bo.User;
import com.github.drizzleme.bo.UserAccessLog;
import com.github.drizzleme.bo.UserInfo;
import com.github.drizzleme.bo.UserRolePermission;
import com.github.drizzleme.service.UserService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with iBoot
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/10/8
 */
@Controller
@RequestMapping
public class UserController {
    @Autowired
    UserService userService;

    Map<String, String> map = new HashMap<>(20);
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @RequestMapping("/selectAll")
    @ResponseBody
    public List<User> selectAll(){
        return userService.selectAll();
    }

    @RequestMapping(value="/user/index02")
    public String index(){
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        //取身份信息
        UserInfo userInfo = (UserInfo) currentUser.getPrincipal();
        session.setAttribute("userInfo", userInfo);
        Integer roleId = userInfo.getRoleId();
        if(userInfo!=null && roleId!=null){
            Integer uid = userInfo.getUserId();
            UserRolePermission userRolePermisssion = userService.getRolePerByUidWithRid(uid, roleId);
            map.put(uid.toString(), userRolePermisssion.getPermissionBitOperator());
            //isPermission(uid,PermissionBitOperator.PERMISSION_ASSIGNADMINISTRATOR_STATE);
        }
        UserAccessLog userAccessLog = new UserAccessLog();
        if(userInfo!=null){
            userAccessLog.setUserId(userInfo.getUserId());
            userAccessLog.setRecordType(0);
            userAccessLog.setRecordAddress("127.0.0.1");
            userAccessLog.setDeviceModel("2");
            userAccessLog.setDeviceVersion("1.0.0");
            userAccessLog.setRecordTime(new Timestamp(System.currentTimeMillis()));
            userAccessLog.setSystemVersion("5.4.0");
            userService.insertUserAccessLog(userAccessLog);
        }
        System.out.println("go to login");
        return "/index02";
    }

    @RequestMapping(value="/login",method=RequestMethod.GET)
    public String login(HttpServletRequest request){
        request.setAttribute("message", "");
        return "/login";
    }
    //账户密码登录
    @RequestMapping(value="/login",method= RequestMethod.POST)
    public String login(@Valid UserInfo info, HttpServletRequest request, RedirectAttributes redirectAttributes){
        System.out.println("UserController.login()");
        //登录失败从request中获取shiro处理的异常信息
        // shiroLoginFailure:就是shiro异常类的全类名.
        //String exception = (String) request.getAttribute("shiroLoginFailure");
        String userName = info.getUserName();

        //开始调用shiro验证
        UsernamePasswordToken token = new UsernamePasswordToken(userName,info.getUserPwd(),"login");
        //获取当前的subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            logger.info("对用户[" + userName + "]进行登录验证..验证开始");
            currentUser.login(token);
            logger.info("对用户[" + userName + "]进行登录验证..验证通过");
        }catch(UnknownAccountException uae){
            logger.info("对用户[" + userName + "]进行登录验证..验证未通过,未知账户");
            redirectAttributes.addFlashAttribute("message", "未知账户");
        }catch(IncorrectCredentialsException ice){
            logger.info("对用户[" + userName + "]进行登录验证..验证未通过,错误的凭证");
            redirectAttributes.addFlashAttribute("message", "密码不正确");
        }catch(LockedAccountException lae){
            logger.info("对用户[" + userName + "]进行登录验证..验证未通过,账户已锁定");
            redirectAttributes.addFlashAttribute("message", "账户已锁定");
        }catch(ExcessiveAttemptsException eae){
            logger.info("对用户[" + userName + "]进行登录验证..验证未通过,错误次数过多");
            redirectAttributes.addFlashAttribute("message", "用户名或密码错误次数过多");
        }catch(AuthenticationException ae){
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            logger.info("对用户[" + userName + "]进行登录验证..验证未通过,堆栈轨迹如下");
            ae.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");
        }
        //验证是否登录成功
        if(currentUser.isAuthenticated()){
            logger.info("用户[" + userName + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
            return "redirect:/user/index02";
        }else{
            token.clear();
            return "redirect:/login";
        }
    }


    //手机号验证码登录
    //@ResponseBody
    @RequestMapping(value="/login_phone/{phoneNumber}/{phoneValidate}",method=RequestMethod.GET)
    public String login_phone(@PathVariable("phoneNumber") String phoneNumber,
                              @PathVariable("phoneValidate") String phoneValidate
            ,RedirectAttributes redirectAttributes){


        UsernamePasswordToken token = new UsernamePasswordToken(phoneNumber,phoneValidate,"login_phone");
        //获取当前的subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            logger.info("对用户[" + phoneNumber + "]进行登录验证..验证开始");
            currentUser.login(token);

            logger.info("对用户[" + phoneNumber + "]进行登录验证..验证通过");
        }catch(UnknownAccountException uae){
            logger.info("对用户[" + phoneNumber + "]进行登录验证..验证未通过,未知账户");

            redirectAttributes.addFlashAttribute("message", "未知账户");
        }catch(IncorrectCredentialsException ice){
            logger.info("对用户[" + phoneNumber + "]进行登录验证..验证未通过,错误的凭证");

            redirectAttributes.addFlashAttribute("message", "验证码不正确");
        }catch(LockedAccountException lae){
            logger.info("对用户[" + phoneNumber + "]进行登录验证..验证未通过,账户已锁定");

            redirectAttributes.addFlashAttribute("message", "账户已锁定");
        }catch(ExcessiveAttemptsException eae){
            logger.info("对用户[" + phoneNumber + "]进行登录验证..验证未通过,错误次数过多");

            redirectAttributes.addFlashAttribute("message", "手机号或验证码错误次数过多");
        }catch(AuthenticationException ae){
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            logger.info("对用户[" + phoneNumber + "]进行登录验证..验证未通过,堆栈轨迹如下");

            ae.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "手机号或验证码不正确");
        }
        //验证是否登录成功
        if(currentUser.isAuthenticated()){
            logger.info("用户[" + phoneNumber + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
            return "redirect:/user/index02";
            //return responseVo;
        }else{
            token.clear();
            return "redirect:/login";
            //return responseVo;
        }
    }

    @RequestMapping(value="/user/logout",method=RequestMethod.GET)
    public String logout(RedirectAttributes redirectAttributes ){
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息

        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        UserAccessLog userAccessLog = new UserAccessLog();
        UserInfo userInfo=(UserInfo) session.getAttribute("userInfo");
        if(userInfo!=null){
            userAccessLog.setUserId(userInfo.getUserId());
            userAccessLog.setRecordType(1);
            userAccessLog.setRecordAddress("127.0.0.1");
            userAccessLog.setDeviceModel("2");
            userAccessLog.setDeviceVersion("1.0.0");
            userAccessLog.setRecordTime(new Timestamp(System.currentTimeMillis()));
            userAccessLog.setSystemVersion("5.4.0");
            userService.insertUserAccessLog(userAccessLog);
        }
        System.out.println("go to logout");
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return "redirect:/user/login";
    }


    /**
     *
     */
    public boolean isNull(String parameter){
        return "".equals(parameter) ||  parameter.length()<1 || parameter.isEmpty();
    }
}
