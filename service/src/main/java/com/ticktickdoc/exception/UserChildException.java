package com.ticktickdoc.exception;

public class UserChildException {

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
            super("У данного аккаунта пользователь уже является дочерним");
        }
    }

    public static class UserAlreadyChildOfAnotherUserException extends RuntimeException {
        public UserAlreadyChildOfAnotherUserException() {
            super("Пользователь уже является дочерним у другого аккаунта");
        }
    }

}
