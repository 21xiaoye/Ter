package com.cabin.ter.sys.mapper;

import com.cabin.ter.sys.domain.SysDomain;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *     服务mapper层
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 08:54
 */
public interface SysDomainMapper {
    Integer insertSysWsList(List<SysDomain> sysDomainList);
}
