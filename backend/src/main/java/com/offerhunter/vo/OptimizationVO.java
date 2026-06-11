package com.offerhunter.vo;

import lombok.Data;
import java.util.List;

@Data
public class OptimizationVO {

    private Long resumeId;
    private Long jobId;
    private String company;
    private String position;
    private List<String> currentIssues;
    private List<String> suggestions;
    private String optimizedContent;
    private String starVersion;
}
