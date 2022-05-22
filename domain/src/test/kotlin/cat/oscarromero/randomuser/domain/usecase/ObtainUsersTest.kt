package cat.oscarromero.randomuser.domain.usecase

import cat.oscarromero.randomuser.domain.model.Location
import cat.oscarromero.randomuser.domain.model.User
import cat.oscarromero.randomuser.domain.repository.UsersRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

class ObtainUsersTest {

    private val usersRepositoryNetwork: UsersRepository = mockk(relaxed = true)
    private val usersRepositoryLocal: UsersRepository = mockk(relaxed = true)

    private lateinit var obtainUsers: ObtainUsers

    private val listWithDuplicates = listOf(
        User("1", "", "", "", "", "", Location("", "", ""), "", Date()),
        User("1", "", "", "", "", "", Location("", "", ""), "", Date()),
        User("2", "", "", "", "", "", Location("", "", ""), "", Date())
    )

    @Before
    fun setUp() {
        obtainUsers = ObtainUsers(usersRepositoryLocal, usersRepositoryNetwork)
    }

    @Test
    fun `GIVEN user on app, WHEN user gets random users, THEN local repository is invoked`() {
        runBlocking {

            obtainUsers.run(Unit)

            coVerify(exactly = 1) { usersRepositoryLocal.obtainUsers() }
        }
    }

    @Test
    fun `GIVEN user getting random users, WHEN users list is empty, THEN network repository is invoked`() {
        runBlocking {
            coEvery { usersRepositoryLocal.obtainUsers() } returns Result.Success(emptyList())

            obtainUsers.run(Unit)

            coVerify(exactly = 1) { usersRepositoryNetwork.obtainUsers() }
        }
    }

    @Test
    fun `GIVEN user getting random users, WHEN users list is NOT empty, THEN list is returned`() {
        runBlocking {
            val expectedValue =
                listOf(
                    User("1", "", "", "", "", "", Location("", "", ""), "", Date()),
                    User("2", "", "", "", "", "", Location("", "", ""), "", Date())
                )

            coEvery { usersRepositoryLocal.obtainUsers() } returns Result.Success(expectedValue)

            val receivedValue = obtainUsers.run(Unit) as Result.Success

            Assert.assertEquals(expectedValue, receivedValue.successData)
        }
    }

    @Test
    fun `GIVEN user getting random users, WHEN users list has duplicated, THEN returned list is filtered`() {
        runBlocking {
            val result = listWithDuplicates

            coEvery { usersRepositoryLocal.obtainUsers() } returns Result.Success(emptyList())
            coEvery { usersRepositoryNetwork.obtainUsers() } returns Result.Success(result)

            val receivedValue = obtainUsers.run(Unit) as Result.Success

            Assert.assertEquals(2, receivedValue.successData.size)
        }
    }

    @Test
    fun `GIVEN random users from network, WHEN users list is filtered, THEN filtered list is saved`() {
        runBlocking {
            val result = listWithDuplicates

            coEvery { usersRepositoryLocal.obtainUsers() } returns Result.Success(emptyList())
            coEvery { usersRepositoryNetwork.obtainUsers() } returns Result.Success(result)

            val receivedValue = obtainUsers.run(Unit) as Result.Success

            coVerify(exactly = 1) { usersRepositoryLocal.saveUsers(receivedValue.successData) }
        }
    }

    @Test
    fun `GIVEN random users from local, WHEN failure is received, THEN received failure is returned`() {
        runBlocking {
            val expectedValue = FailureType.ServerFailure()

            coEvery { usersRepositoryLocal.obtainUsers() } returns Result.Failure(expectedValue)

            val receivedValue = obtainUsers.run(Unit) as Result.Failure

            Assert.assertEquals(expectedValue, receivedValue.failureData)
        }
    }

    @Test
    fun `GIVEN random users from network, WHEN failure is received, THEN received failure is returned`() {
        runBlocking {
            val expectedValue = FailureType.NetworkConnectionFailure()

            coEvery { usersRepositoryLocal.obtainUsers() } returns Result.Success(emptyList())
            coEvery { usersRepositoryNetwork.obtainUsers() } returns Result.Failure(expectedValue)

            val receivedValue = obtainUsers.run(Unit) as Result.Failure

            Assert.assertEquals(expectedValue, receivedValue.failureData)
        }
    }
}
