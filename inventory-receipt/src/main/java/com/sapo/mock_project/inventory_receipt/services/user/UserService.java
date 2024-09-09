package com.sapo.mock_project.inventory_receipt.services.user;

import com.sapo.mock_project.inventory_receipt.dtos.request.user.ChangePasswordRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.user.LoginAccountRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.user.RefreshTokenRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.user.RegisterAccountRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ResponseObject<Object>> createAccount(RegisterAccountRequest request);

    ResponseEntity<ResponseObject<Object>> loginAccount(LoginAccountRequest request);

    ResponseEntity<ResponseObject<Object>> getUserDetailByToken();

    ResponseEntity<ResponseObject<Object>> refreshToken(RefreshTokenRequest request);

    ResponseEntity<ResponseObject<Object>> changePasswordAccount(ChangePasswordRequest request);
}
