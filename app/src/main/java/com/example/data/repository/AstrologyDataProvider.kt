package com.example.data.repository

import com.example.data.model.*
import java.text.SimpleDateFormat
import java.util.*

object AstrologyDataProvider {

    val allRasis: List<RasiInfo> = listOf(
        RasiInfo(
            id = RasiId.MESHAM,
            nameTamil = "மேஷம்",
            nameEnglish = "Aries",
            symbolEmoji = "♈",
            lordTamil = "செவ்வாய்",
            lordEnglish = "Mars",
            elementTamil = "நெருப்பு",
            elementEnglish = "Fire",
            nakshatrasCoveredTamil = "அஸ்வினி (1-4), பரணி (1-4), கார்த்திகை (1)",
            nakshatrasCoveredEnglish = "Ashwini (1-4), Bharani (1-4), Krittika (1)",
            defaultLuckyNumber = 9,
            defaultLuckyColorTamil = "சிகப்பு / குங்கும நிறம்",
            defaultLuckyColorEnglish = "Crimson Red",
            gemStoneTamil = "பவளம் (Coral)",
            gemStoneEnglish = "Red Coral"
        ),
        RasiInfo(
            id = RasiId.RISHABAM,
            nameTamil = "ரிஷபம்",
            nameEnglish = "Taurus",
            symbolEmoji = "♉",
            lordTamil = "சுக்கிரன்",
            lordEnglish = "Venus",
            elementTamil = "நிலம்",
            elementEnglish = "Earth",
            nakshatrasCoveredTamil = "கார்த்திகை (2,3,4), ரோகிணி (1-4), மிருகசீரிஷம் (1,2)",
            nakshatrasCoveredEnglish = "Krittika (2-4), Rohini (1-4), Mrigashira (1,2)",
            defaultLuckyNumber = 6,
            defaultLuckyColorTamil = "வெள்ளை / கிரீம்",
            defaultLuckyColorEnglish = "White / Cream",
            gemStoneTamil = "வைரம் (Diamond)",
            gemStoneEnglish = "Diamond / White Zircon"
        ),
        RasiInfo(
            id = RasiId.MITHUNAM,
            nameTamil = "மிதுனம்",
            nameEnglish = "Gemini",
            symbolEmoji = "♊",
            lordTamil = "புதன்",
            lordEnglish = "Mercury",
            elementTamil = "காற்று",
            elementEnglish = "Air",
            nakshatrasCoveredTamil = "மிருகசீரிஷம் (3,4), திருவாதிரை (1-4), புனர்பூசம் (1,2,3)",
            nakshatrasCoveredEnglish = "Mrigashira (3,4), Ardra (1-4), Punarvasu (1-3)",
            defaultLuckyNumber = 5,
            defaultLuckyColorTamil = "பச்சை / இளம்பச்சை",
            defaultLuckyColorEnglish = "Emerald Green",
            gemStoneTamil = "மரகதம் (Emerald)",
            gemStoneEnglish = "Emerald"
        ),
        RasiInfo(
            id = RasiId.KADAGAM,
            nameTamil = "கடகம்",
            nameEnglish = "Cancer",
            symbolEmoji = "♋",
            lordTamil = "சந்திரன்",
            lordEnglish = "Moon",
            elementTamil = "நீர்",
            elementEnglish = "Water",
            nakshatrasCoveredTamil = "புனர்பூசம் (4), பூசம் (1-4), ஆயில்யம் (1-4)",
            nakshatrasCoveredEnglish = "Punarvasu (4), Pushya (1-4), Ashlesha (1-4)",
            defaultLuckyNumber = 2,
            defaultLuckyColorTamil = "முத்து வெள்ளை / வெள்ளி",
            defaultLuckyColorEnglish = "Pearl White / Silver",
            gemStoneTamil = "முத்து (Pearl)",
            gemStoneEnglish = "Natural Pearl"
        ),
        RasiInfo(
            id = RasiId.SIMMAM,
            nameTamil = "சிம்மம்",
            nameEnglish = "Leo",
            symbolEmoji = "♌",
            lordTamil = "சூரியன்",
            lordEnglish = "Sun",
            elementTamil = "நெருப்பு",
            elementEnglish = "Fire",
            nakshatrasCoveredTamil = "மகம் (1-4), பூரம் (1-4), உத்திரம் (1)",
            nakshatrasCoveredEnglish = "Magha (1-4), Purva Phalguni (1-4), Uttara Phalguni (1)",
            defaultLuckyNumber = 1,
            defaultLuckyColorTamil = "மாணிக்க சிவப்பு / ஆரஞ்சு",
            defaultLuckyColorEnglish = "Ruby Red / Gold",
            gemStoneTamil = "மாணிக்கம் (Ruby)",
            gemStoneEnglish = "Ruby"
        ),
        RasiInfo(
            id = RasiId.KANNI,
            nameTamil = "கன்னி",
            nameEnglish = "Virgo",
            symbolEmoji = "♍",
            lordTamil = "புதன்",
            lordEnglish = "Mercury",
            elementTamil = "நிலம்",
            elementEnglish = "Earth",
            nakshatrasCoveredTamil = "உத்திரம் (2,3,4), அஸ்தம் (1-4), சித்திரை (1,2)",
            nakshatrasCoveredEnglish = "Uttara Phalguni (2-4), Hasta (1-4), Chitra (1,2)",
            defaultLuckyNumber = 5,
            defaultLuckyColorTamil = "கிளிப்பச்சை / சந்தனம்",
            defaultLuckyColorEnglish = "Parrot Green / Sandal",
            gemStoneTamil = "மரகதம் (Emerald)",
            gemStoneEnglish = "Emerald"
        ),
        RasiInfo(
            id = RasiId.THULAAM,
            nameTamil = "துலாம்",
            nameEnglish = "Libra",
            symbolEmoji = "♎",
            lordTamil = "சுக்கிரன்",
            lordEnglish = "Venus",
            elementTamil = "காற்று",
            elementEnglish = "Air",
            nakshatrasCoveredTamil = "சித்திரை (3,4), சுவாதி (1-4), விசாகம் (1,2,3)",
            nakshatrasCoveredEnglish = "Chitra (3,4), Swati (1-4), Vishakha (1-3)",
            defaultLuckyNumber = 7,
            defaultLuckyColorTamil = "வெள்ளை / வெளிர் நீலம்",
            defaultLuckyColorEnglish = "Sky Blue / White",
            gemStoneTamil = "வைரம் (Diamond)",
            gemStoneEnglish = "Diamond"
        ),
        RasiInfo(
            id = RasiId.VIRUCHIGAM,
            nameTamil = "விருச்சிகம்",
            nameEnglish = "Scorpio",
            symbolEmoji = "♏",
            lordTamil = "செவ்வாய்",
            lordEnglish = "Mars",
            elementTamil = "நீர்",
            elementEnglish = "Water",
            nakshatrasCoveredTamil = "விசாகம் (4), அனுஷம் (1-4), கேட்டை (1-4)",
            nakshatrasCoveredEnglish = "Vishakha (4), Anuradha (1-4), Jyeshtha (1-4)",
            defaultLuckyNumber = 9,
            defaultLuckyColorTamil = "ஆழ்ந்த சிவப்பு / மெரூன்",
            defaultLuckyColorEnglish = "Deep Maroon / Red",
            gemStoneTamil = "பவளம் (Coral)",
            gemStoneEnglish = "Red Coral"
        ),
        RasiInfo(
            id = RasiId.DHANUSU,
            nameTamil = "தனுசு",
            nameEnglish = "Sagittarius",
            symbolEmoji = "♐",
            lordTamil = "குரு",
            lordEnglish = "Jupiter",
            elementTamil = "நெருப்பு",
            elementEnglish = "Fire",
            nakshatrasCoveredTamil = "மூலம் (1-4), பூராடம் (1-4), உத்திராடம் (1)",
            nakshatrasCoveredEnglish = "Mula (1-4), Purva Ashadha (1-4), Uttara Ashadha (1)",
            defaultLuckyNumber = 3,
            defaultLuckyColorTamil = "மஞ்சள் / பொன் நிறம்",
            defaultLuckyColorEnglish = "Golden Yellow",
            gemStoneTamil = "புஷ்பராகம் (Yellow Sapphire)",
            gemStoneEnglish = "Yellow Sapphire"
        ),
        RasiInfo(
            id = RasiId.MAGARAM,
            nameTamil = "மகரம்",
            nameEnglish = "Capricorn",
            symbolEmoji = "♑",
            lordTamil = "சனி",
            lordEnglish = "Saturn",
            elementTamil = "நிலம்",
            elementEnglish = "Earth",
            nakshatrasCoveredTamil = "உத்திராடம் (2,3,4), திருவோணம் (1-4), அவிட்டம் (1,2)",
            nakshatrasCoveredEnglish = "Uttara Ashadha (2-4), Shravana (1-4), Dhanishta (1,2)",
            defaultLuckyNumber = 8,
            defaultLuckyColorTamil = "நீலம் / அடர் நீலம்",
            defaultLuckyColorEnglish = "Dark Blue / Indigo",
            gemStoneTamil = "நீலக்கல் (Blue Sapphire)",
            gemStoneEnglish = "Blue Sapphire"
        ),
        RasiInfo(
            id = RasiId.KUMBAM,
            nameTamil = "கும்பம்",
            nameEnglish = "Aquarius",
            symbolEmoji = "♒",
            lordTamil = "சனி",
            lordEnglish = "Saturn",
            elementTamil = "காற்று",
            elementEnglish = "Air",
            nakshatrasCoveredTamil = "அவிட்டம் (3,4), சதயம் (1-4), பூரட்டாதி (1,2,3)",
            nakshatrasCoveredEnglish = "Dhanishta (3,4), Shatabhisha (1-4), Purva Bhadrapada (1-3)",
            defaultLuckyNumber = 4,
            defaultLuckyColorTamil = "கருநீலம் / ஊதா",
            defaultLuckyColorEnglish = "Violet / Royal Navy",
            gemStoneTamil = "நீலக்கல் (Blue Sapphire)",
            gemStoneEnglish = "Blue Sapphire"
        ),
        RasiInfo(
            id = RasiId.MEENAM,
            nameTamil = "மீனம்",
            nameEnglish = "Pisces",
            symbolEmoji = "♓",
            lordTamil = "குரு",
            lordEnglish = "Jupiter",
            elementTamil = "நீர்",
            elementEnglish = "Water",
            nakshatrasCoveredTamil = "பூரட்டாதி (4), உத்திரட்டாதி (1-4), ரேவதி (1-4)",
            nakshatrasCoveredEnglish = "Purva Bhadrapada (4), Uttara Bhadrapada (1-4), Revati (1-4)",
            defaultLuckyNumber = 3,
            defaultLuckyColorTamil = "மஞ்சள் / குங்குமப்பூ நிறம்",
            defaultLuckyColorEnglish = "Saffron Gold / Yellow",
            gemStoneTamil = "புஷ்பராகம் (Yellow Sapphire)",
            gemStoneEnglish = "Yellow Sapphire"
        )
    )

