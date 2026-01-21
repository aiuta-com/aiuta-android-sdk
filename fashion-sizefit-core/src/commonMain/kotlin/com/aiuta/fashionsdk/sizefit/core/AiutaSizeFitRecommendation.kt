package com.aiuta.fashionsdk.sizefit.core

/**
 * Represents a size recommendation.
 *
 * This class contains the recommended size along with detailed measurement information
 * for all available sizes and the user's calculated body measurements.
 *
 * @property bodyMeasurements The user's body measurements calculated based on their profile
 * @property recommendedSizeName The name of the recommended size (e.g., "M", "L", "XL")
 * @property sizes List of all available sizes with their measurement details and fit information
 *
 * @see BodyMeasurement
 * @see Size
 */
public class AiutaSizeFitRecommendation(
    public val bodyMeasurements: List<BodyMeasurement>,
    public val recommendedSizeName: String,
    public val sizes: List<Size>,
) {
    /**
     * Represents a user's body measurement used for the size recommendation.
     *
     * Body measurements are calculated by the NaizFit API based on the user's
     * physical characteristics (height, weight, age, gender, and body shape).
     *
     * @property type The type of measurement (e.g., "chest_c", "waist_c", "hip_c")
     * @property value The measurement value in centimeters
     */
    public class BodyMeasurement(
        public val type: String,
        public val value: Int,
    )

    /**
     * Represents detailed information about a specific size.
     *
     * Each size contains measurements with ranges and fit information
     * to help understand how well the size matches the user's body.
     *
     * @property measurements List of measurement details for this size
     * @property name The size name (e.g., "XS", "S", "M", "L", "XL")
     *
     * @see Measurement
     */
    public class Size(
        public val measurements: List<Measurement>,
        public val name: String,
    ) {
        /**
         * Represents detailed measurement information for a specific size.
         *
         * This class provides information about the measurement range (min-max)
         * and how the user's body measurement compares to this range.
         *
         * @property dist Distance from the user's body measurement to the midpoint
         *                of the size's measurement range (in centimeters).
         *                Negative values indicate the user's measurement is below the midpoint,
         *                positive values indicate it's above.
         * @property fitRatio Value obtained by dividing the distance by the range of the size.
         *                    This ratio indicates how well the size fits:
         *                    - Values close to 0 indicate a good fit
         *                    - Negative values indicate the user's measurement is below the range
         *                    - Positive values indicate the user's measurement is above the range
         * @property max Maximum value of the measurement range in centimeters
         * @property min Minimum value of the measurement range in centimeters
         * @property type The type of measurement (e.g., "chest_c", "waist_c", "hip_c")
         */
        public class Measurement(
            public val dist: Int,
            public val fitRatio: Int,
            public val max: Int,
            public val min: Int,
            public val type: String,
        )
    }
}
