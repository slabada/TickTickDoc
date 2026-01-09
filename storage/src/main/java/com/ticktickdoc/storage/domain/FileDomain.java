package com.ticktickdoc.storage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileDomain {

    private Long id;
    private String fileName;
    private String originalFileName;
    private LocalDateTime addDate;
}
