package com.offerhunter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.offerhunter.common.BusinessException;
import com.offerhunter.entity.Resume;
import com.offerhunter.mapper.ResumeMapper;
import com.offerhunter.service.ResumeService;
import com.offerhunter.util.FileUtil;
import com.offerhunter.vo.ResumeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeMapper resumeMapper;
    private final FileUtil fileUtil;

    @Override
    public ResumeVO upload(Long userId, MultipartFile file) {
        String filePath = fileUtil.uploadFile(file);
        String rawText = fileUtil.extractText(filePath);

        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setFileName(file.getOriginalFilename());
        resume.setFilePath(filePath);
        resume.setRawText(rawText);
        resume.setStatus(1);
        resumeMapper.insert(resume);

        return convertToVO(resume);
    }

    @Override
    public Resume getResume(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            throw new BusinessException("简历不存在");
        }
        return resume;
    }

    @Override
    public List<ResumeVO> getUserResumes(Long userId) {
        LambdaQueryWrapper<Resume> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Resume::getUserId, userId)
                .orderByDesc(Resume::getCreateTime);
        return resumeMapper.selectList(wrapper)
                .stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    private ResumeVO convertToVO(Resume resume) {
        ResumeVO vo = new ResumeVO();
        vo.setId(resume.getId());
        vo.setFileName(resume.getFileName());
        vo.setStatus(resume.getStatus());
        vo.setCreateTime(resume.getCreateTime());
        return vo;
    }
}
