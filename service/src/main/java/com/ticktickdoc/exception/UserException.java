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

    public static class ConflictAddChildUserException extends RuntimeException {
        public ConflictAddChildUserException() {
            super("Чтобы пользователя мог быть дочерним, необходимо чтобы он сам не был родителем");
        }
    }

    public static class ConflictAddChildCurrentUserException extends RuntimeException {
        public ConflictAddChildCurrentUserException() {
            super("Пользователь не может быть сам у себя дочерним");
        }
    }

    public static class ConflictAddChildDuplicateUserException extends RuntimeException {
        public ConflictAddChildDuplicateUserException() {
            super("Один из пользователей уже есть среди дочерних");
        }
    }
}
