package me.dark.commands

import me.dark.AdminManager
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

class AdminCommand constructor(name: String) : BukkitCommand(name) {

    override fun execute(p0: CommandSender?, p1: String?, p2: Array<out String>?): Boolean {
        if (p0 !is Player) {
            p0?.sendMessage("Apenas jogadores!")
            return true
        }


        if (p1?.toLowerCase().equals("admin")) {
            val player: Player = p0 as Player
            if (!player.hasPermission("comando.admin"))
                return true

            AdminManager().set(player)
        }

        return false
    }
}