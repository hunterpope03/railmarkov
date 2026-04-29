package railmarkov.domain; 


public enum State {
    ACQUIRED("A"),
    RETIRED("R"),
    TO_YARD("TY"),
    SWITCHING("SW"),
    AT_YARD("AY"),
    TO_STORAGE("TYS"),
    IN_STORAGE("IYS"),
    FROM_STORAGE("FYS"),
    UNDER_INSPECTION("UI"),
    BAD_ORDER("BO"),
    UNDER_REPAIR("UR"),
    TO_INDUSTRY("TI"),
    AT_INDUSTRY("AI"),
    TO_SPUR("TIS"),
    AT_SPUR("AIS"),
    FROM_SPUR("FIS"),
    LOADING("L"),
    UNLOADING("U");

    private final String code;

    State(String code) {
        this.code = code;
    }

    public String getCode() { 
        return this.code; 
    }

    public static State fromCode(String code) {
        for (State s : values()) {
            if (s.code.equals(code)) return s;
        }
        throw new IllegalArgumentException("unknown 'railmarkov.model.State' code: " + code);
    }
}