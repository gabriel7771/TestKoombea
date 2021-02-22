package com.example.koombeatest.repositories

import com.couchbase.lite.*
import com.example.koombeatest.R
import com.example.koombeatest.data.remote.Data
import com.example.koombeatest.data.remote.Post
import com.example.koombeatest.data.remote.UserPosts
import com.example.koombeatest.utils.Constants
import com.example.koombeatest.utils.Resource
import com.example.koombeatest.utils.Status
import timber.log.Timber

class FakeUserPostsRepositoryTesting (private val userPostsDatabase: Database) : UserPostsRepository {

    private val posts = mutableListOf<UserPosts>()

    override suspend fun insertUserPosts(userPosts: UserPosts) {
        for(data in userPosts.data) {
            val document = HashMap<String, Any>()

            document[Constants.UID_TAG] = data.uid
            document[Constants.NAME_TAG] = data.name
            document[Constants.EMAIL_TAG] = data.email
            document[Constants.PROFILE_PIC_TAG] = data.profile_pic
            document[Constants.ID_TAG] = data.post.id
            document[Constants.DATE_TAG] = data.post.date
            document[Constants.PICS_TAG] = data.post.pics

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
            val dict = r.getDictionary(Constants.USER_POSTS_DATABASE_NAME_TEST)
            val post = Post(
                date = dict.getString(Constants.DATE_TAG),
                id = dict.getInt(Constants.ID_TAG),
                pics = dict.getArray(Constants.PICS_TAG).toList() as List<String>
            )
            val data = Data(
                uid = dict.getString(Constants.UID_TAG),
                name = dict.getString(Constants.NAME_TAG),
                email = dict.getString(Constants.EMAIL_TAG),
                profile_pic = dict.getString(Constants.PROFILE_PIC_TAG),
                post = post
            )
            dataList.add(data)
        }
        return if (dataList.size>0){
            Resource.success(UserPosts(dataList))
        } else {
            Resource.error("context.resources.getString(R.string.no_data_found_in_db)", null)
        }
    }

    override suspend fun getRemoteUserPosts(): Resource<UserPosts> {
        return Resource(Status.SUCCESS, posts[0], "")
    }

    override suspend fun getUserPosts(): Resource<UserPosts> {
        return Resource(Status.SUCCESS, posts[0], "")
    }
}