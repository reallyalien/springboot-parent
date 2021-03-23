package com.ot.session.service;

import com.ot.session.model.AuthenticationRequest;
import com.ot.session.model.UserDto;

/**
 * 认证服务
 */
public interface AuthenticationService {

    UserDto authentication(AuthenticationRequest request);
}
