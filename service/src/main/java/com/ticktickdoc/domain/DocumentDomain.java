package com.ticktickdoc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDomain implements Serializable {

    private Long id;
    private String name;
    private String description;
    private LocalDate dateExecution;
    private String urlFile;
    private Long linkAuthor;
}
