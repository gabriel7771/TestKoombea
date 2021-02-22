package com.example.koombeatest.di

import android.content.Context
import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import com.example.koombeatest.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModuleTest {

    @Provides
    @Named("test_db")
    fun provideUserPostsDatabaseTest(
        @ApplicationContext context: Context
    ) : Database {
        CouchbaseLite.init(context)
        val config = DatabaseConfiguration()
        config.directory = String.format("%s/%s", context.filesDir,
            Constants.USER_POSTS_DATABASE_NAME_TEST
        )
        return Database(Constants.USER_POSTS_DATABASE_NAME_TEST, config)
    }
}