package moe.sebiann.system.enums;

public enum Severity {
    SUCCESS("#83c916"),  // lime
    WARNING("#f09c0b"),  // orange
    ERROR("#d82625"),    // red
    INFO("#999999");     // gray

    private final String hexColor;

    Severity(String hexColor) {
        this.hexColor = hexColor;
    }

    public String getHexColor() {
        return hexColor;
    }

    public static boolean severityCheck(Severity severity){

        boolean severityExists = false;
        for(Severity severity2 : Severity.values()) {
            if (severity2.name().equalsIgnoreCase(severity.name())) {
                severityExists = true;
                break;
            }
        }
        return severityExists;
    }
}