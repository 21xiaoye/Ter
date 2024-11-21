package com.cabin.ter.strategy;

import cn.hutool.core.bean.BeanUtil;
import com.cabin.ter.adapter.MessageAdapter;
import com.cabin.ter.chat.domain.MessageDomain;
import com.cabin.ter.chat.mapper.MessageDomainMapper;
import com.cabin.ter.constants.enums.MessageTypeEnum;
import com.cabin.ter.util.AsserUtil;
import com.cabin.ter.constants.request.ChatMessageReq;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;

/**
 *  <p>
 *      消息处理模板类
 *  </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-29 14-45
 */
@Component
public abstract class AbstractMsgHandler<Req> {
    @Autowired
    private MessageDomainMapper messageDomainMapper;
    private Class<Req> bodyClass;

    /**
     * 初始化消息处理类
     */
    @SuppressWarnings("unchecked")
    @PostConstruct
    private void init(){
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.bodyClass = (Class<Req>) parameterizedType.getActualTypeArguments()[0];
        MsgHandlerFactory.register(getMsgTypeEnum().getStatus(), this);
    }

    abstract MessageTypeEnum getMsgTypeEnum();

    protected void checkMsg(Req body, Long roomId, Long uid){

    }
    @Transactional
    public Long checkAndSaveMsg(ChatMessageReq request, Long uid){
        Req body = this.toBean(request.getBody());

        AsserUtil.allCheckValidateThrow(body);
        checkMsg(body, request.getRoomId(), uid);

        MessageDomain messageDomain = MessageAdapter.buildMsgSave(request, uid);
        messageDomainMapper.saveMessage(messageDomain);
        saveMsg(messageDomain, body);
        return messageDomain.getId();
    }

    private Req toBean(Object body){
        if(bodyClass.isAssignableFrom(body.getClass())){
            return (Req) body;
        }
        return BeanUtil.toBean(body, bodyClass);
    }

    protected abstract void saveMsg(MessageDomain message, Req body);

    /**
     * 展示消息
     */
    public abstract Object showMsg(MessageDomain msg);

    /**
     * 被回复时——展示的消息
     */
    public abstract Object showReplyMsg(MessageDomain msg);

    /**
     * 会话列表——展示的消息
     */
    public abstract String showContactMsg(MessageDomain msg);

}
