package com.cabin.ter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import com.cabin.ter.adapter.ChatAdapter;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.cache.*;
import com.cabin.ter.chat.domain.*;
import com.cabin.ter.chat.domain.contact.RoomBaseInfo;
import com.cabin.ter.constants.enums.RoomTypeEnum;
import com.cabin.ter.chat.mapper.ContactDomainMapper;
import com.cabin.ter.chat.mapper.GroupMemberDomainMapper;
import com.cabin.ter.chat.mapper.MessageDomainMapper;
import com.cabin.ter.constants.response.GroupInfoResp;
import com.cabin.ter.listener.event.GroupMemberAddEvent;
import com.cabin.ter.service.RoomInfoService;
import com.cabin.ter.service.RoomService;
import com.cabin.ter.strategy.AbstractMsgHandler;
import com.cabin.ter.strategy.MsgHandlerFactory;
import com.cabin.ter.constants.request.GroupAddReq;
import com.cabin.ter.constants.response.ChatRoomResp;
import com.cabin.ter.constants.response.CursorPageBaseResp;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 19:44
 */
@Service
@Slf4j
public class RoomInfoServiceImpl implements RoomInfoService {
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomCache roomCache;
    @Autowired
    private RoomGroupCache roomGroupCache;
    @Autowired
    private ContactDomainMapper contactDomainMapper;
    @Autowired
    private RoomFriendCache roomFriendCache;
    @Autowired
    private UserInfoCache userInfoCache;
    @Autowired
    private MessageDomainMapper messageDomainMapper;
    @Autowired
    private GroupMemberDomainMapper groupMemberDomainMapper;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    /**
     * 新建群组
     *
     * @param userId    用户userId
     * @param request   群组如愿uid列表
     * @return
     */
    @Override
    public Long addGroup(Long userId, GroupAddReq request) {
        List<GroupAddReq.MemberInfo> memberInfoList = request.getMemberInfoList();
        // 创建群组
        GroupRoomDomain group = roomService.createGroup(userId);
        // 构建群组要求人员基本信息
        List<GroupMemberDomain> groupMemberDomains = ChatAdapter.buildGroupMemberBatch(memberInfoList, group.getRoomId(),userId);
        // 保存群成员
        groupMemberDomainMapper.saveGroupMemberList(groupMemberDomains);
        // 发送群通知
        applicationEventPublisher.publishEvent(new GroupMemberAddEvent(this, group, groupMemberDomains, userId));
        return group.getRoomId();
    }

    @Override
    public GroupInfoResp getGroupInfo(Long roomId) {
        return null;
    }

    @Override
    public void quitGroup(Long roomId, Long userId) {

    }

    @Override
    public void inviteMember(Long roomId, Long userId, GroupAddReq groupAddReq) {

    }

    @Override
    public void kickMember(Long roomId, List<Long> memberList) {

    }

    @Override
    public void modifyMemberRole(Long roomId, Long userId, Long modifyUserId, Long roleId) {

    }

    @Override
    public void modifyMemberRemark(Long userId, Long roomId, String memberRemark) {

    }

    @Override
    public CursorPageBaseResp<ChatRoomResp> getUserContactPage(Long userId) {
        List<ContactDomain> contactPage = null;
        if(Objects.nonNull(userId)){
            contactPage = contactDomainMapper.getUserContactList(userId);
        }
        List<ChatRoomResp> chatRoomResp = this.buildContactResp(userId, contactPage);
        return CursorPageBaseResp.init(chatRoomResp);
    }

