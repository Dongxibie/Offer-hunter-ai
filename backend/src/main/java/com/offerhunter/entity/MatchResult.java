package com.offerhunter.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("match_result")
public class MatchResult {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long resumeId;

    private Long jobId;

    private BigDecimal matchScore;

    private BigDecimal skillScore;

    private BigDecimal projectScore;

    private String matchedSkills;

    private String missingSkills;

    private String matchReason;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
