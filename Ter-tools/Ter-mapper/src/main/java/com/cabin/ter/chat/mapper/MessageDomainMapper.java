package com.cabin.ter.chat.mapper;

import com.cabin.ter.chat.domain.MessageDomain;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface MessageDomainMapper {
    Integer saveMessage(MessageDomain messageDomain);

    Integer updateByMsgId(@Param("messageDomain") MessageDomain messageDomain);

    @Select("SELECT id, roomId,fromUid,content,replyMsgId,status,gapCount,type,extra,createTime FROM ter_message WHERE id = #{ msgId }")
    MessageDomain getByMsgId(Long msgId);
}
