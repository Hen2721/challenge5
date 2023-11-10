package com.example.challengechapter5.Database.local

import androidx.room.*

@Dao
interface UserDAO {
    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity): Long

    @Delete
    suspend fun deleteUser(userEntity: UserEntity): Int
}
