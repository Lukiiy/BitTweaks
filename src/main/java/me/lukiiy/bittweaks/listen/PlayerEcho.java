package me.lukiiy.bittweaks.listen;

import me.lukiiy.bittweaks.BitTweaks;
import me.lukiiy.bittweaks.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerEcho extends PlayerListener {
    @Override
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack hand = player.getItemInHand();
        Block block = e.getClickedBlock();

        World world = player.getWorld();

        if (block != null && !block.isEmpty() && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            switch (block.getType()) {
                case GRASS:
                    if (hand.getType().name().contains("_HOE") && BitTweaks.getTweak("hoeOnGrassForSeeds") && ThreadLocalRandom.current().nextInt(8) == 0) world.dropItem(block.getLocation().add(0, 1, 0), new ItemStack(Material.SEEDS, 1));
                    return;

                case SUGAR_CANE_BLOCK:
                    if (Objects.equals(hand.getData(), new MaterialData(Material.INK_SACK, (byte) 15)) && BitTweaks.getTweak("canBonemealSugarcane")) {
                        Block up = block.getRelative(BlockFace.UP);
                        Block up1 = up.getRelative(BlockFace.UP);

                        if (!up.isEmpty() || !up1.isEmpty()) return;

                        up.setType(Material.SUGAR_CANE_BLOCK);
                        up1.setType(Material.SUGAR_CANE_BLOCK);
                        hand.setAmount(hand.getAmount() - 1);
                        Utils.swingArm(player);
                    }
                    return;
            }
        }
    }

    @Override
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        ItemStack hand = player.getItemInHand();

        Entity clicked = e.getRightClicked();
        Location clickedLoc = clicked.getLocation();

        if (clicked instanceof Squid && BitTweaks.getTweak("milkableSquids") && hand.getType() == Material.BUCKET && hand.getAmount() == 1) {
            player.setItemInHand(null);
            Utils.drop(clickedLoc, new ItemStack(Material.MILK_BUCKET, 1));
            player.updateInventory();
            Utils.swingArm(player);
            return;
        }

        if (clicked instanceof TNTPrimed && BitTweaks.getTweak("alphaTNT")) {
            clicked.remove();
            Utils.drop(clickedLoc, new ItemStack(Material.TNT, 1));
            Utils.swingArm(player);
            return;
        }

        if (clicked instanceof Sheep) {
            if (hand.getType() == Material.SHEARS && BitTweaks.getTweak("fixSheepShearing") && ((Sheep) clicked).getHealth() < 1) {
                e.setCancelled(true);
                return;
            }

            if (hand.getType() == Material.INK_SACK && hand.getDurability() == 15 && BitTweaks.getTweak("bonemealOnSheep") && !clicked.isDead()) {
                ((Sheep) clicked).setSheared(false);
                hand.setAmount(hand.getAmount() - 1);
                player.updateInventory();
                Utils.swingArm(player);
                return;
            }
        }
    }

    @Override
    public void onPlayerBedEnter(PlayerBedEnterEvent e) {
        if (BitTweaks.getTweak("disableSleeping")) {
            e.setCancelled(true);
            return;
        }
    }

}
