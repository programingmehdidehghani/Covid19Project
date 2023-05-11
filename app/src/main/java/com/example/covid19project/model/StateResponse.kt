package com.example.covid19project.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize


@Parcelize
data class StateResponse(
    @Json(name = "statewise")
    val stateWiseDetails: List<Details>
): Parcelable
