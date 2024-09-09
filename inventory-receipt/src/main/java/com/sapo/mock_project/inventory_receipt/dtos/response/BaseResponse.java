package com.sapo.mock_project.inventory_receipt.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.DateTimePattern;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Lớp cơ sở đại diện cho đối tượng phản hồi cơ bản bao gồm các dấu thời gian
 * khi đối tượng được tạo ra và cập nhật lần cuối.
 */
@Setter
public class BaseResponse {

    /**
     * Dấu thời gian khi đối tượng được tạo ra.
     * Được định dạng dưới dạng chuỗi theo mẫu 'yyyyMMddHHmmss'.
     */
    @JsonProperty("created_at")
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    /**
     * Dấu thời gian khi đối tượng được cập nhật lần cuối.
     * Được định dạng dưới dạng chuỗi theo mẫu 'yyyyMMddHHmmss'.
     */
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
