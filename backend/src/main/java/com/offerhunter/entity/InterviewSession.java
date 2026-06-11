package com.offerhunter.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("interview_session")
public class InterviewSession {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private Long resumeId;

    private Long jobId;

    private Integer status;

    private Integer totalScore;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
