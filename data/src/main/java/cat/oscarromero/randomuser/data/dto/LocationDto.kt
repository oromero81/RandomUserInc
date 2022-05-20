package cat.oscarromero.randomuser.data.dto


data class LocationDto(
    val street: StreetDto?,
    val city: String?,
    val state: String?,
    val country: String?,
    val postcode: String?,
    val coordinates: CoordinatesDto?,
    val timezone: TimezoneDto?
)
