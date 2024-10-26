package com.cabin.ter.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.vo.response.UserInfoResp;

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
}
