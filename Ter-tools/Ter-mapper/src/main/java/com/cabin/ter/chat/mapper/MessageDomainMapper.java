package com.cabin.ter.chat.mapper;

import com.cabin.ter.chat.domain.MessageDomain;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MessageDomainMapper {
    Integer saveMessage(MessageDomain messageDomain);

    Integer updateByMsgId(@Param("messageDomain") MessageDomain messageDomain);

    @Select("SELECT id, roomId,fromUid,content,replyMsgId,status,gapCount,type,extra,createTime FROM ter_message WHERE id = #{ msgId }")
    MessageDomain getByMsgId(Long msgId);

    List<MessageDomain> listByIds(@Param("roomIdsList") List<Long> roomIdsList);

    Integer getUnReadCount(@Param("roomId") Long roomId, @Param("readTime") Long readTime);
}
