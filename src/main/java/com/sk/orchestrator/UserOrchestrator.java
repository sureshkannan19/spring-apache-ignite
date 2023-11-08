package com.sk.orchestrator;

import java.util.List;

import com.sk.constants.FilterConstants;
import com.sk.service.JWTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sk.enums.Gender;
import com.sk.model.UsersModel;
import com.sk.service.UserService;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class UserOrchestrator {

    private final UserService userService;
    private final JWTokenService tokenService;

    public List<UsersModel> findByGender(String gender) {
        return userService.findByGender(Gender.findByCode(gender));
    }

    public ResponseEntity<UsersModel> logIn(String userName, HttpServletResponse response) {
        UsersModel usersModel = new UsersModel();
        usersModel.setUserName(userName);
        usersModel.setId(7L);
        String jwtToken = tokenService.generateToken(usersModel);
        response.setHeader(FilterConstants.SIGNED_IN_USER, jwtToken);
        return new ResponseEntity<>(usersModel, HttpStatus.OK);
    }
}
