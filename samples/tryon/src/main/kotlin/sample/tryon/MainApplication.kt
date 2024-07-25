package sample.tryon

import android.app.Application
import com.aiuta.fashionsdk.Aiuta

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initFashion()
    }

    private fun initFashion() {
        aiuta =
            Aiuta.Builder()
                .setApiKey(API_KEY)
                .setApplication(this)
                .build()
    }

    companion object {
        lateinit var aiuta: Aiuta

        const val API_KEY = "HEPSI2024" // TODO
    }
}
