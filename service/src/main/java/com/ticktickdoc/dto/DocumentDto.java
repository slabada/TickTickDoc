package com.ticktickdoc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ticktickdoc.enums.RemindDayEnum;
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
    private String urlFile;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateExecution;
    private String email;
    private RemindDayEnum remindDay;
    private String file;
}
