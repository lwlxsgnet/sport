package com.lwl.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Login {
    @Schema(description = "账号")
    private String username;

    @Schema(description = "密码")
    private String password;
}
