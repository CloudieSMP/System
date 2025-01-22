package moe.sebiann.system.util;

public class HexToRGB {
    public HexToRGB() {
    }

    public static int[] hexToRgb(String hex) {
        int[] rgb = new int[3];
        if (hex.length() == 7 && hex.charAt(0) == '#') {
            try {
                rgb[0] = Integer.parseInt(hex.substring(1, 3), 16);
                rgb[1] = Integer.parseInt(hex.substring(3, 5), 16);
                rgb[2] = Integer.parseInt(hex.substring(5, 7), 16);
            } catch (NumberFormatException var3) {
                var3.printStackTrace();
            }

            return rgb;
        } else if (hex.length() == 6) {
            try {
                rgb[0] = Integer.parseInt(hex.substring(0, 2), 16);
                rgb[1] = Integer.parseInt(hex.substring(2, 4), 16);
                rgb[2] = Integer.parseInt(hex.substring(4, 6), 16);
            } catch (NumberFormatException var3) {
                var3.printStackTrace();
            }

            return rgb;
        } else {
            throw new IllegalArgumentException("Invalid HEX format. It should be in the format #RRGGBB.");
        }
    }
}