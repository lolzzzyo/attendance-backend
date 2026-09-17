package com.example.attendancebackend.repository;

import com.example.attendancebackend.model.Account;
import com.example.attendancebackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    Optional<Account> findById(Long id);

    boolean existsByEmail(String email);

    List<Account> findByRole(Role role);
}
