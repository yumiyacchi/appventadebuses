package cl.travy.app.viewmodel

import androidx.lifecycle.ViewModel
import cl.travy.app.model.data.HomeUiState
import cl.travy.app.model.data.MenuItem // Asegúrate de importar tu clase MenuItem
import cl.travy.app.model.data.getHomeMenuItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun setupMenuItems(
        onNavigateToSearch: () -> Unit,
        onNavigateToSettings: () -> Unit
    ) {
        val menuItems = getHomeMenuItems(
            onBuscarViajeClick = onNavigateToSearch,
            onOpcionesClick = onNavigateToSettings
        )

        _uiState.update { it.copy(menuItems = menuItems) }
    }
}