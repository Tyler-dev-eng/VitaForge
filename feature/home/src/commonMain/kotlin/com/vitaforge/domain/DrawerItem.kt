package com.vitaforge.domain

import com.tylerdev.vitaforge.shared.Resources
import org.jetbrains.compose.resources.DrawableResource

enum class DrawerItem(
    val title: String,
    val icon: DrawableResource,
) {
    Profile(
        title = "Profile",
        icon = Resources.Icon.User,
    ),
    Blog(
        title = "Blog",
        icon = Resources.Icon.Blog,
    ),
    Locations(
        title = "Locations",
        icon = Resources.Icon.LocationPin,
    ),
    ContactUs(
        title = "Contact Us",
        icon = Resources.Icon.ContactUs,
    ),
    SignOut(
        title = "Sign Out",
        icon = Resources.Icon.SignOut,
    ),
    Admin(
        title = "Admin",
        icon = Resources.Icon.Admin,
    ),
}
