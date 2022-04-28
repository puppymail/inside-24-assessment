package info.the_inside.assessment.controller;

import info.the_inside.assessment.dto.AuthRequest;
import info.the_inside.assessment.dto.JwtTokenResponse;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;

public interface AuthController {

    JwtTokenResponse login(AuthRequest request) throws FailedLoginException, AccountNotFoundException;

    void register(AuthRequest request) throws AccountException;

}
