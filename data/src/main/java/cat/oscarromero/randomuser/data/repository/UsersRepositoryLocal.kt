package cat.oscarromero.randomuser.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import cat.oscarromero.randomuser.data.parseToList
import cat.oscarromero.randomuser.data.toJson
import cat.oscarromero.randomuser.domain.model.User
import cat.oscarromero.randomuser.domain.repository.UsersRepository
import cat.oscarromero.randomuser.domain.usecase.FailureType
import cat.oscarromero.randomuser.domain.usecase.Result
import javax.inject.Inject

class UsersRepositoryLocal @Inject constructor(private val sharedPreferences: SharedPreferences) :
    UsersRepository {

    override suspend fun obtainUsers(): Result<List<User>, FailureType> =
        Result.Success(getUsersSaved())

    override suspend fun saveUsers(users: List<User>): Result<Unit, FailureType> {
        val usersSaved = getUsersSaved()
        val usersToSave = usersSaved + users

        sharedPreferences.edit { putString(KEY_USERS_SAVED, toJson(usersToSave)) }

        return Result.Success(Unit)
    }

    private fun getUsersSaved(): List<User> {
        val usersSaved = sharedPreferences.getString(KEY_USERS_SAVED, "") ?: ""

        return if (usersSaved.isEmpty()) {
            listOf()
        } else {
            parseToList(usersSaved)
        }
    }

    companion object {
        private const val KEY_USERS_SAVED = "shared_prefs_random_users_saved"
    }
}
