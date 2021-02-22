package com.example.koombeatest.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.couchbase.lite.*
import com.example.koombeatest.data.remote.Data
import com.example.koombeatest.data.remote.Post
import com.example.koombeatest.data.remote.UserPosts
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class DatabaseTesting {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database : Database

    lateinit var repositoryTesting : FakeUserPostsRepositoryTesting

    @Before
    fun setup(){
        hiltRule.inject()
        repositoryTesting = FakeUserPostsRepositoryTesting(database)
    }

    @After
    fun teardown(){
        //database.close()
        database.delete()
    }

    @Test
    fun insertUserPostsTest() = runBlockingTest{
        val postInserted = Post(
        "Tue Dec 14 2021 00:11:26 GMT-0500 (Colombia Standard Time)",
        1234,
            listOf()
        )
        val dataInserted = Data(
            "juan@mail.com",
            "juan",
            postInserted,
            "profile_pic.jpg",
            "1234"
        )
        repositoryTesting.insertUserPosts(UserPosts(listOf(dataInserted)))

        val dataList = repositoryTesting.getLocalUserPosts()

        assertThat(dataList.data?.data).contains(dataInserted)
    }
}