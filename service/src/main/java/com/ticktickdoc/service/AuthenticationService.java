package com.ticktickdoc.service;

import com.ticktickdoc.domain.LoginDomain;
import com.ticktickdoc.domain.UserDomain;

public interface AuthenticationService {

    /**
     * Метод для регистрации нового пользователя
     *
     * @param user Объект хронящий всю информацию для регистрации
     * @return JWT токен
     */
    String register(UserDomain user);

    /**
     * Метод для авторизации пользователя
     *
     * @param login Объект который хранит всю информацию для авторизации
     * @return JWT токен
     */
    String login(LoginDomain login);

}
