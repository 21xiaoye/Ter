package com.cabin.ter.chat.mapper;

import com.cabin.ter.chat.domain.FriendApplyDomain;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024/10/27 20:58
 */
public interface FriendApplyDomainMapper {
    /**
     * 获取所有好友申请记录
     * @param userId 用户uId
     * @return  返回申请记录列表
     */
    @Select("SELECT applyId, userId, targetId, applyMessage, applyStatus, createTime " +
            "FROM ter_friend_apply " +
            "WHERE (userId = #{userId} OR targetId = #{userId}) AND applyStatus != 3")
    List<FriendApplyDomain> getFriendApplyRecord(Long userId);


    /**
     * 审批好友申请记录
     * @param applyId    申请记录Id
     * @param applyStatus   申请状态
     * @param approvalTime  审批时间
     */
    @Update("UPDATE ter_friend_apply SET applyStatus = #{applyStatus}, approvalTime = #{approvalTime} " +
            "WHERE applyId = #{applyId}")
    Integer approvalFriendApplyRecord(Long applyId, Integer applyStatus, Long approvalTime);

    /**
     * 删除好友状态
     * @param applyId       申请记录Id
     * @param applyStatus   申请状态
     * @param deleteTime    审批时间
     */
    @Update("UPDATE ter_friend_apply SET applyStatus = #{applyStatus}, deleteTime = #{deleteTime} " +
            "WHERE applyId = #{applyId}")
    Integer deleteFriendApplyRecord(Long applyId, Integer applyStatus, Long deleteTime);

    /**
     * 保存用户好友申请记录
     * @param friendApplyDomain 需要保存的申请记录
     */
    Integer saveFriendApplyRecord(FriendApplyDomain friendApplyDomain);
}
