package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (20:16 18.04.20)

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class InventoryBuilder {

    private Integer size;
    private String title, pattern;
    private ItemStack[] contents;
    private Inventory tmpInventory;

    public InventoryBuilder(Integer rows, Integer columns, String title) {
        this(rows * columns, title);
    }

    public InventoryBuilder(Integer size, String title) {
        this.size = size;
        this.title = title;
        this.contents = new ItemStack[size];
        this.pattern = "";
        fillInventory(Material.AIR);
        build();
    }

    public InventoryBuilder setItem(Integer row, Integer column, ItemStack item) {
        return setItem(row * column - 1, item);
    }

    public InventoryBuilder setItem(Integer slot, ItemStack item) {
        this.contents[slot] = item;
        return this;
    }

    public InventoryBuilder setItem(Integer row, Integer column, ItemBuilder item) {
        return setItem(row * column - 1, item);
    }

    public InventoryBuilder setItem(Integer slot, ItemBuilder item) {
        return setItem(slot, item.build());
    }

    public InventoryBuilder fillInventory(Material filler) {
        return fillInventory(new ItemBuilder(filler).addAllItemFlags().noName());
    }

    public InventoryBuilder addItem(Material item) {
        return addItem(new ItemBuilder(item).addAllItemFlags().noName());
    }

    public InventoryBuilder addItem(ItemBuilder item) {
        return addItem(item.build());
    }

    public InventoryBuilder addItem(ItemStack item) {
        for (int i = 0; i < contents.length; i++)
            if (contents[i].getType() == Material.AIR || contents[i].getType() == Material.CAVE_AIR || contents[i].getType() == Material.VOID_AIR || contents[i] == null) {
                contents[i] = item;
                break;
            }
        return this;
    }

    public InventoryBuilder fillInventory(ItemStack filler) {
        Arrays.fill(this.contents, filler);
        return this;
    }

    public InventoryBuilder fillInventory(ItemBuilder filler) {
        return fillInventory(filler.build());
    }

    public ItemStack getItem(Integer row, Integer column) {
        return getItem(row * column - 1);
    }

    public ItemStack getItem(Integer slot) {
        return this.contents[slot];
    }

    public InventoryBuilder(Integer rows, Integer columns) {
        this(rows * columns);
    }

    public InventoryBuilder(Integer size) {
        this(size, "");
    }

    public int getSize() {
        return size;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public String getTitle() {
        return title;
    }

    public InventoryBuilder setContents(ItemStack[] contents) {
        this.contents = contents;
        return this;
    }

    public InventoryBuilder setSize(Integer size) {
        this.size = size;
        this.contents = new ItemStack[size];
        return this;
    }

    public InventoryBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public InventoryBuilder setSize(Integer rows, Integer columns) {
        return setSize(rows * columns);
    }

    public Inventory build() {
        Inventory inventory = Bukkit.createInventory(null, size, title);
        inventory.setContents(contents);
        this.tmpInventory = inventory;
        return inventory;
    }

    public InventoryBuilder setItemPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public InventoryBuilder setItemInPattern(Character character, ItemStack item) {
        String[] splitPattern = pattern.split("");
        for (int i = 0; i < splitPattern.length; i++)
            if (splitPattern[i].equals(character.toString()))
                setItem(i, item);
        return this;
    }

    public InventoryBuilder setItemInPattern(Character character, Material item) {
        return setItemInPattern(character, new ItemBuilder(item).addAllItemFlags().noName());
    }

    public InventoryBuilder setItemInPattern(Character character, ItemBuilder item) {
        return setItemInPattern(character, item.build());
    }

    public Inventory getTmpInventory() {
        return tmpInventory;
    }
}