package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.RasiPalanDatabase
import com.example.data.model.RasiId
import com.example.data.repository.AstrologyDataProvider
import com.example.data.repository.GeminiRasiRepository
import com.example.data.repository.RasiRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Calendar

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun testAppNameResource() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Tamil Rasi Palan", appName)
  }

  @Test
  fun testAstrologyDataProviderHas12RasisAnd27Nakshatras() {
    assertEquals(12, AstrologyDataProvider.allRasis.size)
    assertEquals(27, AstrologyDataProvider.allNakshatras.size)

    val today = Calendar.getInstance()
    val panchangam = AstrologyDataProvider.getPanchangamForDate(today)
    assertNotNull(panchangam.nallaNeramMorning)
    assertNotNull(panchangam.rahuKaalam)

    val horoscope = AstrologyDataProvider.generateHoroscopeForRasi(RasiId.MESHAM, today)
    assertEquals(RasiId.MESHAM, horoscope.rasiId)
    assertTrue(horoscope.overallPercentage in 50..100)
    assertTrue(horoscope.generalTamil.isNotEmpty())
  }

  @Test
  fun testDatePickerCalculationFutureAndPast() {
    val futureCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 5) }
    val futureHoroscope = AstrologyDataProvider.generateHoroscopeForRasi(RasiId.SIMMAM, futureCal)
    assertEquals(RasiId.SIMMAM, futureHoroscope.rasiId)
    assertNotNull(futureHoroscope.dateKey)
    assertTrue(futureHoroscope.luckyNumber in 1..9)

    val pastCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -5) }
    val pastHoroscope = AstrologyDataProvider.generateHoroscopeForRasi(RasiId.KANNI, pastCal)
    assertEquals(RasiId.KANNI, pastHoroscope.rasiId)
    assertNotNull(pastHoroscope.dateKey)
  }

  @Test
  fun testGeminiRasiRepositoryIntegration() = runTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = RasiPalanDatabase.getInstance(context)
    val geminiRepo = GeminiRasiRepository(db.rasiDao(), context)
    val rasiRepo = RasiRepository(db.rasiDao(), geminiRepo)

    val today = Calendar.getInstance()
    val horoscope = rasiRepo.getHoroscope(RasiId.MESHAM, today).first()
    assertNotNull(horoscope)
    assertEquals(RasiId.MESHAM, horoscope.rasiId)
    assertTrue(horoscope.generalTamil.isNotEmpty())
  }
}
