package cat.oscarromero.randomuser.data.network.api

import cat.oscarromero.randomuser.data.dto.RandomUserResponseDto
import cat.oscarromero.randomuser.data.network.GenericResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUsersApi {

    @GET("/")
    suspend fun getUsers(@Query("results") results: Int): GenericResponse<RandomUserResponseDto>
}
