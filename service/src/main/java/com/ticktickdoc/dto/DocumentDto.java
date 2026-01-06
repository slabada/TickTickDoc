package com.ticktickdoc.dto;

import com.ticktickdoc.enums.StatusDocumentEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {

    private Long id;
    private String name;
    private String description;
    private OffsetDateTime dateExecution;
    private StatusDocumentEnum status;
    private String urlFile;
    private Long LinkAuthor;
    private String responsible;
}
