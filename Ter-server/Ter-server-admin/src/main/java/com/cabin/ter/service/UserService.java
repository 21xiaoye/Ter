package com.cabin.ter.service;

import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.constants.domain.OssReq;
import com.cabin.ter.vo.enums.OperateEnum;
import com.cabin.ter.vo.request.LoginAndRegisterRequest;
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
    UserDomain userRegister(LoginAndRegisterRequest loginRequest);

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
     * @param uid
     * @return
     */
    UserInfoResp getUserInfo(Long uid);

    /**
     * 保存用户
     * @param userDomain 需要保存的用户
     */
    void saveUser(UserDomain userDomain);
}
