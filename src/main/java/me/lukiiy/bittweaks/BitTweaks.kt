package me.lukiiy.bittweaks

import me.lukiiy.bittweaks.listen.BlockEcho
import me.lukiiy.bittweaks.listen.EntityEcho
import me.lukiiy.bittweaks.listen.PlayerEcho
import org.bukkit.event.Event
import org.bukkit.plugin.java.JavaPlugin

class BitTweaks : JavaPlugin() {
    override fun onEnable() {
        instance = this
        setupConfig()

        val server = getServer()

        getCommand("bittweaks").executor = Cmd()

        val pEcho = PlayerEcho()
        val bEcho = BlockEcho()
        val eEcho = EntityEcho()

        server.pluginManager.apply {
            registerEvent(Event.Type.PLAYER_INTERACT, pEcho, Event.Priority.Low, instance)
            registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, pEcho, Event.Priority.Low, instance)
            registerEvent(Event.Type.PLAYER_BED_ENTER, pEcho, Event.Priority.Low, instance)
            registerEvent(Event.Type.BLOCK_PLACE, bEcho, Event.Priority.Low, instance)
            registerEvent(Event.Type.BLOCK_BREAK, bEcho, Event.Priority.Low, instance)
            registerEvent(Event.Type.BLOCK_DISPENSE, bEcho, Event.Priority.Low, instance)
            registerEvent(Event.Type.LEAVES_DECAY, bEcho, Event.Priority.Low, instance)
            registerEvent(Event.Type.ENTITY_DAMAGE, eEcho, Event.Priority.Low, instance)
            registerEvent(Event.Type.ENTITY_INTERACT, eEcho, Event.Priority.Low, instance)
            registerEvent(Event.Type.ENTITY_EXPLODE, eEcho, Event.Priority.Low, instance)
            registerEvent(Event.Type.ENTITY_DEATH, eEcho, Event.Priority.Low, instance)
        }
    }

    override fun onDisable() {}

    // Config
    fun setupConfig() {
        configuration.apply {
            load()
            getBoolean("tweaks.fixSlimeSplit", true)
            getBoolean("tweaks.fixBlockDrops", true)
            getBoolean("tweaks.milkableSquids", false)
            getBoolean("tweaks.alphaTNT", false)
            getBoolean("tweaks.explosionNoBlockBreak", false)
            getBoolean("tweaks.disableSleeping", false)
            getBoolean("tweaks.disablePigZombieDrops", true)
            getBoolean("tweaks.silkyGoldenPickaxe", false)
            getBoolean("tweaks.hoeOnGrassForSeeds", true)
            getBoolean("tweaks.canBonemealSugarcane", true)
            getBoolean("tweaks.bonemealOnSheep", true)
            getBoolean("tweaks.disableLeafDecay", false)
            getBoolean("tweaks.fixSheepShearing", false)
            getBoolean("tweaks.fixWaterFallDmg", false)
            getBoolean("tweaks.fixCobwebFallDmg", true)
            getBoolean("tweaks.fixPigSaddle", true)
            save()
        }
    }

    companion object {
        private var instance: BitTweaks? = null

        @JvmStatic
        fun getInstance(): BitTweaks = instance!!

        @JvmStatic
        fun getTweak(tweak: String?): Boolean = instance!!.configuration.getBoolean("tweaks.$tweak", false)
    }
}
