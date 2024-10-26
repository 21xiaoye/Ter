package com.cabin.ter.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.constants.enums.EncryptionEnum;
import com.cabin.ter.constants.enums.RoleEnum;
import com.cabin.ter.factory.MyPasswordEncoderFactory;
import com.cabin.ter.vo.request.LoginAndRegisterRequest;
import com.cabin.ter.vo.response.UserInfoResp;

import java.util.Objects;

/**
 * <p>
 *     用户适配器
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-28 16:00
 */
public class UserAdapter {
    public static UserInfoResp buildUserInfoResp(UserDomain userDomain) {
        UserInfoResp userInfoResp = new UserInfoResp();
        BeanUtil.copyProperties(userDomain, userInfoResp);
        return userInfoResp;
    }
    public static UserDomain buildUserDomain(LoginAndRegisterRequest request, Long userId, String userPasswd,String salt){
        UserDomain userDomain = UserDomain.builder()
                .userId(userId)
                .userEmail(request.getUserEmail())
                .userPasswd(userPasswd)
                .userName(request.getUserEmail())
                .salt(salt)
                .createTime(System.currentTimeMillis())
                .roleId(Objects.isNull(request.getRoleId()) ? RoleEnum.ORDINARY.getStatus() : RoleEnum.ADMIN.getStatus())
                .sex('1')
                .build();
        if (request.getRoleId() != null) {
            userDomain.setRoleId(request.getRoleId());
        }
        return userDomain;
    }
}
