package me.lukiiy.bittweaks.listen;

import me.lukiiy.bittweaks.BitTweaks;
import me.lukiiy.bittweaks.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class BlockEcho extends BlockListener {
    private static final Set<Material> stairDropPatch = new HashSet<>();
    private static final Set<Material> shearsExtra = new HashSet<>();
    private static final Set<Material> silkDroppable = new HashSet<>();

    static {
        stairDropPatch.add(Material.COBBLESTONE_STAIRS);
        stairDropPatch.add(Material.WOOD_STAIRS);

        shearsExtra.add(Material.WEB);
        shearsExtra.add(Material.LONG_GRASS);

        silkDroppable.add(Material.GRASS);
        silkDroppable.add(Material.STONE);
        silkDroppable.add(Material.COAL_ORE);
        silkDroppable.add(Material.GOLD_ORE);
        silkDroppable.add(Material.IRON_ORE);
        silkDroppable.add(Material.REDSTONE_ORE);
        silkDroppable.add(Material.GLOWING_REDSTONE_ORE);
        silkDroppable.add(Material.DIAMOND_ORE);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        Block block = e.getBlock();
        Material material = block.getType();
        Location location = block.getLocation();
        World world = location.getWorld();

        ItemStack hand = player.getItemInHand();

        if ((BitTweaks.getTweak("fixBlockDrops") && stairDropPatch.contains(material)) || (shearsExtra.contains(material) && hand.getType() == Material.SHEARS)) {
            e.setCancelled(true);
            block.setType(Material.AIR);

            Utils.drop(location.add(0, 0.25, 0), new ItemStack(material, 1));
            Utils.damageItem(player, hand, 1);
            return;
        }

        if (material == Material.TNT && BitTweaks.getTweak("alphaTNT")) {
            e.setCancelled(true);
            block.setType(Material.AIR);

            TNTPrimed primed = world.spawn(location.add(.5, .25, .5), TNTPrimed.class);
            primed.setFuseTicks(80);

            return;
        }

        if (hand.getType() == Material.GOLD_PICKAXE && BitTweaks.getTweak("silkyGoldenPickaxe") && silkDroppable.contains(material)) {
            e.setCancelled(true);

            block.setType(Material.AIR);
            Utils.drop(location.add(0, .25, 0), new ItemStack(material, 1));
            Utils.damageItem(player, hand, 2);

            return;
        }
    }

    @Override
    public void onLeavesDecay(LeavesDecayEvent e) {
        if (BitTweaks.getTweak("disableLeafDecay")) e.setCancelled(true);
    }
}
