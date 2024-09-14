package com.sapo.mock_project.inventory_receipt.services.user;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.JwtTokenUtil;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.dtos.request.user.ChangePasswordRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.user.LoginAccountRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.user.RefreshTokenRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.user.RegisterAccountRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.user.LoginResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.user.UserDetailResponse;
import com.sapo.mock_project.inventory_receipt.entities.User;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.exceptions.ExpiredTokenException;
import com.sapo.mock_project.inventory_receipt.mappers.UserMapper;
import com.sapo.mock_project.inventory_receipt.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service xử lý các chức năng liên quan đến người dùng như đăng ký, đăng nhập, làm mới token và thay đổi mật khẩu.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;

    /**
     * Tạo tài khoản mới cho người dùng.
     *
     * @param request đối tượng chứa thông tin đăng ký tài khoản.
     * @return ResponseEntity chứa kết quả thực hiện thao tác.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> createAccount(RegisterAccountRequest request) {
        try {
            if (userRepository.existsByPhone(request.getPhone())) {
                // Nếu số điện thoại đã tồn tại, trả về lỗi xác thực
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.USER_USERNAME_EXISTED));
            }

            User newAccount = userMapper.mapToEntity(request);
            newAccount.setPassword(passwordEncoder.encode(request.getPassword())); // Mã hóa mật khẩu trước khi lưu
            newAccount.setActive(true);

            userRepository.save(newAccount);

            // Trả về phản hồi thành công với mã trạng thái 201
            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Đăng nhập người dùng và trả về token.
     *
     * @param request đối tượng chứa thông tin đăng nhập của người dùng.
     * @return ResponseEntity chứa thông tin đăng nhập và token.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> loginAccount(LoginAccountRequest request) {
        try {
            Optional<User> existingUserOptional = userRepository.findByPhone(request.getPhone());
            if (existingUserOptional.isEmpty()) {
                throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_NOT_FOUND_BY_PHONE));
            }

            User existingUser = existingUserOptional.get();

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword());
            authenticationManager.authenticate(authenticationToken);

            String token = jwtTokenUtil.generateToken(existingUser);
            String refreshToken = jwtTokenUtil.generateRefreshToken(existingUser);

            LoginResponse loginResponse = LoginResponse.builder()
                    .userId(existingUser.getId())
                    .fullName(existingUser.getFullName())
                    .phone(existingUser.getPhone())
                    .token(token)
                    .refreshToken(refreshToken)
                    .tokenType(jwtTokenUtil.getTokenType())
                    .role(existingUser.getRole())
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_SUCCESSFULLY), loginResponse);
        } catch (BadCredentialsException e) {
            // Xử lý lỗi đăng nhập, ví dụ như mật khẩu sai
            return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_WRONG_PHONE_OR_PASSWORD));
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Lấy thông tin chi tiết người dùng từ token hiện tại.
     *
     * @return ResponseEntity chứa thông tin chi tiết người dùng.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> getUserDetailByToken() {
        User user = authHelper.getUser();

        UserDetailResponse userDetail = userMapper.mapToResponse(user);

        return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_GET_DETAIL_SUCCESSFULLY), userDetail);
    }

    /**
     * Làm mới token cho người dùng.
     *
     * @param request đối tượng chứa refresh token của người dùng.
     * @return ResponseEntity chứa token mới hoặc thông báo lỗi.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> refreshToken(RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();

            final String id = jwtTokenUtil.extractId(refreshToken);
            if (id != null) {
                Optional<User> userDetails = userRepository.findById(id);
                if (userDetails.isEmpty() || !jwtTokenUtil.validateToken(refreshToken)) {
                    throw new ExpiredTokenException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_TOKEN_EXPIRED));
                }

                String token = jwtTokenUtil.generateToken(userDetails.get());
                String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails.get());

                LoginResponse loginResponse = LoginResponse.builder()
                        .userId(userDetails.get().getId())
                        .fullName(userDetails.get().getFullName())
                        .phone(userDetails.get().getPhone())
                        .token(token)
                        .refreshToken(newRefreshToken)
                        .tokenType(jwtTokenUtil.getTokenType())
                        .role(userDetails.get().getRole())
                        .build();

                return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_REFRESH_TOKEN_SUCCESSFULLY), loginResponse);
            }

            return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_TOKEN_INVALID));
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Thay đổi mật khẩu của người dùng hiện tại.
     *
     * @param request đối tượng chứa thông tin yêu cầu thay đổi mật khẩu.
     * @return ResponseEntity chứa thông báo kết quả của việc thay đổi mật khẩu.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> changePasswordAccount(ChangePasswordRequest request) {
        try {
            User existingUser = authHelper.getUser();

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(existingUser.getUsername(),
                    request.getOldPassword());
            authenticationManager.authenticate(authenticationToken);

            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                // Kiểm tra mật khẩu mới và xác nhận mật khẩu không khớp
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.USER_PASSWORD_NOT_MATCH));
            }

            existingUser.setPassword(passwordEncoder.encode(request.getNewPassword()));

            userRepository.save(existingUser);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_CHANGE_PASSWORD_SUCCESSFULLY));
        } catch (BadCredentialsException e) {
            // Xử lý lỗi khi mật khẩu cũ không đúng
            return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_OLD_PASSWORD_INCORRECT));
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}
