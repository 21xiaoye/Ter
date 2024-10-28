package com.cabin.ter.admin.mapper;

import com.cabin.ter.admin.domain.FriendApplyDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024/10/27 20:58
 */
@Mapper
public interface FriendApplyDomainMapper {
    /**
     * 获取所有好友申请记录
     * @param userId 用户Uid
     * @return  返回申请记录列表
     */
    @Select("SELECT applyId, userId, targetId, applyMessage, applyStatus, createTime, approvalTime " +
            "FROM ter_friend_apply " +
            "WHERE userId = #{userId} OR targetId = #{userId}")
    List<FriendApplyDomain> getFriendApplyRecord(Long userId);

    /**
     * 保存好友申请记录
     */
    Integer saveFriendApplyRecord(FriendApplyDomain friendApplyDomain);

    /**
     * 审批好友申请记录
     * @param applyId    申请记录Id
     * @param applyStatus   申请状态
     */
    @Update("UPDATE ter_friend_apply SET applyStatus = #{applyStatus}, approvalTime = #{approvalTime} " +
            "WHERE applyId = #{applyId}")
    Integer approvalFriendApplyRecord(Long applyId, Integer applyStatus, Long approvalTime);
}
