package br.edu.utfpr.appcontatos.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.edu.utfpr.appcontatos.ui.contact.details.ContactDetailsScreen
import br.edu.utfpr.appcontatos.ui.contact.form.ContactFormScreen
import br.edu.utfpr.appcontatos.ui.contact.list.ContactsListScreen

@Composable
fun AppContacts(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "list"
    ){
        composable(route = "list") {
            ContactsListScreen(
                onAddPressed = {
                    navController.navigate("form")
                },
                onContactPressed = { contact ->
                    navController.navigate("details/${contact.id}")
                }

            )
        }
        composable(
            route = "details/{id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.IntType
                }
            )
        ) { navBackStackEntry ->
            val contactId = navBackStackEntry.arguments?.getInt("id") ?: 0
                ContactDetailsScreen(
                    modifier = Modifier,
                    onBackPressed = {
                        navController.popBackStack()
                    },
                    onEditPressed = {
                        navController.navigate("form?id=$contactId")
                    },
                    onDeletePressed = {
                        navigateToList(navController)
                    },)
        }
        composable(
            route = "form?id={id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.StringType
                    nullable= true
                }
            )
        ){
            ContactFormScreen(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                onBackPressed = {
                    navController.popBackStack()
                },
                onContactSaved = {
                    navigateToList(navController)
                }
            )
        }
    }


}
private fun navigateToList(navController: NavHostController) {
    navController.navigate("list") {
        popUpTo(navController.graph.findStartDestination().id) {
            inclusive = true
        }
    }
}