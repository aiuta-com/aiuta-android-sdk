package sample.tryon.kmp.utils

import com.aiuta.fashionsdk.compose.resources.media.AiutaMedia
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.configuration.defaults.images.features.onboarding.bestresult.DefaultsAiutaOnboardingBestResultsPageFeatureImages
import com.aiuta.fashionsdk.configuration.defaults.images.features.selector.DefaultAiutaImagePickerFeatureImages
import com.aiuta.fashionsdk.configuration.mode.modes
import com.aiuta.fashionsdk.configuration.mode.shoes.onboarding.images.AiutaShoesModeOnboardingPageImages
import com.aiuta.fashionsdk.configuration.mode.shoes.onboarding.onboardingShoesPage
import com.aiuta.fashionsdk.configuration.mode.shoes.onboarding.strings.AiutaShoesModeOnboardingPageStrings
import com.aiuta.fashionsdk.configuration.mode.shoes.picker.imagePicker
import com.aiuta.fashionsdk.configuration.mode.shoes.picker.images.AiutaShoesModeImagePickerImages
import com.aiuta.fashionsdk.configuration.mode.shoes.picker.model.predefinedModels
import com.aiuta.fashionsdk.configuration.mode.shoes.picker.model.strings.AiutaShoesModeImagePickerPredefinedModelsStrings
import com.aiuta.fashionsdk.configuration.mode.shoes.picker.strings.AiutaShoesModeImagePickerStrings
import com.aiuta.fashionsdk.configuration.mode.shoes.shoes

fun AiutaConfiguration.Builder.sampleShoesMode(): AiutaConfiguration.Builder = modes {
    shoes {
        onboardingShoesPage {
            images = SampleShoesOnboardingPageImages
            strings = AiutaShoesModeOnboardingPageStrings.Default()
        }
        imagePicker {
            predefinedModels {
                strings = AiutaShoesModeImagePickerPredefinedModelsStrings.Default()
            }
            images = SampleShoesImagePickerImages
            strings = AiutaShoesModeImagePickerStrings.Default()
        }
    }
}

/**
 * Placeholder media reused from the default assets
 */
private object SampleShoesOnboardingPageImages : AiutaShoesModeOnboardingPageImages {
    override val onboardingShoesBestResultsItem: AiutaMedia = DefaultsAiutaOnboardingBestResultsPageFeatureImages().onboardingBestResultsItem
}

private object SampleShoesImagePickerImages : AiutaShoesModeImagePickerImages {
    override val shoesExample: AiutaMedia = DefaultAiutaImagePickerFeatureImages().example
}
