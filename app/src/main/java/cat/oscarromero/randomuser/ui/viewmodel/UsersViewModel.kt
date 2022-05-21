package cat.oscarromero.randomuser.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import cat.oscarromero.randomuser.domain.usecase.ObtainUsers
import cat.oscarromero.randomuser.domain.usecase.Result
import cat.oscarromero.randomuser.ui.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val obtainUsers: ObtainUsers) : BaseViewModel() {

    val users = MutableLiveData<List<UserModel>>()

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
}
