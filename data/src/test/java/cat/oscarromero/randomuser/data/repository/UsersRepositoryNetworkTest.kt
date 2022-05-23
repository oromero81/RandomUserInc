package cat.oscarromero.randomuser.data.repository

import cat.oscarromero.randomuser.data.dto.GenericErrorDto
import cat.oscarromero.randomuser.data.dto.RandomUserResponseDto
import cat.oscarromero.randomuser.data.network.adapter.NetworkResponse
import cat.oscarromero.randomuser.data.network.api.RandomUsersApi
import cat.oscarromero.randomuser.data.parseToObject
import cat.oscarromero.randomuser.domain.model.Location
import cat.oscarromero.randomuser.domain.model.User
import cat.oscarromero.randomuser.domain.usecase.FailureType
import cat.oscarromero.randomuser.domain.usecase.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.text.SimpleDateFormat

class UsersRepositoryNetworkTest {

    private val retrofit: Retrofit = mockk()
    private val randomUsersApi: RandomUsersApi = mockk()

    private lateinit var usersRepositoryNetwork: UsersRepositoryNetwork

    private val dto: RandomUserResponseDto = parseToObject(RESPONSE_JSON)
    private val dtoWithUserIdNull: RandomUserResponseDto =
        parseToObject(RESPONSE_JSON_USER_ID_IS_NULL)

    @Before
    fun setUp() {
        usersRepositoryNetwork = UsersRepositoryNetwork(retrofit)

        coEvery { retrofit.create(RandomUsersApi::class.java) } returns randomUsersApi
    }

    @Test
    fun `GIVEN a user getting random users, WHEN users are get from network, THEN api service is invoked`() {
        runBlocking {
            coEvery { randomUsersApi.getUsers(any()) } returns NetworkResponse.Success(dto, 200)

            usersRepositoryNetwork.obtainUsers()

            coVerify(exactly = 1) { randomUsersApi.getUsers(any()) }
        }
    }

    @Test
    fun `GIVEN random users get from network, WHEN users are successful retrieved, THEN received data is parsed and is returned`() {
        runBlocking {
            coEvery { randomUsersApi.getUsers(any()) } returns NetworkResponse.Success(dto, 200)

            val expectedValue = listOf(
                User(
                    "43370442-D",
                    "Marta",
                    "Caballero",
                    "marta.caballero@example.com",
                    "https://randomuser.me/api/portraits/women/37.jpg",
                    "987-025-572",
                    Location("Avenida de La Albufera", "Gijón", "Castilla y León"),
                    "female",
                    SimpleDateFormat("yyyy-MM-dd").parse("2019-05-08")
                ),
                User(
                    "1NNaN84457129 34",
                    "Timeo",
                    "Da Silva",
                    "timeo.dasilva@example.com",
                    "https://randomuser.me/api/portraits/men/3.jpg",
                    "02-41-63-97-58",
                    Location("Place de L'Abbé-Basset", "Asnières-sur-Seine", "Yvelines"),
                    "male",
                    SimpleDateFormat("yyyy-MM-dd").parse("2004-06-19")
                )
            )

            val receivedValue = usersRepositoryNetwork.obtainUsers() as Result.Success

            Assert.assertEquals(expectedValue, receivedValue.successData)
        }
    }

    @Test
    fun `GIVEN random users get from network, WHEN users fails, THEN received error is returned`() {
        runBlocking {
            val message = "message"
            val code = 404
            val expectedValue = FailureType.ServerFailure("$code - $message")

            coEvery { randomUsersApi.getUsers(any()) } returns
                    NetworkResponse.ApiError(GenericErrorDto(message), code)

            val receivedValue = usersRepositoryNetwork.obtainUsers() as Result.Failure

            Assert.assertEquals(expectedValue, receivedValue.failureData)
        }
    }

    @Test
    fun `GIVEN random users get from network, WHEN response body is null, THEN failure is returned`() {
        runBlocking {
            coEvery { randomUsersApi.getUsers(any()) } returns NetworkResponse.Success(null, 200)

            val receivedValue = usersRepositoryNetwork.obtainUsers()

            Assert.assertTrue(receivedValue is Result.Failure)
        }
    }

