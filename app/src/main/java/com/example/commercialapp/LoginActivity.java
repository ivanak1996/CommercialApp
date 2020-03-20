package com.example.commercialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.commercialapp.roomDatabase.user.User;
import com.example.commercialapp.roomDatabase.user.UserViewModel;

public class LoginActivity extends AppCompatActivity implements AsyncResponse {

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView messageTextView = findViewById(R.id.text_view_message);
        messageTextView.setText("");

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    public void onClickLogin(View view) {
        EditText usernameEditText = findViewById(R.id.edit_text_username);
        EditText passwordEditText = findViewById(R.id.edit_text_password);

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        new GetUserFromApiAsyncTask(this, username, password).execute();

    }

    @Override
    public void processFinish(User user) {
        if (user != null) {
            // save to db
            userViewModel.insert(user);
            // proceed to next activity
            Intent intent = new Intent(this, ProductListActivity.class);
            startActivity(intent);
        } else {
            // mark as error
            TextView messageTextView = findViewById(R.id.text_view_message);
            messageTextView.setText("Wrong username or password");
        }
    }

    private static class GetUserFromApiAsyncTask extends AsyncTask<Void, Void, User> {

        private AsyncResponse delegate;
        private String username;
        private String password;

        public GetUserFromApiAsyncTask(AsyncResponse delegate, String username, String password) {
            this.delegate = delegate;
            this.username = username;
            this.password = password;
        }

        @Override
        protected User doInBackground(Void... voids) {
            User user = JsonParser.getUserFromApi(username, password);
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            delegate.processFinish(user);
        }
    }
}
