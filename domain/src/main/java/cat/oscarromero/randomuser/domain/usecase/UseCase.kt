package cat.oscarromero.randomuser.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class UseCase<in Params, out Type> {

    abstract suspend fun run(params: Params): Result<Type, FailureType>

    operator fun invoke(params: Params, onResult: (Result<Type, FailureType>) -> Unit) {
        val job = GlobalScope.async(Dispatchers.IO) { run(params) }
        GlobalScope.launch(Dispatchers.Main) { onResult(job.await()) }
    }
}
