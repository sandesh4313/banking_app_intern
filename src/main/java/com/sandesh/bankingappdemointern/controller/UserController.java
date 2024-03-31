package com.sandesh.bankingappdemointern.controller;

import com.sandesh.bankingappdemointern.dto.*;
import com.sandesh.bankingappdemointern.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name="User Account Management APIs")
public class UserController {

    private final UserService userService;
    @Operation(summary = "Create New User Account",
    description = "Creating a new user and assigning an account ID")
    @ApiResponse(
            responseCode = "201",
            description="Http status 201 CREATED"
    )
    @PostMapping("/")
    public BankResponse createAccount(@RequestBody UserRequestDto userRequestDto){
        return userService.createAccount(userRequestDto);
    }

    @Operation(summary = "Balance Enquiry",
            description = "Given an account number, check how much the user has")
    @ApiResponse(
            responseCode = "200",
            description="Http status 200 SUCCESS"
    )

    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequestDto enquiryRequestDto){
        return userService.balanceEnquiry(enquiryRequestDto);
    }

    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequestDto enquiryRequestDto){
        return userService.nameEnquiry(enquiryRequestDto);
    }

    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.creditAccount(creditDebitRequest);
    }

    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userService.debitAccount(creditDebitRequest);
    }

    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }




}
