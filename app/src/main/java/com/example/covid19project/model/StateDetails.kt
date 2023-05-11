package com.example.covid19project.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class StateDetailsResponse(
    val districtData: List<DistrictData>,
    val state: String
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class DistrictData(
    val confirmed: Int = 0,
    val active: Int = 0,
    val deceased: Int = 0,
    val recovered: Int = 0,
    val delta: Delta,
    val notes: String = "",
    val district: String = ""
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Delta(
    val confirmed: Int = 0,
    val active: Int = 0,
    val deceased: Int = 0,
    val recovered: Int = 0
) : Parcelable
