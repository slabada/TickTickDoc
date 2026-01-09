package com.ticktickdoc.exception;

public class FileException {

    public static class BadRequestAddFileForDocumentException extends RuntimeException {
        public BadRequestAddFileForDocumentException() {
            super("Файл можно прикрепить только к своим документам");
        }
    }

    public static class NonFileException extends RuntimeException {
        public NonFileException() {
            super("файл не найден");
        }
    }
}
