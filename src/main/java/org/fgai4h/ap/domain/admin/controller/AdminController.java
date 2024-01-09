package org.fgai4h.ap.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.api.AdminApi;
import org.fgai4h.ap.api.model.UserDto;
import org.fgai4h.ap.domain.admin.service.AdminService;
import org.fgai4h.ap.domain.user.mapper.UserApiMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController implements AdminApi {

    private final AdminService adminService;
    private final UserApiMapper userApiMapper;

    @Override
    public ResponseEntity<List<UserDto>> retrieveAllUsers() {
        return new ResponseEntity<>(
                adminService.retrieveAllUsers().stream().map(userApiMapper::toUserDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
