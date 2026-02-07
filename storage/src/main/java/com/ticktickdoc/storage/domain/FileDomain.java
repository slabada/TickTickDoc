package com.ticktickdoc.storage.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FileDomain {

    private Long id;
    private String fileName;
    private String originalFileName;
    private LocalDateTime addDate;
}
