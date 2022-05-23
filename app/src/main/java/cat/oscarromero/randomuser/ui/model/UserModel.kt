package cat.oscarromero.randomuser.ui.model

import cat.oscarromero.randomuser.domain.model.User
import java.text.DateFormat
import java.util.*

data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val picture: String,
    val phone: String,
    val gender: String,
    val location: String,
    val registeredDate: String
) {
    companion object {
        fun fromUser(user: User): UserModel {
            with(user) {
                return UserModel(
                    id,
                    "$name $surname",
                    email,
                    picture,
                    phone,
                    gender,
                    "${location.street} (${location.city}, ${location.state})",
                    DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
                        .format(registerDate)
                )
            }
        }
    }
}
