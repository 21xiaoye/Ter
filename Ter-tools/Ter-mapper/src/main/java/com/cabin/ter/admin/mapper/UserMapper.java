package com.cabin.ter.admin.mapper;

import com.cabin.ter.admin.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;


/**
 * <p>
 *    用户mapper层
 * </p>
 * @author xiaoye
 * @date Created in 2024-04-23 9:54
 */

public interface UserMapper{
    /**
     * 根据Id查询用户信息
     *
     * @param userId    用户Id
     * @return  User
     */
    @Select("select userId, userEmail,userPasswd from ter_user where userId = #{userId}")
    User getUserId(Long userId);

    /**
     * 根据邮箱查询用户信息
     *
     * @param email     用户邮箱
     * @return  User
     */
    @Select("select userId,userName, userEmail,userPasswd,userAvatar,salt,userStatus from ter_user where userEmail=#{userEmail}")
    Optional<User> findByUsernameOrEmailOrPhone(String email);

    /**
     * 插入用户
     *
     * @param user  用户对象
     * @return  Integer
     */
    @Insert("INSERT INTO ter_user (userId, userName,userPasswd, salt,userEmail,createTime) VALUES (#{userId},#{userName}, #{userPasswd},#{salt},#{userEmail},#{createTime})")
    Integer insertTerUser(User user);
}
