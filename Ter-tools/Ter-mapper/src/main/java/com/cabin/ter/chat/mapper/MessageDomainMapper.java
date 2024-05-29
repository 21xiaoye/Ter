package com.cabin.ter.chat.mapper;

import com.cabin.ter.chat.domain.MessageDomain;

public interface MessageDomainMapper {
    Integer saveMessage(MessageDomain messageDomain);
}
