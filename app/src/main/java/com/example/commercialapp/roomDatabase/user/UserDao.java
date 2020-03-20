package com.example.commercialapp.roomDatabase.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("select * from user_table")
    LiveData<List<User>> getAllUsers();

    @Query("select * from user_table where username=:username and password=:password")
    LiveData<User> getUserByUsernameAndPassword(String username, String password);

}
