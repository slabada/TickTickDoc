package com.ticktickdoc.service;

import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.exception.UserException;
import com.ticktickdoc.mapper.UserMapper;
import com.ticktickdoc.model.entity.UserModel;
import com.ticktickdoc.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUser_shouldReturnUser() {
        Long id = 1L;
        UserModel model = new UserModel();
        UserDomain domain = new UserDomain();

        when(userRepository.findById(id)).thenReturn(Optional.of(model));
        when(userMapper.toDomain(model)).thenReturn(domain);

        UserDomain result = userService.getUser(id);

        assertEquals(domain, result);
        verify(userRepository).findById(id);
        verify(userMapper).toDomain(model);
    }

    @Test
    void getUser_shouldThrowIfNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserException.NullUserException.class,
                () -> userService.getUser(1L));

        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userMapper);
    }

    @Test
    void createUser_shouldCreateIfEmailNotExists() {
        UserDomain domain = new UserDomain();
        domain.setEmail("test@mail.com");

        UserModel model = new UserModel();
        UserModel saved = new UserModel();

        when(userRepository.findByEmail(domain.getEmail()))
                .thenReturn(Optional.empty());
        when(userMapper.toModel(domain)).thenReturn(model);
        when(userRepository.save(model)).thenReturn(saved);
        when(userMapper.toDomain(saved)).thenReturn(domain);

        UserDomain result = userService.createUser(domain);

        assertEquals(domain, result);
        verify(userRepository).findByEmail(domain.getEmail());
        verify(userRepository).save(model);
    }

    @Test
    void createUser_shouldThrowIfEmailExists() {
        UserDomain domain = new UserDomain();
        domain.setEmail("test@mail.com");

        when(userRepository.findByEmail(domain.getEmail()))
                .thenReturn(Optional.of(new UserModel()));

        assertThrows(UserException.ConflictRegistrationUserException.class,
                () -> userService.createUser(domain));

        verify(userRepository).findByEmail(domain.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_shouldUpdateExistingUser() {
        Long id = 1L;
        UserDomain input = new UserDomain();
        UserModel model = new UserModel();

        when(userRepository.findById(id)).thenReturn(Optional.of(model));
        when(userRepository.save(model)).thenReturn(model);
        when(userMapper.toDomain(model)).thenReturn(input);

        UserDomain result = userService.updateUser(id, input);

        verify(userMapper).updateUser(input, model);
        verify(userRepository).save(model);
        assertEquals(input, result);
    }

    @Test
    void deleteUser_shouldDeleteIfExists() {
        Long id = 1L;

        when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteUser(id);

        verify(subscriptionService).deleteAllSubscriptionsByUserId(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    void deleteUser_shouldDoNothingIfNotExists() {
        when(userRepository.existsById(1L)).thenReturn(false);

        userService.deleteUser(1L);

        verify(subscriptionService, never()).deleteAllSubscriptionsByUserId(any());
        verify(userRepository, never()).deleteById(any());
    }
}