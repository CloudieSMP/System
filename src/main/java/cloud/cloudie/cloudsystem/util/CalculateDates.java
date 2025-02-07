package cloud.cloudie.cloudsystem.util;

import java.util.Calendar;
import java.util.Date;

public class CalculateDates {

    public static Date calculateDate(String args) {
        long currentTime = System.currentTimeMillis();
        int durationValue = Integer.parseInt(args.replaceAll("[^0-9]", ""));
        char unit = args.charAt(args.length() - 1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);

        switch (unit) {
            case 's':
                calendar.add(Calendar.SECOND, durationValue);
                break;
            case 'm':
                calendar.add(Calendar.MINUTE, durationValue);
                break;
            default:
                return null; // Invalid unit
        }
        return calendar.getTime();
    }

    public static boolean isValidDuration(String duration) {
        // Regular expression to match valid duration format (e.g., 1d, 3M, etc.)
        String durationPattern = "\\d+[smhdwMy]";
        return duration.matches(durationPattern);
    }

}