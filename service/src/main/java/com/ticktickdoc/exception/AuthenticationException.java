package com.ticktickdoc.exception;

public class AuthenticationException {

    public static class ConflictAuthException extends RuntimeException {
        public ConflictAuthException() {
            super("Неверный логин или пароль");
        }
    }

    public static class InvalidJwtTokenException extends RuntimeException {
        public InvalidJwtTokenException() {
            super("Токен умер");
        }
    }
}
