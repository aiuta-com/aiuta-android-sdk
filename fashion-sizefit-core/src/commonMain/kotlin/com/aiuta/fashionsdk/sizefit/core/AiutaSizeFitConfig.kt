package com.aiuta.fashionsdk.sizefit.core

/**
 * Configuration class representing user's body measurements and shape for size recommendations.
 *
 * This class encapsulates all the physical characteristics needed to calculate
 * a personalized size recommendation through the NaizFit API.
 *
 * @property age User's age in years
 * @property height User's height in centimeters
 * @property weight User's weight in kilograms
 * @property gender User's gender
 * @property hipShape User's perceived hip shape (optional, used for more accurate recommendations)
 * @property bellyShape User's perceived belly shape (optional, used for more accurate recommendations)
 * @property braSize Bra band size (optional, e.g., 32, 34, 36)
 * @property braCup Bra cup size (optional)
 *
 * @see Gender
 * @see HipShape
 * @see BellyShape
 * @see BraCup
 */
public class AiutaSizeFitConfig(
    public val age: Int,
    public val height: Int,
    public val weight: Int,
    public val gender: Gender,
    public val hipShape: HipShape? = null,
    public val bellyShape: BellyShape? = null,
    public val braSize: Int? = null,
    public val braCup: BraCup? = null,
) {
    /**
     * User's gender classification for size recommendations.
     */
    public enum class Gender {
        /**
         * Female gender classification.
         */
        FEMALE,

        /**
         * Male gender classification.
         */
        MALE,
    }

    /**
     * User's perceived hip shape for more accurate size recommendations.
     */
    public enum class HipShape {
        /**
         * Slim hip shape - narrower hips relative to body frame.
         */
        SLIM,

        /**
         * Normal hip shape - average hip width relative to body frame.
         */
        NORMAL,

        /**
         * Curvy hip shape - wider hips relative to body frame.
         */
        CURVY,
    }

    /**
     * User's perceived belly shape for more accurate size recommendations.
     */
    public enum class BellyShape {
        /**
         * Flat belly shape - minimal abdominal protrusion.
         */
        FLAT,

        /**
         * Normal belly shape - average abdominal shape.
         */
        NORMAL,

        /**
         * Curvy belly shape - more prominent abdominal area.
         */
        CURVY,
    }

    /**
     * Bra cup size classification.
     */
    public enum class BraCup {
        /** AA cup size */
        AA,

        /** A cup size */
        A,

        /** B cup size */
        B,

        /** C cup size */
        C,

        /** D cup size */
        D,

        /** DD cup size (also known as E in some regions) */
        DD,

        /** E cup size */
        E,

        /** F cup size */
        F,

        /** G cup size */
        G,
    }
}
