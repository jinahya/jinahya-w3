package io.github.jinahya.w3.css.color;

public final class HwbConstants {

    // -----------------------------------------------------------------------------------------------------------------
    public static final double MIN_HUE = 0.0d;

    public static final double MAX_HUE = 360.0d;

    // -----------------------------------------------------------------------------------------------------------------
    public static final double MIN_WHITENESS = 0;

    public static final double MAX_WHITENESS = 100;

    public static final String DECIMAL_MIN_NORMALIZED_WHITENESS = "0.0";

    public static final String DECIMAL_MAX_NORMALIZED_WHITENESS = "1.0";

    public static final double MIN_NORMALIZED_WHITENESS = 0.0d;

    public static final double MAX_NORMALIZED_WHITENESS = 1.0;

    // -----------------------------------------------------------------------------------------------------------------
    public static final double MIN_BLACKNESS = 0;

    public static final double MAX_BLACKNESS = 100;

    public static final String DECIMAL_MIN_NORMALIZED_BLACKNESS = "0.0";

    public static final String DECIMAL_MAX_NORMALIZED_BLACKNESS = "1.0";

    public static final double MIN_NORMALIZED_BLACKNESS = 0.0d;

    public static final double MAX_NORMALIZED_BLACKNESS = 1.0;

    // -----------------------------------------------------------------------------------------------------------------
    public static final double MIN_ALPHA = 0;

    public static final double MAX_ALPHA = 100;

    public static final String DECIMAL_MIN_NORMALIZED_ALPHA = "0.0";

    public static final String DECIMAL_MAX_NORMALIZED_ALPHA = "1.0";

    public static final double MIN_NORMALIZED_ALPHA = 0.0d;

    public static final double MAX_NORMALIZED_ALPHA = 1.0;

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    private HwbConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
