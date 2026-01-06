package com.ticktickdoc.exception;

public class DocumentException {

    public static class NonDocumentException extends RuntimeException {
        public NonDocumentException() {
            super("Документ не найден");
        }
    }
}
