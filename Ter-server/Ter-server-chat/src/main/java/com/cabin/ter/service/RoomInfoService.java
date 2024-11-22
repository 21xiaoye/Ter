package com.cabin.ter.service;

import com.cabin.ter.constants.request.GroupAddReq;
import com.cabin.ter.constants.response.ChatRoomResp;
import com.cabin.ter.constants.response.CursorPageBaseResp;
import com.cabin.ter.constants.response.GroupInfoResp;

import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 19:44
 */
public interface RoomInfoService {
    /**
     * 新建群组
     *
     * @param userId    创建者userId
     * @param request   要求如愿uid
     * @return
     */
    Long addGroup(Long userId, GroupAddReq request);

    /**
     * 获取群组信息
     * @param roomId    取roomId
     * @return  群组信息
     */
    GroupInfoResp getGroupInfo(Long roomId);

    /**
     * 退出群组
     * @param roomId    需要退出的群组roomId
     * @param userId    用户userId
     */
    void quitGroup(Long roomId, Long userId);

    /**
     * 邀请群组成员
     * @param roomId    群组roomId
     * @param userId    邀请人用户userId
     * @param groupAddReq   邀请成岩列表信息
     */
    void inviteMember(Long roomId, Long userId, GroupAddReq groupAddReq);

    /**
     * 踢出群组成员
     * @param roomId        群组roomId
     * @param memberList    需要踢出群组的成员userId列表
     */
    void kickMember(Long roomId, List<Long> memberList);

    /**
     * 修改群组成员权限
     * @param roomId    群组的roomId
     * @param userId    操作成员的userId
     * @param modifyUserId  被修改成员userId
     * @param roleId    群组权限roleId
     */
    void modifyMemberRole(Long roomId, Long userId, Long modifyUserId ,Long roleId);
    /**
     * 用户修改在群中的群备注
     * @param userId    用户userId
     * @param roomId    需要修改群备注所在群roomId
     * @param memberRemark  新的群备注
     */
    void modifyMemberRemark(Long userId, Long roomId, String memberRemark);


    /**
     * 获取会话列表
     * @param userId       请求用户userId
     * @return
     */
    CursorPageBaseResp<ChatRoomResp> getUserContactPage(Long userId);
}
