package pl.pollub.android.mastermind

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {

    val Factory: ViewModelProvider.Factory = viewModelFactory {
        // ProfileViewModel
        initializer {
            ProfileViewModel(MasterAndApplication().container.playersRepository)
        }

        // GameViewModel
        initializer {
            GameViewModel(MasterAndApplication().container.scoresRepository)
        }

        // ResultsViewModel
        initializer {
            ResultsViewModel(MasterAndApplication().container.scoresRepository)
        }
    }
}

/**
 * Pomocnicza funkcja do pobierania referencji do MasterAndApplication
 * z CreationExtras ViewModelFactory.
 */
fun androidx.lifecycle.viewmodel.CreationExtras.MasterAndApplication(): MasterAndApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MasterAndApplication)


