package com.example.data.model

enum class RasiId(val index: Int) {
    MESHAM(0),
    RISHABAM(1),
    MITHUNAM(2),
    KADAGAM(3),
    SIMMAM(4),
    KANNI(5),
    THULAAM(6),
    VIRUCHIGAM(7),
    DHANUSU(8),
    MAGARAM(9),
    KUMBAM(10),
    MEENAM(11);

    companion object {
        fun fromIndex(index: Int): RasiId = entries.firstOrNull { it.index == index } ?: MESHAM
    }
}

enum class Language {
    TAMIL,
    ENGLISH
}

enum class ThemeMode {
    SYSTEM,
    DARK,
    LIGHT
}

data class RasiInfo(
    val id: RasiId,
    val nameTamil: String,
    val nameEnglish: String,
    val symbolEmoji: String,
    val lordTamil: String,
    val lordEnglish: String,
    val elementTamil: String,
    val elementEnglish: String,
    val nakshatrasCoveredTamil: String,
    val nakshatrasCoveredEnglish: String,
    val defaultLuckyNumber: Int,
    val defaultLuckyColorTamil: String,
    val defaultLuckyColorEnglish: String,
    val gemStoneTamil: String,
    val gemStoneEnglish: String
)

data class NakshatraInfo(
    val id: Int,
    val nameTamil: String,
    val nameEnglish: String,
    val rasiId: RasiId,
    val lordTamil: String,
    val lordEnglish: String
)

data class PanchangamData(
    val dateString: String,
    val tamilDate: String,
    val tamilYear: String,
    val paksham: String, // சுக்கில பட்சம் / கிருஷ்ண பட்சம்
    val thithi: String,
    val nakshatram: String,
    val yogam: String,
    val karanam: String,
    val nallaNeramMorning: String,
    val nallaNeramEvening: String,
    val gowriNallaNeram: String,
    val rahuKaalam: String,
    val yamagandam: String,
    val kuligai: String,
    val chandirashtamamTamil: String,
    val chandirashtamamEnglish: String,
    val suryodayam: String,
    val suryasthamanam: String,
    val auspiciousThoughtTamil: String,
    val auspiciousThoughtEnglish: String
)

data class HoroscopeItem(
    val rasiId: RasiId,
    val dateKey: String, // "yyyy-MM-dd"
    val dateDisplayTamil: String,
    val dateDisplayEnglish: String,
    val overallPercentage: Int,
    val careerScore: Int,
    val financeScore: Int,
    val healthScore: Int,
    val familyScore: Int,
    val generalTamil: String,
    val generalEnglish: String,
    val careerTamil: String,
    val careerEnglish: String,
    val financeTamil: String,
    val financeEnglish: String,
    val healthTamil: String,
    val healthEnglish: String,
    val familyTamil: String,
    val familyEnglish: String,
    val luckyNumber: Int,
    val luckyColorTamil: String,
    val luckyColorEnglish: String,
    val luckyDirectionTamil: String,
    val luckyDirectionEnglish: String,
    val pariharamTamil: String,
    val pariharamEnglish: String
)
