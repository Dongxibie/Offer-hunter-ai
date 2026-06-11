package com.offerhunter.vo;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class AnalysisVO {

    private Long resumeId;
    private String jobDirection;
    private List<String> skillTags;
    private Map<String, Integer> abilityScores;
    private String summary;
}
