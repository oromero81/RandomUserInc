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
        Result.Success(getUsersSaved(KEY_USERS_SAVED))

    override suspend fun saveUsers(users: List<User>): Result<Unit, FailureType> {
        val usersSaved = getUsersSaved(KEY_USERS_SAVED)
        val usersToSave = usersSaved + users

        saveUsersToSharedPreference(usersToSave, KEY_USERS_SAVED)

        return Result.Success(Unit)
    }

    override suspend fun deleteUser(userId: String): Result<Unit, FailureType> {
        val usersSaved = getUsersSaved(KEY_USERS_SAVED)

        val userToDelete = usersSaved.find { it.id == userId }

        userToDelete?.let {
            val usersToSave = usersSaved.toMutableList()
            usersToSave.remove(userToDelete)

            saveUsersToSharedPreference(usersToSave, KEY_USERS_SAVED)

            val usersDeleted = getUsersSaved(KEY_USERS_DELETED).toMutableList()
            usersDeleted.add(userToDelete)

            saveUsersToSharedPreference(usersDeleted, KEY_USERS_DELETED)
        }
        return Result.Success(Unit)
    }

    override suspend fun obtainDeletedUsers(): Result<List<User>, FailureType> =
        Result.Success(getUsersSaved(KEY_USERS_DELETED))

    private fun getUsersSaved(key: String): List<User> {
        val usersSaved = sharedPreferences.getString(key, "") ?: ""

        return if (usersSaved.isEmpty()) {
            listOf()
        } else {
            parseToList(usersSaved)
        }
    }

    private fun saveUsersToSharedPreference(users: List<User>, key: String) {
        sharedPreferences.edit { putString(key, toJson(users)) }
    }

    companion object {
        private const val KEY_USERS_SAVED = "shared_prefs_random_users_saved"
        private const val KEY_USERS_DELETED = "shared_prefs_random_users_deleted"
    }
}
