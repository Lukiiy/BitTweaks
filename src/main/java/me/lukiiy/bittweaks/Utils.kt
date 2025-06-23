package me.lukiiy.bittweaks

import net.minecraft.server.EntityPlayer
import net.minecraft.server.Packet18ArmAnimation
import net.minecraft.server.StatisticList
import net.minecraft.server.WorldServer
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object Utils {
    @JvmStatic
    fun damageItem(player: Player, item: ItemStack, damage: Int) {
        if (item.type.maxDurability <= 0) return
        val dmg = (item.durability + damage).toShort()

        if (dmg >= item.type.maxDurability) {
            player.asNMS().a(StatisticList.F[item.typeId], 1)
            item.amount--
        } else item.durability = dmg
    }

    // NMS
    fun ItemStack.asNMSCopy(): net.minecraft.server.ItemStack = net.minecraft.server.ItemStack(typeId, amount, durability.toInt())
    fun Player.asNMS(): EntityPlayer = (this as CraftPlayer).handle
    fun World.asNMS(): WorldServer = (this as CraftWorld).handle

    @JvmStatic
    fun swingArm(player: Player) {
        val packet = Packet18ArmAnimation(player.asNMS(), 1)

        player.world.players.stream().filter { !it.equals(player) }.forEach { it.asNMS().netServerHandler?.sendPacket(packet) }
    }

    @JvmStatic
    fun drop(location: Location, item: ItemStack): Item? {
        if (item.type == Material.AIR || item.amount == 0) return null

        return location.world.dropItemNaturally(location, item).apply {
            if (item.amount < 1) item.amount = 1
        }
    }

    /* drop but NMS. Used to debug certain Bukkit quirks...
    @JvmStatic
    fun dropPickableItem(location: Location, item: ItemStack, delay: Int) {
        if (item.type == Material.AIR || item.amount == 0) return

        EntityItem(location.world.asNMS(), location.x, location.y, location.z, item.asNMSCopy()).apply {
            pickupDelay = delay
            world.addEntity(this)
        }
    }
    */
}