    @Test
    fun `GIVEN random users get from network, WHEN user has id null, THEN returned list is filtered`() {
        runBlocking {
            coEvery { randomUsersApi.getUsers(any()) } returns
                    NetworkResponse.Success(dtoWithUserIdNull, 200)

            val receivedValue = usersRepositoryNetwork.obtainUsers() as Result.Success

            Assert.assertTrue(receivedValue.successData.size == 1)
        }
    }

    companion object {
        private const val RESPONSE_JSON = "" +
                "{\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"gender\": \"female\",\n" +
                "      \"name\": {\n" +
                "        \"title\": \"Mrs\",\n" +
                "        \"first\": \"Marta\",\n" +
                "        \"last\": \"Caballero\"\n" +
                "      },\n" +
                "      \"location\": {\n" +
                "        \"street\": {\n" +
                "          \"number\": 2653,\n" +
                "          \"name\": \"Avenida de La Albufera\"\n" +
                "        },\n" +
                "        \"city\": \"Gijón\",\n" +
                "        \"state\": \"Castilla y León\",\n" +
                "        \"country\": \"Spain\",\n" +
                "        \"postcode\": 66819,\n" +
                "        \"coordinates\": {\n" +
                "          \"latitude\": \"-17.1458\",\n" +
                "          \"longitude\": \"89.1624\"\n" +
                "        },\n" +
                "        \"timezone\": {\n" +
                "          \"offset\": \"-3:00\",\n" +
                "          \"description\": \"Brazil, Buenos Aires, Georgetown\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"email\": \"marta.caballero@example.com\",\n" +
                "      \"login\": {\n" +
                "        \"uuid\": \"a1854255-a5bd-4165-8782-9abf6dc0489c\",\n" +
                "        \"username\": \"bluemeercat713\",\n" +
                "        \"password\": \"lady\",\n" +
                "        \"salt\": \"wT2extdB\",\n" +
                "        \"md5\": \"658b184d5e57c85ee0540cd31328c52a\",\n" +
                "        \"sha1\": \"06a1150416efec1906c2f52e8046444c7ccf211b\",\n" +
                "        \"sha256\": \"f12fce612819f47c87b2be5b2c5802995efadf91915d2468f760449102e44906\"\n" +
                "      },\n" +
                "      \"dob\": {\n" +
                "        \"date\": \"1984-08-02T02:33:23.432Z\",\n" +
                "        \"age\": 38\n" +
                "      },\n" +
                "      \"registered\": {\n" +
                "        \"date\": \"2019-05-08T17:19:37.262Z\",\n" +
                "        \"age\": 3\n" +
                "      },\n" +
                "      \"phone\": \"987-025-572\",\n" +
                "      \"cell\": \"633-213-464\",\n" +
                "      \"id\": {\n" +
                "        \"name\": \"DNI\",\n" +
                "        \"value\": \"43370442-D\"\n" +
                "      },\n" +
                "      \"picture\": {\n" +
                "        \"large\": \"https://randomuser.me/api/portraits/women/37.jpg\",\n" +
                "        \"medium\": \"https://randomuser.me/api/portraits/med/women/37.jpg\",\n" +
                "        \"thumbnail\": \"https://randomuser.me/api/portraits/thumb/women/37.jpg\"\n" +
                "      },\n" +
                "      \"nat\": \"ES\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"gender\": \"male\",\n" +
                "      \"name\": {\n" +
                "        \"title\": \"Mr\",\n" +
                "        \"first\": \"Timeo\",\n" +
                "        \"last\": \"Da Silva\"\n" +
                "      },\n" +
                "      \"location\": {\n" +
                "        \"street\": {\n" +
                "          \"number\": 3629,\n" +
                "          \"name\": \"Place de L'Abbé-Basset\"\n" +
                "        },\n" +
                "        \"city\": \"Asnières-sur-Seine\",\n" +
                "        \"state\": \"Yvelines\",\n" +
                "        \"country\": \"France\",\n" +
                "        \"postcode\": 55917,\n" +
                "        \"coordinates\": {\n" +
                "          \"latitude\": \"-37.6506\",\n" +
                "          \"longitude\": \"-36.1993\"\n" +
                "        },\n" +
                "        \"timezone\": {\n" +
                "          \"offset\": \"+1:00\",\n" +
                "          \"description\": \"Brussels, Copenhagen, Madrid, Paris\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"email\": \"timeo.dasilva@example.com\",\n" +
                "      \"login\": {\n" +
                "        \"uuid\": \"54256977-54b0-4674-a86e-27531f853ae6\",\n" +
                "        \"username\": \"orangelion731\",\n" +
                "        \"password\": \"yvette\",\n" +
                "        \"salt\": \"tmQ4AuvA\",\n" +
                "        \"md5\": \"be949dd4bbac6c80d03ac6062009b0ba\",\n" +
                "        \"sha1\": \"6432486a0fbb4f5b30a8437be2502d3aa66fb3fb\",\n" +
                "        \"sha256\": \"ca15c2365c551ce8e2d4e3f31aa23afc1beebcfa82aaab6da09a7470abb4c35b\"\n" +
                "      },\n" +
                "      \"dob\": {\n" +
                "        \"date\": \"1992-12-15T16:05:48.763Z\",\n" +
                "        \"age\": 30\n" +
                "      },\n" +
                "      \"registered\": {\n" +
                "        \"date\": \"2004-06-19T19:53:14.752Z\",\n" +
                "        \"age\": 18\n" +
                "      },\n" +
                "      \"phone\": \"02-41-63-97-58\",\n" +
                "      \"cell\": \"06-54-35-71-82\",\n" +
                "      \"id\": {\n" +
                "        \"name\": \"INSEE\",\n" +
                "        \"value\": \"1NNaN84457129 34\"\n" +
                "      },\n" +
                "      \"picture\": {\n" +
                "        \"large\": \"https://randomuser.me/api/portraits/men/3.jpg\",\n" +
                "        \"medium\": \"https://randomuser.me/api/portraits/med/men/3.jpg\",\n" +
                "        \"thumbnail\": \"https://randomuser.me/api/portraits/thumb/men/3.jpg\"\n" +
                "      },\n" +
                "      \"nat\": \"FR\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"info\": {\n" +
                "    \"seed\": \"cd1ffbd34c2b5e73\",\n" +
                "    \"results\": 2,\n" +
                "    \"page\": 1,\n" +
                "    \"version\": \"1.3\"\n" +
                "  }\n" +
                "}"

        private const val RESPONSE_JSON_USER_ID_IS_NULL = "" +
                "{\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"gender\": \"female\",\n" +
                "      \"name\": {\n" +
                "        \"title\": \"Mrs\",\n" +
                "        \"first\": \"Marta\",\n" +
                "        \"last\": \"Caballero\"\n" +
                "      },\n" +
                "      \"location\": {\n" +
                "        \"street\": {\n" +
                "          \"number\": 2653,\n" +
                "          \"name\": \"Avenida de La Albufera\"\n" +
                "        },\n" +
                "        \"city\": \"Gijón\",\n" +
                "        \"state\": \"Castilla y León\",\n" +
                "        \"country\": \"Spain\",\n" +
                "        \"postcode\": 66819,\n" +
                "        \"coordinates\": {\n" +
                "          \"latitude\": \"-17.1458\",\n" +
                "          \"longitude\": \"89.1624\"\n" +
                "        },\n" +
                "        \"timezone\": {\n" +
                "          \"offset\": \"-3:00\",\n" +
                "          \"description\": \"Brazil, Buenos Aires, Georgetown\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"email\": \"marta.caballero@example.com\",\n" +
                "      \"login\": {\n" +
                "        \"uuid\": \"a1854255-a5bd-4165-8782-9abf6dc0489c\",\n" +
                "        \"username\": \"bluemeercat713\",\n" +
                "        \"password\": \"lady\",\n" +
                "        \"salt\": \"wT2extdB\",\n" +
                "        \"md5\": \"658b184d5e57c85ee0540cd31328c52a\",\n" +
                "        \"sha1\": \"06a1150416efec1906c2f52e8046444c7ccf211b\",\n" +
                "        \"sha256\": \"f12fce612819f47c87b2be5b2c5802995efadf91915d2468f760449102e44906\"\n" +
                "      },\n" +
                "      \"dob\": {\n" +
                "        \"date\": \"1984-08-02T02:33:23.432Z\",\n" +
                "        \"age\": 38\n" +
                "      },\n" +
                "      \"registered\": {\n" +
                "        \"date\": \"2019-05-08T17:19:37.262Z\",\n" +
                "        \"age\": 3\n" +
                "      },\n" +
                "      \"phone\": \"987-025-572\",\n" +
                "      \"cell\": \"633-213-464\",\n" +
                "      \"id\": {\n" +
                "        \"name\": \"DNI\",\n" +
                "        \"value\": null" +
                "      },\n" +
                "      \"picture\": {\n" +
                "        \"large\": \"https://randomuser.me/api/portraits/women/37.jpg\",\n" +
                "        \"medium\": \"https://randomuser.me/api/portraits/med/women/37.jpg\",\n" +
                "        \"thumbnail\": \"https://randomuser.me/api/portraits/thumb/women/37.jpg\"\n" +
                "      },\n" +
                "      \"nat\": \"ES\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"gender\": \"male\",\n" +
                "      \"name\": {\n" +
                "        \"title\": \"Mr\",\n" +
                "        \"first\": \"Timeo\",\n" +
                "        \"last\": \"Da Silva\"\n" +
                "      },\n" +
                "      \"location\": {\n" +
                "        \"street\": {\n" +
                "          \"number\": 3629,\n" +
                "          \"name\": \"Place de L'Abbé-Basset\"\n" +
                "        },\n" +
                "        \"city\": \"Asnières-sur-Seine\",\n" +
                "        \"state\": \"Yvelines\",\n" +
                "        \"country\": \"France\",\n" +
                "        \"postcode\": 55917,\n" +
                "        \"coordinates\": {\n" +
                "          \"latitude\": \"-37.6506\",\n" +
                "          \"longitude\": \"-36.1993\"\n" +
                "        },\n" +
                "        \"timezone\": {\n" +
                "          \"offset\": \"+1:00\",\n" +
                "          \"description\": \"Brussels, Copenhagen, Madrid, Paris\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"email\": \"timeo.dasilva@example.com\",\n" +
                "      \"login\": {\n" +
                "        \"uuid\": \"54256977-54b0-4674-a86e-27531f853ae6\",\n" +
                "        \"username\": \"orangelion731\",\n" +
                "        \"password\": \"yvette\",\n" +
                "        \"salt\": \"tmQ4AuvA\",\n" +
                "        \"md5\": \"be949dd4bbac6c80d03ac6062009b0ba\",\n" +
                "        \"sha1\": \"6432486a0fbb4f5b30a8437be2502d3aa66fb3fb\",\n" +
                "        \"sha256\": \"ca15c2365c551ce8e2d4e3f31aa23afc1beebcfa82aaab6da09a7470abb4c35b\"\n" +
                "      },\n" +
                "      \"dob\": {\n" +
                "        \"date\": \"1992-12-15T16:05:48.763Z\",\n" +
                "        \"age\": 30\n" +
                "      },\n" +
                "      \"registered\": {\n" +
                "        \"date\": \"2004-06-19T19:53:14.752Z\",\n" +
                "        \"age\": 18\n" +
                "      },\n" +
                "      \"phone\": \"02-41-63-97-58\",\n" +
                "      \"cell\": \"06-54-35-71-82\",\n" +
                "      \"id\": {\n" +
                "        \"name\": \"INSEE\",\n" +
                "        \"value\": \"1NNaN84457129 34\"\n" +
                "      },\n" +
                "      \"picture\": {\n" +
                "        \"large\": \"https://randomuser.me/api/portraits/men/3.jpg\",\n" +
                "        \"medium\": \"https://randomuser.me/api/portraits/med/men/3.jpg\",\n" +
                "        \"thumbnail\": \"https://randomuser.me/api/portraits/thumb/men/3.jpg\"\n" +
                "      },\n" +
                "      \"nat\": \"FR\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"info\": {\n" +
                "    \"seed\": \"cd1ffbd34c2b5e73\",\n" +
                "    \"results\": 2,\n" +
                "    \"page\": 1,\n" +
                "    \"version\": \"1.3\"\n" +
                "  }\n" +
                "}"
    }
}
