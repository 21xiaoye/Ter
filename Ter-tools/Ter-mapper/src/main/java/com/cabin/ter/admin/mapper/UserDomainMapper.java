package com.cabin.ter.admin.mapper;

import com.cabin.ter.admin.domain.UserDomain;
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

public interface UserDomainMapper {
    /**
     * 根据Id查询用户信息
     *
     * @param userId    用户Id
     * @return  UserDomain
     */
    @Select("SELECT userId, userEmail,userPasswd FROM ter_user WHERE userId = #{userId}")
    UserDomain getUserId(Long userId);

    /**
     * 根据邮箱查询用户信息
     *
     * @param userEmail     用户邮箱
     * @return  UserDomain
     */
    @Select("SELECT userId,userName, userEmail,userPasswd,userAvatar,salt,userStatus FROM ter_user WHERE userEmail=#{userEmail}")
    Optional<UserDomain> findByUsernameOrEmailOrPhone(String userEmail);

    /**
     * 查询用户是否存在
     *
     * @param userEmail 用户邮箱
     * @return  UserDomain
     */
    @Select("SELECT userEmail FROM ter_user WHERE userEmail=#{userEmail}")
    Optional<UserDomain> findByUserEmail(String userEmail);
    /**
     * 插入用户
     *
     * @param user  用户对象
     * @return  Integer
     */
    @Insert("INSERT INTO ter_user (userId, userName,userPasswd, salt,userEmail,createTime) VALUES (#{userId},#{userName}, #{userPasswd},#{salt},#{userEmail},#{createTime})")
    Integer insertTerUser(UserDomain user);
}