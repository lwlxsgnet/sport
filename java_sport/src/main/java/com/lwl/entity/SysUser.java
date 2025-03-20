package com.lwl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = false)
//代码生成工具发现你的类继承了其他类，但是生成的 equals() 和 hashCode() 方法没有调用父类的对应方法
public class SysUser extends BaseEntity {
    @Schema(title = "用户名", description = "登陆账号")
    private String username;
    @Schema(description = "密码")
    private String password;
    @Schema(hidden = true)
    private String slat;
    @Schema(title = "性别", description = "0:女 1:男 2未知")
    private String sex;
    @Schema(description = "昵称")
    private String nickname;
    @Schema(description = "电话")
    private String phone;
    @Schema(description = "邮件")
    private String email;
    @Schema(description = "头像")
    private String avatar;


}
