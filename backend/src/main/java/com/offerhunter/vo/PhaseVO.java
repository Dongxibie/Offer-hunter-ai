package com.offerhunter.vo;

import lombok.Data;
import java.util.List;

@Data
public class PhaseVO {

    private Integer phase;
    private String duration;
    private String title;
    private List<String> goals;
    private List<String> tasks;
    private List<String> resources;
}
