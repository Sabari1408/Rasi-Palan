package com.example.ui.components

import android.content.Context
import android.content.Intent
import com.example.data.model.HoroscopeItem
import com.example.data.model.Language
import com.example.data.model.RasiInfo

object ShareHelper {

    fun shareHoroscope(
        context: Context,
        rasiInfo: RasiInfo,
        horoscope: HoroscopeItem,
        language: Language,
        tamilDate: String
    ) {
        val shareText = if (language == Language.TAMIL) {
            """
            🕉️ இன்றைய ${rasiInfo.nameTamil} (${rasiInfo.symbolEmoji}) ராசி பலன் 🌟
            📅 நாள்: $tamilDate
            ------------------------------------
            ✨ பொதுப்பலன்:
            ${horoscope.generalTamil}

            💼 தொழில் & உத்தியோகம்:
            ${horoscope.careerTamil}

            💰 தனம் & வரவு:
            ${horoscope.financeTamil}

            ❤️ குடும்பம் & நிம்மதி:
            ${horoscope.familyTamil}

            🌿 உடல்நலம்:
            ${horoscope.healthTamil}

            🎯 அதிர்ஷ்ட எண்: ${horoscope.luckyNumber}
            🎨 அதிர்ஷ்ட நிறம்: ${horoscope.luckyColorTamil}
            🧭 அதிர்ஷ்ட திசை: ${horoscope.luckyDirectionTamil}

            🙏 இன்றைய பரிகாரம்:
            ${horoscope.pariharamTamil}

            ------------------------------------
            📲 பகிர்வு: தமிழ் ராசி பலன் செயலி (Tamil Rasi Palan)
            வாழ்க வளமுடன்! 🪔
            """.trimIndent()
        } else {
            """
            🕉️ Today's ${rasiInfo.nameEnglish} (${rasiInfo.symbolEmoji}) Horoscope 🌟
            📅 Date: $tamilDate
            ------------------------------------
            ✨ Overview:
            ${horoscope.generalEnglish}

            💼 Career & Business:
            ${horoscope.careerEnglish}

            💰 Wealth & Finance:
            ${horoscope.financeEnglish}

            ❤️ Family & Harmony:
            ${horoscope.familyEnglish}

            🌿 Health:
            ${horoscope.healthEnglish}

            🎯 Lucky Number: ${horoscope.luckyNumber}
            🎨 Lucky Color: ${horoscope.luckyColorEnglish}
            🧭 Lucky Direction: ${horoscope.luckyDirectionEnglish}

            🙏 Sacred Remedy:
            ${horoscope.pariharamEnglish}

            ------------------------------------
            📲 Shared via Tamil Rasi Palan App
            May this day bring peace and prosperity! 🪔
            """.trimIndent()
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val chooserIntent = Intent.createChooser(sendIntent, "பகிர் / Share via")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    fun sharePanchangam(
        context: Context,
        tamilDate: String,
        nallaNeram: String,
        rahuKalam: String,
        yamagandam: String,
        chandirashtamam: String
    ) {
        val text = """
        🪔 இன்றைய தினசரி பஞ்சாங்கம் & சுப நேரங்கள் 🪔
        📅 $tamilDate
        ------------------------------------
        ✨ நல்ல நேரம்: $nallaNeram
        ⚠️ ராகு காலம்: $rahuKalam
        ⚠️ எமகண்டம்: $yamagandam
        🌙 சந்திராஷ்டமம்: $chandirashtamam

        📲 பகிரப்பட்டது: தமிழ் ராசி பலன் செயலி
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val chooser = Intent.createChooser(sendIntent, "பஞ்சாங்கம் பகிர் / Share Panchangam")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }
}
