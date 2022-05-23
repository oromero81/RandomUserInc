package cat.oscarromero.randomuser.domain.usecase

import cat.oscarromero.randomuser.domain.repository.UsersRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteUserTest {

    private val usersRepository: UsersRepository = mockk(relaxed = true)

    private lateinit var deleteUser: DeleteUser

    private val userId = "userID"

    @Before
    fun setUp() {
        deleteUser = DeleteUser(usersRepository)
    }

    @Test
    fun `GIVEN user on app, WHEN user delete random user, THEN local repository is invoked`() {
        runBlocking {

            deleteUser.run("")

            coVerify(exactly = 1) { usersRepository.deleteUser(any()) }
        }
    }

    @Test
    fun `GIVEN user on app, WHEN user delete random user, THEN local repository is invoked with received params`() {
        runBlocking {

            deleteUser.run(userId)

            coVerify(exactly = 1) { usersRepository.deleteUser(userId) }
        }
    }
}
