package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ResultMap;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import java.net.UnknownServiceException;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by panyuanyuan on 2017/6/12.
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResultMap login(String username, String password, HttpSession session) {

        int userCount = userMapper.checkUser(username);
        if (userCount == 0) {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "用户不存在");
        }

        User sessionUser = (User) session.getAttribute(Const.LOGIN_USER);
        if (sessionUser != null && StringUtils.equals(sessionUser.getUsername(), username)) {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "用户已经登录");
        }

        //MD5加密后再登录
        User user = userMapper.login(username, MD5Util.MD5EncodeUtf8(password));
        if (user != null) {
            user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
            session.setAttribute(Const.LOGIN_USER, user);
            return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "登录成功", user);
        } else {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "密码错误");
        }
    }

    @Override
    @Transactional
    public ResultMap register(User user) {

        ResultMap checkUser = validateParam(user.getUsername(), Const.USER_NAME);
        if(200 != checkUser.getCode()) {
            return checkUser;
        }

        ResultMap checkEmail = validateParam(user.getEmail(), Const.USER_EAMIL);
        if(200 != checkEmail.getCode()) {
            return checkEmail;
        }

        //MD5加密密码
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        user.setUpdateTime(new Date());
        int registerResult = userMapper.insertSelective(user);
        if (registerResult != 1) {
            return ResultMap.getResultMap(ResponseCode.ERROR.getCode(), "注册失败");
        }

        return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "注册成功");
    }

    @Override
    public ResultMap validateParam(String value, String type) {
        switch (type) {
            case Const.USER_NAME:
                int checkUsername = userMapper.checkUser(value);
                if (checkUsername > 0) {
                    return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "用户名已经存在");
                }
                break;
            case Const.USER_EAMIL:
                int checkEmail = userMapper.checkEmail(value);
                if (checkEmail > 0) {
                    return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "邮箱已经存在");
                }
                break;
        }
        return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "校验成功");
    }

    @Override
    public ResultMap getUserInfo(HttpSession session) {

        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);
        if(null != loginUser) {
            return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), loginUser);
        }

        return ResultMap.getResultMap(ResponseCode.ERROR.getCode(), "用户没有登录");
    }

    @Override
    public ResultMap getQuestionByUsername(String username) {

        ResultMap checkUsername = validateParam(username, Const.USER_NAME);
        if(200 == checkUsername.getCode()) {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "用户名不存在");
        }

        String question =  userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)) {
            return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(),"获取成功", question);
        } else {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"此用户没有设置问题");
        }
    }

    @Override
    public ResultMap checkAnswer(String username, String question, String answer) {

        int answerCount =  userMapper.selectQuestionAnswer(username, question, answer);
        if(answerCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(Const.TOKEN_PREFIX + username, forgetToken);
            return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "答案正确", forgetToken);
        }

        return ResultMap.getResultMap(ResponseCode.ERROR.getCode(), "问题答案错误", null);
    }

    @Override
    public ResultMap forgetResetPassword(String username, String password, String token) {

        if(StringUtils.isBlank(token)) {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "Token过期或者无效");
        }

        ResultMap usernameCheck = validateParam(username, Const.LOGIN_USER);
        if(200 == usernameCheck.getCode()) {
            return ResultMap.getResultMap(ResponseCode.NEED_LOGIN.getCode(), "用户名不存在");
        }

        //获取缓存中的Token
        String localToken = TokenCache.getToken(Const.TOKEN_PREFIX + username);
        if(StringUtils.isBlank(localToken)) {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "Token无效或者过期");
        }

        if(StringUtils.equals(token, localToken)) {
            password = MD5Util.MD5EncodeUtf8(password);
            int rowCount = userMapper.updatePassword(username, password);

            if(rowCount > 0) {
                return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "修改密码成功");
            }
        } else {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "Token无效或者过期");
        }

        return ResultMap.getResultMap(ResponseCode.ERROR.getCode(), "修改密码失败");
    }

    @Override
    public ResultMap loginResetPassword(String oldPassword, String newPassword, HttpSession session) {

        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);
        if(null == loginUser) {
            return ResultMap.getResultMap(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        //检查旧密码是否正确
        int rowCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword), loginUser.getId());
        if(rowCount == 0) {
            return ResultMap.getResultMap(ResponseCode.ERROR.getCode(), "旧密码不正确");
        }

        //设置新的密码
        int updateRowCount = userMapper.updatePassword(loginUser.getUsername(), MD5Util.MD5EncodeUtf8(newPassword));
        if(updateRowCount > 0) {
            return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "修改密码成功");
        }

        return ResultMap.getResultMap(ResponseCode.ERROR.getCode(), "修改密码失败");
    }

    @Override
    public ResultMap updateUserInfo(User user, HttpSession session) {

        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);
        if(null == loginUser) {
            return ResultMap.getResultMap(ResponseCode.NEED_LOGIN.getCode(), "用户没有登录");
        }

        //校验邮箱是否存在
        int rowCount = userMapper.checkEmailByUserId(user.getEmail(), loginUser.getId());
        if(rowCount > 0) {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "邮箱已经存在");
        }

        user.setUsername(loginUser.getUsername());
        user.setId(loginUser.getId());
        int updateRowCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateRowCount > 0) {
            //更新session中的信息
            session.setAttribute(Const.LOGIN_USER,user);
            return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "修改用户信息成功");
        }
        return ResultMap.getResultMap(ResponseCode.ERROR.getCode(), "更新用户信息失败");
    }
}




















