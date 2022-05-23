package cat.oscarromero.randomuser.ui.model

import cat.oscarromero.randomuser.domain.model.User

data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val picture: String,
    val phone: String
) {
    companion object {
        fun fromUser(user: User): UserModel {
            with(user) {
                return UserModel(id, "$name $surname", email, picture, phone)
            }
        }
    }
}
