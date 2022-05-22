package cat.oscarromero.randomuser.data.repository

import android.content.SharedPreferences
import cat.oscarromero.randomuser.domain.model.Location
import cat.oscarromero.randomuser.domain.model.User
import cat.oscarromero.randomuser.domain.usecase.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat

class UsersRepositoryLocalTest {

    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)

    private val editor: SharedPreferences.Editor = mockk(relaxed = true)

    private lateinit var usersRepositoryLocal: UsersRepositoryLocal

    @Before
    fun setUp() {
        usersRepositoryLocal = UsersRepositoryLocal(sharedPreferences)
    }

    @Test
    fun `GIVEN a user getting random users, WHEN users are get from local, THEN Shared Preferences are invoked`() {
        runBlocking {
            usersRepositoryLocal.obtainUsers()

            coVerify(exactly = 1) { sharedPreferences.getString(any(), any()) }
        }
    }

    @Test
    fun `GIVEN random users get from local, WHEN users are empty, THEN empty list is returned`() {
        runBlocking {
            val receivedValue = usersRepositoryLocal.obtainUsers() as Result.Success

            Assert.assertTrue(receivedValue.successData.isEmpty())
        }
    }

    @Test
    fun `GIVEN random users get from local, WHEN users are successful retrieved, THEN received data is parsed and is returned`() {
        runBlocking {
            val expectedValue = listOf(
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
            coEvery { sharedPreferences.getString(any(), any()) } returns USERS_JSON

            val receivedValue = usersRepositoryLocal.obtainUsers() as Result.Success

            Assert.assertEquals(expectedValue, receivedValue.successData)
        }
    }

    @Test
    fun `GIVEN a user on app, WHEN users are saved, THEN Shared Preferences are invoked`() {
        runBlocking {
            coEvery { sharedPreferences.edit() } returns editor

            usersRepositoryLocal.saveUsers(listOf())

            coVerify(exactly = 1) { editor.putString(any(), any()) }
        }
    }

    companion object {
        private const val USERS_JSON = "" +
                "[\n" +
                "  {\n" +
                "    \"email\": \"edwin.freeman@example.com\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"id\": \"819-37-3624\",\n" +
                "    \"location\": {\n" +
                "      \"city\": \"Addison\",\n" +
                "      \"state\": \"Minnesota\",\n" +
                "      \"street\": \"Saddle Dr\"\n" +
                "    },\n" +
                "    \"name\": \"Edwin\",\n" +
                "    \"phone\": \"(351)-071-1128\",\n" +
                "    \"picture\": \"https://randomuser.me/api/portraits/men/30.jpg\",\n" +
                "    \"registerDate\": \"Jan 27, 2006 12:00:00 AM\",\n" +
                "    \"surname\": \"Freeman\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"email\": \"christian.hansen@example.com\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"id\": \"150287-6138\",\n" +
                "    \"location\": {\n" +
                "      \"city\": \"Ulsted, Hals\",\n" +
                "      \"state\": \"Syddanmark\",\n" +
                "      \"street\": \"Harevænget\"\n" +
                "    },\n" +
                "    \"name\": \"Christian\",\n" +
                "    \"phone\": \"98612539\",\n" +
                "    \"picture\": \"https://randomuser.me/api/portraits/men/97.jpg\",\n" +
                "    \"registerDate\": \"Oct 15, 2012 12:00:00 AM\",\n" +
                "    \"surname\": \"Hansen\"\n" +
                "  }\n" +
                "]"
    }
}
