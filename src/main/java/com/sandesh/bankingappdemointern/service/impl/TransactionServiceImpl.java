package com.sandesh.bankingappdemointern.service.impl;

import com.sandesh.bankingappdemointern.dto.TransactionDto;
import com.sandesh.bankingappdemointern.entity.Transaction;
import com.sandesh.bankingappdemointern.repository.TransactionRepository;
import com.sandesh.bankingappdemointern.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction=new Transaction();
        transaction.setTransactionType(transactionDto.getTransactionType());
        transaction.setAccountNumber(transactionDto.getAccountNumber());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setStatus("SUCCESS");
        transactionRepository.save(transaction);
        System.out.println("Transaction Saved Successful");



    }
}
