package com.offerhunter.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class JobMatchVO {

    private Long jobId;
    private String company;
    private String position;
    private String salaryRange;
    private String location;
    private BigDecimal matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private String matchReason;
}
