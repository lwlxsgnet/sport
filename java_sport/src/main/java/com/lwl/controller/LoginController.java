package com.lwl.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lwl.config.annotation.RestControllerMapping;
import com.lwl.entity.SysUser;
import com.lwl.entity.vo.Login;
import com.lwl.model.Result;
import com.lwl.service.SysUserService;
import com.lwl.utils.PasswordUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@RestControllerMapping("login")
public class LoginController {
    @Resource
    private SysUserService sysUserService;

    @SaIgnore
    @PostMapping("username")
    @Operation(summary = "账号密码登录")
    public Result<Map<String, String>> login(@RequestBody Login login) {
        SysUser user = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, login.getUsername()));
        if (user == null) return Result.error("账号不存在");
        //校验密码
        String password = login.getPassword();
        String slat = user.getSlat();
        String decrypt = PasswordUtils.decrypt(slat, user.getPassword());
        if (decrypt.equals(password)) {
            StpUtil.login(user.getId());
            Map<String, String> map = new HashMap<>();
            SaTokenInfo token = StpUtil.getTokenInfo();
            map.put("tokenHead", token.tokenName);
            map.put("token", token.tokenValue);
            return Result.success(map);
        }
        return Result.error("账号或者密码错误");
    }

    @GetMapping("logout")
    @Operation(summary = "退出登录")
    public Result<Boolean> logout() {
        StpUtil.logout();
        return Result.success("退出登录成功");
    }

    @GetMapping("test")
    @SaCheckPermission("login:test")
    public Result<String> test() {
        return Result.success("");
    }
}
