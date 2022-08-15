/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data.tutorial

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TutorialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTutorial(tutorial: Tutorial)

    @Query("SELECT * FROM tutorial_db ORDER BY id ASC")
    fun readAllTutorial(): LiveData<List<Tutorial>>

    @Query("UPDATE tutorial_db SET completion = :completion WHERE id = :id")
    suspend fun setCompletion(id: Int, completion: Int)

    @Query("SELECT * FROM tutorial_db WHERE id = :id")
    suspend fun getTutorial(id: Int): Tutorial
}
