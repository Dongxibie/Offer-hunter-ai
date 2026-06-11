package com.offerhunter.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("resume_optimization")
public class ResumeOptimization {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long resumeId;

    private Long jobId;

    private String currentIssues;

    private String suggestions;

    private String optimizedContent;

    private String starVersion;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
