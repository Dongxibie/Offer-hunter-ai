package com.offerhunter.service;

import com.offerhunter.entity.Job;
import com.offerhunter.vo.JobMatchVO;

import java.util.List;

public interface JobService {

    List<Job> listJobs();

    Job getJob(Long jobId);

    List<JobMatchVO> matchJobs(Long resumeId);
}