    /**
     *
     * @param userId   用户 userId
     * @param contactPage   用户会话列表
     * @return
     */
    private List<ChatRoomResp> buildContactResp(Long userId, List<ContactDomain> contactPage){
        if(contactPage.isEmpty()){
            return new ArrayList<>();
        }
        List<Long> roomIds = contactPage.stream().map(ContactDomain::getRoomId).collect(Collectors.toList());
        // 查询每个房间的基本信息
        Map<Long, RoomBaseInfo> roomBaseInfoMap = this.getRoomBaseInfoMap(userId, roomIds);
        // 每个房间的最后一条消息
        List<Long> msgIds = roomBaseInfoMap.values().stream().map(RoomBaseInfo::getLastMsgId).collect(Collectors.toList());
        //TODO: 这里我直接从数据库中查询了，未来设计从先从缓存中查询并保证数据同步，实在没时间了 ~_~ ~_~
        List<MessageDomain> messageDomainList = CollectionUtil.isEmpty(msgIds) ? new ArrayList<>() : messageDomainMapper.listByIds(msgIds);
        Map<Long, MessageDomain> messageDomainMap = messageDomainList.stream().collect(Collectors.toMap(MessageDomain::getId, Function.identity()));
        // 查询每条消息发送者信息
        Map<Long, UserDomain> userDomainMap = userInfoCache.getBatch(messageDomainList.stream().map(MessageDomain::getFromUid).collect(Collectors.toList()));

        // 消息未读数
        Map<Long, Integer> unReadCountMap = this.getUnReadCountMap(userId, contactPage);
        return roomBaseInfoMap.values().stream()
                .map(room ->{
                    ChatRoomResp chatRoomResp = new ChatRoomResp();
                    RoomBaseInfo roomBaseInfo = roomBaseInfoMap.get(room.getRoomId());
                    chatRoomResp.setAvatar(roomBaseInfo.getAvatar());
                    chatRoomResp.setRoomId(room.getRoomId());
                    chatRoomResp.setActiveTime(room.getActiveTime());
                    chatRoomResp.setHotFlag(roomBaseInfo.getHotFlag());
                    chatRoomResp.setType(roomBaseInfo.getType());
                    chatRoomResp.setName(roomBaseInfo.getName());
                    MessageDomain message = messageDomainMap.get(room.getLastMsgId());
                    if (Objects.nonNull(message)) {
                        AbstractMsgHandler strategyNoNull = MsgHandlerFactory.getStrategyNoNull(message.getType());
                        chatRoomResp.setText(strategyNoNull.showContactMsg(message));
                    }
                    chatRoomResp.setUnreadCount(unReadCountMap.getOrDefault(room.getRoomId(), 0));
                    return chatRoomResp;
                }).sorted(Comparator.comparing(ChatRoomResp::getActiveTime).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 获取未读数
     */
    private Map<Long, Integer> getUnReadCountMap(Long uid, List<ContactDomain> contactPage) {
        if (Objects.isNull(uid)) {
            return new HashMap<>();
        }
        return contactPage.parallelStream()
                .map(contact -> Pair.of(contact.getRoomId(), messageDomainMapper.getUnReadCount(contact.getRoomId(), contact.getReadTime())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }


    /**
     * 用户参与的房间id列表，根据类型查询每个房间的的信息
     *
     * 并构建成统一房间类型RoomBaseInfo,最后返回 Map
     * key 为房间 id
     * value 为构建的房间信息
     *
     * @param uid       用户uId
     * @param roomIds   用户参与的房间id列表
     * @return  Map<Long, RoomBaseInfo>
     */
    private Map<Long, RoomBaseInfo> getRoomBaseInfoMap(Long uid, List<Long> roomIds){
        // 获取房间基本信息
        Map<Long, RoomDomain> roomMap = roomCache.getBatch(roomIds);

        // 根据房间类型进行分组
        Map<Integer, List<Long>> groupRoomMap = roomMap.values().stream()
                .collect(Collectors.groupingBy(RoomDomain::getRoomType, Collectors.mapping(RoomDomain::getUserId, Collectors.toList())));
        // 获取群聊信息
        List<Long> groupRoomIds = groupRoomMap.get(RoomTypeEnum.GROUP.getType());
        Map<Long, GroupRoomDomain> groupRoomDomainMap = roomGroupCache.getBatch(groupRoomIds);

        // 获取单聊房间信息(好友信息)
        List<Long> friendRoomIds = groupRoomMap.get(RoomTypeEnum.FRIEND.getType());
        Map<Long, UserDomain> friendRoomMap = this.getFriendRoomMap(uid, friendRoomIds);

        return roomMap.values().stream().map(room->{
            RoomBaseInfo roomBaseInfo = new RoomBaseInfo();
            roomBaseInfo.setRoomId(room.getUserId());
            roomBaseInfo.setType(room.getRoomType());
            roomBaseInfo.setHotFlag(room.getHotFlag());
            roomBaseInfo.setLastMsgId(room.getLastMsgId());
            roomBaseInfo.setActiveTime(room.getActiveTime());

            // 房间为群聊
            if(RoomTypeEnum.of(room.getRoomType()) == RoomTypeEnum.GROUP){
                GroupRoomDomain groupRoomDomain = groupRoomDomainMap.get(room.getRoomId());
                roomBaseInfo.setName(groupRoomDomain.getRoomName());
                roomBaseInfo.setAvatar(groupRoomDomain.getRoomAvatar());
            }
            // 房间为单聊
            if(RoomTypeEnum.of(room.getRoomType()) == RoomTypeEnum.FRIEND){
                UserDomain userDomain = friendRoomMap.get(room.getRoomId());
                roomBaseInfo.setName(userDomain.getUserName());
                roomBaseInfo.setAvatar(userDomain.getUserAvatar());
            }

            return roomBaseInfo;
        }).collect(Collectors.toMap(RoomBaseInfo::getRoomId, Function.identity()));
    }

    /**
     * 获取好友信息
     * 首先根据单聊房间id列表roomIds，查询每个房间的信息 friendRoomDomainMap，
     * 并查出其中每个房间的好友id集合 friendUidSet，
     * 然后根据friendUidSet集合，查询出好友信息 userDomainMap 集合(key: 好友uid, value: 好友信息)
     *
     *  返回的时候，需要将房间的id和好友信息进行一一映射
     *
     * @param uid       用户uId
     * @param roomIds   单聊房间id集合
     * @return  返回每个房间的好友信息，key: 好友uId, value：好友信息
     */
    private Map<Long, UserDomain> getFriendRoomMap(Long uid, List<Long> roomIds){
        if(CollectionUtil.isNotEmpty(roomIds)){
            return new HashMap<>();
        }
        // 单聊房间信息
        Map<Long, FriendRoomDomain> friendRoomDomainMap = roomFriendCache.getBatch(roomIds);
        // 单聊房间有两个uid,根据用户自己的uid，找出单聊房间中好友的uid
        Set<Long> friendUidSet = ChatAdapter.getFriendUidSet(friendRoomDomainMap.values(), uid);
        // 根据好友uid集合查出好友明细信息， key为好友uid, key 为好友明细信息
        Map<Long, UserDomain> userDomainMap = userInfoCache.getBatch(new ArrayList<>(friendUidSet));
        // 将每个房间和好友进行映射，获取到最终的Map,key为房间id, value为用户信息
        return friendRoomDomainMap.values().stream()
                .collect(Collectors.toMap(FriendRoomDomain::getRoomId, friendRoom->{
                    Long friendUid = ChatAdapter.getFriendUid(friendRoom, uid);
                    return userDomainMap.get(friendUid);
                }));
    }
    private Double getCursorOrNull(String cursor) {
        return Optional.ofNullable(cursor).map(Double::parseDouble).orElse(null);
    }
}
