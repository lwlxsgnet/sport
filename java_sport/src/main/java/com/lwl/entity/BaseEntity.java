package com.lwl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "公共类")
public class BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;
    @Schema(description = "创建时间")
    private Date create_time;
    @Schema(hidden = true)
    private String create_by;
    @Schema(description = "备注")
    private String remarks;
}
