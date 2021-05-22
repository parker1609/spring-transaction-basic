package me.parker.springtransaction.service;

import lombok.RequiredArgsConstructor;
import me.parker.springtransaction.domain.User;
import me.parker.springtransaction.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public void updateById(Long userId, String name, int age) {
        userRepository.updateById(name, age, userId);
    }
}
