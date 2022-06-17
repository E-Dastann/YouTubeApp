package com.example.youtubekotlin.core.network


import com.example.youtubekotlin.core.network.result.Resource
import retrofit2.Response

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {

        try {
            val response = call()

            if (response.isSuccessful) {

                val body = response.body()

                if (body != null) {
                    return Resource.success(body)
                } else {
                    return Resource.error(response.message(), null)
                }
            }
        }catch ( e:Exception){
            return  Resource.error(e.localizedMessage,null)

        }
        return Resource.error(null,null)
    }

}