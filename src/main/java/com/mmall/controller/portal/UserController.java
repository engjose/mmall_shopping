package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ResultMap;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by panyuanyuan on 2017/6/12.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
    * 用户登录
    *
    * @param username
    * @param password
    * @return
    */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultMap login(String username,String password, HttpSession session) {
        return userService.login(username, password, session);
    }

    /**
    *
    * @param session
    * @return
    */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResultMap logout(HttpSession session) {
        session.removeAttribute(Const.LOGIN_USER);
        return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "注销成功");
    }

    /**
    * 用户注册
    *
    * @param user
    * @return
    */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResultMap register(@RequestBody  User user) {
        return userService.register(user);
    }


    /**
    * 用户参数校验:如果不存在此value返回码为200
    *
    * @param value
    * @param type
    * @return
    */
    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public ResultMap validateParam(String value, String type) {
        return userService.validateParam(value, type);
    }

    /**
     * 获取用户信息
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ResultMap getUserInfo(HttpSession session) {
        return userService.getUserInfo(session);
    }

    /**
     * 通过用户名查询用户问题
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/getQuestion", method = RequestMethod.GET)
    public ResultMap getQuestionByUsername(String username){
        return userService.getQuestionByUsername(username);
    }

    /**
     * 用户查看检查答案正确性
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "/checkAnswer", method = RequestMethod.POST)
    public ResultMap checkAnswer(String username, String question, String answer) {
        return userService.checkAnswer(username, question, answer);
    }

    /**
     * 忘记密码后的重置密码
     *
     * @param username
     * @param password
     * @param token
     * @return
     */
    @RequestMapping(value = "/forgetResetPassword", method = RequestMethod.POST)
    public ResultMap forgetResetPassword(String username, String password, String token) {
        return userService.forgetResetPassword(username, password, token);
    }

    /**
     * 登录状态下修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param session
     * @return
     */
    @RequestMapping(value = "/loginResetPassword", method = RequestMethod.POST)
    public ResultMap loginResetPassword(String oldPassword, String newPassword, HttpSession session) {
        return userService.loginResetPassword(oldPassword, newPassword, session);
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @param session
     * @return
     */
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.PUT)
    public ResultMap updateUserInfo(@RequestBody User user, HttpSession session) {
        return userService.updateUserInfo(user, session);
    }
}



















