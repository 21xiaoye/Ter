package com.cabin.ter.constants.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserOfflineResp {
    private Long userId;
}
