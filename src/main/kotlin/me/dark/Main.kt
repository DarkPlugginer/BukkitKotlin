package me.dark

import me.dark.commands.AdminCommand
import org.bukkit.craftbukkit.v1_12_R1.CraftServer
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onLoad() {
        super.onLoad()
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(Events, this)
        val craftServer: CraftServer = server as CraftServer
        craftServer.commandMap.register("admin", AdminCommand("admin"))
        super.onEnable()
    }

    override fun onDisable() {
        super.onDisable()
    }
}