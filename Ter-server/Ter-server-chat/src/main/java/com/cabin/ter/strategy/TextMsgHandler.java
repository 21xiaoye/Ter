package com.cabin.ter.strategy;

import cn.hutool.core.collection.CollectionUtil;
import com.cabin.ter.adapter.MessageAdapter;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.cache.MessageCache;
import com.cabin.ter.cache.UserInfoCache;
import com.cabin.ter.chat.domain.MessageDomain;
import com.cabin.ter.constants.dto.MessageExtra;
import com.cabin.ter.constants.enums.MessageStatusEnum;
import com.cabin.ter.constants.enums.YesOrNoEnum;
import com.cabin.ter.chat.mapper.MessageDomainMapper;
import com.cabin.ter.constants.enums.MessageTypeEnum;
import com.cabin.ter.util.AsserUtil;
import com.cabin.ter.constants.request.TextMsgReq;
import com.cabin.ter.constants.response.TextMsgResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 *     文本消息处理程序
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-29 15:37
 */
@Slf4j
@Component
public class TextMsgHandler extends AbstractMsgHandler<TextMsgReq> {
    @Autowired
    private UserInfoCache userInfoCache;
    @Autowired
    private UserInfoCache userCache;
    @Autowired
    private MessageCache messageCache;
    @Autowired
    private MessageDomainMapper messageDomainMapper;
    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.TEXT;
    }
    @Override
    protected void checkMsg(TextMsgReq body, Long roomId, Long uid) {
        if(Objects.nonNull(body.getReplyMsgId())){
            log.info("有回复消息，对回复消息进行校验");
        }
        if(CollectionUtil.isNotEmpty(body.getAtUidList())){
            //TODO: 这里感觉还可以进行优化，虽说是从缓存中拿取数据
            List<Long> atUidList = body.getAtUidList().stream().distinct().collect(Collectors.toList());
            Map<Long, UserDomain> batch = userInfoCache.getBatch(atUidList);
            long count = batch.values().stream().filter(Objects::nonNull).count();
            AsserUtil.equal((long)atUidList.size(), count, "@用户不存在");
        }
    }
    @Override
    protected void saveMsg(MessageDomain message, TextMsgReq body) {
//        MessageExtra extra = Optional.ofNullable(message.getExtra()).orElse(new MessageExtra());
        MessageDomain messageDomain = new MessageDomain();
        //TODO: 未来在这里进行敏感词过滤后保存，现在直接保存
        messageDomain.setId(message.getId());
        messageDomain.setContent(body.getContent());

//        messageDomain.setExtra(extra);

        if(Objects.nonNull(body.getReplyMsgId())){
            //TODO: 这里对回复消息进行处理
            log.info("有回复消息");
        }
//        if(CollectionUtil.isNotEmpty(body.getAtUidList())){
//            extra.setAtUidList(body.getAtUidList());
//        }
        messageDomainMapper.updateByMsgId(messageDomain);
    }

    @Override
    public Object showMsg(MessageDomain msg) {
        TextMsgResp resp = new TextMsgResp();
        resp.setContent(msg.getContent());
        resp.setUrlContentMap(Optional.ofNullable(msg.getExtra()).map(MessageExtra::getUrlContentMap).orElse(null));
        resp.setAtUidList(Optional.ofNullable(msg.getExtra()).map(MessageExtra::getAtUidList).orElse(null));
        //回复消息
        Optional<MessageDomain> reply = Optional.ofNullable(msg.getReplyMsgId())
                .map(messageCache::getMsg)
                .filter(a -> Objects.equals(a.getStatus(), MessageStatusEnum.NORMAL.getStatus()));
        if (reply.isPresent()) {
            MessageDomain replyMessage = reply.get();
            TextMsgResp.ReplyMsg replyMsgVO = new TextMsgResp.ReplyMsg();
            replyMsgVO.setId(replyMessage.getId());
            replyMsgVO.setUid(replyMessage.getFromUid());
            replyMsgVO.setType(replyMessage.getType());
            replyMsgVO.setBody(MsgHandlerFactory.getStrategyNoNull(replyMessage.getType()).showReplyMsg(replyMessage));
            UserDomain replyUser = userCache.getUserInfo(replyMessage.getFromUid());
            replyMsgVO.setUsername(replyUser.getUserName());
            replyMsgVO.setCanCallback(YesOrNoEnum.toStatus(Objects.nonNull(msg.getGapCount()) && msg.getGapCount() <= MessageAdapter.CAN_CALLBACK_GAP_COUNT));
            replyMsgVO.setGapCount(msg.getGapCount());
            resp.setReply(replyMsgVO);
        }
        return resp;
    }

    @Override
    public Object showReplyMsg(MessageDomain msg) {
        return msg.getContent();
    }

    @Override
    public String showContactMsg(MessageDomain msg) {
        return msg.getContent();
    }
}
