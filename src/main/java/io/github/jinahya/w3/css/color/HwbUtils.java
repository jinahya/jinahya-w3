package io.github.jinahya.w3.css.color;

import java.util.function.DoubleFunction;

public final class HwbUtils {

    // https://www.w3.org/TR/css-color-4/#hwb-to-rgb
    public static <R> R hwbToRgb(
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
        assert hue >= HwbConstants.MIN_HUE;
        assert hue <= HwbConstants.MAX_HUE;
        assert whiteness >= HwbConstants.MIN_WHITENESS;
        assert whiteness <= HwbConstants.MAX_WHITENESS;
        assert blackness >= HwbConstants.MIN_BLACKNESS;
        assert blackness <= HwbConstants.MAX_BLACKNESS;
        assert function != null;
        if ((whiteness + blackness) >= 1.0d) {
            final var grey = whiteness / (whiteness + blackness);
            return function.apply(grey).apply(grey).apply(grey);
        }
        return HslUtils.hslToRgb(
                hue,
                100, // saturation
                50,  // lightness
                r -> g -> b -> {
                    final var rgb = new double[] {r, g, b};
                    for (int i = 0; i < 3; i++) {
                        rgb[i] *= (1 - whiteness - blackness);
                        rgb[i] += whiteness;
                    }
                    return function.apply(rgb[0]).apply(rgb[1]).apply(rgb[2]);
                }
        );
    }

    // https://www.w3.org/TR/css-color-4/#hwb-to-rgb
    static <R> R hwbToRgb(
            final int hue,
            final int whiteness,
            final int blackness,
            final DoubleFunction< // r
                    ? extends DoubleFunction< // g
                            ? extends DoubleFunction< // b
                                    ? extends R>>> function) {
        assert hue >= HwbConstants.MIN_HUE;
        assert hue <= HwbConstants.MAX_HUE;
        assert whiteness >= HwbConstants.MIN_WHITENESS;
        assert whiteness <= HwbConstants.MAX_WHITENESS;
        assert blackness >= HwbConstants.MIN_BLACKNESS;
        assert blackness <= HwbConstants.MAX_BLACKNESS;
        return hwbToRgb(
                hue,
                whiteness / (double) HwbConstants.MAX_WHITENESS,
                blackness / (double) HwbConstants.MAX_BLACKNESS,
                function
        );
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS
    private HwbUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
