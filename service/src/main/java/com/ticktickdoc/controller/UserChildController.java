package com.ticktickdoc.controller;

import com.ticktickdoc.domain.RequestIdDomain;
import com.ticktickdoc.domain.ResponseIdDomain;
import com.ticktickdoc.dto.RequestIdDto;
import com.ticktickdoc.dto.ResponseIdDto;
import com.ticktickdoc.mapper.ChildMapper;
import com.ticktickdoc.model.projection.UserChildDocumentProjection;
import com.ticktickdoc.service.UserChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserChildController {

    private final UserChildService userChildService;

    private final ChildMapper childMapper;

    @GetMapping("/user/child/me")
    public List<UserChildDocumentProjection> getUserChildDocument() {
        return userChildService.getUserChildDocument();
    }

    @PostMapping("/user/child/add")
    public ResponseEntity<ResponseIdDto> addSubsidiaryUser(@RequestBody RequestIdDto request) {
        RequestIdDomain domain = childMapper.toDomain(request);
        ResponseIdDomain responseIdDomains = userChildService.addUserChild(domain);
        ResponseIdDto dto = childMapper.toDto(responseIdDomains);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/user/child/delete")
    public ResponseEntity<ResponseIdDto> deleteSubsidiaryUser(@RequestBody RequestIdDto request) {
        RequestIdDomain domain = childMapper.toDomain(request);
        ResponseIdDomain deleted = userChildService.deleteUserChild(domain);
        ResponseIdDto dto = childMapper.toDto(deleted);
        return ResponseEntity.ok().body(dto);
    }
}
