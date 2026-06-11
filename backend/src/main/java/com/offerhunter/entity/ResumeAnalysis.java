package com.offerhunter.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("resume_analysis")
public class ResumeAnalysis {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long resumeId;

    private String jobDirection;

    private String skillTags;

    private String abilityScores;

    private String summary;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
