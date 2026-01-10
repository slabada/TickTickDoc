package com.ticktickdoc.dto;

import com.ticktickdoc.enums.StatusDocumentEnum;
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
public class DocumentDto implements Serializable {

    private Long id;
    private String name;
    private String description;
    private LocalDate dateExecution;
    private StatusDocumentEnum status;
    private String urlFile;
    private Long LinkAuthor;
}
