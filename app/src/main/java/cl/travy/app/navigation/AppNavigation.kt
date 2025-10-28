package cl.travy.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.graphics.values
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.travy.app.model.data.SeleccionViajeUiState
import cl.travy.app.ui.screens.HomeScreen
import cl.travy.app.ui.screens.MenuPrincipal
import cl.travy.app.ui.screens.BuscarViajesScreen
import cl.travy.app.ui.screens.ElegirAsientoScreen
import cl.travy.app.ui.screens.LoginScreen
import cl.travy.app.ui.screens.SeleccionViajeScreen
import cl.travy.app.ui.screens.SettingsScreen
import cl.travy.app.ui.screens.ComprobanteViajeScreen
import cl.travy.app.ui.screens.PagoScreen
import cl.travy.app.viewmodel.AuthViewModel
import cl.travy.app.viewmodel.BuscarViajeViewModel
import cl.travy.app.viewmodel.HomeViewModel
import cl.travy.app.viewmodel.PagoViewModel
import cl.travy.app.viewmodel.SeleccionAsientoViewModel
import cl.travy.app.viewmodel.SeleccionViajeViewModel
import android.util.Log

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val pagoViewModel: PagoViewModel = viewModel()


    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login.route
    ) {


        composable(route = AppRoutes.Login.route) {
            val viewModel: AuthViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LoginScreen(

                uiState = uiState,
                onLoginValueChange = viewModel::onLoginValueChange,
                onLoginClick = {
                    viewModel.login(
                        onLoginSuccess = {
                            navController.navigate(AppRoutes.Home.route) {
                                popUpTo(AppRoutes.Login.route) { inclusive = true }
                            }
                        }
                    )
                },
                onRegisterClick = { null },
                onBackClick = null,


                )
        }

        composable(route = AppRoutes.Home.route) {
            val viewModel: HomeViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(key1 = Unit) {
                viewModel.setupMenuItems(
                    onNavigateToSearch = {
                        navController.navigate(AppRoutes.BuscarViaje.route)
                    },
                    onNavigateToSettings = {
                        navController.navigate(AppRoutes.Settings.route)
                    }
                )
            }
            HomeScreen(uiState = uiState)
        }

        composable(route = AppRoutes.BuscarViaje.route) {
            BuscarViajesScreen(
                onNavigateBack = { navController.popBackStack() },
                onSearchClicked = { origen, destino, fecha ->
                    navController.navigate(
                        AppRoutes.SeleccionarViaje.createRoute(origen, destino, fecha)
                    )
                }
            )
        }

        composable(
            route = AppRoutes.SeleccionarViaje.route,
            arguments = listOf(
                navArgument("origen") { type = NavType.StringType },
                navArgument("destino") { type = NavType.StringType },
                navArgument("fecha") { type = NavType.LongType }
            )
        ) { backStackEntry ->


            val viewModel: SeleccionViajeViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            SeleccionViajeScreen(
                state = uiState,
                onViajeClick = { idViajeSeleccionado ->
                    val rutaDestino = AppRoutes.SeleccionarAsiento.createRoute(idViajeSeleccionado)
                    Log.d("AppNavigation", "Intentando navegar a la ruta: $rutaDestino")
                    navController.navigate(rutaDestino)
                    
                },
                onNavigateBack = { navController.popBackStack() }

            )
        }

        composable(
            route = AppRoutes.SeleccionarAsiento.route,
            arguments = listOf(
                navArgument("idViaje") { type = NavType.IntType }
            )
        ) {
            val viewModel: SeleccionAsientoViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            ElegirAsientoScreen(
                state = uiState,
                onAsientoClick = viewModel::onAsientoClick,
                onContinuarClick = {
                   pagoViewModel.prepararCompra(
                       pasajeros = uiState.pasajerosPorAsiento,
                       precio = uiState.precioTotal
                   )
                    navController.navigate(AppRoutes.PagarViaje.route)
                },
                onNavigateBack = { navController.popBackStack() },
                onGuardarDatosPasajero = viewModel::guardarDatosPasajero,
                onCancelarDialogo = { viewModel.cancelarEdicionPasajero()},
                onDatosPasajeroChange = viewModel::onDatosPasajeroChange
            )
        }

        composable(route = AppRoutes.PagarViaje.route) {

            val uiState by pagoViewModel.uiState.collectAsState()


            LaunchedEffect(uiState.pagoRealizadoConExito) {
                if (uiState.pagoRealizadoConExito) {
                    navController.navigate(AppRoutes.ComprobanteViaje.route) {

                        popUpTo(AppRoutes.PagarViaje.route) { inclusive = true }
                    }
                }
            }


            PagoScreen(
                state = uiState,
                onMetodoSeleccionado = { metodo ->
                    pagoViewModel.seleccionarMetodoDePago(metodo)
                },
                onConfirmarPago = {
                    pagoViewModel.ejecutarPago()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = AppRoutes.ComprobanteViaje.route) {
            val uiState by pagoViewModel.uiState.collectAsState()
            
            if (uiState.pasajerosPorAsiento.isNotEmpty() && uiState.metodoSeleccionado != null) {
                ComprobanteViajeScreen(
                    pasajerosPorAsiento = uiState.pasajerosPorAsiento,
                    precioTotal = uiState.precioTotal,
                    metodoPago = uiState.metodoSeleccionado!!,
                    onVolverAlInicio = {
                        pagoViewModel.finalizarYResetear()
                        navController.navigate(AppRoutes.Home.route) {
                            popUpTo(AppRoutes.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }


        composable(route = AppRoutes.Settings.route) {
            val authViewModel: AuthViewModel = viewModel()
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogoutClick = {
                    authViewModel.logout(
                        onLogoutSuccess = {
                            navController.navigate(AppRoutes.Login.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            )

        }


    }
}
