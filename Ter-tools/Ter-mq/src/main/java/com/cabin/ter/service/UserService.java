package com.cabin.ter.service;

import com.cabin.ter.admin.domain.UserDomain;
import org.springframework.stereotype.Service;

/**
 * @author xiaoye
 * @date Created in 2024-05-11 11-28
 */
@Service
public interface UserService {
    void register(UserDomain user);
}
