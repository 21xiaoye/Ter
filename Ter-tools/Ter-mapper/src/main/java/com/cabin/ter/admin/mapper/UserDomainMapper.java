package com.cabin.ter.admin.mapper;

import com.cabin.ter.admin.domain.UserDomain;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    @Select("SELECT userId,userName,userEmail,userPasswd,userAvatar,salt,userStatus FROM ter_user WHERE userEmail=#{userEmail}")
    Optional<UserDomain> findByUsernameOrEmailOrPhone(String userEmail);

    /**
     * 查询用户是否存在
     *
     * @param userEmail 用户邮箱
     * @return  UserDomain
     */
    @Select("SELECT userId,userName,userEmail,userPasswd,userAvatar,salt,roleId, userStatus  FROM ter_user WHERE userEmail=#{userEmail}")
    UserDomain findByUserEmail(String userEmail);
    /**
     * 插入用户
     *
     * @param user  用户对象
     * @return  Integer
     */
    Integer insertTerUser(UserDomain user);


    /**
     * 根据openId 查询用户信息
     *
     * @param openId    用户微信标识
     * @return  UserDomain
     */
    @Select("SELECT userId, userEmail,openId FROM ter_user WHERE openId=#{openId}")
    UserDomain findByUserOpenId(String openId);

    /**
     * 根据用户 Id 绑定 openId
     *
     * @param openId 用户 openId
     * @return Integer
     */
    @Update("UPDATE ter_user SET openId = #{openId} WHERE userId = #{userId}")
    Integer updateUserOpenId(Long userId,String openId);

    List<UserDomain> listByIds(@Param("uIdsList") List<Long> uIdsList);
}
