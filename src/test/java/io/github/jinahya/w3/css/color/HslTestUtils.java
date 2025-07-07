package io.github.jinahya.w3.css.color;

import java.util.concurrent.ThreadLocalRandom;

final class HslTestUtils {

    // ------------------------------------------------------------------------------------------------------------- hue
    static double randomHue() {
        return ThreadLocalRandom.current().nextDouble(
                HslConstants.MAX_HUE + Double.MIN_VALUE
        );
    }

    static double randomNormalizedHue() {
        return randomHue() / HslConstants.MAX_HUE;
    }

    // ------------------------------------------------------------------------------------------------------ saturation
    static double randomSaturation() {
        return ThreadLocalRandom.current().nextDouble(
                HslConstants.MAX_SATURATION + Double.MIN_VALUE
        );
    }

    static double randomNormalizedSaturation() {
        return ThreadLocalRandom.current().nextDouble(
                HslConstants.MAX_NORMALIZED_SATURATION + Double.MIN_VALUE
        );
    }

    // ------------------------------------------------------------------------------------------------------- lightness
    static double randomLightness() {
        return ThreadLocalRandom.current().nextDouble(
                HslConstants.MAX_LIGHTNESS + Double.MIN_VALUE
        );
    }

    static double randomNormalizedLightness() {
        return ThreadLocalRandom.current().nextDouble(
                HslConstants.MAX_NORMALIZED_LIGHTNESS + Double.MIN_VALUE
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    static double randomAlpha() {
        return ThreadLocalRandom.current().nextDouble(
                HslConstants.MAX_ALPHA + Double.MIN_VALUE
        );
    }

    static double randomNormalizedAlpha() {
        return ThreadLocalRandom.current().nextDouble(
                HslConstants.MAX_NORMALIZED_ALPHA + Double.MIN_VALUE
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    private HslTestUtils() {
        throw new AssertionError("instantiation is not allowed");
    }
}
