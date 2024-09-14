package com.sapo.mock_project.inventory_receipt.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtils {
    public static LocalDateTime getDateTime(LocalDate date) {
        return date != null ? date.atStartOfDay() : null;
    }
}
