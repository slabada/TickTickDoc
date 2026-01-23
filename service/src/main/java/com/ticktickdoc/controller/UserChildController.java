package com.ticktickdoc.controller;

import com.ticktickdoc.domain.RequestIdDomain;
import com.ticktickdoc.domain.ResponseIdDomain;
import com.ticktickdoc.dto.RequestIdDto;
import com.ticktickdoc.dto.ResponseIdDto;
import com.ticktickdoc.mapper.UserChildMapper;
import com.ticktickdoc.model.projection.UserChildDocumentProjection;
import com.ticktickdoc.service.UserChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserChildController {

    private final UserChildService userChildService;

    private final UserChildMapper userChildMapper;

    @GetMapping("/user/child/me")
    public List<UserChildDocumentProjection> getUserChildDocument() {
        return userChildService.getUserChildDocument();
    }

    @DeleteMapping("/user/child/delete")
    public ResponseEntity<ResponseIdDto> deleteSubsidiaryUser(@RequestBody RequestIdDto request) {
        RequestIdDomain domain = userChildMapper.toDomain(request);
        ResponseIdDomain deleted = userChildService.deleteUserChild(domain);
        ResponseIdDto dto = userChildMapper.toDto(deleted);
        return ResponseEntity.ok().body(dto);
    }
}
