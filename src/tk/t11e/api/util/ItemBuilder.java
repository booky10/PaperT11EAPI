package tk.t11e.api.util;
// Created by booky10 in knockIt (19:36 14.01.20)

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemBuilder {

    private Material material;
    private int amount;
    private String name;
    private boolean unbreakable;
    private HashMap<Enchantment, Integer> enchantments;
    private List<ItemFlag> itemFlags;
    private List<String> lore;
    private Integer customDataModel;

    public ItemBuilder(Material material, int amount, String name) {
        this.amount = amount;
        this.material = material;
        this.name = name;
        unbreakable = false;
        enchantments = new HashMap<>();
        itemFlags = new ArrayList<>();
        lore=new ArrayList<>();
        customDataModel = 0;
    }

    public ItemBuilder(Material material, int amount) {
        this(material, amount, "§f" + material.getKey().getKey().replace('_', ' '));
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public int getAmount() {
        return amount;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder addAllItemFlags() {
        return addItemFlags(ItemFlag.values());
    }

    public ItemBuilder noName() {
        return setName(" ");
    }

    public Material getMaterial() {
        return material;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public List<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    public ItemBuilder setItemFlags(List<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
        this.itemFlags.addAll(Arrays.asList(itemFlags));
        return this;
    }

    public ItemBuilder removeItemFlags(ItemFlag... itemFlags) {
        this.itemFlags.removeAll(Arrays.asList(itemFlags));
        return this;
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        enchantments.remove(enchantment);
        return this;
    }

    public HashMap<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public ItemBuilder setEnchantments(HashMap<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public Integer getCustomDataModel() {
        return customDataModel;
    }

    public ItemBuilder setCustomDataModel(Integer customDataModel) {
        this.customDataModel = customDataModel;
        return this;
    }

    public ItemBuilder setLore(String... lore){
        return setLore(Arrays.asList(lore));
    }

    public ItemBuilder setLore(List<String> lore){
        this.lore=lore;
        return this;
    }

    public ItemBuilder clearLore(){
        lore.clear();
        return this;
    }

    public ItemBuilder addLoreLine(String line){
        lore.add(line);
        return this;
    }

    public ItemBuilder addLoreLines(String... lines){
        lore.addAll(Arrays.asList(lines));
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount);
        if (material != Material.AIR && material != Material.CAVE_AIR && material != Material.VOID_AIR) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setUnbreakable(unbreakable);
            itemMeta.setDisplayName(name);
            itemMeta.setCustomModelData(customDataModel);
            itemMeta.setLore(lore);
            for (ItemFlag itemFlag : itemFlags)
                itemMeta.addItemFlags(itemFlag);

            itemStack.setItemMeta(itemMeta);
            for (Enchantment enchantment : enchantments.keySet())
                itemStack.addUnsafeEnchantment(enchantment, enchantments.get(enchantment));
        }
        return itemStack.clone().clone().clone().clone().clone();
    }
}