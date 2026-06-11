package com.offerhunter.vo;

import lombok.Data;
import java.util.List;

@Data
public class InterviewVO {

    private String sessionId;
    private String company;
    private String position;
    private List<QuestionVO> questions;
}
