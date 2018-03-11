package me.dark

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AdminManager {

    companion object {
        val admins: ArrayList<UUID>? = ArrayList()
        val inventory: HashMap<UUID, ArrayList<Array<out ItemStack>>> = HashMap()
    }

    fun set(player: Player) {
        System.out.println(admins.toString())
        if (admins?.contains(player.uniqueId)!!) {
            admins.remove(player.uniqueId)

            player.inventory.clear()
            player.inventory.armorContents = null

            player.inventory.contents = inventory[player.uniqueId]!![0]
            player.inventory.armorContents = inventory[player.uniqueId]!![1]

            player.sendTitle("§fModo §c§nADMIN", "§eSaiu")

            Bukkit.getOnlinePlayers().forEach { player1: Player? ->
                if (!player1?.canSee(player)!!)
                    player1.showPlayer(player)
            }

            player.gameMode = GameMode.SURVIVAL
        } else {
            admins.add(player.uniqueId)

            val list: ArrayList<Array<out ItemStack>> = ArrayList()
            list.add(player.inventory.contents)
            list.add(player.inventory.armorContents)
            inventory[player.uniqueId] = list

            Bukkit.getOnlinePlayers().forEach { player1: Player? ->
                player1?.hidePlayer(player)
            }

            player.sendTitle("§fModo §c§nADMIN", "§aEntrou")
            player.gameMode = GameMode.CREATIVE

            player.inventory.setItem(4, createItem("§eJail", "§fPrenda um jogador", Material.BEDROCK))
        }
    }

    fun getAdmins(): ArrayList<UUID>? {
        return admins
    }

    private fun createItem(name: String, desc: String, type: Material): ItemStack {
        var stack = ItemStack(type)
        var meta: ItemMeta? = stack.itemMeta
        meta?.displayName = name
        meta?.lore = Arrays.asList(desc)
        meta?.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        stack.itemMeta = meta
        return stack
    }
}