package io.github.jinahya.w3.css.color;

public final class RgbConstants {

    // -----------------------------------------------------------------------------------------------------------------
    public static final double MIN_COMPONENT = 0.0d;

    public static final double MAX_COMPONENT = 255.0d;

    public static final String DECIMAL_MIN_NORMALIZED_COMPONENT = "0.0";

    public static final String DECIMAL_MAX_NORMALIZED_COMPONENT = "1.0";

    public static final double MIN_NORMALIZED_COMPONENT = 0.0d;

    public static final double MAX_NORMALIZED_COMPONENT = 1.0d;

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS
    private RgbConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
