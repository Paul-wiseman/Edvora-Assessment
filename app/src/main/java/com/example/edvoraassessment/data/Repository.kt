package com.example.edvoraassessment.data

import com.example.edvoraassessment.models.Products
import retrofit2.Response

interface Repository {
    suspend fun getProducts(): Response<Products>
}