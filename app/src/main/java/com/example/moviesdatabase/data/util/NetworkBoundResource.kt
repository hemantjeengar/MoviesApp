package com.example.moviesdatabase.data.util

import com.example.moviesdatabase.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = {true}
) : Flow<Resource<ResultType>> = flow {
    emit(Resource.Loading(null))

    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))

        try {
            saveFetchResult(fetch())
            query().map { Resource.Success(it)}
        } catch (throwable: Throwable) {
            query().map { Resource.Error(throwable.message ?: "Unknown error", it)}
        }
    } else {
        query().map { Resource.Success(it)}
    }

    emitAll(flow)
}