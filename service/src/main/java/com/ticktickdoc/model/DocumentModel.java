package com.ticktickdoc.model;

import com.ticktickdoc.enums.StatusDocumentEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "Document")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private OffsetDateTime dateExecution;

    private StatusDocumentEnum status;

    private String urlFile;

    private Long linkAuthor;

    private String responsible;
}
