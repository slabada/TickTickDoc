package com.ticktickdoc.dto;

import com.ticktickdoc.enums.RemindDayEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDocumentDto {
    private String name;
    private String urlFile;
    private LocalDate dateExecution;
    private String email;
    private RemindDayEnum remindDay;
    private String file;
}
