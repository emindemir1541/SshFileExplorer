package com.emindev.sshfileexplorer.helperlibrary.common.helper


import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.TextStyle
import java.util.*


@SuppressLint("SimpleDateFormat")
object DateUtil {

    private const val PATTERN_TR = "dd/MM/yyyy HH:mm"
    private const val PATTERN_EU = "yyyy-MM-dd"

    val currentTime: Long
        get() = LocalDateTime.now().toLong()
    val currentDateTime: LocalDateTime
        get() = LocalDateTime.now()

    //Helpers
     val formatter = SimpleDateFormat(PATTERN_TR)
     fun splitDate(date: String): MutableList<String> = date.split("/").toMutableList()

    //Conversations
    fun convertToString(day: Int, month: Int, year: Int): String = "$day/$month/$year "
    fun convertToString(timeInMillis: Long): String = android.text.format.DateFormat.format(PATTERN_TR, timeInMillis).toString()
    fun convertToString(dateTime: LocalDateTime): String = "${dateTime.dayOfMonth}/${dateTime.monthValue}/${dateTime.year}"
    fun convertToString(dateTime: LocalDate): String = "${dateTime.dayOfMonth}/${dateTime.monthValue}/${dateTime.year}"
    private fun convertToStringEu(timeInMillis: Long): String = android.text.format.DateFormat.format(PATTERN_EU, timeInMillis).toString()

    fun convertToDateTime(dateTime: Long): LocalDateTime = Instant.ofEpochMilli(dateTime).atZone(ZoneId.systemDefault()).toLocalDateTime()
    fun convertToDateTime(date: LocalDate, time: LocalTime = LocalTime.of(0, 0, 0)): LocalDateTime = LocalDateTime.of(date, time)

    /*fun convertToDateTime(getSavedDate:String)=*/
    fun convertToDateTime(year: Int, month: Int, dayOfMonth: Int, hour: Int = 0, minute: Int = 0, second: Int = 0, nanoOfSecond: Int = 0): LocalDateTime =
        LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond).atZone(ZoneId.systemDefault()).toLocalDateTime()


    fun LocalDateTime.toLong(): Long = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    fun LocalDate.toLong(): Long = convertToDateTime(this).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    fun toLong(year: Int, month: Int, dayOfMonth: Int, hour: Int = 0, minute: Int = 0, second: Int = 0, nanoOfSecond: Int = 0): Long =
        convertToDateTime(year, month, dayOfMonth, hour, minute, second, nanoOfSecond).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()


    //addLesson and subtract
    fun plusMonth(dateTime: LocalDateTime, months: Long): Long = dateTime.plusMonths(months).toLong()
    fun plusMonth(dateTime: Long, months: Long): Long = convertToDateTime(dateTime).plusMonths(months).toLong()
    fun minusMonth(dateTime: LocalDateTime, months: Long): Long = dateTime.minusMonths(months).toLong()
    fun minusMonth(dateTime: Long, months: Long): Long = convertToDateTime(dateTime).plusMonths(months).toLong()

    fun LocalDateTime.monthString(): String = this.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    fun LocalDate.monthString(): String = this.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    fun getMonth(localDateTime: Long): String = convertToDateTime(localDateTime).month.getDisplayName(TextStyle.FULL, Locale.getDefault())

    fun LocalDateTime.isToday(): Boolean {
        val date = convertToDateTime(currentTime)
        return this.dayOfMonth == date.dayOfMonth && this.month == date.month && this.year == date.year
    }

    fun Long.ifToday(): Boolean {
        val currentDate = convertToDateTime(currentTime)
        val selectedDate = convertToDateTime(this)
        return currentDate.toLocalDate() == selectedDate.toLocalDate()
    }

    //Month And Year
    fun LocalDateTime.checkMonthAndYear(localDateTime: LocalDateTime): Boolean = localDateTime.monthValue == this.monthValue && localDateTime.year == this.year
    fun LocalDateTime.checkMonthAndYear(localDateTime: LocalDate): Boolean = localDateTime.monthValue == this.monthValue && localDateTime.year == this.year
    fun LocalDate.checkMonthAndYear(localDateTime: LocalDate): Boolean = localDateTime.monthValue == this.monthValue && localDateTime.year == this.year
    fun LocalDate.checkMonthAndYear(localDateTime: LocalDateTime): Boolean = localDateTime.monthValue == this.monthValue && localDateTime.year == this.year
    fun LocalDate.toDateTime(): LocalDateTime = convertToDateTime(this)


    //difference between two getSavedDate
    fun dayBetweenTwoDate(dateTimeA: LocalDateTime, dateTimeB: LocalDateTime) = java.util.concurrent.TimeUnit.DAYS.convert(dateTimeA.toLong() - dateTimeB.toLong(), java.util.concurrent.TimeUnit.MILLISECONDS)
    fun dayBetweenTwoDate(dateTimeA: LocalDate, dateTimeB: LocalDate) = java.util.concurrent.TimeUnit.DAYS.convert(dateTimeA.toLong() - dateTimeB.toLong(), java.util.concurrent.TimeUnit.MILLISECONDS)
    fun dayBetweenTwoDate(dateTimeA: Long, dateTimeB: Long) = java.util.concurrent.TimeUnit.DAYS.convert(dateTimeA - dateTimeB, java.util.concurrent.TimeUnit.MILLISECONDS)


//fun getLocalDate(): String = DateTimeFormatter.ofPattern(UTC_TR).format(LocalDate.now())
/*  fun dateToMonthAndYear(dateLong: String): String {
    val splintedDate = dateLong.split("/")
    return splintedDate[1] + "/" + splintedDate[2]
}*/
//fun convertToString(day: Int, month: Int, year: Int): String = "$day/$month/$year"
//fun convertToTimeInMillis(dateLong: String): Long = convertToDate(dateLong).time
//fun convertToDate(dateLong: String): java.util.Date = formatter().parse(dateLong)
//fun convertToDate(dateLong:Long):java.util.Date= convertToDate(convertToString(dateLong))

}







