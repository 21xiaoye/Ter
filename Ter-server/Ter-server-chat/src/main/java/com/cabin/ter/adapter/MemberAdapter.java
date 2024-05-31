package com.cabin.ter.adapter;

import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.constants.enums.WSRespTypeEnum;
import com.cabin.ter.constants.vo.response.WSBaseResp;
import com.cabin.ter.vo.response.WSMemberChange;

import static com.cabin.ter.vo.response.WSMemberChange.CHANGE_TYPE_ADD;

/**
 * @author xiaoye
 * @date Created in 2024-05-31 20:20
 */
public class MemberAdapter {
    public static WSBaseResp<WSMemberChange> buildMemberAddWS(Long roomId, UserDomain user) {
        WSBaseResp<WSMemberChange> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.MEMBER_CHANGE.getType());
        WSMemberChange wsMemberChange = new WSMemberChange();
        wsMemberChange.setActiveStatus(user.getUserStatus());
        wsMemberChange.setUid(user.getUserId());
        wsMemberChange.setRoomId(roomId);
        wsMemberChange.setChangeType(CHANGE_TYPE_ADD);
        wsBaseResp.setData(wsMemberChange);
        return wsBaseResp;
    }
}
