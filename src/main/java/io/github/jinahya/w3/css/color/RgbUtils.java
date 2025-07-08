package io.github.jinahya.w3.css.color;

import java.nio.DoubleBuffer;
import java.util.Objects;
import java.util.function.DoubleFunction;

public final class RgbUtils {

    private static void checkRgbToHslArguments(final double red, final double green, final double blue) {
        if (red < RgbConstants.MIN_NORMALIZED_COMPONENT || red > RgbConstants.MAX_NORMALIZED_COMPONENT) {
            throw new IllegalArgumentException("red: " + red);
        }
        if (green < RgbConstants.MIN_NORMALIZED_COMPONENT || green > RgbConstants.MAX_NORMALIZED_COMPONENT) {
            throw new IllegalArgumentException("green: " + green);
        }
        if (blue < RgbConstants.MIN_NORMALIZED_COMPONENT || blue > RgbConstants.MAX_NORMALIZED_COMPONENT) {
            throw new IllegalArgumentException("blue: " + blue);
        }
    }

    /**
     * Put HSL color components, converted from specified RGB color components, to the specified buffer, and returns the
     * buffer.
     *
     * @param red   a value of {@code red} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *              {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param green a value of {@code green} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *              {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param blue  a value of {@code blue} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *              {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @return an array of {@code hue}({@code [0..360]}, {@code whiteness}({@code [0..100]}, and
     * {@code blackness}{@code [0..100]}.
     */
    public static double[] rgbToHsl(final double red, final double green, final double blue) {
        checkRgbToHslArguments(red, green, blue);
        final var max = Math.max(Math.max(red, green), blue);
        final var min = Math.min(Math.min(red, green), blue);
        var h = Double.NaN;
        var s = .0d;
        var l = (min + max) / 2;
        final var d = max - min;
        final var epsilon = 1 / 100000.0d;
        if (d != 0) {
            s = (l == 0 || l == 1) ? 0 : (max - l) / Math.min(l, 1 - l);
            if (max == red) {
                h = (green - blue) / d + (green < blue ? 6 : 0);
            } else if (max == green) {
                h = (blue - red) / d + 2;
            } else {
                h = (red - green) / d + 4;
            }
            h = h * 60;
        }
        if (s < 0) {
            h += 180;
            s = Math.abs(s);
        }
        if (h >= 360) {
            h -= 360;
        }
        if (s <= epsilon) {
            h = Double.NaN;
        }
        return new double[] {h, s * 100, l * 100};
    }

    /**
     * Puts HSL color components, converted from specified RGB color components, to the specified buffer, and returns
     * the buffer.
     *
     * @param red    a value of {@code red} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *               {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param green  a value of {@code green} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *               {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param blue   a value of {@code blue} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *               {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param buffer the function.
     * @return given {@code buffer} put with {@code hue}({@code [0..360]}, {@code whiteness}({@code [0..100]}, and
     * {@code blackness}{@code [0..100]}.
     * @see DoubleBuffer#put(double)
     */
    @SuppressWarnings("unchecked")
    public static <T extends DoubleBuffer> T rgbToHsl(final double red, final double green, final double blue,
                                                      final T buffer) {
        checkRgbToHslArguments(red, green, blue);
        Objects.requireNonNull(buffer, "buffer is null");
        return (T) buffer.put(rgbToHsl(red, green, blue));
    }

