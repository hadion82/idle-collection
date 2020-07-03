package com.idle.collection.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import io.reactivex.Single

interface ReactDao<T>: IdleDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReactive(vararg values : T): Single<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReactive(values : List<T>): Single<List<Long>>

    @Update
    fun updateReactive(vararg values : T): Single<Int>

    @Update
    fun updateReactive(values : List<T>): Single<Int>

    @Delete
    fun deleteReactive(vararg values : T): Single<Int>

    @Delete
    fun deleteReactive(values : List<T>): Single<Int>
}