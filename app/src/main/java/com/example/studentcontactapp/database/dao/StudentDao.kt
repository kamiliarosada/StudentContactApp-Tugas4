package com.example.studentcontactapp.database.dao

import androidx.room.*
import com.example.studentcontactapp.database.entity.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(students: List<StudentEntity>): List<Long>

    @Query("SELECT * FROM students ORDER BY name ASC")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getStudentById(id: Int): StudentEntity?

    @Query("SELECT * FROM students WHERE name LIKE '%' || :keyword || '%' OR nim LIKE '%' || :keyword || '%'")
    fun searchStudents(keyword: String): Flow<List<StudentEntity>>

    @Update
    suspend fun update(student: StudentEntity): Int

    @Query("DELETE FROM students WHERE id = :id")
    suspend fun deleteById(id: Int): Int

    @Query("SELECT COUNT(*) FROM students")
    suspend fun getStudentCount(): Int
}