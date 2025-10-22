package cl.travy.app.viewmodel

import androidx.lifecycle.ViewModel

class SettingsViewModel {

    fun performLogout(authViewModel: AuthViewModel, onLogoutSuccess: ()-> Unit) {

        authViewModel.logout(onLogoutSuccess = onLogoutSuccess)
    }
}