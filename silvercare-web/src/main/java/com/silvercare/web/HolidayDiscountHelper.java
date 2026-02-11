package com.silvercare.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

public class HolidayDiscountHelper {

    /**
     * Checks if today is a holiday and returns discount percentage.
     * @return Discount percentage (e.g., 0.10 for 10%).
     */
    public static BigDecimal getDiscount() {
        LocalDate today = LocalDate.now();
        Month month = today.getMonth();
        int day = today.getDayOfMonth();

        // Christmas
        if (month == Month.DECEMBER && day == 25) {
            return new BigDecimal("0.15"); // 15% off
        }
        
        // New Year
        if (month == Month.JANUARY && day == 1) {
            return new BigDecimal("0.10"); // 10% off
        }

        // National Day (Example: Aug 9 for Singapore)
        if (month == Month.AUGUST && day == 9) {
            return new BigDecimal("0.20"); // 20% off
        }

        return BigDecimal.ZERO;
    }
}
