package cat.oscarromero.randomuser.domain.model

import java.util.*

data class User(
    val id: String,
    val name: String,
    val surname: String,
    val email: String,
    val picture: String,
    val phone: String,
    val location: Location,
    val gender: String,
    val registerDate: Date
)
