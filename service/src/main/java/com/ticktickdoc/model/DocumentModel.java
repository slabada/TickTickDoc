package com.ticktickdoc.model;

import com.ticktickdoc.enums.StatusDocumentEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "document")
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

    private LocalDate dateExecution;

    private StatusDocumentEnum status;

    private String urlFile;

    private Long linkAuthor;
}
