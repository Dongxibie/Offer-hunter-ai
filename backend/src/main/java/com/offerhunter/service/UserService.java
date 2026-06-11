package com.offerhunter.service;

import com.offerhunter.dto.LoginDTO;
import com.offerhunter.dto.RegisterDTO;
import com.offerhunter.vo.UserVO;

public interface UserService {

    UserVO register(RegisterDTO dto);

    UserVO login(LoginDTO dto);

    UserVO getUserInfo(Long userId);
}
