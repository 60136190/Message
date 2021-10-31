    package Application.message.activities;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;

    import androidx.appcompat.app.AppCompatActivity;

    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.QueryDocumentSnapshot;
    import com.google.firebase.messaging.Constants;

    import java.util.ArrayList;
    import java.util.List;

    import Application.message.adapters.UserAdapter;
    import Application.message.databinding.ActivityUsersBinding;
    import Application.message.listeners.UserListener;
    import Application.message.models.User;
    import Application.message.utilities.Constans;
    import Application.message.utilities.PreferenceManager;

    public class UsersActivity extends BaseActivity implements UserListener {

        private ActivityUsersBinding binding;
        private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();
    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(v ->onBackPressed());
    }

    private void getUsers(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constans.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                   loading(false);
                   String currentUserId = preferenceManager.getString(Constans.KEY_USER_ID);
                   if (task.isSuccessful() && task.getResult() != null){
                       List<User> users = new ArrayList<>();
                       for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                           if (currentUserId.equals(queryDocumentSnapshot.getId())){
                               continue;
                           }
                           User user = new User();
                           user.name= queryDocumentSnapshot.getString(Constans.KEY_NAME);
                           user.email = queryDocumentSnapshot.getString(Constans.KEY_EMAIL);
                           user.image = queryDocumentSnapshot.getString(Constans.KEY_IMAGE);
                           user.token = queryDocumentSnapshot.getString(Constans.KEY_FCM_TOKEN);
                           user.id = queryDocumentSnapshot.getId();
                           users.add(user);
                       }
                       if (users.size() > 0){
                           UserAdapter userAdapter = new UserAdapter(users, this);
                            binding.userRecyclerView.setAdapter(userAdapter);
                            binding.userRecyclerView.setVisibility(View.VISIBLE);
                       }else {
                           showErrorMessage();
                       }
                   }else {
                       showErrorMessage();
                   }
                });
    }

    private void showErrorMessage(){
         binding.teztErrorMessage.setText(String.format("%s","No user available"));
         binding.teztErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isloading ){
        if (isloading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

        @Override
        public void onUserClicked(User user) {
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra(Constans.KEY_USER,user);
            startActivity(intent);
            finish();
        }
    }