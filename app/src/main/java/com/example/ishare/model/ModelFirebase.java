package com.example.ishare.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.LinkedList;

import javax.annotation.Nullable;

public class ModelFirebase {
    FirebaseFirestore db;
    FirebaseAuth auth;

    ListenerRegistration userListener;
    ListenerRegistration postsListener;

    public ModelFirebase() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false).build();
        db.setFirestoreSettings(settings);
        auth = FirebaseAuth.getInstance();
    }

    public void getAllPosts(Date from, final Model.GetAllPostsListener listener) {
        postsListener = db.collection("posts").whereGreaterThan("lastUpdate", new Timestamp(from)).orderBy("lastUpdate", Query.Direction.DESCENDING)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                LinkedList<Post> data = new LinkedList<>();
                if (e != null) {
                    listener.onComplete(data);
                    return;
                }
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Post post = doc.toObject(Post.class);
                    if (post.lastUpdate != null)
                        data.add(post);
                }
                listener.onComplete(data);
            }
        });
    }

    public void addPost(Post post, final Model.AddPostListener listener) {
//        post.lastUpdate = Double.parseDouble(FieldValue.serverTimestamp().toString());
        db.collection("posts").document(post.id)
                .set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete(task.isSuccessful());
            }
        });
    }

    public void createUser(String email, String password, final String name, final Model.CreateUserListener listener) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    User user = new User(task.getResult().getUser().getUid(), name);
                    updateUser(user);
                    listener.onComplete(true);
                }
                else
                {
                    listener.onComplete(false);
                }
            }
        });
    }

    public void updateUser(User user) {
        db.collection("users").document(user.id).set(user);
    }

    public void signIn(String email, String password, final Model.SignInListener listener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                listener.OnComplete(task.isSuccessful());
            }
        });
    }

    public boolean isSignedIn() {
        return (auth.getCurrentUser() != null);
    }

    public boolean signOut() {
        try {
            auth.signOut();
            removeObservers();
            return true;
        }
        catch (Error error)
        {
            return false;
        }
    }

    // TODO : use
    private void removeObservers() {
        userListener.remove();
        postsListener.remove();
    }

    public void getUserDetailsAndObserve(String userId, final Model.GetUserDetailsListener listener) {
        userListener = db.collection("users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                User user = documentSnapshot.toObject(User.class);
                listener.onComplete(user);
            }
        });
    }

    public String getUserId() {
        return auth.getUid();
    }

    interface GetPostListener {
        void onComplete(Post post);
    }

    public void getStudent(String id, final GetPostListener listener) {
        db.collection("posts").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            Post post = snapshot.toObject(Post.class);
                            listener.onComplete(post);
                            return;
                        }
                        listener.onComplete(null);
                    }
                });
    }

    public void saveImage(Bitmap imageBitmap, final Model.SaveImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        Date d = new Date();
        // Create a reference to "mountains.jpg"
        final StorageReference imageStorageRef = storageRef.child("image_" + d.getTime() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageStorageRef.putBytes(data);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageStorageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    listener.onComplete(downloadUri.toString());
                } else {
                    listener.onComplete(null);
                }
            }
        });
    }

}
