package lt.vilniustech.bps.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TicketPriceUtil {
    private static final NumberFormat priceFormat = new DecimalFormat("#0.00");

    public static String formatPrice(BigDecimal price) {
        return priceFormat.format(roundPrice(price));
    }

    public static BigDecimal roundPrice(BigDecimal price) {
        return price.setScale(2, RoundingMode.UP);
    }

    public static BigDecimal calculateLowerTicketPrice(BigDecimal fullTicketPrice) {
        // Lower price ticket is 50% cheaper
        return fullTicketPrice.divide(BigDecimal.valueOf(2), 2, RoundingMode.UP);
    }
}
