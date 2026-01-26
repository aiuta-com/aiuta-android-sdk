package sample.tryon.kmp.navigation

sealed class NavigationState {
    data object Selector : NavigationState()
    data object TryOn : NavigationState()
    data object History : NavigationState()
    data object SizeFit : NavigationState()
}
