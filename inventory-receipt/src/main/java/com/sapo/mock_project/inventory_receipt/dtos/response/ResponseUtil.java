package com.sapo.mock_project.inventory_receipt.dtos.response;

import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    private ResponseUtil() {
    }

    public static ResponseEntity<ResponseObject<Object>> success200Response(String message, Object data) {
        return ResponseEntity
                .ok()
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK.getReasonPhrase())
                        .message(message)
                        .data(data)
                        .build());
    }

    public static ResponseEntity<ResponseObject<Object>> success201Response(String message) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED.getReasonPhrase())
                        .message(message)
                        .build());
    }

    public static ResponseEntity<ResponseObject<Object>> errorValidationResponse(String message) {
        Map<String, String> messageMap = new HashMap<>();
        String[] splittedMessage = message.split(", ");

        for (String msg : splittedMessage) {
            String[] splittedMsg = msg.split(": ");
            messageMap.put(splittedMsg[0], splittedMsg[1]);
        }

        return ResponseEntity
                .badRequest()
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .status(MessageValidateKeys.ERROR_VALIDATION)
                        .message(message)
                        .data(messageMap)
                        .build());
    }

    public static ResponseEntity<ResponseObject<Object>> error400Response(String message) {
        return ResponseEntity
                .badRequest()
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message(message)
                        .build());
    }

    public static ResponseEntity<ResponseObject<Object>> error401Response(String message) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .status(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                        .message(message)
                        .build());
    }

    public static ResponseEntity<ResponseObject<Object>> error404Response(String message) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .message(message)
                        .build());
    }

    public static ResponseEntity<ResponseObject<Object>> error500Response(String message) {
        return ResponseEntity
                .internalServerError()
                .body(ResponseObject.<Object>builder()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .message(message)
                        .build());
    }
}
