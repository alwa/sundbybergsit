package com.sundbybergsit.objects;

/**
 * TODO: Make use of this class
 */
public class Role {

    public enum Roles {
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

        Roles(int code, String key) {
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
        public static Roles parseCode(int code) throws EnumConstantNotPresentException {
            for (Roles value : values()) {
                if (value.code() == code) {
                    return value;
                }
            }
            throw new EnumConstantNotPresentException(Roles.class, String.valueOf(code));
        }

    }

    /**
     * Which of the pre-defined roles this role represents
     */
    private Roles role;

    @SuppressWarnings({"UnusedDeclaration"})
    Role() {
    }

    /**
     * Creates a new role.
     *
     * @param role The role to use from the Roles enumeration.
     */
    public Role(Roles role) {
        super();
        this.role = role;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

}
