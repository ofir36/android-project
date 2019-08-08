package com.example.ishare.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class Model {
    final public static Model instance = new Model();

    ModelSql modelSql;
    ModelFirebase modelFirebase;

    private Model() {
        modelSql = new ModelSql();
        modelFirebase = new ModelFirebase();
    }

    public String getUserId() {
        return modelFirebase.getUserId();
    }

    public void updateUser(User user) {
        modelFirebase.updateUser(user);
    }

    public interface GetAllPostsListener{
        void onComplete(List<Post> data);
    }

    public LiveData<List<Post>> getAllPosts() {
        final MutableLiveData<List<Post>> data = new
                MutableLiveData<>();

        //1. read local students last update date
        getLastUpdate("posts", new GetLastUpdateListener() {
            @Override
            public void onComplete(Date lastUpdate) {
                //2. get updates from firebase and observe
                modelFirebase.getAllPosts(lastUpdate, new GetAllPostsListener() {
                    @Override
                    public void onComplete(List<Post> posts) {
                        if (posts.size() > 0) {
                            //3. write new records to the local DB
                            PostAsyncDao.insertPosts(posts.toArray(new Post[posts.size()]));
                            Date lastUpdate = posts.get(0).lastUpdate;

                            //4. update the local posts last update date
                            setLastUpdate("posts", lastUpdate);
                        }

                        //5. get the full data
                        PostAsyncDao.getAllPosts(new GetAllPostsListener() {
                            @Override
                            public void onComplete(List<Post> posts) {
                                //6. notify observers with full data
                                data.setValue(posts);
                            }
                        });
                    }
                });
            }
        });

        return data;
    }

    public interface AddPostListener{
        void onComplete(boolean success);
    }
    public void addPost(Post post, AddPostListener listener) {
        modelFirebase.addPost(post, listener);
    }

    public interface GetLastUpdateListener {
        void onComplete(Date lastUpdate);
    }
    public void getLastUpdate(String tableName, GetLastUpdateListener listener)
    {
        LastUpdateAsyncDao.getLastUpdate(tableName, listener);
    }

    public void setLastUpdate(String tableName, Date date)
    {
        LastUpdateAsyncDao.setLastUpdate(new LastUpdate(tableName, date));
    }


    public interface SaveImageListener{
        void onComplete(String url);
    }
    public void saveImage(Bitmap imageBitmap, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, listener);
    }
    public void getImage(String url, ImageView imageView) {
        Picasso.get().load(url).into(imageView);
    }

    public interface CreateUserListener {
        void onComplete(boolean success);
    }
    public void createUser(String email, String password, String name, CreateUserListener listener)
    {
        modelFirebase.createUser(email, password, name, listener);
    }

    public interface SignInListener {
        void OnComplete(boolean success);
    }

    public interface GetUserDetailsListener {
        void onComplete(User user);
    }
    public void getUserDetails(final GetUserDetailsListener listener) {
        modelFirebase.getUserDetailsAndObserve(getUserId(), new GetUserDetailsListener() {
            @Override
            public void onComplete(final User user) {
                UserAsyncDao.insertUser(user, new UserAsyncDao.InsertUserListener() {
                    @Override
                    public void onComplete() {
                        listener.onComplete(user);
                    }
                });
            }
        });
    }

    public void getUserDetails(String userId, final GetUserDetailsListener listener)
    {
        modelFirebase.getUserDetails(userId, new GetUserDetailsListener() {
            @Override
            public void onComplete(final User user) {
                UserAsyncDao.insertUser(user, new UserAsyncDao.InsertUserListener() {
                    @Override
                    public void onComplete() {
                        listener.onComplete(user);
                    }
                });
            }
        });
    }


    public void signIn(String email, String password, SignInListener listener)
    {
        modelFirebase.signIn(email, password, listener);
    }

    public boolean signOut()
    {
        return modelFirebase.signOut();
    }

    public boolean isSignedIn() {
        return modelFirebase.isSignedIn();
    }
}
