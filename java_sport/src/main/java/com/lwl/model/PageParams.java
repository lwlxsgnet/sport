package com.lwl.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "分页请求入参")
public class PageParams<T> {
    @Getter
    @Schema(description = "页码")
    private int pageNo;

    @Getter
    @Schema(description = "每页大小")
    private int pageSize;

    @Getter
    @Setter
    @Schema(description = "请求参数")
    private T params;

    @Schema(hidden = true)
    public Page<T> getPage() { return new Page<>(pageNo, pageSize); }

}
