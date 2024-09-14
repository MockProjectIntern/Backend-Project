package com.sapo.mock_project.inventory_receipt.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapo.mock_project.inventory_receipt.entities.Supplier;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CommonUtils {

    public static Map<String, Boolean> getFilterParamsFromCookie(String name, HttpServletRequest request) {
        // Lấy tất cả các cookie từ request
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            // Duyệt qua mảng các cookie
            for (Cookie cookie : cookies) {
                // Kiểm tra nếu cookie có tên là "filter_suppliers"
                if (cookie.getName().equals(name)) {
                    // Lấy giá trị của cookie name
                    String cookieValue = cookie.getValue();

                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        // Chuyển giá trị của cookie thành Map
                        Map<String, Boolean> filterParams = objectMapper.readValue(cookieValue, Map.class);

                        return filterParams;
                    } catch (Exception e) {
                        // Xử lý ngoại lệ (ví dụ: lỗi JSON không hợp lệ)
                        e.printStackTrace();
                        return new HashMap<>(); // Trả về map rỗng nếu có lỗi
                    }
                }
            }
        }

        // Trả về map rỗng nếu không tìm thấy cookie hoặc không có cookie nào
        return new HashMap<>();
    }

    public static Object getFieldValue(Object object, String field) {
        try {
            // Lấy phương thức get tương ứng từ đối tượng supplier
            Method getMethod = object.getClass().getMethod("get" + StringUtils.snakeCaseToCamelCase(field));

            // Gọi phương thức get để lấy giá trị của trường
            return getMethod.invoke(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get value for field " + field, e);
        }
    }
}
