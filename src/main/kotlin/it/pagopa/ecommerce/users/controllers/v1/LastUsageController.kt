package it.pagopa.ecommerce.users.controllers.v1

import it.pagopa.ecommerce.users.services.UserStatisticsService
import it.pagopa.generated.ecommerce.users.api.UserApi
import it.pagopa.generated.ecommerce.users.model.UserLastPaymentMethodData
import java.util.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@RestController("LastUsageControllerV1")
class LastUsageController(@Autowired private val userStatisticsService: UserStatisticsService) :
    UserApi {
    override fun getLastPaymentMethodUsed(
        xUserId: UUID,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<UserLastPaymentMethodData>> =
        userStatisticsService.findUserLastMethodById(xUserId.toString()).map {
            ResponseEntity.ok(it)
        }

    /*
     * @formatter:off
     *
     * Warning kotlin:S6508 - "Unit" should be used instead of "Void"
     * Suppressed because controller interface is generated from openapi descriptor as java code which use Void as return type.
     * User stats interfaces are generated as java code because kotlin generation generate broken code
     *
     * @formatter:on
     */
    @SuppressWarnings("kotlin:S6508")
    override fun saveLastPaymentMethodUsed(
        xUserId: UUID,
        userLastPaymentMethodDataDto: Mono<UserLastPaymentMethodData>,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<Void>> =
        userLastPaymentMethodDataDto.flatMap {
            userStatisticsService
                .saveUserLastUsedMethodInfo(userId = xUserId, userLastPaymentMethodData = it)
                .map { ResponseEntity.status(HttpStatus.CREATED).build() }
        }
}
