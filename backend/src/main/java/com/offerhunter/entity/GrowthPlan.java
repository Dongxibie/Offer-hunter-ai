package com.offerhunter.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("growth_plan")
public class GrowthPlan {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long resumeId;

    private Integer phase;

    private String duration;

    private String title;

    private String goals;

    private String tasks;

    private String resources;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
