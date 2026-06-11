package com.offerhunter.controller;

import com.offerhunter.common.Result;
import com.offerhunter.entity.Resume;
import com.offerhunter.service.ResumeService;
import com.offerhunter.vo.ResumeVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/upload")
    public Result<ResumeVO> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(resumeService.upload(userId, file));
    }

    @GetMapping("/{id}")
    public Result<Resume> getResume(@PathVariable Long id) {
        return Result.success(resumeService.getResume(id));
    }

    @GetMapping("/list")
    public Result<List<ResumeVO>> getUserResumes(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(resumeService.getUserResumes(userId));
    }
}
