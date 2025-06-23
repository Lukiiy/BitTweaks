package me.lukiiy.bittweaks.listen;

import me.lukiiy.bittweaks.BitTweaks;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.inventory.ItemStack;

public class EntityEcho extends EntityListener {
    @Override
    public void onEntityDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();

        if (entity instanceof Slime && BitTweaks.getTweak("fixSlimeSplit")) {
            Slime slime = (Slime) entity;
            int health = slime.getHealth();

            if (e.getDamage() > health) e.setDamage(health);
            return;
        }

        if (entity instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Block block = entity.getLocation().getBlock();

            if (BitTweaks.getTweak("fixWaterFallDmg") && block.isLiquid()) {
                e.setCancelled(true);
                return;
            }

            if (BitTweaks.getTweak("fixCobwebFallDmg") && block.getType() == Material.WEB) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent e) {
        if (BitTweaks.getTweak("explosionNoBlockBreak")) e.blockList().clear();
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        Entity entity = e.getEntity();

        if (entity instanceof Pig && BitTweaks.getTweak("fixPigSaddle") && ((Pig) entity).hasSaddle()) {
            e.getDrops().add(new ItemStack(Material.SADDLE, 1));
            return;
        }

        if (entity instanceof PigZombie && BitTweaks.getTweak("pigZombieNoBeefDrop")) {
            e.getDrops().clear();
            return;
        }
    }
}
