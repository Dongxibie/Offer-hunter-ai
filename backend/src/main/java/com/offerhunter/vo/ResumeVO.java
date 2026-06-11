package com.offerhunter.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResumeVO {

    private Long id;
    private String fileName;
    private Integer status;
    private LocalDateTime createTime;
}
