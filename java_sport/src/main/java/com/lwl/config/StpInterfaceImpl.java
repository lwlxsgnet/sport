package com.lwl.config;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return List.of("login:test");
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
    }
}
