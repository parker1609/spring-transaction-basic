package me.parker.springtransaction.propagation;

import lombok.RequiredArgsConstructor;
import me.parker.springtransaction.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BService {

    private final UserRepository userRepository;

    @Transactional(rollbackFor = RollbackPointException.class, propagation = Propagation.REQUIRED)
    public void updateRequired(Long userId, RollbackPoint rollbackPoint) {
        userRepository.updateAgeById(22, userId);
        userRepository.updateNameById("KIM", userId);

        if (RollbackPoint.INNER_NESTED_TRANSACTION == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }
    }
}
