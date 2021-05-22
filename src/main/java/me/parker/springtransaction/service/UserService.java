package me.parker.springtransaction.service;

import lombok.RequiredArgsConstructor;
import me.parker.springtransaction.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
}
