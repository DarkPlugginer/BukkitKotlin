package me.dark

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

object Events : Listener {

    private val adminManager: AdminManager = AdminManager()

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        var player = event.player

        if (!(player.hasPermission("admin.veroutros"))) {
            adminManager.getAdmins()!!.forEach { uuid: UUID ->
                player.hidePlayer(Bukkit.getPlayer(uuid))
            }
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        var player = event.player

        if (adminManager.getAdmins()!!.contains(player.uniqueId)) {
            if (player.itemInHand.hasItemMeta()) {
                if (player.itemInHand.type == Material.BEDROCK) {
                    player.openInventory.close()

                    val players: Inventory = Bukkit.createInventory(null, 54, "§aJogadores online")

                    Bukkit.getOnlinePlayers().forEach { player1: Player? ->
                        if (!(adminManager.getAdmins()!!.contains(player1?.uniqueId)) && player != player1) {
                            var head = ItemStack(Material.SKULL_ITEM)
                            head.durability = 3
                            var meta: SkullMeta = head.itemMeta as SkullMeta
                            meta.owningPlayer = player1
                            meta.displayName = "§a" + player1?.name
                            head.itemMeta = meta
                            players.addItem(head)
                        }
                    }

                    player.openInventory(players)
                }
            }
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player: Player = event.whoClicked as Player

        if (event.inventory.title == "§aJogadores online") {
            val current: ItemStack = event.currentItem
            if (current == null || current.type == Material.AIR) {
                event.isCancelled = true
                return
            }
            if (current.type == Material.SKULL_ITEM) {
                event.isCancelled = true

                val name: String = ChatColor.stripColor(current.itemMeta.displayName)
                var rightClicked = Bukkit.getPlayer(name)

                rightClicked.location.add(0.0, 12.0, 0.0).block.type = Material.GLASS
                rightClicked.location.add(0.0, 10.0, 1.0).block.type = Material.GLASS
                rightClicked.location.add(1.0, 10.0, 1.0).block.type = Material.GLASS
                rightClicked.location.subtract(0.0, 0.0, 1.0).block.type = Material.GLASS
                rightClicked.location.subtract(1.0, 0.0, 0.0).block.type = Material.GLASS
                rightClicked.location.add(1.0, 9.0, 1.0).block.type = Material.GLASS
                rightClicked.teleport(rightClicked.location.add(0.0, 10.0, 0.0))

                player.teleport(rightClicked.location.add(2.0, 2.0, 2.0))
            }
        }
    }

    @EventHandler
    fun onInteractEntity(event: PlayerInteractEntityEvent) {
        var player = event.player

        if (adminManager.getAdmins()!!.contains(player.uniqueId)) {
            var rightClicked = event.rightClicked
            if (rightClicked is Player) {
                if (player.itemInHand.type == Material.GLASS) {
                    rightClicked.location.add(0.0, 12.0, 0.0).block.type = Material.GLASS
                    rightClicked.location.add(0.0, 10.0, 1.0).block.type = Material.GLASS
                    rightClicked.location.add(1.0, 10.0, 1.0).block.type = Material.GLASS
                    rightClicked.location.subtract(0.0, 0.0, 1.0).block.type = Material.GLASS
                    rightClicked.location.subtract(1.0, 0.0, 0.0).block.type = Material.GLASS
                    rightClicked.location.add(1.0, 9.0, 1.0).block.type = Material.GLASS
                    rightClicked.teleport(rightClicked.location.add(0.0, 10.0, 0.0))
                } else if (player.itemInHand.type == Material.AIR) {
                    player.openInventory(rightClicked.inventory)
                }
            }
        }
    }
}