package com.cabin.ter.service;

import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.constants.domain.OssReq;
import com.cabin.ter.constants.request.LoginAndRegisterReq;
import com.cabin.ter.constants.response.ApiResponse;
import com.cabin.ter.constants.response.LoginSuccessResp;
import com.cabin.ter.constants.response.UserInfoResp;

/**
 * @author xiaoye
 * @date Created in 2024-04-23 14:46
 */
public interface UserService {
    /**
     * 用户登录
     *
     * @param loginRequest
     * @return
     */
    LoginSuccessResp userLogin(LoginAndRegisterReq loginRequest);

    /**
     * 用户注册
     * @param loginRequest
     * @return
     */
    UserDomain userRegister(LoginAndRegisterReq loginRequest);

    /**
     * 发送邮箱验证码
     *
     * @param userEmail     目标用户
     * @param operationType   邮件操作用途
     */
    void sendMailCode(String userEmail, Integer operationType);
    /**
     * 上传头像
     *
     * @param ossReq
     * @return
     */
    ApiResponse uploadAvatar(OssReq ossReq);
    /**
     * 获取前端展示信息
     *
     * @param userId
     * @return
     */
    UserInfoResp getUserInfo(Long userId);

    /**
     * 保存用户
     * @param userDomain 需要保存的用户
     */
    void saveUser(UserDomain userDomain);

    /**
     * 用户退出登录
     * @param userId    退出用户userId
     */
    void userLogOut(Long userId);
}
