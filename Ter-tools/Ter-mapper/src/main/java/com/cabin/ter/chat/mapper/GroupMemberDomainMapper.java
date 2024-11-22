package com.cabin.ter.chat.mapper;

import com.cabin.ter.chat.domain.GroupMemberDomain;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 20:35
 */
public interface GroupMemberDomainMapper {
    /**
     * 保存群组成员
     *
     * @param groupMemberDomain
     * @return
     */
    @Insert("INSERT INTO ter_group_member (id, groupId, userId, role,createTime) values (#{groupMemberDomain.id},#{groupMemberDomain.groupId},#{groupMemberDomain.userId},#{groupMemberDomain.role},#{groupMemberDomain.createTime})")
    Integer saveGroupMember(@Param("groupMemberDomain") GroupMemberDomain groupMemberDomain);

    /**
     * 根据 roomId 查询成员id
     *
     * @param roomId    群id
     * @return
     */
    @Select("SELECT userId FROM ter_group_member WHERE groupId = #{roomId}")
    List<Long> getMemberUidList(Long roomId);

    /**
     * 根据 roomId列表 查询所有成员信息
     *
     * @param roomIdsList
     * @return
     */
    List<GroupMemberDomain> listByIds(@Param("roomIdsList") List<Long> roomIdsList);

    /**
     * 批量保存群成员
     * @param groupMemberDomainList 需要保存的群成员列表
     */
    void saveGroupMemberList(@Param("groupMemberDomainList") List<GroupMemberDomain> groupMemberDomainList);
}
