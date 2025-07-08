package io.github.jinahya.w3.css.color;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Test class for {@link RgbUtils} color space conversion utilities.
 *
 * <p>This class provides comprehensive tests for RGB to HSL and RGB to HWB color space
 * conversions. It uses parameterized tests with predefined color vectors to ensure accurate conversion results across
 * various color scenarios including primary colors, secondary colors, achromatic colors (grays), and edge cases.</p>
 *
 * <p>The test vectors include:</p>
 * <ul>
 *   <li>Primary colors (Red, Green, Blue)</li>
 *   <li>Secondary colors (Yellow, Cyan, Magenta)</li>
 *   <li>Achromatic colors (Black, White, Gray)</li>
 *   <li>Common colors (Orange, Pink, Gold, Dark Green, Navy)</li>
 * </ul>
 *
 * <p>Each test validates that the conversion results match expected values within
 * specified tolerances to account for floating-point precision differences.</p>
 *
 * @author jin.kwon&lt;onacit_at_gmail.com&gt;
 * @see RgbUtils
 * @see <a href="https://www.w3.org/TR/css-color-4/">CSS Color Module Level 4</a>
 */
@Slf4j
class RgbUtilsTest {

    // -----------------------------------------------------------------------------------------------------------------
    @DisplayName("rgbToHsl(DDD)[D")
    @Nested
    class RgbToHslTest {

        // Provides test vectors for RGB to HSL color space conversion
        private static Stream<Arguments> rgbToHsl() {
            return Stream.of(
                    // Format: red, green, blue, hue, saturation, lightness
                    Arguments.of(1.0, 0.0, 0.0, 0.0, 100.0, 50.0),        // Red
                    Arguments.of(0.0, 1.0, 0.0, 120.0, 100.0, 50.0),      // Green
                    Arguments.of(0.0, 0.0, 1.0, 240.0, 100.0, 50.0),      // Blue
                    Arguments.of(1.0, 1.0, 0.0, 60.0, 100.0, 50.0),       // Yellow
                    Arguments.of(0.0, 1.0, 1.0, 180.0, 100.0, 50.0),      // Cyan
                    Arguments.of(1.0, 0.0, 1.0, 300.0, 100.0, 50.0),      // Magenta
                    Arguments.of(0.0, 0.0, 0.0, Double.NaN, 0.0, 0.0),    // Black
                    Arguments.of(1.0, 1.0, 1.0, Double.NaN, 0.0, 100.0),  // White
                    Arguments.of(0.5, 0.5, 0.5, Double.NaN, 0.0, 50.0),   // 50% Gray
                    Arguments.of(1.0, 0.65, 0.0, 39.0, 100.0, 50.0),      // Orange
                    Arguments.of(1.0, 0.75, 0.8, 348.0, 100.0, 87.5),     // Pink
                    Arguments.of(1.0, 0.84, 0.0, 50.4, 100.0, 50.0),      // Gold
                    Arguments.of(0.0, 0.5, 0.0, 120.0, 100.0, 25.0),      // Dark Green
                    Arguments.of(0.0, 0.0, 0.5, 240.0, 100.0, 25.0)       // Navy
                    // Add more as needed
            );
        }

        /**
         * Tests RGB to HSL color space conversion using parameterized test vectors.
         *
         * @param red        the red component in range [0, 1]
         * @param green      the green component in range [0, 1]
         * @param blue       the blue component in range [0, 1]
         * @param hue        the expected hue in degrees [0, 360] or NaN for achromatic colors
         * @param saturation the expected saturation percentage [0, 100]
         * @param lightness  the expected lightness percentage [0, 100]
         */
        @MethodSource({"rgbToHsl"})
        @ParameterizedTest(name = "[{index}]: {0}, {1}, {2} -> {3}, {4}%, {5}%")
        void __(final double red, final double green, final double blue,
                final double hue, final double saturation, final double lightness) {
            final double[] hsl = RgbUtils.rgbToHsl(red, green, blue);
            if (Double.isNaN(hue)) {
                assertThat(hsl[0]).isNaN();
            } else {
                assertThat(hsl[0]).isCloseTo(hue, within(0.5));
            }
            assertThat(hsl[1]).isCloseTo(saturation, within(1.0));
            assertThat(hsl[2]).isCloseTo(lightness, within(1.0));
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @DisplayName("rgbToHwb(DDD)[D")
    @Nested
    class RgbToHwbTest {

        // Provides test vectors for RGB to HWB color space conversion
        private static Stream<Arguments> rgbToHwb() {
            return Stream.of(
                    // Format: red, green, blue, hue, whiteness, blackness
                    Arguments.of(1.0, 0.0, 0.0, 0.0, 0.0, 0.0),           // Red
                    Arguments.of(0.0, 1.0, 0.0, 120.0, 0.0, 0.0),         // Green
                    Arguments.of(0.0, 0.0, 1.0, 240.0, 0.0, 0.0),         // Blue
                    Arguments.of(1.0, 1.0, 0.0, 60.0, 0.0, 0.0),          // Yellow
                    Arguments.of(0.0, 1.0, 1.0, 180.0, 0.0, 0.0),         // Cyan
                    Arguments.of(1.0, 0.0, 1.0, 300.0, 0.0, 0.0),         // Magenta
                    Arguments.of(0.0, 0.0, 0.0, Double.NaN, 0.0, 100.0),  // Black
                    Arguments.of(1.0, 1.0, 1.0, Double.NaN, 100.0, 0.0),  // White
                    Arguments.of(0.5, 0.5, 0.5, Double.NaN, 50.0, 50.0),  // 50% Gray
                    Arguments.of(1.0, 0.65, 0.0, 39.0, 0.0, 0.0),         // Orange
                    Arguments.of(1.0, 0.75, 0.8, 348.0, 75.0, 0.0),       // Pink
                    Arguments.of(1.0, 0.84, 0.0, 50.4, 0.0, 0.0),         // Gold
                    Arguments.of(0.0, 0.5, 0.0, 120.0, 0.0, 50.0),        // Dark Green
                    Arguments.of(0.0, 0.0, 0.5, 240.0, 0.0, 50.0)         // Navy
                    // Add more as needed
            );
        }

        /**
         * Tests RGB to HWB color space conversion using parameterized test vectors.
         *
         * @param red       the red component in range [0, 1]
         * @param green     the green component in range [0, 1]
         * @param blue      the blue component in range [0, 1]
         * @param hue       the expected hue in degrees [0, 360] or NaN for achromatic colors
         * @param whiteness the expected whiteness percentage [0, 100]
         * @param blackness the expected blackness percentage [0, 100]
         */
        @MethodSource({"rgbToHwb"})
        @ParameterizedTest(name = "[{index}]: {0}, {1}, {2} -> {3}, {4}%, {5}%")
        void __(final double red, final double green, final double blue,
                final double hue, final double whiteness, final double blackness) {
            final double[] hwb = RgbUtils.rgbToHwb(red, green, blue);
            if (Double.isNaN(hue)) {
                assertThat(hwb[0]).isNaN();
            } else {
                assertThat(hwb[0]).isCloseTo(hue, within(0.5));
            }
            assertThat(hwb[1]).isCloseTo(whiteness, within(1.0));
            assertThat(hwb[2]).isCloseTo(blackness, within(1.0));
        }
    }
}
