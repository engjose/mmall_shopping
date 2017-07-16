package com.mmall.service;

import com.mmall.common.ResultMap;
import com.mmall.pojo.User;
import javax.servlet.http.HttpSession;

/**
 * Created by panyuanyuan on 2017/6/12.
 */
public interface IUserService {

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 返回登录结果
     */
    ResultMap login(String username, String password, HttpSession session);

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    ResultMap register(User user);

    /**
     * 用户参数校验
     *
     * @param value
     * @param type
     * @return
     */
    ResultMap validateParam(String value, String type);

    /**
     * 获取已经登录用户的信息
     *
     * @param session
     * @return
     */
    ResultMap getUserInfo(HttpSession session);

    /**
     * 根据用户名获取用户密码
     *
     * @param username
     * @return
     */
    ResultMap getQuestionByUsername(String username);

    /**
     * 用户问题答案的校验
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ResultMap checkAnswer(String username, String question, String answer);

    /**
     * 忘记密码后的重置密码
     *
     * @param username
     * @param password
     * @param token
     * @return
     */
    ResultMap forgetResetPassword(String username, String password, String token);

    /**
     * 登录状态下的重置密码
     *
     * @param oldPassword
     * @param newPassword
     * @param session
     * @return
     */
    ResultMap loginResetPassword(String oldPassword, String newPassword, HttpSession session);

    /**
     * 修改用户信息
     *
     * @param user
     * @param session
     * @return
     */
    ResultMap updateUserInfo(User user, HttpSession session);
}
