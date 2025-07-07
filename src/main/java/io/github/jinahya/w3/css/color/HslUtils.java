package io.github.jinahya.w3.css.color;

import java.nio.DoubleBuffer;
import java.util.Objects;
import java.util.function.DoubleFunction;

public final class HslUtils {

    private static void checkHslToRgbArguments(final double hue, final double saturation, final double lightness) {
        if (hue < HslConstants.MIN_HUE || hue > HslConstants.MAX_HUE) {
            throw new IllegalArgumentException("hue is out of range: " + hue);
        }
        if (saturation < HslConstants.MIN_SATURATION || saturation > HslConstants.MAX_SATURATION) {
            throw new IllegalArgumentException("saturation is out of range: " + saturation);
        }
        if (lightness < HslConstants.MIN_LIGHTNESS || lightness > HslConstants.MAX_LIGHTNESS) {
            throw new IllegalArgumentException("lightness is out of range: " + lightness);
        }
    }

    // https://www.w3.org/TR/css-color-3/#hsl-color
    private static double hslToRgbComponent(final int n, final double h, final double s, final double l) {
        final double k = (n + h / 30) % 12;
        return l - (s * Math.min(l, 1 - l)) * Math.clamp(Math.min(k - 3, 9 - k), -1, 1);
    }

    /**
     * Returns an array of RGB color components, converted from specified HSL color components.
     * <p>
     * {@snippet lang = "java":
     * var rgb = hslToRgb(
     *         hue,        // [0..360]
     *         saturation, // [0..100]
     *         lightness   // [0..100]
     * );
     * final var r = rgb[0];
     * final var g = rgb[1];
     * final var b = rgb[2];
     * assert r >= 0.0d && r <= 1.0d;
     * assert g >= 0.0d && g <= 1.0d;
     * assert b >= 0.0d && b <= 1.0d;
     *}
     *
     * @param hue        a value of {@code hue} between {@value HslConstants#MIN_HUE} and {@value HslConstants#MAX_HUE},
     *                   both inclusive.
     * @param saturation a value of {@code saturation}, in percent, between {@value HslConstants#MIN_SATURATION} and
     *                   {@value HslConstants#MAX_SATURATION}, both inclusive.
     * @param lightness  a value of {@code lightness}, in percent, between {@value HslConstants#MIN_LIGHTNESS} and
     *                   {@value HslConstants#MAX_LIGHTNESS}, both inclusive.
     * @return an array of RGB color components, which each is between {@value RgbConstants#MIN_NORMALIZED_COMPONENT}
     * and {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @see #hslToRgb(double, double, double, DoubleBuffer)
     * @see <a href="https://www.w3.org/TR/css-color-3/#hsl-color">4.2.4. HSL color values</a> (CSS Color Module Level
     * 3, W3C Recommendation, 18 January 2022)
     */
    // https://www.w3.org/TR/css-color-3/#hsl-color
    // https://www.rapidtables.com/convert/color/hsl-to-rgb.html
    // https://colordesigner.io/convert/hsltorgb
    public static double[] hslToRgb(final double hue, final double saturation, final double lightness) {
        checkHslToRgbArguments(hue, saturation, lightness);
        final var h = hue % HslConstants.MAX_HUE;
        final var s = saturation / HslConstants.MAX_SATURATION;
        final var l = lightness / HslConstants.MAX_LIGHTNESS;
        final var r = hslToRgbComponent(0, h, s, l);
        final var g = hslToRgbComponent(8, h, s, l);
        final var b = hslToRgbComponent(4, h, s, l);
        assert r >= RgbConstants.MIN_NORMALIZED_COMPONENT;
        assert r <= RgbConstants.MAX_NORMALIZED_COMPONENT;
        assert g >= RgbConstants.MIN_NORMALIZED_COMPONENT;
        assert g <= RgbConstants.MAX_NORMALIZED_COMPONENT;
        assert b >= RgbConstants.MIN_NORMALIZED_COMPONENT;
        assert b <= RgbConstants.MAX_NORMALIZED_COMPONENT;
        return new double[] {r, g, b};
    }

