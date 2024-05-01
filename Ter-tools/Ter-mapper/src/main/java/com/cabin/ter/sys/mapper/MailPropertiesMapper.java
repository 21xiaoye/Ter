package com.cabin.ter.sys.mapper;

import com.cabin.ter.sys.domain.MailProperties;
import org.apache.ibatis.annotations.Select;


import java.util.List;

/**
 * <p>
 *     邮箱mapper层
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-01 14:55
 */
public interface MailPropertiesMapper {
    /**
     * 查询所有邮箱源
     *
     * @return 邮箱属性列表
     */
    @Select("SELECT mailOriginId,mailName,mailPasswd,mailHost,protocol,port,encoding FROM ter_mail_origin")
    List<MailProperties> findMailList();
}
