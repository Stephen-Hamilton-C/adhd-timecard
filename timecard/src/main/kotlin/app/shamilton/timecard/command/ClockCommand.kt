package app.shamilton.timecard.command

import app.shamilton.timecard.App
import app.shamilton.timecard.Color.yellow
import app.shamilton.timecard.serializer.LocalTimeSerializer
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

abstract class ClockCommand : ICommand {

	override val m_HelpArgs: List<String> = listOf("[offset/time]")
	override val m_DetailedHelp: String? = null

	protected fun now(): LocalTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES)

	protected fun getTime(): LocalTime? {
		val arg: String? = App.getArg(1)
		return if(arg == null) {
			now()
		} else if(arg.contains(":")) {
			getTimeFromInput(arg)
		} else {
			getTimeFromOffset(arg)
		}
	}

	private fun getTimeFromOffset(offset: String): LocalTime? {
		// Assume minutes offset
		try {
			val offsetMinutes: Long = offset.toLong().absoluteValue

			// Cannot clock out yesterday
			if (now().toSecondOfDay() - offsetMinutes * 60 < 0){
				println(yellow("You cannot clock ${m_Name.lowercase()} before midnight! Did you forget a colon?"))
				return null
			}

			return now().minusMinutes(offsetMinutes)
		} catch(e: NumberFormatException) {
			println(yellow("Unknown arguments. Use 'timecard help ${m_Name.lowercase()}' for usage."))
			return null
		}
	}

	private fun getTimeFromInput(input: String): LocalTime? {
		try {
			return LocalTimeSerializer.decodeData(input)
		} catch (e: IllegalStateException) {
			// User input error
			println(yellow("Unknown arguments. Use 'timecard help ${m_Name.lowercase()}' for usage."))
			return null
		}
	}

}