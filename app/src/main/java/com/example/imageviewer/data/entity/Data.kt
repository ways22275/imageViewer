package com.example.imageviewer.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Data(

  @PrimaryKey(autoGenerate = true)
  val id: Int,

  @ColumnInfo(name = "description")
  @SerializedName("description")
  val description: String,

  @ColumnInfo(name = "copyright")
  @SerializedName("copyright")
  val copyright: String,

  @ColumnInfo(name = "title")
  @SerializedName("title")
  val title: String,

  @ColumnInfo(name = "url")
  @SerializedName("url")
  val url: String,

  @ColumnInfo(name = "apod_site")
  @SerializedName("apod_site")
  val apodSite: String,

  @ColumnInfo(name = "date")
  @SerializedName("date")
  val date: String,

  @ColumnInfo(name = "media_type")
  @SerializedName("media_type")
  val mediaType: String,

  @ColumnInfo(name = "hdurl")
  @SerializedName("hdurl")
  val hdUrl: String,

  @ColumnInfo(name = "is_favorite")
  val isFavorite: Int? = 0
)
