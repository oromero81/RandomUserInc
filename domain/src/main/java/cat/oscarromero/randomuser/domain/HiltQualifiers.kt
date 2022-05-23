package cat.oscarromero.randomuser.domain

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalImplementation

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NetworkImplementation