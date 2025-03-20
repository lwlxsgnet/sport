package com.lwl.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.lwl.entity.SysUser;
import com.lwl.model.PageParams;
import com.lwl.model.Result;
import com.lwl.service.SysUserService;
import com.lwl.utils.PasswordUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sysUser")
@Tag(name = "用户管理")
@ApiSupport(author = "leo lau: lwlxsg@163.com", order = 1)
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    /* select by id */
    @GetMapping("info/{id}")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "根据id查询单条数据",
            parameters = { @Parameter(name = "id", description = "主键", required = true, in = ParameterIn.PATH)}
    )
    public Result<SysUser> getById(@PathVariable String id){
        return Result.success(sysUserService.getById(id));
    }

    /* select by page */
    @PostMapping("page")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "分页查询")
    public Result<Page<SysUser>> pageQuery(@RequestBody PageParams<SysUser> pageParams){
        SysUser params = pageParams.getParams();
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(params.getNickname()), SysUser::getNickname, params.getNickname());
        return Result.success(sysUserService.page(pageParams.getPage(), queryWrapper));
    }

    /* insert */
    @PostMapping("insert")
    @Operation(summary = "新增数据")
    @ApiOperationSupport(order = 3)
    public Result<Boolean> insert(@RequestBody SysUser sysUser){
        String slat = RandomUtil.randomString(16);
        sysUser.setSlat(slat);
        String password = sysUser.getPassword();
        String encrypt = PasswordUtils.encrypt(slat, password);
        sysUser.setPassword(encrypt);
        if (sysUserService.save(sysUser)) return Result.success();
        return Result.error();
    }

    /* update */
    @PostMapping("update")
    @Operation(summary = "更新数据")
    @ApiOperationSupport(order = 4)
    public Result<Boolean> update(@RequestBody SysUser sysUser) {
        if (sysUserService.updateById(sysUser)) return Result.success();
        return Result.error();
    }

    /* delete */
    @GetMapping("/delete/{id}")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "根据id删除数据",
            parameters = { @Parameter(name = "id", description = "主键", required = true, in = ParameterIn.PATH)}
    )
    public Result<Boolean> delete(@PathVariable String id) {
        if (sysUserService.removeById(id)) return Result.success();
        return Result.error();
    }
}