    /**
     * Puts RGB color components, converted from specified HSL color components, to the specified buffer, and return the
     * buffer.
     * <p>
     * {@snippet lang = "java":
     * final var buffer = hslToRgb(
     *         hue,        // [0..360]
     *         saturation, // [0..100]
     *         lightness,  // [0..100]
     *         DoubleBuffer.allocate(3)
     * );
     * final var r = buffer.get(0);
     * final var g = buffer.get(1);
     * final var b = buffer.get(2);
     * assert r >= 0.0d && r <= 1.0d;
     * assert g >= 0.0d && g <= 1.0d;
     * assert b >= 0.0d && b <= 1.0d;
     *}
     *
     * @param hue        a value of {@code hue} between {@value HslConstants#MIN_HUE} and {@value HslConstants#MAX_HUE},
     *                   both inclusive.
     * @param saturation a value of {@code saturation}, in percent, between {@value HslConstants#MIN_SATURATION} and
     *                   {@value HslConstants#MAX_SATURATION}, both inclusive.
     * @param lightness  a value of {@code lightness}, in percent, between {@value HslConstants#MIN_LIGHTNESS} and
     *                   {@value HslConstants#MAX_LIGHTNESS}, both inclusive.
     * @param buffer     the buffer to be put with {@code red}, {@code green}, and {@code blue}, which each is between
     *                   {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *                   {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @return given {@code buffer}.
     * @see <a href="https://www.w3.org/TR/css-color-3/#hsl-color">4.2.4. HSL color values</a> (CSS Color Module Level
     * 3, W3C Recommendation, 18 January 2022)
     */
    @SuppressWarnings({"unchecked"})
    // https://www.w3.org/TR/css-color-3/#hsl-color
    // https://www.rapidtables.com/convert/color/hsl-to-rgb.html
    // https://colordesigner.io/convert/hsltorgb
    public static <T extends DoubleBuffer> T hslToRgb(final double hue, final double saturation, final double lightness,
                                                      final T buffer) {
        checkHslToRgbArguments(hue, saturation, lightness);
        Objects.requireNonNull(buffer, "buffer is null");
        return (T) buffer.put(hslToRgb(hue, saturation, lightness));
    }

    /**
     * Applies RGB color components, converted from specified HSL color components, to the specified function, and
     * returns the result.
     * <p>
     * {@snippet lang = "java":
     * hslToRgb(
     *         hue,        // [0..360]
     *         saturation, // [0..100]
     *         lightness,  // [0..100]
     *         r -> g -> b -> {
     *                 assert r >= 0.0d && r <= 1.0d;
     *                 assert g >= 0.0d && g <= 1.0d;
     *                 assert b >= 0.0d && b <= 1.0d;
     *                 return null;
     *         }
     * );
     *}
     *
     * @param hue        a value of {@code hue} between {@value HslConstants#MIN_HUE} and {@value HslConstants#MAX_HUE},
     *                   both inclusive.
     * @param saturation a value of {@code saturation}, in percent, between {@value HslConstants#MIN_SATURATION} and
     *                   {@value HslConstants#MAX_SATURATION}, both inclusive.
     * @param lightness  a value of {@code lightness}, in percent, between {@value HslConstants#MIN_LIGHTNESS} and
     *                   {@value HslConstants#MAX_LIGHTNESS}, both inclusive.
     * @param function   the function to be applied, in currying, with {@code red}, {@code green}, and {@code blue},
     *                   which each is between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *                   {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param <R>        result type parameter
     * @return the result of the {@code function}.
     * @see #hslToRgb(double, double, double, DoubleBuffer)
     * @see <a href="https://www.w3.org/TR/css-color-3/#hsl-color">4.2.4. HSL color values</a> (CSS Color Module Level
     * 3, W3C Recommendation, 18 January 2022)
     */
    // https://www.w3.org/TR/css-color-3/#hsl-color
    // https://www.rapidtables.com/convert/color/hsl-to-rgb.html
    // https://colordesigner.io/convert/hsltorgb
    public static <R> R hslToRgb(final double hue, final double saturation, final double lightness,
                                 final DoubleFunction< // r
                                         ? extends DoubleFunction< // g
                                                 ? extends DoubleFunction< // b
                                                         ? extends R>>> function) {
        checkHslToRgbArguments(hue, saturation, lightness);
        Objects.requireNonNull(function, "function is null");
        final var array = hslToRgb(hue, saturation, lightness);
        return function
                .apply(array[0])
                .apply(array[1])
                .apply(array[2])
                ;
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS
    private HslUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
