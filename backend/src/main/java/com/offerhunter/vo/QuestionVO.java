package com.offerhunter.vo;

import lombok.Data;

@Data
public class QuestionVO {

    private Long id;
    private String type;
    private String content;
    private String userAnswer;
    private String aiComment;
    private Integer aiScore;
    private String improvement;
}
