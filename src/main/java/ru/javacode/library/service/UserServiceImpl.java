package ru.javacode.library.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javacode.library.exception.EmailAlreadyExistsException;
import ru.javacode.library.exception.EntityNotFoundException;
import ru.javacode.library.exception.UserNotFoundException;
import ru.javacode.library.model.User;
import ru.javacode.library.repository.AuthenticationAttemptRepository;
import ru.javacode.library.repository.UserDao;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    private final AuthenticationAttemptRepository authenticationAttemptRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    @Transactional
    public User create(User user) {
        if (userDao.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Пользователь с таким email уже существует");
        }

        return userDao.save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userDao.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email = %s is not found".formatted(email)));
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id = %d is not found".formatted(id)));
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByEmail(email);
    }

    @Override
    @Transactional
    public void setIsBlock(long id, boolean locked) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id = %d is not found".formatted(id)));

        updateBlockStatus(locked, user);
    }

    @Override
    @Transactional
    public void setIsBlock(String email, boolean locked) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email = %s is not found".formatted(email)));

        updateBlockStatus(locked, user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByEmail(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User with login: %s not found".formatted(username)));
    }

    private void updateBlockStatus(boolean locked, User user) {
        user.setBlocked(locked);

        userDao.save(user);

        if (locked) {
            logger.info("Пользователь с email = {} заблокирован", user.getEmail());
        } else {
            logger.info("Пользователь с email = {} разблокирован", user.getEmail());
        }

        if (!locked) {
            authenticationAttemptRepository.clearAttemptsByEmail(user.getEmail());
        }
    }
}
