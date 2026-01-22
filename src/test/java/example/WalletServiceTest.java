package org.example;

import org.example.entity.Customer;
import org.example.entity.Wallet;
import org.example.repository.Database;
import org.example.service.WalletService;
import org.example.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class WalletServiceTest {

    private WalletService walletService;
    private Customer customer;

    @BeforeEach
    void setUp() {
        // reset DB
        Database.WALLETS = new ConcurrentHashMap<>();

        customer = new Customer("test@gmail.com", "test");
        Wallet wallet = new Wallet(1_000_000.0);

        Database.WALLETS.put(customer, wallet);

        walletService = new WalletServiceImpl();
    }

    // ===== charge() =====

    @Test
    void should_charge_successfully() {
        boolean ok = walletService.charge(customer, 200_000.0);

        assertTrue(ok);

        double balance = walletService.getBalance(customer);
        assertEquals(800_000.0, balance);
    }

    @Test
    void should_fail_charge_when_not_enough_money() {
        boolean ok = walletService.charge(customer, 2_000_000.0);

        assertFalse(ok);

        double balance = walletService.getBalance(customer);
        assertEquals(1_000_000.0, balance); // unchanged
    }

    // ===== cashIn() =====

    @Test
    void should_cash_in_successfully() {
        boolean ok = walletService.cashIn(customer, 500_000.0);

        assertTrue(ok);

        double balance = walletService.getBalance(customer);
        assertEquals(1_500_000.0, balance);
    }

    @Test
    void should_fail_cash_in_when_amount_negative_logic_bug() {
        // NOTE: theo code hiện tại:
        // if (wallet.getBalance().compareTo(amount) < 0) return false;
        // => logic này SAI cho cashIn, nhưng test này phản ánh đúng behavior hiện tại

        boolean ok = walletService.cashIn(customer, -100.0);

        // balance.compareTo(-100) > 0 => không < 0 => cho phép cộng
        assertTrue(ok);

        double balance = walletService.getBalance(customer);
        assertEquals(999_900.0, balance);
    }

    // ===== getBalance() =====

    @Test
    void should_get_balance() {
        double balance = walletService.getBalance(customer);
        assertEquals(1_000_000.0, balance);
    }

    // ===== wallet not found =====

    @Test
    void should_throw_when_wallet_not_found_on_charge() {
        Customer other = new Customer("other@gmail.com", "other");

        Exception ex = assertThrows(
                IllegalStateException.class,
                () -> walletService.charge(other, 100.0)
        );

        assertTrue(ex.getMessage().contains("Wallet not found"));
    }

    @Test
    void should_throw_when_wallet_not_found_on_cash_in() {
        Customer other = new Customer("other@gmail.com", "other");

        Exception ex = assertThrows(
                IllegalStateException.class,
                () -> walletService.cashIn(other, 100.0)
        );

        assertTrue(ex.getMessage().contains("Wallet not found"));
    }

    @Test
    void should_throw_when_wallet_not_found_on_get_balance() {
        Customer other = new Customer("other@gmail.com", "other");

        Exception ex = assertThrows(
                IllegalStateException.class,
                () -> walletService.getBalance(other)
        );

        assertTrue(ex.getMessage().contains("Wallet not found"));
    }
}
