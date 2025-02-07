package ru.luk.reelytras.listener
import ru.luk.reelytras.ReElytras

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ElytraDamageListener : Listener {
    @EventHandler
    fun onElytraDamage(event: PlayerItemDamageEvent) {
        val chestplateItem = event.player.inventory.chestplate ?: return

        if (chestplateItem.type == Material.ELYTRA) {
            val meta: Damageable = chestplateItem.itemMeta as Damageable

            if (event.player.isSneaking) {
                val enchantments = chestplateItem.enchantments

                for (enchantment in enchantments) {
                    if (enchantment.key.key.toString() == "vane_enchantments:angel") {
                        event.damage = ReElytras.damagePerSecond * 2
                    }
                }
            } else {
                event.damage = ReElytras.damagePerSecond
            }

            if (chestplateItem.type.maxDurability.toInt() - meta.damage <= 2) {
                if (ReElytras.permanentDestroy) {
                    event.player.inventory.chestplate = null
                }

                if (ReElytras.playDestroySound) {
                    event.player.world.playSound(event.player.location, Sound.ENTITY_ITEM_BREAK, 1f, 0.8f)
                }

                event.player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, 400, 1, true, true, true))
            }
        }
    }
}