    /**
     * Applies HSL color components, converted from specified RGB color components, to the specified function, and
     * returns the result.
     *
     * @param red      a value of {@code red} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *                 {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param green    a value of {@code green} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *                 {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param blue     a value of {@code blue} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *                 {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param function the function.
     * @param <R>      result type parameter
     * @return the result of the {@code function} applied, in currying, with {@code hue}({@code [0..360]},
     * {@code whiteness}({@code [0..100]}, and {@code blackness}{@code [0..100]}.
     */
    // https://www.w3.org/TR/css-color-4/#rgb-to-hsl
    public static <R> R rgbToHsl(final double red, final double green, final double blue,
                                 final DoubleFunction< // h
                                         ? extends DoubleFunction< // w
                                                 ? extends DoubleFunction< // b
                                                         ? extends R>>> function) {
        checkRgbToHslArguments(red, green, blue);
        Objects.requireNonNull(function, "function is null");
        final var hsl = rgbToHsl(red, green, blue);
        return function
                .apply(hsl[0])
                .apply(hsl[1])
                .apply(hsl[2])
                ;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static void checkRgbToHwbArguments(final double red, final double green, final double blue) {
        if (red < RgbConstants.MIN_NORMALIZED_COMPONENT || red > RgbConstants.MAX_NORMALIZED_COMPONENT) {
            throw new IllegalArgumentException("red: " + red);
        }
        if (green < RgbConstants.MIN_NORMALIZED_COMPONENT || green > RgbConstants.MAX_NORMALIZED_COMPONENT) {
            throw new IllegalArgumentException("green: " + green);
        }
        if (blue < RgbConstants.MIN_NORMALIZED_COMPONENT || blue > RgbConstants.MAX_NORMALIZED_COMPONENT) {
            throw new IllegalArgumentException("blue: " + blue);
        }
    }

    /**
     * Calculates {@code hue} color component from specified RGB color components.
     *
     * @param red   red.
     * @param green green.
     * @param blue  blue.
     * @return a value of {@code hue} as degrees of {@code [0..360]} or the {@link Double#NaN} which means that the
     * color is considered achromatic(grayscale).
     */
    // https://www.w3.org/TR/css-color-4/#rgb-to-hwb
    static double rgbToHue(final double red, final double green, final double blue) {
        assert red >= RgbConstants.MIN_NORMALIZED_COMPONENT;
        assert red <= RgbConstants.MAX_NORMALIZED_COMPONENT;
        assert green >= RgbConstants.MIN_NORMALIZED_COMPONENT;
        assert green <= RgbConstants.MAX_NORMALIZED_COMPONENT;
        assert blue >= RgbConstants.MIN_NORMALIZED_COMPONENT;
        assert blue <= RgbConstants.MAX_NORMALIZED_COMPONENT;
        final var max = Math.max(red, Math.max(green, blue));
        final var min = Math.min(red, Math.min(green, blue));
        double hue = Double.NaN;
        final var d = max - min;
        if (d != 0) {
            if (max == red) {
                hue = (green - blue) / d + (green < blue ? 6 : 0);
            } else if (max == green) {
                hue = (blue - red) / d + 2;
            } else {
                assert max == blue;
                hue = (red - green) / d + 4;
            }
            hue *= 60;
        }
        if (hue >= HwbConstants.MAX_HUE) {
            hue -= HwbConstants.MAX_HUE;
        }
        return hue;
    }

    /**
     * Returns an array of HWB color components, converted from specified RGB color components.
     *
     * @param red   a value of {@code red} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *              {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param green a value of {@code green} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *              {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param blue  a value of {@code blue} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *              {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @return an array of {@code hue}({@code [0..360] or {@link Double#NaN}}), {@code whiteness}({@code [0..100]}), and
     * {@code blackness}({@code [0..100]}).
     * @apiNote Note that the {@code hue}(the first element of the returned array) may be the {@link Double#NaN} which
     * means that the color is considered achromatic(grayscale).
     * @see <a href="https://www.w3.org/TR/css-color-4/#rgb-to-hwb">8.2. Converting sRGB Colors to HWB</a> (CSS Color
     * Module Level 4, W3C Candidate Recommendation Draft, 24 April 2025)
     */
    public static double[] rgbToHwb(final double red, final double green, final double blue) {
        checkRgbToHwbArguments(red, green, blue);
        final var epsilon = 1 / 100000.0d;
        var h = rgbToHue(red, green, blue);
        final var w = Math.min(Math.min(red, green), blue);
        final var b = 1 - Math.max(Math.max(red, green), blue);
        if (w + b >= 1 - epsilon) {
            h = Double.NaN;
        }
        return new double[] {h, w * 100, b * 100};
    }

    /**
     * Puts HWB color components, converted from specified RGB color components, to the specified buffer, and returns
     * the buffer.
     *
     * @param red    a value of {@code red} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *               {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param green  a value of {@code green} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *               {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param blue   a value of {@code blue} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *               {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param buffer the function.
     * @return given {@code buffer} put with {@code hue}({@code [0..360]} or {@link Double#NaN}),
     * {@code whiteness}({@code [0..100]}), and {@code blackness}({@code [0..100]}).
     * @see #rgbToHwb(double, double, double)
     * @see DoubleBuffer#put(double[])
     */
    @SuppressWarnings("unchecked")
    public static <T extends DoubleBuffer> T rgbToHwb(final double red, final double green, final double blue,
                                                      final T buffer) {
        checkRgbToHwbArguments(red, green, blue);
        Objects.requireNonNull(buffer, "buffer is null");
        return (T) buffer.put(rgbToHwb(red, green, blue));
    }

    /**
     * Applies HWB color components, converted from specified RGB color components, to the specified function, and
     * returns the result.
     *
     * @param red      a value of {@code red} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *                 {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param green    a value of {@code green} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *                 {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param blue     a value of {@code blue} component between {@value RgbConstants#MIN_NORMALIZED_COMPONENT} and
     *                 {@value RgbConstants#MAX_NORMALIZED_COMPONENT}, both inclusive.
     * @param function the function.
     * @param <R>      result type parameter
     * @return the result of the {@code function} applied, in currying, with {@code hue}({@code [0..360]} or
     * {@link Double#NaN}), {@code whiteness}({@code [0..100]}), and {@code blackness}({@code [0..100]}).
     */
    // https://www.w3.org/TR/css-color-4/#rgb-to-hwb
    public static <R> R rgbToHwb(final double red, final double green, final double blue,
                                 final DoubleFunction< // h
                                         ? extends DoubleFunction< // w
                                                 ? extends DoubleFunction< // b
                                                         ? extends R>>> function) {
        checkRgbToHwbArguments(red, green, blue);
        Objects.requireNonNull(function, "function is null");
        final var hwb = rgbToHwb(red, green, blue);
        return function
                .apply(hwb[0])
                .apply(hwb[1])
                .apply(hwb[2])
                ;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private RgbUtils() {
        throw new AssertionError("No instances");
    }
}
