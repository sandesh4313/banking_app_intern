package com.sandesh.bankingappdemointern.service;

import com.sandesh.bankingappdemointern.dto.*;

public interface UserService
{
    BankResponse createAccount(UserRequestDto userRequestDto);
    BankResponse balanceEnquiry(EnquiryRequestDto enquiryRequest);
    String nameEnquiry(EnquiryRequestDto enquiryRequestDto);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
    BankResponse transfer(TransferRequest request);
}