    val allNakshatras: List<NakshatraInfo> = listOf(
        NakshatraInfo(0, "அஸ்வினி", "Ashwini", RasiId.MESHAM, "கேது", "Ketu"),
        NakshatraInfo(1, "பரணி", "Bharani", RasiId.MESHAM, "சுக்கிரன்", "Venus"),
        NakshatraInfo(2, "கார்த்திகை", "Krittika", RasiId.MESHAM, "சூரியன்", "Sun"),
        NakshatraInfo(3, "ரோகிணி", "Rohini", RasiId.RISHABAM, "சந்திரன்", "Moon"),
        NakshatraInfo(4, "மிருகசீரிஷம்", "Mrigashira", RasiId.RISHABAM, "செவ்வாய்", "Mars"),
        NakshatraInfo(5, "திருவாதிரை", "Thiruvathirai (Ardra)", RasiId.MITHUNAM, "ராகு", "Rahu"),
        NakshatraInfo(6, "புனர்பூசம்", "Punarpoosam", RasiId.MITHUNAM, "குரு", "Jupiter"),
        NakshatraInfo(7, "பூசம்", "Poosam", RasiId.KADAGAM, "சனி", "Saturn"),
        NakshatraInfo(8, "ஆயில்யம்", "Ayilyam", RasiId.KADAGAM, "புதன்", "Mercury"),
        NakshatraInfo(9, "மகம்", "Magam", RasiId.SIMMAM, "கேது", "Ketu"),
        NakshatraInfo(10, "பூரம்", "Pooram", RasiId.SIMMAM, "சுக்கிரன்", "Venus"),
        NakshatraInfo(11, "உத்திரம்", "Uthiram", RasiId.SIMMAM, "சூரியன்", "Sun"),
        NakshatraInfo(12, "அஸ்தம்", "Hastham", RasiId.KANNI, "சந்திரன்", "Moon"),
        NakshatraInfo(13, "சித்திரை", "Chithirai", RasiId.KANNI, "செவ்வாய்", "Mars"),
        NakshatraInfo(14, "சுவாதி", "Swathi", RasiId.THULAAM, "ராகு", "Rahu"),
        NakshatraInfo(15, "விசாகம்", "Visakam", RasiId.THULAAM, "குரு", "Jupiter"),
        NakshatraInfo(16, "அனுஷம்", "Anusham", RasiId.VIRUCHIGAM, "சனி", "Saturn"),
        NakshatraInfo(17, "கேட்டை", "Kettai", RasiId.VIRUCHIGAM, "புதன்", "Mercury"),
        NakshatraInfo(18, "மூலம்", "Moolam", RasiId.DHANUSU, "கேது", "Ketu"),
        NakshatraInfo(19, "பூராடம்", "Pooradam", RasiId.DHANUSU, "சுக்கிரன்", "Venus"),
        NakshatraInfo(20, "உத்திராடம்", "Uthiradam", RasiId.DHANUSU, "சூரியன்", "Sun"),
        NakshatraInfo(21, "திருவோணம்", "Thiruvonam", RasiId.MAGARAM, "சந்திரன்", "Moon"),
        NakshatraInfo(22, "அவிட்டம்", "Avittam", RasiId.MAGARAM, "செவ்வாய்", "Mars"),
        NakshatraInfo(23, "சதயம்", "Sathayam", RasiId.KUMBAM, "ராகு", "Rahu"),
        NakshatraInfo(24, "பூரட்டாதி", "Poorattathi", RasiId.KUMBAM, "குரு", "Jupiter"),
        NakshatraInfo(25, "உத்திரட்டாதி", "Uthirattathi", RasiId.MEENAM, "சனி", "Saturn"),
        NakshatraInfo(26, "ரேவதி", "Revathi", RasiId.MEENAM, "புதன்", "Mercury")
    )

