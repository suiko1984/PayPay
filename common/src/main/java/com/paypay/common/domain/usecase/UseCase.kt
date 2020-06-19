package com.paypay.common.domain.usecase

interface UseCase<I, O> {
    suspend fun invoke(param: I): O
}