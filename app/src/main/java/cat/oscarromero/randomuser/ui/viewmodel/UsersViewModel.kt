package cat.oscarromero.randomuser.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import cat.oscarromero.randomuser.domain.usecase.DeleteUser
import cat.oscarromero.randomuser.domain.usecase.ObtainMoreUsers
import cat.oscarromero.randomuser.domain.usecase.ObtainUsers
import cat.oscarromero.randomuser.domain.usecase.Result
import cat.oscarromero.randomuser.ui.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val obtainUsers: ObtainUsers,
    private val obtainMoreUsers: ObtainMoreUsers,
    private val deleteUser: DeleteUser
) : BaseViewModel() {

    val users = MutableLiveData<List<UserModel>>()
    val userSelected = MutableLiveData<String>()
    val user = MutableLiveData<UserModel>()

    var isMoreUsersRequest = false

    fun loadUsers() {
        showLoading()
        obtainUsers(Unit) {
            when (it) {
                is Result.Success -> {
                    hideLoading()
                    users.value = it.successData.map { user -> UserModel.fromUser(user) }
                }
                is Result.Failure -> {
                    hideLoading()
                    failure.value = it.failureData
                }
            }
        }
    }

    fun loadMoreUsers() {
        showLoading()

        if (!isMoreUsersRequest) {
            isMoreUsersRequest = true

            obtainMoreUsers(Unit) {
                isMoreUsersRequest = false

                when (it) {
                    is Result.Success -> {
                        hideLoading()

                        val moreUsers = users.value?.toMutableList() ?: mutableListOf()
                        moreUsers.addAll(it.successData.map { user -> UserModel.fromUser(user) })

                        users.value = moreUsers
                    }
                    is Result.Failure -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    fun deleteUser(id: String) {
        deleteUser(id) {}
    }

    fun userSelected(id: String) {
        userSelected.value = id
    }

    fun loadUser(id: String) {
        user.value = users.value?.find { it.id == id }
    }
}
