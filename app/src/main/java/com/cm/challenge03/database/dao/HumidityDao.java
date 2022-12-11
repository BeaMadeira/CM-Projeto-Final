package com.cm.challenge03.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cm.challenge03.database.entities.Humidity;

import java.util.List;

@Dao
public interface HumidityDao {
    @Query("SELECT * FROM humidities")
    List<Humidity> getHumidities();

    @Query("SELECT * FROM humidities ORDER BY date ASC")
    List<Humidity> getHumiditiesOrderByDateAsc();

    @Query("SELECT * FROM humidities ORDER BY date DESC")
    List<Humidity> getHumiditiesOrderByDateDesc();

    @Query("SELECT * FROM humidities WHERE uid IN (:humiditiesIds)")
    List<Humidity> getHumiditiesByIds(int... humiditiesIds);

    @Query("SELECT * FROM humidities WHERE uid IN (:humiditiesIds) ORDER BY date DESC")
    List<Humidity> getHumiditiesByIdsOrderByDateDesc(int... humiditiesIds);

    @Query("SELECT * FROM humidities WHERE uid = :uid")
    Humidity findHumidityById(int uid);

    @Insert
    Long insertHumidity(Humidity humidity);

    @Insert
    List<Long> insertHumidities(Humidity... humidities);

    @Update
    Integer updateHumidity(Humidity humidity);

    @Update
    Integer updateHumidities(Humidity... humidities);

    @Delete
    Integer deleteHumidity(Humidity humidity);

    @Delete
    Integer deleteHumidities(Humidity... humidities);
}