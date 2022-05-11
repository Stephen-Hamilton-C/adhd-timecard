package app.shamilton.timecard.command

import app.shamilton.timecard.core.TimeEntries
import app.shamilton.timecard.core.TimeEntry
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.rendering.TextColors.*

class LogCommand : ICommand {

	override val m_Name: String = "LOG"

	private val _t = Terminal()

	override fun execute() {
		val timeEntries = TimeEntries.loadFromFile()
		if(timeEntries.m_Entries.isEmpty()){
			_t.println(yellow("You haven't clocked in yet today!"))
			return
		}

		for (entry: TimeEntry in timeEntries.m_Entries) {
			_t.println(green("Clocked ${brightGreen("in")}: ${entry.startTime}"))
			if(entry.endTime != null){
				_t.println(red("Clocked ${brightRed("out")}: ${entry.endTime}"))
			}
		}
	}

}