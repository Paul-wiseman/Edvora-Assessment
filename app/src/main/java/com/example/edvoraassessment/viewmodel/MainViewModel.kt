package com.example.edvoraassessment.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edvoraassessment.data.Repository
import com.example.edvoraassessment.models.ProductItem
import com.example.edvoraassessment.util.NetworkResult
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.M)
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
):AndroidViewModel(application) {

    /** Fetching Products Response*/
    private val _getProductResponse = MutableStateFlow<NetworkResult<List<ProductItem>>>(NetworkResult.Loading())
    val getProductResponse = _getProductResponse.asStateFlow()

    /** Network call to fetch products*/
    fun getProducts(){
        viewModelScope.launch(Dispatchers.IO) {
            if (hasInternetConnection()){
                try {
                    val response = repository.getProducts()
                    if (response.isSuccessful && response.body() !=null){
                        _getProductResponse.value = NetworkResult.Success(response.body()!!)
                    }else{
                        _getProductResponse.value = NetworkResult.Failure(response.errorBody().toString())
                    }
                }catch (e:Exception){
                    _getProductResponse.value = NetworkResult.Failure("Please try again $e")
                }
            }else{
                _getProductResponse.value = NetworkResult.Failure("No Internet network")
            }
        }
    }

    /**[hasInternetConnection] is used to check if the user's device has an internet connection*/
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}