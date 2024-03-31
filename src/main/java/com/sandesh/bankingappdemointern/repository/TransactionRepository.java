package com.sandesh.bankingappdemointern.repository;

import com.sandesh.bankingappdemointern.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,String> {
}
