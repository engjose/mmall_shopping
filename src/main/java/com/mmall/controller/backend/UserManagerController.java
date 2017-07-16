package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ResultMap;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by panyuanyuan on 2017/6/18.
 */
@RestController
@RequestMapping(value = "/manage/user")
public class UserManagerController {

    @Autowired
    private IUserService userService;

    /**
     * 管理员登录接口
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultMap login(String username, String password, HttpSession session) {

        ResultMap loginMap = userService.login(username, password, session);
        User loginUser = (User) loginMap.getData();

        if(loginMap.getCode() == 200) {
            if(Const.ROL_MANAGER == loginUser.getRole()) {
                session.setAttribute(Const.LOGIN_USER, loginUser);
                return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "登录成功");
            } else {
                return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "不是管理员登录");
            }
        }
        return ResultMap.getResultMap(ResponseCode.ERROR.getCode(), "登录失败");
    }
}
