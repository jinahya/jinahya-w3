package io.github.jinahya.w3.css.color;

import java.util.Objects;
import java.util.function.DoubleFunction;

public final class HslUtils {

    // https://www.w3.org/TR/css-color-3/#hsl-color
    private static double f(final int n, final double h, final double s, final double l) {
        final double k = (n + h / 30) % 12;
        return l - (s * Math.min(l, 1 - l)) * Math.clamp(Math.min(k - 3, 9 - k), -1, 1);
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
     *             assert r >= 0.0d && r <= 1.0d;
     *             assert g >= 0.0d && g <= 1.0d;
     *             assert b >= 0.0d && b <= 1.0d;
     *             final var   red = (int) Math.round(r * 255);
     *             final var green = (int) Math.round(g * 255);
     *             final var  blue = (int) Math.round(b * 255);
     *             assert   red >= 0 &&   red <= 255;
     *             assert green >= 0 && green <= 255;
     *             assert  blue >= 0 &&  blue <= 255;
     *             return null;
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
     * @see <a href="https://www.w3.org/TR/css-color-3/#hsl-color">4.2.4. HSL color values</a> (CSS Color Module Level
     * 3, W3C Recommendation, 18 January 2022)
     */
    // https://www.w3.org/TR/css-color-3/#hsl-color
    // https://www.rapidtables.com/convert/color/hsl-to-rgb.html
    // https://colordesigner.io/convert/hsltorgb
    public static <R> R hslToRgb(
            final double hue, final double saturation, final double lightness,
            final DoubleFunction<? extends DoubleFunction<? extends DoubleFunction<? extends R>>> function) {
        if (hue < HslConstants.MIN_HUE || hue > HslConstants.MAX_HUE) {
            throw new IllegalArgumentException("hue is out of range: " + hue);
        }
        if (saturation < HslConstants.MIN_SATURATION || saturation > HslConstants.MAX_SATURATION) {
            throw new IllegalArgumentException("saturation is out of range: " + saturation);
        }
        if (lightness < HslConstants.MIN_LIGHTNESS || lightness > HslConstants.MAX_LIGHTNESS) {
            throw new IllegalArgumentException("lightness is out of range: " + lightness);
        }
        Objects.requireNonNull(function, "function is null");
        final var h = hue % HslConstants.MAX_HUE;
        final var s = saturation / HslConstants.MAX_SATURATION;
        final var l = lightness / HslConstants.MAX_LIGHTNESS;
        final var r = f(0, h, s, l);
        final var g = f(8, h, s, l);
        final var b = f(4, h, s, l);
        assert r >= RgbConstants.MIN_NORMALIZED_COMPONENT;
        assert r <= RgbConstants.MAX_NORMALIZED_COMPONENT;
        assert g >= RgbConstants.MIN_NORMALIZED_COMPONENT;
        assert g <= RgbConstants.MAX_NORMALIZED_COMPONENT;
        assert b >= RgbConstants.MIN_NORMALIZED_COMPONENT;
        assert b <= RgbConstants.MAX_NORMALIZED_COMPONENT;
        return function.apply(r).apply(g).apply(b);
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS
    private HslUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
