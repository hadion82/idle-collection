package com.idle.collection.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface IdleDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg values : T): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(values : List<T>): List<Long>

    @Update
    fun update(vararg values : T): Int

    @Update
    fun update(values : List<T>): Int

    @Delete
    fun delete(vararg values : T): Int

    @Delete
    fun delete(values : List<T>): Int
}