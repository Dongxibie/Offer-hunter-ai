package com.offerhunter.controller;

import com.offerhunter.common.Result;
import com.offerhunter.entity.Job;
import com.offerhunter.service.JobService;
import com.offerhunter.vo.JobMatchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/list")
    public Result<List<Job>> listJobs() {
        return Result.success(jobService.listJobs());
    }

    @GetMapping("/{id}")
    public Result<Job> getJob(@PathVariable Long id) {
        return Result.success(jobService.getJob(id));
    }

    @GetMapping("/match")
    public Result<List<JobMatchVO>> matchJobs(@RequestParam Long resumeId) {
        return Result.success(jobService.matchJobs(resumeId));
    }
}
