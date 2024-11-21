package com.cabin.ter.adapter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.constants.enums.RoleEnum;
import com.cabin.ter.constants.request.LoginAndRegisterReq;
import com.cabin.ter.constants.response.LoginSuccessResp;
import com.cabin.ter.constants.response.UserInfoResp;

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
    private static final Snowflake snowflake = new Snowflake();
    public static UserInfoResp buildUserInfoResp(UserDomain userDomain) {
        UserInfoResp userInfoResp = new UserInfoResp();
        BeanUtil.copyProperties(userDomain, userInfoResp);
        return userInfoResp;
    }
    public static UserDomain buildUserDomain(LoginAndRegisterReq request, String userPasswd, String salt){
        UserDomain userDomain = UserDomain.builder()
                .userId(snowflake.nextId())
                .userEmail(request.getUserEmail())
                .userPasswd(userPasswd)
                .userName(request.getUserEmail())
                .salt(salt)
                .createTime(System.currentTimeMillis())
                .roleId(Objects.isNull(request.getRoleId()) ? RoleEnum.ORDINARY.getStatus() : RoleEnum.ADMIN.getStatus())
                .sex(UserDomain.SEX_MALE)
                .build();
        if (request.getRoleId() != null) {
            userDomain.setRoleId(request.getRoleId());
        }
        return userDomain;
    }
    public static LoginSuccessResp buildLoginSuccessResp(String token){
        return LoginSuccessResp.builder()
                .token(token)
                .build();
    }
}
