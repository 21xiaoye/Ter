package com.cabin.ter.chat.domain;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 20:32
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupMemberDomain implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 正常状态
     */
    public static final Integer NORMAL = 0;
    /**
     * 退群状态
     */
    public static final Integer QUIT = 1;

    /**
     * id
     */
    private Long id;

    /**
     * 群组id
     */
    private Long groupId;

    /**
     * 成员uid
     */
    private Long userId;

    /**
     * 成员角色1群主(可撤回，可移除，可解散) 2管理员(可撤回，可移除) 3普通成员
     *
     */
    private Integer role;
    /**
     * 成员群备注
     */
    private String groupRemark;
    /**
     * 成员状态0正常 1退群
     */
    private Integer memberStatus;
    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;
}
