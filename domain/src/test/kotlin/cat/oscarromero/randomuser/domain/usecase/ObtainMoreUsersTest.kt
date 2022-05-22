package cat.oscarromero.randomuser.domain.usecase

import cat.oscarromero.randomuser.domain.model.Location
import cat.oscarromero.randomuser.domain.model.User
import cat.oscarromero.randomuser.domain.repository.UsersRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class ObtainMoreUsersTest {

    private val usersRepositoryNetwork: UsersRepository = mockk(relaxed = true)
    private val usersRepositoryLocal: UsersRepository = mockk(relaxed = true)

    private lateinit var obtainMoreUsers: ObtainMoreUsers

    private val listWithDuplicates = listOf(
        User("1", "", "", "", "", "", Location("", "", ""), "", Date()),
        User("1", "", "", "", "", "", Location("", "", ""), "", Date()),
        User("2", "", "", "", "", "", Location("", "", ""), "", Date())
    )

    @Before
    fun setUp() {
        obtainMoreUsers = ObtainMoreUsers(usersRepositoryLocal, usersRepositoryNetwork)
    }

    @Test
    fun `GIVEN user on app, WHEN user gets more random users, THEN network repository is invoked`() {
        runBlocking {
            obtainMoreUsers.run(Unit)

            coVerify(exactly = 1) { usersRepositoryNetwork.obtainUsers() }
        }
    }

    @Test
    fun `GIVEN user getting more random users, WHEN users list has duplicates, THEN returned list is filtered`() {
        runBlocking {
            val result = listWithDuplicates

            coEvery { usersRepositoryNetwork.obtainUsers() } returns Result.Success(result)
            coEvery { usersRepositoryLocal.obtainDeletedUsers() } returns Result.Success(listOf())

            val receivedValue = obtainMoreUsers.run(Unit) as Result.Success

            assertEquals(2, receivedValue.successData.size)
        }
    }

    @Test
    fun `GIVEN more random users from network, WHEN users list is filtered, THEN filtered list is saved`() {
        runBlocking {
            val result = listWithDuplicates

            coEvery { usersRepositoryNetwork.obtainUsers() } returns Result.Success(result)
            coEvery { usersRepositoryLocal.obtainDeletedUsers() } returns Result.Success(listOf())

            val receivedValue = obtainMoreUsers.run(Unit) as Result.Success

            coVerify(exactly = 1) { usersRepositoryLocal.saveUsers(receivedValue.successData) }
        }
    }

    @Test
    fun `GIVEN more random users from network, WHEN failure is received, THEN received failure is returned`() {
        runBlocking {
            val expectedValue = FailureType.NetworkConnectionFailure()

            coEvery { usersRepositoryNetwork.obtainUsers() } returns Result.Failure(expectedValue)

            val receivedValue = obtainMoreUsers.run(Unit) as Result.Failure

            assertEquals(expectedValue, receivedValue.failureData)
        }
    }
}