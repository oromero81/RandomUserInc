package cat.oscarromero.randomuser.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cat.oscarromero.randomuser.domain.model.Location
import cat.oscarromero.randomuser.domain.model.User
import cat.oscarromero.randomuser.domain.usecase.FailureType
import cat.oscarromero.randomuser.domain.usecase.ObtainUsers
import cat.oscarromero.randomuser.domain.usecase.Result
import cat.oscarromero.randomuser.ui.model.UserModel
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.text.SimpleDateFormat

class UsersViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private val obtainUsers: ObtainUsers = mockk(relaxed = true)
    private lateinit var usersViewModel: UsersViewModel

    @Before
    fun setUp() {
        usersViewModel = UsersViewModel(obtainUsers)
    }

    @Test
    fun `GIVEN user on users screen, WHEN random users are requested, THEN loading is show`() {
        usersViewModel.loadUsers()

        Assert.assertTrue(usersViewModel.isLoading.value!!)
    }

    @Test
    fun `GIVEN user on users screen, WHEN random users are requested, THEN use case is invoked`() {
        usersViewModel.loadUsers()

        verify { obtainUsers.invoke(Unit, any()) }
    }

    @Test
    fun `GIVEN user on users screen, WHEN random users are successful retrieved, THEN loading is hide`() {
        runSuccessScenario()

        Assert.assertFalse(usersViewModel.isLoading.value!!)
    }

    @Test
    fun `GIVEN user on users screen, WHEN random users are successful retrieved, THEN model is set`() {
        val expectedValue = listOf(
            UserModel(
                "819-37-3624",
                "Edwin Freeman",
                "edwin.freeman@example.com",
                "https://randomuser.me/api/portraits/men/30.jpg",
                "(351)-071-1128"
            ),
            UserModel(
                "150287-6138",
                "Christian Hansen",
                "christian.hansen@example.com",
                "https://randomuser.me/api/portraits/men/97.jpg",
                "98612539",
            )
        )

        runSuccessScenario(
            listOf(
                User(
                    "819-37-3624",
                    "Edwin",
                    "Freeman",
                    "edwin.freeman@example.com",
                    "https://randomuser.me/api/portraits/men/30.jpg",
                    "(351)-071-1128",
                    Location("Saddle Dr", "Addison", "Minnesota"),
                    "male",
                    SimpleDateFormat("yyyy-MM-dd").parse("2006-01-27")
                ),
                User(
                    "150287-6138",
                    "Christian",
                    "Hansen",
                    "christian.hansen@example.com",
                    "https://randomuser.me/api/portraits/men/97.jpg",
                    "98612539",
                    Location("Harevænget", "Ulsted, Hals", "Syddanmark"),
                    "male",
                    SimpleDateFormat("yyyy-MM-dd").parse("2012-10-15")
                )
            )
        )

        Assert.assertEquals(expectedValue, usersViewModel.users.value!!)
    }

    @Test
    fun `GIVEN user on users screen, WHEN random users fail, THEN loading is hide`() {
        runFailureScenario()

        Assert.assertFalse(usersViewModel.isLoading.value!!)
    }

    @Test
    fun `GIVEN user on users screen, WHEN random users fail, THEN failure is set`() {
        runFailureScenario()

        Assert.assertNotNull(usersViewModel.failure.value)
    }

    private fun runSuccessScenario(result: List<User> = listOf()) {
        val functionSlot = slot<(Result<List<User>, FailureType>) -> Unit>()

        usersViewModel.loadUsers()

        verify { obtainUsers.invoke(Unit, capture(functionSlot)) }

        functionSlot.captured.invoke(Result.Success(result))
    }

    private fun runFailureScenario() {
        val functionSlot = slot<(Result<List<User>, FailureType>) -> Unit>()

        usersViewModel.loadUsers()

        verify { obtainUsers.invoke(Unit, capture(functionSlot)) }

        functionSlot.captured.invoke(Result.Failure(FailureType.UnknownFailure()))
    }
}
