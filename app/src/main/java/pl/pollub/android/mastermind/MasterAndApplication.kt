package pl.pollub.android.mastermind

import android.app.Application
import pl.pollub.android.mastermind.data.db.AppContainer
import pl.pollub.android.mastermind.data.db.AppDataContainer

class MasterAndApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}