package me.parker.springtransaction.propagation.config;

import lombok.RequiredArgsConstructor;
import me.parker.springtransaction.propagation.BankStatementInNestedService;
import me.parker.springtransaction.propagation.BankStatementInRequiredService;
import me.parker.springtransaction.propagation.BankStatementInRequiresNewService;
import me.parker.springtransaction.propagation.BankingService;
import me.parker.springtransaction.repository.BankStatementRepository;
import me.parker.springtransaction.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class TransactionPropagationConfig {

    private final UserRepository userRepository;
    private final BankStatementInRequiredService bankStatementInRequiredService;
    private final BankStatementInRequiresNewService bankStatementInRequiresNewService;
    private final BankStatementInNestedService bankStatementInNestedService;

    @Bean
    public BankingService requiredBankingService() {
        return new BankingService(userRepository, bankStatementInRequiredService);
    }

    @Bean
    public BankingService requiresNewBankingService() {
        return new BankingService(userRepository, bankStatementInRequiresNewService);
    }

    @Bean
    public BankingService nestedBankingService() {
        return new BankingService(userRepository, bankStatementInNestedService);
    }
}
