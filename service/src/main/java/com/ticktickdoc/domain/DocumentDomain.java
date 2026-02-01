package com.ticktickdoc.domain;

import com.ticktickdoc.enums.RemindDayEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class DocumentDomain implements Serializable {

    private Long id;
    private String name;
    private String urlFile;
    private LocalDate dateExecution;
    private String email;
    private RemindDayEnum remindDay;
    private Long linkAuthorId;
    private Long linkFileId;
    private String file;
}
