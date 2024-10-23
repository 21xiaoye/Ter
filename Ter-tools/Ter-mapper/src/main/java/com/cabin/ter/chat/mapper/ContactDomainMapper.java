package com.cabin.ter.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cabin.ter.chat.domain.ContactDomain;

import java.util.List;

/**
 * 总是充满遗憾的
 */
public interface ContactDomainMapper{
    List<ContactDomain> getUserContactList(Long uId);
}
