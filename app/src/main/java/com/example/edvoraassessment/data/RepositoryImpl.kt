package com.example.edvoraassessment.data

import android.util.Log
import com.example.edvoraassessment.models.ProductItem
import com.example.edvoraassessment.models.Products
import com.example.edvoraassessment.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService
):Repository {
    override suspend fun getProducts(): Response<Products> {
        return apiService.getProducts()
    }
}