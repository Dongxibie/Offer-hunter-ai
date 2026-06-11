package com.offerhunter.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("interview_question")
public class InterviewQuestion {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String sessionId;

    private String questionType;

    private String questionContent;

    private String answerContent;

    private String aiComment;

    private Integer aiScore;

    private String improvementSuggestion;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
