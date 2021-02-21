package com.example.koombeatest.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import com.example.koombeatest.R
import com.example.koombeatest.data.UserPostsApi
import com.example.koombeatest.repositories.DefaultUserPostsRepository
import com.example.koombeatest.repositories.UserPostsRepository
import com.example.koombeatest.utils.Constants.BASE_USER_POSTS_URL
import com.example.koombeatest.utils.Constants.INTERNET_VALIDATION
import com.example.koombeatest.utils.Constants.USER_POSTS_DATABASE_NAME
import com.example.koombeatest.utils.isConnectedToInternet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUserPostsApi() : UserPostsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_USER_POSTS_URL)
            .build()
            .create(UserPostsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDefaultUserPostsRepository(
        api: UserPostsApi,
        database: Database,
        @Named(INTERNET_VALIDATION) hasInternet: Boolean
    ) = DefaultUserPostsRepository(api, database, hasInternet) as UserPostsRepository

    @Singleton
    @Provides
    fun provideUserPostsDatabase(
        @ApplicationContext context: Context
    ) : Database {
        //CouchbaseLite.init(context)
        val config = DatabaseConfiguration()
        config.directory = String.format("%s/%s", context.filesDir, USER_POSTS_DATABASE_NAME)
        return Database(USER_POSTS_DATABASE_NAME, config)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
    )

    @Named(INTERNET_VALIDATION)
    @Provides
    fun provideInternetConnectionValidation(
        @ApplicationContext context: Context
    ) : Boolean {
        return isConnectedToInternet(context)
    }
}