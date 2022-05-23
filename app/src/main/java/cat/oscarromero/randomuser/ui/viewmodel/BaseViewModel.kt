package cat.oscarromero.randomuser.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.oscarromero.randomuser.domain.usecase.FailureType

open class BaseViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val failure = MutableLiveData<FailureType>()

    protected fun showLoading() {
        isLoading.value = true
    }

    protected fun hideLoading() {
        isLoading.value = false
    }
}
