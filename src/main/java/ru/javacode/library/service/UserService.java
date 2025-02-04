package ru.javacode.library.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.javacode.library.model.User;

public interface UserService extends UserDetailsService {
    User create(User user);

    User getUserByEmail(String email);

    User getById(long id);

    User getCurrentUser();

    void setIsBlock(long id, boolean locked);

    void setIsBlock(String email, boolean locked);
}