    private val tamilMonths = listOf(
        "சித்திரை", "வைகாசி", "ஆனி", "ஆடி", "ஆவணி", "புரட்டாசி",
        "ஐப்பசி", "கார்த்திகை", "மார்கழி", "தை", "மாசி", "பங்குனி"
    )

    private val tamilWeekdays = listOf(
        "ஞாயிற்றுக்கிழமை", "திங்கட்கிழமை", "செவ்வாய்க்கிழமை",
        "புதன்கிழமை", "வியாழக்கிழமை", "வெள்ளிக்கிழமை", "சனிக்கிழமை"
    )

    fun getPanchangamForDate(calendar: Calendar = Calendar.getInstance()): PanchangamData {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy, EEEE", Locale.ENGLISH)
        val dateString = dateFormat.format(calendar.time)

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 1 = Sunday, 7 = Saturday
        val dayIndex0 = dayOfWeek - 1
        val weekdayTamil = tamilWeekdays[dayIndex0]

        val monthGregorian = calendar.get(Calendar.MONTH) // 0 = Jan
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

        // Tamil solar month approximation
        val tamilMonthIndex = ((monthGregorian + 8) % 12)
        val tamilMonthName = tamilMonths[tamilMonthIndex]
        val tamilDateNumber = (dayOfMonth + 5) % 30 + 1
        val tamilDate = "$tamilMonthName $tamilDateNumber, $weekdayTamil"

        // Authentic Tamil timing tables according to day of week
        val (rahu, yama, kuli) = when (dayOfWeek) {
            Calendar.SUNDAY -> Triple("04:30 PM - 06:00 PM", "12:00 PM - 01:30 PM", "03:00 PM - 04:30 PM")
            Calendar.MONDAY -> Triple("07:30 AM - 09:00 AM", "10:30 AM - 12:00 PM", "01:30 PM - 03:00 PM")
            Calendar.TUESDAY -> Triple("03:00 PM - 04:30 PM", "09:00 AM - 10:30 AM", "12:00 PM - 01:30 PM")
            Calendar.WEDNESDAY -> Triple("12:00 PM - 01:30 PM", "07:30 AM - 09:00 AM", "10:30 AM - 12:00 PM")
            Calendar.THURSDAY -> Triple("01:30 PM - 03:00 PM", "06:00 AM - 07:30 AM", "09:00 AM - 10:30 AM")
            Calendar.FRIDAY -> Triple("10:30 AM - 12:00 PM", "03:00 PM - 04:30 PM", "07:30 AM - 09:00 AM")
            else -> Triple("09:00 AM - 10:30 AM", "01:30 PM - 03:00 PM", "06:00 AM - 07:30 AM")
        }

        val chandirashtamamRasisTamil = listOf(
            "விருச்சிகம் (அனுஷம்)", "தனுசு (மூலம்)", "மகரம் (திருவோணம்)",
            "கும்பம் (சதயம்)", "மீனம் (ரேவதி)", "மேஷம் (பரணி)", "ரிஷபம் (ரோகிணி)"
        )
        val chandirashtamamRasisEnglish = listOf(
            "Scorpio (Anuradha)", "Sagittarius (Mula)", "Capricorn (Shravana)",
            "Aquarius (Shatabhisha)", "Pisces (Revati)", "Aries (Bharani)", "Taurus (Rohini)"
        )
        val cycleIndex = (dayOfYear + dayOfWeek) % 7

        val paksham = if ((dayOfMonth % 30) < 15) "வளர்பிறை (சுக்கில பட்சம்)" else "தேய்பிறை (கிருஷ்ண பட்சம்)"
        val thithis = listOf("பிரதமை", "துவிதியை", "திரிதியை", "சதுர்த்தி", "பஞ்சமி", "சஷ்டி", "சப்தமி", "அஷ்டமி", "நவமி", "தசமி", "ஏகாதசி", "துவாதசி", "திரயோதசி", "சதுர்த்தசி", "பௌர்ணமி / அமாவாசை")
        val currentThithi = thithis[dayOfMonth % thithis.size]
        val currentNakshatra = allNakshatras[dayOfYear % allNakshatras.size]

        return PanchangamData(
            dateString = dateString,
            tamilDate = tamilDate,
            tamilYear = "சுபகிருது / குரோதி வருடம்",
            paksham = paksham,
            thithi = "$currentThithi திதி",
            nakshatram = "${currentNakshatra.nameTamil} நட்சத்திரம்",
            yogam = if (dayOfYear % 2 == 0) "சித்த யோகம்" else "அமிர்த யோகம்",
            karanam = "பவ கரணம்",
            nallaNeramMorning = "காலை 09:15 - 10:15",
            nallaNeramEvening = "மாலை 04:45 - 05:45",
            gowriNallaNeram = "காலை 10:30 - 11:30 & மாலை 06:30 - 07:30",
            rahuKaalam = rahu,
            yamagandam = yama,
            kuligai = kuli,
            chandirashtamamTamil = chandirashtamamRasisTamil[cycleIndex],
            chandirashtamamEnglish = chandirashtamamRasisEnglish[cycleIndex],
            suryodayam = "காலை 06:06 AM",
            suryasthamanam = "மாலை 06:02 PM",
            auspiciousThoughtTamil = "இன்றைய தேதியில் சுப காரியங்களை நல்ல நேரத்தில் தொடங்கவும். சிவ-விஷ்ணு வழிபாடு காரிய சித்தி தரும்.",
            auspiciousThoughtEnglish = "Utilize auspicious Nalla Neram windows for new beginnings. Prayers bring peace and mental clarity."
        )
    }

