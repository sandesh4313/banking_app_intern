package com.sandesh.bankingappdemointern.service.impl;

import com.sandesh.bankingappdemointern.dto.*;
import com.sandesh.bankingappdemointern.entity.User;
import com.sandesh.bankingappdemointern.repository.UserRepository;
import com.sandesh.bankingappdemointern.service.EmailService;
import com.sandesh.bankingappdemointern.service.TransactionService;
import com.sandesh.bankingappdemointern.service.UserService;
import com.sandesh.bankingappdemointern.utils.AccountUtils;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
@RequiredArgsConstructor
@Builder
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TransactionService transactionService;

    @Override
    public BankResponse createAccount(UserRequestDto userRequestDto) {
        // creating account-saving a new user into db
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();

        }

        User newUser = User.builder()
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .otherName(userRequestDto.getOtherName())
                .gender(userRequestDto.getGender())
                .address(userRequestDto.getAddress())
                .stateOfOrigin(userRequestDto.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .email(userRequestDto.getEmail())
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .phoneNumber(userRequestDto.getPhoneNumber())

                .alternativePhoneNumber(userRequestDto.getAlternativePhoneNumber())

                .build();
        User savedUser = userRepository.save(newUser);
        //send Email Alert
//        EmailDetails emailDetails = EmailDetails.builder()
//                .recipient(savedUser.getEmail())
//                .subject("ACCOUNT CREATION")
//                .messageBody("Congratulations! your Account has been successfully created .\n Your Account Details: \n" +
//                        "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + " " + savedUser.getAccountNumber())
//                .build();
        EmailDetails emailDetails = new EmailDetails(savedUser.getEmail(), "Congratulations! your Account has been successfully created .\n Your Account Details: \n" +
                "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + " " + savedUser.getAccountNumber(), "ACCOUNT CREATION");
        emailService.sendEmail(emailDetails);
        return new BankResponse(AccountUtils.ACCOUNT_CREATION_SUCCESS, AccountUtils.ACCOUNT_CREATION_MESSAGE, new AccountInfo(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName(), savedUser.getAccountBalance(), savedUser.getAccountNumber()));
//               BankResponse.builder()
//                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
//                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
//                .accountInfo(AccountInfo.builder()
//                        .accountNumber(savedUser.getAccountNumber())
//                        .accountBalance(savedUser.getAccountBalance())
//                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
//                        .build()
//                )
//                .build();

    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequestDto enquiryRequest) {
        //check if the provided  account number exist in the db
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExist) {
            return new BankResponse(AccountUtils.ACCOUNT_NOT_EXIST_CODE, AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE, null);

        }
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return new BankResponse(AccountUtils.ACCOUNT_FOUND_CODE, AccountUtils.ACCOUNT_FOUND_SUCCESS, new AccountInfo(foundUser.getFirstName() + " " + foundUser.getLastName(), foundUser.getAccountBalance(), foundUser.getAccountNumber()));
    }

    @Override
    public String nameEnquiry(EnquiryRequestDto enquiryRequestDto) {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequestDto.getAccountNumber());
        if (!isAccountExist) {
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(enquiryRequestDto.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();

    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        //checking if the account exists
        boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isAccountExists) {
            return new BankResponse(AccountUtils.ACCOUNT_NOT_EXIST_CODE, AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE, null);

        }
        User userCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        userCredit.setAccountBalance(userCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepository.save(userCredit);
        //save transaction
        TransactionDto transactionDto=new TransactionDto();
        transactionDto.setAccountNumber(userCredit.getAccountNumber());
        transactionDto.setTransactionType("CREDIT");
        transactionDto.setAmount(creditDebitRequest.getAmount());
        transactionService.saveTransaction(transactionDto);



        return new BankResponse(AccountUtils.ACCOUNT_CREDITED_SUCCESS, AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE, new AccountInfo(userCredit.getFirstName() + " " + userCredit.getLastName() + " " + userCredit.getOtherName(), userCredit.getAccountBalance(), creditDebitRequest.getAccountNumber()));
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        //check if account exists
        //check if the amount you intend to withdraw is not more than the current amount
        boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isAccountExists) {
            return new BankResponse(AccountUtils.ACCOUNT_NOT_EXIST_CODE, AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE, null);

        }
        User userToDebit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = creditDebitRequest.getAmount().toBigInteger();
        if (availableBalance.intValue() < debitAmount.intValue()) {

            return new BankResponse(AccountUtils.INSUFFICIENT_BALANCE_CODE, AccountUtils.INSUFFICIENT_BALANCE_MESSAGE, null);

        }

        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
            //save transaction
            TransactionDto transactionDto=new TransactionDto();
            transactionDto.setAccountNumber(userToDebit.getAccountNumber());
            transactionDto.setTransactionType("CREDIT");
            transactionDto.setAmount(creditDebitRequest.getAmount());
            transactionService.saveTransaction(transactionDto);

            userRepository.save(userToDebit);
            return new BankResponse(AccountUtils.ACCOUNT_DEBITED_SUCCESS, AccountUtils.ACCOUNT_DEBITED_MESSAGE, new AccountInfo(creditDebitRequest.getAccountNumber(), userToDebit.getAccountBalance(), userToDebit.getFirstName() + " " + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName()));
        }
    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if (!isDestinationAccountExist) {
            return new BankResponse(AccountUtils.ACCOUNT_NOT_EXIST_CODE, AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE, null);
        }

        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        BigDecimal amount = request.getAmount();
        BigDecimal sourceAccountBalance = sourceAccountUser.getAccountBalance();

        if (amount.compareTo(sourceAccountBalance) > 0) {
            return new BankResponse(AccountUtils.INSUFFICIENT_BALANCE_CODE, AccountUtils.INSUFFICIENT_BALANCE_MESSAGE, null);
        }

        // Debit the source account
        sourceAccountUser.setAccountBalance(sourceAccountBalance.subtract(amount));
        userRepository.save(sourceAccountUser);

        // Send debit alert
        String sourceUsername = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName() + " " + sourceAccountUser.getOtherName();
        EmailDetails debitAlert = new EmailDetails(sourceAccountUser.getEmail(), "The sum of " + amount + " has been deducted from your account is " + sourceAccountUser.getAccountBalance(), "DEBIT ALERT");
        emailService.sendEmail(debitAlert);

        // Credit the destination account
        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(amount));
        userRepository.save(destinationAccountUser);

        // Send credit alert
        String destinationUsername = destinationAccountUser.getFirstName() + " " + destinationAccountUser.getLastName() + " " + destinationAccountUser.getOtherName();
        EmailDetails creditAlert = new EmailDetails(destinationAccountUser.getEmail(), "The sum of " + amount + " has been credited to your account from " + sourceUsername + ". Your current balance is " + destinationAccountUser.getAccountBalance(), "CREDIT ALERT");
        emailService.sendEmail(creditAlert);
        //save transaction
        TransactionDto transactionDto=new TransactionDto();
        transactionDto.setAccountNumber(destinationAccountUser.getAccountNumber());
        transactionDto.setTransactionType("CREDIT");
        transactionDto.setAmount(request.getAmount());
        transactionService.saveTransaction(transactionDto);


        return new BankResponse(AccountUtils.TRANSFER_SUCCESSFUL_CODE, AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE, null);
    }

}
