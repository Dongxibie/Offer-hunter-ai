package com.offerhunter.service;

import com.offerhunter.entity.Resume;
import com.offerhunter.vo.ResumeVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResumeService {

    ResumeVO upload(Long userId, MultipartFile file);

    Resume getResume(Long resumeId);

    List<ResumeVO> getUserResumes(Long userId);
}
