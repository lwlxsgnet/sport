package com.lwl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lwl.entity.SysUser;
import com.lwl.mapper.SysUserMapper;
import com.lwl.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

}
