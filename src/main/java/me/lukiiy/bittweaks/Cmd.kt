package me.lukiiy.bittweaks

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class Cmd : CommandExecutor {
    override fun onCommand(commandSender: CommandSender, command: Command?, s: String?, strings: Array<String?>?): Boolean {
        commandSender.sendMessage("§aBitTweaks Reload complete!")
        BitTweaks.getInstance().configuration.load()
        return true
    }
}
