package io.github.jinahya.w3.css.color;

import java.nio.DoubleBuffer;
import java.util.Objects;
import java.util.function.DoubleFunction;

public final class HwbUtils {

    private static void checkHwbToRgbArguments(final double hue, final double whiteness, final double blackness) {
        if (hue < HwbConstants.MIN_HUE || hue > HwbConstants.MAX_HUE) {
            throw new IllegalArgumentException("hue: " + hue);
        }
        if (whiteness < HwbConstants.MIN_WHITENESS || whiteness > HwbConstants.MAX_WHITENESS) {
            throw new IllegalArgumentException("whiteness: " + whiteness);
        }
        if (blackness < HwbConstants.MIN_BLACKNESS || blackness > HwbConstants.MAX_BLACKNESS) {
            throw new IllegalArgumentException("blackness: " + blackness);
        }
    }

    /**
     * Returns an array of RGB color components, converted from specified HSL color components.
     * <p>
     * {@snippet lang = "java":
     * var rgb = hslToRgb(
     *         hue,       // [0..360]
     *         whiteness, // [0..100]
     *         blackness  // [0..100]
     * );
     * final var r = rgb[0];
     * final var g = rgb[1];
     * final var b = rgb[2];
     * assert r >= 0.0d && r <= 1.0d;
     * assert g >= 0.0d && g <= 1.0d;
     * assert b >= 0.0d && b <= 1.0d;
     *}
     *
     * @param hue       a value of {@code hue} between {@value HwbConstants#MIN_HUE} and {@value HwbConstants#MAX_HUE},
     *                  both inclusive.
     * @param whiteness a value of {@code whiteness}, in percent, between {@value HwbConstants#MIN_WHITENESS} and
     *                  {@value HwbConstants#MAX_WHITENESS}, both inclusive.
     * @param blackness a value of {@code blackness}, in percent, between {@value HwbConstants#MIN_BLACKNESS} and
     *                  {@value HwbConstants#MAX_BLACKNESS}, both inclusive.
     * @return an array of RGB color components, which each is between {@value RgbConstants#MIN_NORMALIZED_COMPONENT}
     * and {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @see #hwbToRgb(double, double, double, DoubleBuffer)
     * @see <a href="https://www.w3.org/TR/css-color-3/#hsl-color">4.2.4. HSL color values</a> (CSS Color Module Level
     * 3, W3C Recommendation, 18 January 2022)
     */
    // https://www.w3.org/TR/css-color-4/#hwb-to-rgb
    public static double[] hwbToRgb(final double hue, final double whiteness, final double blackness) {
        checkHwbToRgbArguments(hue, whiteness, blackness);
        final var white = whiteness / HwbConstants.MAX_WHITENESS;
        final var black = blackness / HwbConstants.MAX_BLACKNESS;
        if ((white + black) >= 1.0d) {
            final var grey = white / (white + black);
            return new double[] {grey, grey, grey};
        }
        final var rgb = HslUtils.hslToRgb(
                hue,
                100, // whiteness
                50  // blackness
        );
        for (int i = 0; i < rgb.length; i++) {
            rgb[i] *= (1 - white - black);
            rgb[i] += white;
        }
        return rgb;
    }

    // https://www.w3.org/TR/css-color-4/#hwb-to-rgb
    @SuppressWarnings({"unchecked"})
    public static <T extends DoubleBuffer> T hwbToRgb(final double hue, final double whiteness, final double blackness,
                                                      final T buffer) {
        checkHwbToRgbArguments(hue, whiteness, blackness);
        Objects.requireNonNull(buffer, "buffer is null");
        final var array = hwbToRgb(hue, whiteness, blackness);
        return (T) buffer.put(array);
    }

    public static <R> R hwbToRgb(final double hue, final double whiteness, final double blackness,
                                 final DoubleFunction< // r
                                         ? extends DoubleFunction< // g
                                                 ? extends DoubleFunction< // b
                                                         ? extends R>>> function) {
        checkHwbToRgbArguments(hue, whiteness, blackness);
        Objects.requireNonNull(function, "function is null");
        final var array = hwbToRgb(hue, whiteness, blackness);
        return function
                .apply(array[0])
                .apply(array[1])
                .apply(array[2])
                ;
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS
    private HwbUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
