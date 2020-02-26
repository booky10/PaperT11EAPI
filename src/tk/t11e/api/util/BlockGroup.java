package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (17:34 25.02.20)

import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collection;

public enum BlockGroup {

    SLABS("Slabs", Material.OAK_SLAB, Material.SPRUCE_SLAB, Material.BIRCH_SLAB, Material.JUNGLE_SLAB,
            Material.ACACIA_SLAB, Material.DARK_OAK_SLAB, Material.STONE_SLAB, Material.SMOOTH_STONE_SLAB,
            Material.SANDSTONE_SLAB, Material.CUT_SANDSTONE_SLAB, Material.PETRIFIED_OAK_SLAB,
            Material.COBBLESTONE_SLAB, Material.BRICK_SLAB, Material.BRICK_SLAB, Material.NETHER_BRICK_SLAB,
            Material.QUARTZ_SLAB, Material.RED_SANDSTONE_SLAB, Material.CUT_RED_SANDSTONE_SLAB,
            Material.PURPUR_SLAB, Material.PRISMARINE_SLAB, Material.PRISMARINE_BRICK_SLAB,
            Material.DARK_PRISMARINE_SLAB, Material.POLISHED_GRANITE_SLAB, Material.SMOOTH_RED_SANDSTONE_SLAB,
            Material.MOSSY_STONE_BRICK_SLAB, Material.POLISHED_DIORITE_SLAB, Material.MOSSY_COBBLESTONE_SLAB,
            Material.END_STONE_BRICK_SLAB, Material.SMOOTH_SANDSTONE_SLAB, Material.SMOOTH_QUARTZ_SLAB,
            Material.GRANITE_SLAB, Material.ANDESITE_SLAB, Material.RED_NETHER_BRICK_SLAB,
            Material.POLISHED_ANDESITE_SLAB, Material.DIORITE_SLAB), NO_MOB_SPAWNING("No Mob Spawning Blocks",
            Material.OAK_SAPLING, Material.SPRUCE_SAPLING, Material.BIRCH_SAPLING, Material.JUNGLE_SAPLING,
            Material.ACACIA_SAPLING, Material.DARK_OAK_SAPLING, Material.BEDROCK, Material.OAK_LEAVES,
            Material.BIRCH_LEAVES, Material.SPRUCE_LEAVES, Material.JUNGLE_LEAVES, Material.ACACIA_LEAVES,
            Material.DARK_OAK_LEAVES, Material.GLASS, Material.POWERED_RAIL, Material.ACTIVATOR_RAIL,
            Material.COBWEB, Material.STICKY_PISTON, Material.GRASS, Material.FERN, Material.DEAD_BUSH,
            Material.SEAGRASS, Material.SEA_PICKLE, Material.PISTON, Material.DANDELION, Material.POPPY,
            Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.RED_TULIP,
            Material.ORANGE_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.OXEYE_DAISY,
            Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.WITHER_ROSE, Material.BROWN_MUSHROOM,
            Material.RED_MUSHROOM, Material.OAK_SLAB, Material.SPRUCE_SLAB, Material.BIRCH_SLAB,
            Material.JUNGLE_SLAB, Material.ACACIA_SLAB, Material.DARK_OAK_SLAB, Material.STONE_SLAB,
            Material.SMOOTH_STONE_SLAB, Material.SANDSTONE_SLAB, Material.CUT_SANDSTONE_SLAB,
            Material.PETRIFIED_OAK_SLAB, Material.COBBLESTONE_SLAB, Material.BRICK_SLAB,
            Material.STONE_BRICK_SLAB, Material.NETHER_BRICK_SLAB, Material.QUARTZ_SLAB,
            Material.RED_SANDSTONE_SLAB, Material.CUT_RED_SANDSTONE_SLAB, Material.PURPUR_SLAB,
            Material.PRISMARINE_SLAB, Material.PRISMARINE_BRICK_SLAB, Material.DARK_PRISMARINE_SLAB,
            Material.TNT, Material.TORCH, Material.END_ROD, Material.CHORUS_PLANT, Material.CHORUS_FLOWER,
            Material.PURPUR_STAIRS, Material.OAK_STAIRS, Material.CHEST, Material.FARMLAND, Material.LADDER,
            Material.RAIL, Material.COBBLESTONE_STAIRS, Material.LEVER, Material.STONE_PRESSURE_PLATE,
            Material.OAK_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE,
            Material.JUNGLE_PRESSURE_PLATE, Material.ACACIA_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE,
            Material.REDSTONE_TORCH, Material.STONE_BUTTON, Material.SNOW, Material.ICE, Material.CACTUS,
            Material.OAK_FENCE, Material.SPRUCE_FENCE, Material.BIRCH_FENCE, Material.JUNGLE_FENCE,
            Material.ACACIA_FENCE, Material.DARK_OAK_FENCE, Material.PUMPKIN, Material.CARVED_PUMPKIN,
            Material.JACK_O_LANTERN, Material.OAK_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.BIRCH_TRAPDOOR,
            Material.JUNGLE_TRAPDOOR, Material.ACACIA_TRAPDOOR, Material.DARK_OAK_TRAPDOOR, Material.IRON_BARS,
            Material.GLASS_PANE, Material.MELON, Material.VINE, Material.OAK_FENCE_GATE,
            Material.SPRUCE_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.JUNGLE_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE, Material.BRICK_STAIRS, Material.STONE_BRICK_STAIRS, Material.LILY_PAD,
            Material.NETHER_BRICK_FENCE, Material.NETHER_BRICK_STAIRS, Material.ENCHANTING_TABLE,
            Material.END_PORTAL_FRAME, Material.ENDER_CHEST, Material.TRIPWIRE_HOOK, Material.SPRUCE_STAIRS,
            Material.BIRCH_STAIRS, Material.JUNGLE_STAIRS, Material.BEACON, Material.COBBLESTONE_WALL,
            Material.MOSSY_COBBLESTONE_WALL, Material.BRICK_WALL, Material.PRISMARINE_WALL,
            Material.RED_SANDSTONE_WALL, Material.MOSSY_STONE_BRICK_WALL, Material.GRANITE_WALL,
            Material.STONE_BRICK_WALL, Material.NETHER_BRICK_WALL, Material.ANDESITE_WALL,
            Material.RED_NETHER_BRICK_WALL, Material.SANDSTONE_WALL, Material.END_STONE_BRICK_WALL,
            Material.DIORITE_WALL, Material.OAK_BUTTON, Material.SPRUCE_BUTTON, Material.BIRCH_BUTTON,
            Material.JUNGLE_BUTTON, Material.ACACIA_BUTTON, Material.DARK_OAK_BUTTON, Material.ANVIL,
            Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL, Material.TRAPPED_CHEST,
            Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
            Material.DAYLIGHT_DETECTOR, Material.HOPPER, Material.ACTIVATOR_RAIL, Material.IRON_TRAPDOOR,
            Material.WHITE_CARPET, Material.ORANGE_CARPET, Material.MAGENTA_CARPET, Material.LIGHT_BLUE_CARPET,
            Material.YELLOW_CARPET, Material.LIME_CARPET, Material.PINK_CARPET, Material.GRAY_CARPET,
            Material.LIGHT_GRAY_CARPET, Material.CYAN_CARPET, Material.PURPLE_CARPET, Material.BLUE_CARPET,
            Material.BROWN_CARPET, Material.GREEN_CARPET, Material.RED_CARPET, Material.BLACK_CARPET,
            Material.ACACIA_STAIRS, Material.DARK_OAK_STAIRS, Material.SLIME_BLOCK, Material.GRASS_PATH,
            Material.SUNFLOWER, Material.LILAC, Material.ROSE_BUSH, Material.PEONY, Material.TALL_GRASS,
            Material.LARGE_FERN, Material.WHITE_STAINED_GLASS, Material.ORANGE_STAINED_GLASS,
            Material.MAGENTA_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS,
            Material.LIME_STAINED_GLASS, Material.PINK_STAINED_GLASS, Material.GRAY_STAINED_GLASS,
            Material.LIGHT_GRAY_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.PURPLE_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS, Material.GREEN_STAINED_GLASS,
            Material.RED_STAINED_GLASS, Material.BLACK_STAINED_GLASS, Material.WHITE_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE
            , Material.LIGHT_GRAY_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS_PANE,
            Material.PURPLE_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS_PANE,
            Material.BROWN_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE,
            Material.RED_STAINED_GLASS_PANE, Material.BLACK_STAINED_GLASS_PANE, Material.PRISMARINE_STAIRS,
            Material.PRISMARINE_BRICK_STAIRS, Material.DARK_PRISMARINE_STAIRS, Material.RED_SANDSTONE_STAIRS,
            Material.SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.ORANGE_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX,
            Material.LIME_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.GRAY_SHULKER_BOX,
            Material.LIGHT_GRAY_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.PURPLE_SHULKER_BOX,
            Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.GREEN_SHULKER_BOX,
            Material.RED_SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.TURTLE_EGG, Material.TUBE_CORAL,
            Material.BRAIN_CORAL, Material.BUBBLE_CORAL, Material.FIRE_CORAL, Material.HORN_CORAL,
            Material.DEAD_TUBE_CORAL, Material.DEAD_BRAIN_CORAL, Material.DEAD_BUBBLE_CORAL,
            Material.DEAD_FIRE_CORAL, Material.DEAD_HORN_CORAL, Material.TUBE_CORAL_FAN,
            Material.BRAIN_CORAL_FAN, Material.BUBBLE_CORAL_FAN, Material.FIRE_CORAL_FAN,
            Material.HORN_CORAL_FAN, Material.DEAD_TUBE_CORAL_FAN, Material.DEAD_BRAIN_CORAL_FAN,
            Material.DEAD_BUBBLE_CORAL_FAN, Material.DEAD_FIRE_CORAL_FAN, Material.DEAD_HORN_CORAL_FAN,
            Material.CONDUIT, Material.POLISHED_GRANITE_SLAB, Material.SMOOTH_RED_SANDSTONE_SLAB,
            Material.MOSSY_STONE_BRICK_SLAB, Material.POLISHED_DIORITE_SLAB, Material.MOSSY_COBBLESTONE_SLAB,
            Material.END_STONE_BRICK_SLAB, Material.SMOOTH_SANDSTONE_SLAB, Material.SMOOTH_QUARTZ_SLAB,
            Material.GRANITE_SLAB, Material.ANDESITE_SLAB, Material.RED_NETHER_BRICK_SLAB,
            Material.POLISHED_ANDESITE_SLAB, Material.DIORITE_SLAB, Material.SCAFFOLDING, Material.IRON_DOOR,
            Material.OAK_DOOR, Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR, Material.DARK_OAK_DOOR, Material.REPEATER, Material.COMPARATOR,
            Material.COMPOSTER, Material.OAK_SIGN, Material.SPRUCE_SIGN, Material.BIRCH_SIGN,
            Material.JUNGLE_SIGN, Material.ACACIA_SIGN, Material.DARK_OAK_SIGN, Material.WATER, Material.LAVA,
            Material.WHITE_BED,Material.ORANGE_BED,Material.MAGENTA_BED,Material.LIGHT_BLUE_BED,
            Material.YELLOW_BED,Material.LIME_BED,Material.PINK_BED,Material.GRAY_BED,Material.LIGHT_GRAY_BED,
            Material.CYAN_BED,Material.PURPLE_BED,Material.BLUE_BED,Material.BROWN_BED,Material.GREEN_BED,
            Material.RED_BED,Material.BLACK_BED,Material.BREWING_STAND,Material.CAULDRON,Material.FLOWER_POT,
            Material.SKELETON_SKULL,Material.WITHER_SKELETON_SKULL,Material.PLAYER_HEAD,Material.ZOMBIE_HEAD,
            Material.CREEPER_HEAD,Material.DRAGON_HEAD,Material.WHITE_BANNER,Material.ORANGE_BANNER,
            Material.MAGENTA_BANNER,Material.LIGHT_BLUE_BANNER,Material.YELLOW_BANNER,Material.LIME_BANNER,
            Material.PINK_BANNER,Material.GRAY_BANNER,Material.LIGHT_GRAY_BANNER,Material.CYAN_BANNER,
            Material.PURPLE_BANNER,Material.BLUE_BANNER,Material.BROWN_BANNER,Material.GREEN_BANNER,
            Material.RED_BANNER,Material.BLACK_BANNER,Material.GRINDSTONE,Material.LECTERN,Material.STONECUTTER,
            Material.BELL,Material.LANTERN,Material.CAMPFIRE,Material.HONEY_BLOCK);

    private final String name;
    private final Collection<Material> blocks;

    BlockGroup(String name, Material... blocks) {
        this.name = name;
        this.blocks = Arrays.asList(blocks);
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public Boolean isContained(Material material) {
        return getGroup().contains(material);
    }

    public Collection<Material> getGroup() {
        return blocks;
    }
}