package com.example.edvoraassessment.util

sealed class NetworkResult<T>(
    val data:T? = null,
    val message:String? = null
) {
    class Success<T>(data: T):NetworkResult<T>(data)
    class Failure<T>(message: String?, data: T? = null):NetworkResult<T>(data, message)
    class Loading<T>:NetworkResult<T>()

}