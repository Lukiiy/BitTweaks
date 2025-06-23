package me.lukiiy.bittweaks

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class Cmd : CommandExecutor {
    override fun onCommand(commandSender: CommandSender, command: Command?, s: String?, strings: Array<String?>?): Boolean {
        commandSender.sendMessage("§aBitTweaks Reload complete! §6Some changes may require a server reboot!")
        BitTweaks.getInstance().configuration.load()
        return true
    }
}
