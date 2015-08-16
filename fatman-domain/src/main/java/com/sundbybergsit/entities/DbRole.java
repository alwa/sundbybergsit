package com.sundbybergsit.entities;


/**
 * Purpose: The roles in the Fatman system
 */
public enum DbRole {

    ADMINISTRATOR(0, "role.administrator"),
    USER(1, "role.user");

    /**
     * An int representation of the enum value used to save the value in the database
     */
    private int code;

    /**
     * Localization key
     */
    private String key;

    DbRole(int code, String key) {
        this.code = code;
        this.key = key;
    }

    /**
     * The code (int) representation of the Roles.
     *
     * @return Int value of the Roles.
     */
    public int code() {
        return code;
    }

    /**
     * The localization key used in resource bundle to get the translated name of this role
     *
     * @return the localization key
     */
    public String getKey() {
        return key;
    }

    /**
     * Parses an int value and returns the corresponding title enum.
     *
     * @param code The code to parse
     * @return The Roles enum representing the code.
     * @throws EnumConstantNotPresentException
     *          Thrown if the int value parsed does not correspond to a valid Where.
     */
    public static DbRole parseCode(int code) throws EnumConstantNotPresentException {
        for (DbRole value : values()) {
            if (value.code() == code) {
                return value;
            }
        }
        throw new EnumConstantNotPresentException(DbRole.class, String.valueOf(code));
    }
}
