package com.paypay.exchangerates.domain.usecase

import com.paypay.common.domain.usecase.UseCase
import com.paypay.exchangerates.domain.entity.CurrencyCode
import com.paypay.exchangerates.domain.entity.ExchangeRate
import com.paypay.exchangerates.domain.repository.ExchangeRatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchRatesFromCurrencyUseCase @Inject constructor(
    private val exchangeRatesRepository: ExchangeRatesRepository
) : UseCase<CurrencyCode, @JvmSuppressWildcards List<ExchangeRate>> {

    @Throws(FetchRatesFromCurrencyException::class)
    override suspend fun invoke(param: CurrencyCode): List<ExchangeRate> =
        withContext(Dispatchers.IO) {
            runCatching {
                exchangeRatesRepository.getRatesByCurrency(param)
            }.onFailure {
                throw FetchRatesFromCurrencyException(it.message)
            }.getOrThrow()
        }
}

class FetchRatesFromCurrencyException(override val message: String? = null) : Exception()