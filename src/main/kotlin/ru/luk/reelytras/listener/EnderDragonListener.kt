package ru.luk.reelytras.listener

import com.jeff_media.morepersistentdatatypes.DataType
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.OfflinePlayer
import org.bukkit.advancement.Advancement
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.persistence.PersistentDataType
import java.util.UUID

class EnderDragonListener : Listener {
	@EventHandler
	fun onEnderDragonDamageEvent(event: EntityDamageByEntityEvent) {
		if (event.damager.type != EntityType.PLAYER) { return }

		val player: Player = event.damager as Player
		val damagedEnderDragonKey = NamespacedKey("reelytras", "damaged_ender_dragon")
		val damagedEnderDragonList: MutableList<UUID>? = event.entity.persistentDataContainer.get(damagedEnderDragonKey, DataType.asList(DataType.UUID))

		if (damagedEnderDragonList != null) {
			if (!damagedEnderDragonList.contains(player.uniqueId)) {
				damagedEnderDragonList.add(player.uniqueId)
				event.entity.persistentDataContainer.set(damagedEnderDragonKey, DataType.asList(DataType.UUID), damagedEnderDragonList)
			}
		} else {
			event.entity.persistentDataContainer.set(damagedEnderDragonKey, DataType.asList(DataType.UUID), mutableListOf(player.uniqueId))
		}
	}

	fun onEnderDragonDeathEvent(event: EntityDeathEvent) {
		if (event.entityType != EntityType.ENDER_DRAGON) { return }

		val damagedEnderDragonKey = NamespacedKey("reelytras", "damaged_ender_dragon")
		val damagedEnderDragonList: MutableList<UUID>? = event.entity.persistentDataContainer.get(damagedEnderDragonKey, DataType.asList(DataType.UUID))
		val nearbyPlayers = event.entity.getNearbyEntities(300.0, 300.0, 300.0).filterIsInstance<Player>().map { it.uniqueId }

		if (damagedEnderDragonList == null && nearbyPlayers.isEmpty()) { return }

		val playerList = (damagedEnderDragonList?.plus(nearbyPlayers))?.distinct()

		playerList?.forEach { uuid ->
			val player: Player? = Bukkit.getPlayer(uuid)
			if (player != null) {
				val dragonAdvancement: Advancement? = Bukkit.getAdvancement(NamespacedKey("minecraft", "end/kill_dragon"))
				if (dragonAdvancement != null) {
					val advancementProgress = player.getAdvancementProgress(dragonAdvancement)
					for (criteria in advancementProgress.remainingCriteria) {
						advancementProgress.awardCriteria(criteria)
					}
				}
			}
		}
	}
}