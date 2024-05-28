package com.cabin.ter.service;

import com.cabin.ter.constants.domain.OssReq;
import com.cabin.ter.constants.vo.request.EmailBindingReqMsg;
import com.cabin.ter.constants.vo.request.LoginAndRegisterRequest;
import com.cabin.ter.constants.vo.response.ApiResponse;
import com.cabin.ter.vo.response.UserInfoResp;

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
    ApiResponse userLogin(LoginAndRegisterRequest loginRequest);

    /**
     * 用户注册
     * @param loginRequest
     * @return
     */
    ApiResponse userRegister(LoginAndRegisterRequest loginRequest);

    /**
     * 发送邮箱验证码
     *
     * @param emailBindingReqMsg
     * @return
     */
    ApiResponse sendEmailCode(EmailBindingReqMsg emailBindingReqMsg);

    /**
     * 绑定邮箱
     *
     * @param emailBindingReqMsg
     * @return
     */
    ApiResponse emailBiding(EmailBindingReqMsg emailBindingReqMsg);
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
     * @param uid
     * @return
     */
    UserInfoResp getUserInfo(Long uid);
}
