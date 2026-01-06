package com.ticktickdoc.exception;

public class UserException {

    public static class ConflictRegistrationUserException extends RuntimeException {
        public ConflictRegistrationUserException() {
            super("Данная электронная почта уже используется");
        }
    }

    public static class NullUserException extends RuntimeException {
        public NullUserException() {
            super("Пользователь не найден");
        }
    }
}
