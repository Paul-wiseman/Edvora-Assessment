package com.example.edvoraassessment.network

import com.example.edvoraassessment.models.Products
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/")
    suspend fun getProducts():Response<Products>
}