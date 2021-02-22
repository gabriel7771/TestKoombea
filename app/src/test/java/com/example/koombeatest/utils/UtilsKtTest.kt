package com.example.koombeatest.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class UtilsKtTest {

    @Test
    fun shortDateTest() {
        val expectedDate = "Dec 14 2021"
        val date = "Tue Dec 14 2021 00:11:26 GMT-0500 (Colombia Standard Time)"
        val result = shortDate(date)
        assertThat(result).isEqualTo(expectedDate)
    }
}