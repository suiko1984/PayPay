package com.paypay.exchangerates.domain.usecase

import com.paypay.common.domain.usecase.UseCase
import com.paypay.exchangerates.domain.entity.Currency
import com.paypay.exchangerates.domain.repository.ExchangeRatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchCurrenciesUseCase @Inject constructor(
    private val exchangeRatesRepository: ExchangeRatesRepository
) : UseCase<Unit, @JvmSuppressWildcards List<Currency>> {

    @Throws(FetchCurrenciesException::class)
    override suspend fun invoke(param: Unit): List<Currency> = withContext(Dispatchers.IO) {
        runCatching {
            exchangeRatesRepository.getCurrencies()
        }.onFailure { throw FetchCurrenciesException(it.message) }.getOrThrow()
    }
}

class FetchCurrenciesException(override val message: String? = null) : Exception()