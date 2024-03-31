package com.sandesh.bankingappdemointern.service;

import com.sandesh.bankingappdemointern.dto.TransactionDto;
import com.sandesh.bankingappdemointern.entity.Transaction;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
