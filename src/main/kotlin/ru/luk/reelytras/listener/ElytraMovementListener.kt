package ru.luk.reelytras.listener
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Color
import ru.luk.reelytras.ReElytras

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.advancement.Advancement
import org.bukkit.advancement.AdvancementProgress
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerRiptideEvent

class ElytraFireworkListener : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onElytraToggleGlide(event: EntityToggleGlideEvent) {
        if (event.isGliding && (!ReElytras.allowElytras || !ReElytras.allowElytrasWhenRaining && event.entity.isInRain)) {
            event.isCancelled = true
        }

        if (event.isGliding) {
            val player = event.entity as? Player ?: return
            val dragonAdvancement: Advancement? = Bukkit.getAdvancement(NamespacedKey("minecraft", "end/kill_dragon"))
            if (dragonAdvancement != null) {
                if (!player.getAdvancementProgress(dragonAdvancement).isDone) {
                    player.sendMessage(Component.text("ꐐ")
                        .append(Component.text(" You can't use an elytra until you have slain the Ender Dragon!").color(NamedTextColor.RED)))
                    player.setCooldown(Material.ELYTRA, 100)
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun onElytraMovement(event: PlayerMoveEvent) {
        if (!ReElytras.allowElytrasWhenRaining && event.player.isInRain && event.player.isGliding) {
            event.player.isGliding = false
        }

        if (event.player.isGliding) {
            val dragonAdvancement: Advancement? = Bukkit.getAdvancement(NamespacedKey("minecraft", "end/kill_dragon"))
            if (dragonAdvancement != null) {
                if (!event.player.getAdvancementProgress(dragonAdvancement).isDone) {
                    event.player.isGliding = false
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onElytraFireworkBoost(event: PlayerInteractEvent) {
        if (!ReElytras.allowFireworkBoost && event.player.isGliding && event.action.isRightClick
            && event.item?.type == Material.FIREWORK_ROCKET) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onElytraRiptideBoost(event: PlayerRiptideEvent) {
        val player = event.player
        if (!ReElytras.allowRiptideBoost &&
            (player.isGliding
                    || (player.location.add(0.0, -1.0, 0.0).block.type == Material.AIR
                    && player.inventory.chestplate?.type == Material.ELYTRA))) {
            event.player.teleport(event.player)
        }
    }
}