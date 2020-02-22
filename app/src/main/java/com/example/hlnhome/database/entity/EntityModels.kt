package com.example.hlnhome.database.entity


import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/////////////////////// Main Home Entity ////////////////////

data class HalanHome(
    @SerializedName("data")
    val data: List<ServiceCategories>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)

@Entity(tableName = "home_table")
data class ServiceCategories(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("naming")
    val naming: String,
    @SerializedName("pic")
    val pic: String,
    @SerializedName("prefix")
    val prefix: String,
    @SerializedName("services")
    val services: List<Service>
)

@Parcelize
data class Service(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("pic")
    val pic: String,
    @SerializedName("prefix")
    val prefix: String,
    @SerializedName("terms")
    val terms: String?,
    @SerializedName("termsKey")
    val termsKey: String?,
    @SerializedName("vehicleType")
    val vehicleType: String
) : Parcelable