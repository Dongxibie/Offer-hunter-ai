package com.offerhunter.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("job_skill")
public class JobSkill {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long jobId;

    private String skillName;

    private String skillLevel;

    private Integer isRequired;
}
