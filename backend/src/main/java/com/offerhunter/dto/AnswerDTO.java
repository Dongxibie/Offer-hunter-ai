package com.offerhunter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerDTO {

    @NotBlank(message = "会话ID不能为空")
    private String sessionId;

    @NotNull(message = "问题ID不能为空")
    private Long questionId;

    @NotBlank(message = "回答内容不能为空")
    private String answer;
}
