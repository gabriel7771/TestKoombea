package com.example.koombeatest.repositories

import android.content.Context
import com.couchbase.lite.*
import com.example.koombeatest.data.UserPostsApi
import com.example.koombeatest.data.remote.Data
import com.example.koombeatest.data.remote.Post
import com.example.koombeatest.data.remote.UserPosts
import com.example.koombeatest.utils.Constants.DATE_TAG
import com.example.koombeatest.utils.Constants.EMAIL_TAG
import com.example.koombeatest.utils.Constants.ID_TAG
import com.example.koombeatest.utils.Constants.INTERNET_VALIDATION
import com.example.koombeatest.utils.Constants.NAME_TAG
import com.example.koombeatest.utils.Constants.PICS_TAG
import com.example.koombeatest.utils.Constants.PROFILE_PIC_TAG
import com.example.koombeatest.utils.Constants.UID_TAG
import com.example.koombeatest.utils.Constants.USER_POSTS_DATABASE_NAME
import com.example.koombeatest.utils.Resource
import com.example.koombeatest.utils.Status
import com.example.koombeatest.utils.isConnectedToInternet
import com.example.koombeatest.R
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class DefaultUserPostsRepository @Inject constructor(
    private val userPostsApi: UserPostsApi,
    private val userPostsDatabase: Database,
    private val context: Context
) : UserPostsRepository {

    override suspend fun insertUserPosts(userPosts: UserPosts) {
        for(data in userPosts.data) {
            val document = HashMap<String, Any>()

            document[UID_TAG] = data.uid
            document[NAME_TAG] = data.name
            document[EMAIL_TAG] = data.email
            document[PROFILE_PIC_TAG] = data.profile_pic
            document[ID_TAG] = data.post.id
            document[DATE_TAG] = data.post.date
            document[PICS_TAG] = data.post.pics

            val mutableDocument = MutableDocument(data.uid, document)

            userPostsDatabase.save(mutableDocument)
        }
        Timber.d("Saved to database: ${userPostsDatabase.count} documents")
    }

    override suspend fun getLocalUserPosts(): Resource<UserPosts> {
        val query = QueryBuilder
            .select(SelectResult.all())
            .from(DataSource.database(userPostsDatabase))
        val result = query.execute()
        val dataList = mutableListOf<Data>()

        for(r in result){
            val dict = r.getDictionary(USER_POSTS_DATABASE_NAME)
            Timber.d("Got data from database of user: %s", dict.getString("name"))
            val post = Post(
                date = dict.getString(DATE_TAG),
                id = dict.getInt(ID_TAG),
                pics = dict.getArray(PICS_TAG).toList() as List<String>
            )
            val data = Data(
                uid = dict.getString(UID_TAG),
                name = dict.getString(NAME_TAG),
                email = dict.getString(EMAIL_TAG),
                profile_pic = dict.getString(PROFILE_PIC_TAG),
                post = post
            )
            dataList.add(data)
        }
        return if (dataList.size>0){
            Resource.success(UserPosts(dataList))
        } else {
            Resource.error(context.resources.getString(R.string.no_data_found_in_db), null)
        }
    }

    override suspend fun getRemoteUserPosts(): Resource<UserPosts> {
        return try {
            val response = userPostsApi.getUserPosts()

            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?:Resource.error(context.resources.getString(R.string.unknown_error), null)
            } else {
                return Resource.error(context.resources.getString(R.string.unknown_error), null)
            }
        } catch (e: Exception) {
            return Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }

    override suspend fun getUserPosts() : Resource<UserPosts> {
        return if(isConnectedToInternet(context)) {
            Timber.d("Has internet connection")
            val posts = getRemoteUserPosts()
            posts.data?.let {
                insertUserPosts(it)
            }
            getLocalUserPosts()
        } else {
            Timber.d("No internet connection")
            getLocalUserPosts().apply {
                status = Status.ERROR
                message = context.resources.getString(R.string.no_internet)
            }
        }
    }
}