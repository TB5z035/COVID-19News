package com.java.zhangjiayou.database;

import androidx.annotation.LongDef;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.java.zhangjiayou.util.Passage;

import java.util.List;

@Dao
public interface PassageDAO {
    @Query("SELECT * FROM passages")
    List<Passage> getAllPassages();

    @Query("SELECT * FROM passages WHERE _id=:id")
    Passage getPassageFromId(String id);

    @Query("SELECT * FROM passages WHERE _id IN (:ids)")
    List<Passage> getPassagesFromIds(List<String> ids);

    @Insert
    void insert(Passage... passages);

    @Update
    void update(Passage... passages);

    @Delete
    void delete(Passage... passages);
}
