package com.paypay.exchangerates.domain.usecase

interface UseCase<I, O> {
    suspend fun invoke(param: I): O
}