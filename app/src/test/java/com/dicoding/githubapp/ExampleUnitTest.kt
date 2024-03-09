package com.dicoding.githubapp

import com.dicoding.githubapp.api.RetrofitClient
import com.dicoding.githubapp.api.response.DataDetail
import com.dicoding.githubapp.api.response.DataFollowers
import com.dicoding.githubapp.api.response.DataFollowing
import com.dicoding.githubapp.api.response.DataSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        println("hello world")
        runBlocking {
            val search = fetchData()
            println(search)
        }
    }

    suspend fun fetchData(): List<DataFollowing> = withContext(Dispatchers.IO) {
        val retrofit = RetrofitClient.instance
        val defferedAcc = async { retrofit.getFollowing("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", "mizzy-123") }
        val getDataDetail = defferedAcc.await()
        getDataDetail
    }
}