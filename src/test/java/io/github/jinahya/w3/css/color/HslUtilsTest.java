package io.github.jinahya.w3.css.color;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.DoubleBuffer;
import java.util.stream.Stream;

import static io.github.jinahya.w3.css.color.HslUtils.hslToRgb;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.withSettings;

@Slf4j
class HslUtilsTest {

    private static Stream<Arguments> hslAndRgb() {
        // google://"hsl to rgb test vector with color name"
        return Stream.of(
                Arguments.of("0", "100", "50", "255", "0", "0"),
                Arguments.of("120", "100", "50", "0", "255", "0"),
                Arguments.of("240", "100", "50", "0", "0", "255"),
                Arguments.of("0", "0", "0", "0", "0", "0"),
                Arguments.of("0", "0", "100", "255", "255", "255"),
                Arguments.of("0", "100", "25", "128", "0", "0"),
                Arguments.of("0", "100", "75", "255", "128", "128"),
                Arguments.of("120", "50", "50", "64", "191", "64"),
                Arguments.of("240", "50", "50", "64", "64", "191"),
                Arguments.of("180", "100", "50", "0", "255", "255"),
                Arguments.of("300", "100", "50", "255", "0", "255"),
                Arguments.of("60", "100", "50", "255", "255", "0"),
//                Arguments.of("180", "50", "25", "0", "64", "64"),
                Arguments.of("180", "50", "25", "32", "96", "96"),
//                Arguments.of("180", "50", "75", "128", "255", "255")
                Arguments.of("180", "50", "75", "159", "223", "223")
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"hslAndRgb"})
    @ParameterizedTest(name = "[{index}]: {0}, {1}%, {2}% -> {3}, {4}, {5}")
    void hslToRgb__(final double hue, final double saturation, final double lightness,
                    final int red, final int green, final int blue) {
        final var array = hslToRgb(
                hue,
                saturation,
                lightness
        );
        assertThat(array).isNotNull().hasSize(3);
        final var r = array[0];
        final var g = array[1];
        final var b = array[2];
        assertThat(Math.round(r * RgbConstants.MAX_COMPONENT))
                .as("r")
                .isEqualTo(red);
        assertThat(Math.round(g * RgbConstants.MAX_COMPONENT))
                .as("g")
                .isEqualTo(green);
        assertThat(Math.round(b * RgbConstants.MAX_COMPONENT))
                .as("b")
                .isEqualTo(blue);
    }

    @MethodSource({"hslAndRgb"})
    @ParameterizedTest(name = "[{index}]: {0}, {1}%, {2}% -> {3}, {4}, {5}")
    void hslToRgb__WithBuffer(final double hue, final double saturation, final double lightness,
                              final int red, final int green, final int blue) {
        try (var mockStatic = mockStatic(HslUtils.class, withSettings().defaultAnswer(CALLS_REAL_METHODS))) {
            final var buffer = HslUtils.hslToRgb(
                    hue,
                    saturation,
                    lightness,
                    DoubleBuffer.allocate(3)
            );
            mockStatic.verify(
                    () -> hslToRgb(hue, saturation, lightness),
                    times(1)
            );
            assertThat(buffer).isNotNull().satisfies(b -> {
                assertThat(b.capacity()).isEqualTo(3);
                assertThat(b.hasRemaining()).isFalse();
            });
            assertThat(Math.round(buffer.get(0) * RgbConstants.MAX_COMPONENT))
                    .as("r")
                    .isEqualTo(red);
            assertThat(Math.round(buffer.get(1) * RgbConstants.MAX_COMPONENT))
                    .as("g")
                    .isEqualTo(green);
            assertThat(Math.round(buffer.get(2) * RgbConstants.MAX_COMPONENT))
                    .as("b")
                    .isEqualTo(blue);
        }
    }

    @MethodSource({"hslAndRgb"})
    @ParameterizedTest(name = "[{index}]: {0}, {1}%, {2}% -> {3}, {4}, {5}")
    void hslToRgb__WithFunction(final double hue, final double saturation, final double lightness,
                                final int red, final int green, final int blue) {
        try (var mockStatic = mockStatic(HslUtils.class, withSettings().defaultAnswer(CALLS_REAL_METHODS))) {
            final var rgb = HslUtils.<double[]>hslToRgb(
                    hue,
                    saturation,
                    lightness,
                    r -> g -> b -> new double[] {r, g, b}
            );
            mockStatic.verify(
                    () -> hslToRgb(hue, saturation, lightness),
                    times(1)
            );
            final var r = rgb[0];
            final var g = rgb[1];
            final var b = rgb[2];
            assertThat(Math.round(r * RgbConstants.MAX_COMPONENT))
                    .as("r")
                    .isEqualTo(red);
            assertThat(Math.round(g * RgbConstants.MAX_COMPONENT))
                    .as("g")
                    .isEqualTo(green);
            assertThat(Math.round(b * RgbConstants.MAX_COMPONENT))
                    .as("b")
                    .isEqualTo(blue);
        }
    }
}
