package com.offerhunter.vo;

import lombok.Data;
import java.util.List;

@Data
public class GrowthPlanVO {

    private Long resumeId;
    private List<PhaseVO> phases;
}
