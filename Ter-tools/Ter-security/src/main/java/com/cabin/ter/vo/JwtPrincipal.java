package com.cabin.ter.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtPrincipal {
    private String subject;
    private Long Id;
}
