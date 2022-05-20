package cat.oscarromero.randomuser.data.dto


data class RandomUserResponseDto(
    val results: List<UserDto>,
    val info: InfoDto
)
