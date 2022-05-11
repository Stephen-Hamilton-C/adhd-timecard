package app.shamilton.timecard.command

import app.shamilton.timecard.core.TimeEntries
import app.shamilton.timecard.core.TimeEntry
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.terminal.Terminal

class UndoCommand : ICommand {

	override val m_Name: String = "UNDO"
	override val m_Help: String = "" // TODO

	private val _t = Terminal()

	override fun execute() {
		val timeEntries = TimeEntries.loadFromFile()
		if (timeEntries.m_Entries.isEmpty()){
			_t.println(yellow("Nothing to undo!"))
			println("Clock in first using 'timecard in'.")
			return
		}

		if(timeEntries.isClockedIn()){
			val removedEntry: TimeEntry = timeEntries.m_Entries.removeLast()
			_t.println("Undo: clock ${green("in")} at ${green(removedEntry.startTime.toString())}")

			val lastEntry: TimeEntry = timeEntries.m_Entries.last()
			_t.println(red("Clocked out since ${lastEntry.endTime}"))

			if(timeEntries.m_Entries.isEmpty())
				timeEntries.deleteFile()
			else
				timeEntries.saveToFile()
		} else {
			val lastEntry: TimeEntry = timeEntries.m_Entries.last()
			_t.println("Undo: clock ${red("out")} at ${red(lastEntry.endTime.toString())}")
			_t.println(green("Clocked in since ${lastEntry.startTime}"))

			lastEntry.endTime = null
			timeEntries.saveToFile()
		}
	}

}