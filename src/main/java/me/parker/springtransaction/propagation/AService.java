package me.parker.springtransaction.propagation;

import lombok.RequiredArgsConstructor;
import me.parker.springtransaction.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AService {

    private final UserRepository userRepository;
    private final BService bService;

    @Transactional(rollbackFor = RollbackPointException.class, propagation = Propagation.REQUIRED)
    public void update(Long userId, RollbackPoint rollbackPoint) {
        userRepository.updateAgeById(21, userId);

        if (RollbackPoint.BEFORE_NESTED_TRANSACTION == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }

        bService.updateRequired(userId, rollbackPoint);

        userRepository.updateNameById("LEE", userId);

        if (RollbackPoint.AFTER_NESTED_TRANSACTION == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }
    }
}
