package com.ticktickdoc.model.projection;

public interface UserChildDocumentProjection {

    Long getUserId();

    String getFullName();

    String getEmail();

    String getTelegram();

    Integer getCountDocument();
}
