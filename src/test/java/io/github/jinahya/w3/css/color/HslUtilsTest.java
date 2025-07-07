package io.github.jinahya.w3.css.color;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.nio.DoubleBuffer;
import java.util.stream.Stream;

import static io.github.jinahya.w3.css.color.HslUtils.hslToRgbArray;
import static io.github.jinahya.w3.css.color.HslUtils.hslToRgbBuffer;
import static io.github.jinahya.w3.css.color.HslUtils.hslToRgbFunction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
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
    void hslToRgbBuffer__(final double hue, final double saturation, final double lightness,
                          final int red, final int green, final int blue) {
        final var buffer = hslToRgbBuffer(
                hue,
                saturation,
                lightness,
                DoubleBuffer.allocate(3)
        );
        final var r = buffer.get(0);
        final var g = buffer.get(1);
        final var b = buffer.get(2);
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
    void hslToRgbFunction__(final double hue, final double saturation, final double lightness,
                            final int red, final int green, final int blue) {
        try (var mockStatic = mockStatic(HslUtils.class, withSettings().defaultAnswer(CALLS_REAL_METHODS))) {
            final var rgb = hslToRgbFunction(
                    hue,
                    saturation,
                    lightness,
                    r -> g -> b -> DoubleBuffer.allocate(3).put(r).put(g).put(b)
            );
            final var bufferCaptor = ArgumentCaptor.forClass(DoubleBuffer.class);
            mockStatic.verify(
                    () -> hslToRgbBuffer(eq(hue), eq(saturation), eq(lightness), bufferCaptor.capture()),
                    times(1)
            );
            final var buffer = bufferCaptor.getValue();
            assertThat(buffer).isNotNull().satisfies(b -> {
                assertThat(b.capacity()).isEqualTo(3);
                assertThat(b.hasRemaining()).isFalse();
                assertThat(b.array()).isEqualTo(rgb.array());
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
    void hslToRgbArray__(final double hue, final double saturation, final double lightness,
                         final int red, final int green, final int blue) {
        try (var mockStatic = mockStatic(HslUtils.class, withSettings().defaultAnswer(CALLS_REAL_METHODS))) {
            final var rgb = hslToRgbArray(
                    hue,
                    saturation,
                    lightness
            );
            final var bufferCaptor = ArgumentCaptor.forClass(DoubleBuffer.class);
            mockStatic.verify(
                    () -> hslToRgbBuffer(eq(hue), eq(saturation), eq(lightness), bufferCaptor.capture()),
                    times(1)
            );
            final var buffer = bufferCaptor.getValue();
            assertThat(buffer).isNotNull().satisfies(b -> {
                assertThat(b.capacity()).isEqualTo(3);
                assertThat(b.hasRemaining()).isFalse();
                assertThat(b.array()).isEqualTo(rgb);
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
}
