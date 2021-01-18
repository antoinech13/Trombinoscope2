package com.example.trombinoscope.data;

import android.util.Log;
import android.widget.Toast;

import com.example.trombinoscope.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private String pseudo = "Admin";
    private String psw = "12345678";


    public Result<LoggedInUser> login(String username, String password) {
        if(username.equals(this.pseudo) && password.equals(this.psw)){

            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Admin");
            return new Result.Success<>(fakeUser);
        }
        else{
            return new Result.Error(new IOException("Error logging in"));
        }

        /*try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }*/
    }

    public void logout() {
        // TODO: revoke authentication
    }
}