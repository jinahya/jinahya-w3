package io.github.jinahya.w3.css.color;

import java.nio.DoubleBuffer;
import java.util.Objects;
import java.util.function.DoubleFunction;

final class RgbUtils {

    /**
     * .
     *
     * @param red   Red component 0..1
     * @param green Green component 0..1
     * @param blue  Blue component 0..1
     * @return hue as degrees 0..360
     */
    // https://www.w3.org/TR/css-color-4/#rgb-to-hwb
    static double rgbToHue(final double red, final double green, final double blue) {
        assert red >= RgbConstants.MIN_NORMALIZED_COMPONENT;
        assert red <= RgbConstants.MAX_NORMALIZED_COMPONENT;
        assert green >= RgbConstants.MIN_NORMALIZED_COMPONENT;
        assert green <= RgbConstants.MAX_NORMALIZED_COMPONENT;
        assert blue >= RgbConstants.MIN_NORMALIZED_COMPONENT;
        assert blue <= RgbConstants.MIN_NORMALIZED_COMPONENT;
        final var max = Math.max(red, Math.max(green, blue));
        final var min = Math.min(red, Math.min(green, blue));
        double hue = .0d;
        final var d = max - min;
        if (d != 0) {
            if (max == red) {
                hue = (int) ((green - blue) / d + (green < blue ? 6 : 0));
            } else if (max == green) {
                hue = (int) ((blue - red) / d + 2);
            } else {
                assert max == blue;
                hue = (int) ((red - green) / d + 4);
            }
            hue *= 60;
        }
        if (hue >= HwbConstants.MAX_HUE) {
            hue -= HwbConstants.MAX_HUE;
        }
        return hue;
    }

    /**
     * Put HWB color components, converted from specified RGB color components, to the specified buffer, and returns the
     * buffer.
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
    public static <T extends DoubleBuffer> T rgbToHwb(final double red, final double green, final double blue,
                                                      final T buffer) {
        if (red < RgbConstants.MIN_NORMALIZED_COMPONENT || red > RgbConstants.MAX_NORMALIZED_COMPONENT) {
            throw new IllegalArgumentException("red: " + red);
        }
        if (green < RgbConstants.MIN_NORMALIZED_COMPONENT || green > RgbConstants.MAX_NORMALIZED_COMPONENT) {
            throw new IllegalArgumentException("green: " + green);
        }
        if (blue < RgbConstants.MIN_NORMALIZED_COMPONENT || blue > RgbConstants.MAX_NORMALIZED_COMPONENT) {
            throw new IllegalArgumentException("blue: " + blue);
        }
        Objects.requireNonNull(buffer, "buffer is null");
        final var epsilon = 1 / 100000.0d;
        var hue = rgbToHue(red, green, blue);
        final var white = Math.min(Math.min(red, green), blue);
        final var black = 1 - Math.max(Math.max(red, green), blue);
        if (white + black >= 1 - epsilon) {
            hue = 0;
        }
        assert hue >= HwbConstants.MIN_HUE;
        assert hue <= HwbConstants.MAX_HUE;
        assert white >= HwbConstants.MIN_WHITENESS;
        assert white <= HwbConstants.MAX_WHITENESS;
        assert black >= HwbConstants.MIN_BLACKNESS;
        assert black <= HwbConstants.MAX_BLACKNESS;
        return (T) buffer.put(hue).put(white).put(black);
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
     * @return the result of the {@code function} applied, in currying, with {@code hue}({@code [0..360]},
     * {@code whiteness}({@code [0..100]}, and {@code blackness}{@code [0..100]}.
     */
    // https://www.w3.org/TR/css-color-4/#rgb-to-hwb
    public static <R> R rgbToHwb(final double red, final double green, final double blue,
                                 final DoubleFunction< // h
                                         ? extends DoubleFunction< // w
                                                 ? extends DoubleFunction< // b
                                                         ? extends R>>> function) {
//        final var epsilon = 1 / 100000.0d;
//        var hue = rgbToHue(red, green, blue);
//        final var white = Math.min(Math.min(red, green), blue);
//        final var black = 1 - Math.max(Math.max(red, green), blue);
//        if (white + black >= 1 - epsilon) {
//            hue = 0;
//        }
//        assert hue >= HwbConstants.MIN_HUE;
//        assert hue <= HwbConstants.MAX_HUE;
//        assert white >= HwbConstants.MIN_WHITENESS;
//        assert white <= HwbConstants.MAX_WHITENESS;
//        assert black >= HwbConstants.MIN_BLACKNESS;
//        assert black <= HwbConstants.MAX_BLACKNESS;
//        return function.apply(hue).apply(white).apply(black);
        Objects.requireNonNull(function, "function is null");
        final var buffer = rgbToHwb(
                red,
                green,
                blue,
                DoubleBuffer.allocate(3)
        );
        return function
                .apply(buffer.get(0))
                .apply(buffer.get(1))
                .apply(buffer.get(2));
    }

    /**
     * Return an array of HWB components, converted from specified RGB color components, to the specified function, and
     * returns the result.
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
    // https://www.w3.org/TR/css-color-4/#rgb-to-hwb
    public static double[] rgbToHwb(final double red, final double green, final double blue) {
        return rgbToHwb(
                red,
                green,
                blue,
                DoubleBuffer.allocate(3)
        ).array();
    }

    // -----------------------------------------------------------------------------------------------------------------
    private RgbUtils() {
        throw new AssertionError("No instances");
    }
}