    fun generateHoroscopeForRasi(rasiId: RasiId, calendar: Calendar): HoroscopeItem {
        val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.time)
        val dateFormatEnglish = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH)
        val dateDisplayEnglish = dateFormatEnglish.format(calendar.time)

        val panchang = getPanchangamForDate(calendar)
        val dateDisplayTamil = panchang.tamilDate

        val rasi = allRasis.first { it.id == rasiId }
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // Seeded pseudorandom astrological calculation for the date
        val hash = (dayOfYear * 31 + rasiId.index * 17 + dayOfWeek * 7)
        val baseScore = 70 + (hash % 26) // 70..95

        val careerScore = (baseScore + 4).coerceIn(60, 98)
        val financeScore = (baseScore - 2).coerceIn(60, 96)
        val healthScore = (baseScore + 3).coerceIn(65, 95)
        val familyScore = (baseScore + 5).coerceIn(70, 99)

        val todayCalendar = Calendar.getInstance()
        val isToday = (todayCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                todayCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR))
        val isFuture = calendar.after(todayCalendar)

        val dateContextPrefixTamil = when {
            isToday -> "இன்றைய நாள்"
            isFuture -> "தேர்ந்தெடுக்கப்பட்ட இந்த எதிர்கால நாளில்"
            else -> "இந்த தேதியில்"
        }

        val dateContextPrefixEnglish = when {
            isToday -> "Today,"
            isFuture -> "On this chosen upcoming date,"
            else -> "On this date,"
        }

        val luckyNo = (rasi.defaultLuckyNumber + dayOfWeek) % 9 + 1

        return HoroscopeItem(
            rasiId = rasiId,
            dateKey = dateKey,
            dateDisplayTamil = dateDisplayTamil,
            dateDisplayEnglish = dateDisplayEnglish,
            overallPercentage = baseScore,
            careerScore = careerScore,
            financeScore = financeScore,
            healthScore = healthScore,
            familyScore = familyScore,
            generalTamil = "$dateContextPrefixTamil ${rasi.nameTamil} ராசி அன்பர்களே! சந்திரனின் சுப பார்வை உங்கள் ராசியை வளப்படுத்துகிறது. திட்டமிட்ட காரியங்கள் யாவும் எதிர்பார்த்தபடி சுமுகமாக முடியும். புதிய வாய்ப்புகள் தேடி வரும்.",
            generalEnglish = "$dateContextPrefixEnglish ${rasi.nameEnglish} natives experience supportive lunar alignments. Scheduled endeavors will reach fruitful conclusions with high clarity.",
            careerTamil = "தொழில் மற்றும் உத்தியோகத்தில் சக ஊழியர்களின் ஒத்துழைப்பு சிறப்பாக இருக்கும். மேலதிகாரிகள் உங்கள் உழைப்பை அங்கீகரிப்பார்கள். வியாபாரத்தில் புதிய ஒப்பந்தங்கள் கைகூடும்.",
            careerEnglish = "Professional workspace atmosphere is collaborative and productive. Superiors recognize your contributions. Business negotiations lean favorably.",
            financeTamil = "பணவரவு சீராக இருக்கும். பழைய கடன் பாக்கிகள் வசூலாகும் சூழல் உண்டு. அநாவசிய ஆடம்பர செலவுகளை தவிர்ப்பது எதிர்கால சேமிப்பிற்கு வழிவகுக்கும்.",
            financeEnglish = "Inflow of funds remains steady and encouraging. Pending arrears will be received. Prudent expenditure preserves capital.",
            healthTamil = "உடல்நலம் சீராகவும் உற்சாகத்துடனும் இருக்கும். முறையான உணவு பழக்கமும் தியானமும் மன அமைதியை நிலைநிறுத்தும்.",
            healthEnglish = "Physical wellness and mental vitality remain strong. Balanced diet and mindful meditation ensure inner peace.",
            familyTamil = "குடும்பத்தில் அமைதியும் மகிழ்ச்சியும் நிலவும். வாழ்க்கைத் துணையின் ஆலோசனைகள் பயனுள்ளதாக அமையும். சுப பேச்சுவார்த்தைகள் தொடங்கும்.",
            familyEnglish = "Warm harmony prevails across family members. Spousal guidance offers peace of mind and domestic satisfaction.",
            luckyNumber = luckyNo,
            luckyColorTamil = rasi.defaultLuckyColorTamil,
            luckyColorEnglish = rasi.defaultLuckyColorEnglish,
            luckyDirectionTamil = "கிழக்கு & வடகிழக்கு",
            luckyDirectionEnglish = "East & North-East",
            pariharamTamil = rasi.pariharamTamil(),
            pariharamEnglish = rasi.pariharamEnglish()
        )
    }

    private fun RasiInfo.pariharamTamil(): String = when (id) {
        RasiId.MESHAM -> "செவ்வாய்க்கிழமை முருகப்பெருமானை வணங்கி கந்த சஷ்டி கவசம் பாராயணம் செய்யவும்."
        RasiId.RISHABAM -> "வெள்ளிக்கிழமை ஸ்ரீ மகாலட்சுமிக்கு மல்லிகை மலர் சாற்றி நெய்தீபம் ஏற்றி வழிபடவும்."
        RasiId.MITHUNAM -> "புதன்கிழமை பெருமாள் கோயிலில் துளசி அர்ச்சனை செய்து பசுவுக்கு அகத்திக்கீரை வழங்கவும்."
        RasiId.KADAGAM -> "திங்கட்கிழமை சிவபெருமானுக்கு பால் அபிஷேகம் செய்து ஓம் நமசிவாய ஜெபிக்கவும்."
        RasiId.SIMMAM -> "ஞாயிற்றுக்கிழமை காலையில் சூரிய பகவானுக்கு ஆதித்ய ஹிருதய ஸ்தோத்திரம் கூறி வழிபடவும்."
        RasiId.KANNI -> "புதன்கிழமை ஸ்ரீ ஐயப்பன் அல்லது விநாயகருக்கு அருகம்புல் சாற்றி வழிபடவும்."
        RasiId.THULAAM -> "வெள்ளிக்கிழமை அம்பாளுக்கு குங்கும அர்ச்சனை செய்து லலிதா சகஸ்ரநாமம் கேட்கவும்."
        RasiId.VIRUCHIGAM -> "செவ்வாயன்று துர்க்கை அம்மனுக்கு எலுமிச்சை தீபம் ஏற்றி வழிபட காரியத்தடை நீங்கும்."
        RasiId.DHANUSU -> "வியாழக்கிழமை தட்சிணாமூர்த்திக்கு கொண்டைக்கடலை மாலை அணிவித்து வழிபடவும்."
        RasiId.MAGARAM -> "சனிக்கிழமை ஆஞ்சநேயருக்கு வெண்ணெய் காப்பு அல்லது எள் தீபம் ஏற்றி வழிபடவும்."
        RasiId.KUMBAM -> "சனிக்கிழமை ஏழை எளியவர்களுக்கு அன்னதானம் செய்து நவகிரக சனீஸ்வரரை வணங்கவும்."
        RasiId.MEENAM -> "வியாழக்கிழமை மகாவிஷ்ணு மற்றும் குரு பகவானை வணங்கி மஞ்சள் மலர் சாற்றவும்."
    }

    private fun RasiInfo.pariharamEnglish(): String = when (id) {
        RasiId.MESHAM -> "Pray to Lord Muruga on Tuesday and chant Kanda Sashti Kavasam for courage."
        RasiId.RISHABAM -> "Offer fragrant jasmine flowers to Goddess Mahalakshmi on Friday with ghee lamps."
        RasiId.MITHUNAM -> "Offer Tulsi garlands at Vishnu temple and feed green grass to cows on Wednesday."
        RasiId.KADAGAM -> "Perform milk abhishekam to Lord Shiva on Monday and chant Om Namah Shivaya."
        RasiId.SIMMAM -> "Offer morning salutations to the Sun God with Aditya Hridaya Stotram on Sunday."
        RasiId.KANNI -> "Offer sacred Arugampul grass to Lord Ganesha on Wednesday morning."
        RasiId.THULAAM -> "Offer kumkum archana to Goddess Lalitha Ambika on Friday for grace."
        RasiId.VIRUCHIGAM -> "Light lemon oil lamps for Goddess Durga on Tuesday to dissolve hurdles."
        RasiId.DHANUSU -> "Offer yellow gram garland to Lord Dakshinamurthy on Thursday."
        RasiId.MAGARAM -> "Offer sesame oil deepam or butter to Lord Hanuman on Saturday."
        RasiId.KUMBAM -> "Offer food charity to needy on Saturday and seek blessings from Lord Shani."
        RasiId.MEENAM -> "Worship Lord Maha Vishnu and Guru Bhagavan on Thursday with yellow flowers."
    }
}
