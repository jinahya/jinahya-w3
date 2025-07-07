package io.github.jinahya.w3.css.color;

import java.nio.DoubleBuffer;
import java.util.Objects;
import java.util.function.DoubleFunction;

public final class HwbUtils {

    // https://www.w3.org/TR/css-color-4/#hwb-to-rgb
    public static double[] hwbToRgbArray(
            final double hue, final double whiteness, final double blackness) {
        if (hue < HwbConstants.MIN_HUE || hue > HwbConstants.MAX_HUE) {
            throw new IllegalArgumentException("hue: " + hue);
        }
        if (whiteness < HwbConstants.MIN_WHITENESS || whiteness > HwbConstants.MAX_WHITENESS) {
            throw new IllegalArgumentException("whiteness: " + whiteness);
        }
        if (blackness < HwbConstants.MIN_BLACKNESS || blackness > HwbConstants.MAX_BLACKNESS) {
            throw new IllegalArgumentException("blackness: " + blackness);
        }
        final var white = whiteness / HwbConstants.MAX_WHITENESS;
        final var black = blackness / HwbConstants.MAX_BLACKNESS;
        if ((white + black) >= 1.0d) {
            final var grey = white / (white + black);
            return new double[] {grey, grey, grey};
        }
        final var rgb = HslUtils.hslToRgbArray(
                hue,
                100, // saturation
                50  // lightness
        );
        for (int i = 0; i < rgb.length; i++) {
            rgb[i] *= (1 - white - black);
            rgb[i] += white;
        }
        return rgb;
    }

    // https://www.w3.org/TR/css-color-4/#hwb-to-rgb
    public static <T extends DoubleBuffer> T hwbToRgbBuffer(final double hue, final double whiteness,
                                                            final double blackness, final T buffer) {
        if (hue < HwbConstants.MIN_HUE || hue > HwbConstants.MAX_HUE) {
            throw new IllegalArgumentException("hue: " + hue);
        }
        if (whiteness < HwbConstants.MIN_WHITENESS || whiteness > HwbConstants.MAX_WHITENESS) {
            throw new IllegalArgumentException("whiteness: " + whiteness);
        }
        if (blackness < HwbConstants.MIN_BLACKNESS || blackness > HwbConstants.MAX_BLACKNESS) {
            throw new IllegalArgumentException("blackness: " + blackness);
        }
        Objects.requireNonNull(buffer, "buffer is null");
        return (T) buffer.put(
                hwbToRgbArray(hue, whiteness, blackness)
        );
    }

    public static <R> R hwbToRgbFunction(
            final double hue, final double whiteness, final double blackness,
            final DoubleFunction< // r
                    ? extends DoubleFunction< // g
                            ? extends DoubleFunction< // b
                                    ? extends R>>> function) {
        if (hue < HwbConstants.MIN_HUE || hue > HwbConstants.MAX_HUE) {
            throw new IllegalArgumentException("hue: " + hue);
        }
        if (whiteness < HwbConstants.MIN_WHITENESS || whiteness > HwbConstants.MAX_WHITENESS) {
            throw new IllegalArgumentException("whiteness: " + whiteness);
        }
        if (blackness < HwbConstants.MIN_BLACKNESS || blackness > HwbConstants.MAX_BLACKNESS) {
            throw new IllegalArgumentException("blackness: " + blackness);
        }
        Objects.requireNonNull(function, "function is null");
        final var array = hwbToRgbArray(hue, whiteness, blackness);
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
