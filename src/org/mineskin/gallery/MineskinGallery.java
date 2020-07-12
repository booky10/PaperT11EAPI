package org.mineskin.gallery;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.inventivetalent.nicknamer.api.NickNamerAPI;
import org.mineskin.MineskinClient;
import org.mineskin.Model;
import org.mineskin.SkinOptions;
import org.mineskin.Visibility;
import org.mineskin.data.Skin;
import org.mineskin.data.SkinCallback;
import tk.t11e.api.main.PaperT11EAPIMain;
import tk.t11e.api.util.HeadBuilder;
import tk.t11e.api.util.PlayerUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MineskinGallery implements Listener, CommandExecutor, TabCompleter {

    private final String TITLE = "§7Gallery", PREFIX = "§7[§bGallery§7] ";
    private final String agent = "PaperT11EAPI/" + PaperT11EAPIMain.main.getDescription().getVersion();

    private final Executor connectionExecutor = Executors.newSingleThreadExecutor();
    private MineskinClient mineskinClient;
    private final File cacheDirectory = new File(PaperT11EAPIMain.main.getDataFolder(), "skinCache");

    private final int PAGE_SIZE = 36;
    private boolean nickNamerEnabled = false;

    private final Map<UUID, Integer> playerGalleryPages = new HashMap<>();

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, PaperT11EAPIMain.main);
        Objects.requireNonNull(PaperT11EAPIMain.main.getCommand("mineskin")).setExecutor(this);

        mineskinClient = new MineskinClient(connectionExecutor, agent);

        nickNamerEnabled = Bukkit.getPluginManager().isPluginEnabled("NickNamer");

        if (cacheDirectory.exists())
            if (cacheDirectory.delete())
                PaperT11EAPIMain.main.getLogger().info("Deleted old cached skins.");
            else
                PaperT11EAPIMain.main.getLogger().severe("Failed to delete skin cache!");
        //noinspection ResultOfMethodCallIgnored
        cacheDirectory.mkdirs();
    }

    public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§e------§6[Client for MineSkin.org]§e------");
            sender.sendMessage("§e  - /mineskin gallery [Page] [Filter]");
            sender.sendMessage("§e  - /mineskin view <Skin ID>");
            sender.sendMessage("§e  - /mineskin generate <URL> [Name] [Private]");
            sender.sendMessage("§e------§6[Client for MineSkin.org]§e------");
            return true;
        }

        if (args[0].equalsIgnoreCase("gallery")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Sorry, you need to be a player to open the gallery");
                return true;
            }
            if (!sender.hasPermission("api.mineskin.gallery")) {
                sender.sendMessage(PaperT11EAPIMain.NO_PERMISSION);
                return true;
            }

            int page = 1;
            String filter = null;
            if (args.length > 1) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignored) {
                }
                if (args.length > 2)
                    filter = args[2];
            }
            page = Math.max(page, 1);

            Inventory inventory = Bukkit.createInventory(null, 9 * 6, TITLE);

            sender.sendMessage(PaperT11EAPIMain.PREFIX + "Loading page #" + page + "...");
            playerGalleryPages.put(((Player) sender).getUniqueId(), page);

            int finalPage = page;
            String finalFilter = filter;
            connectionExecutor.execute(() -> {
                try {
                    URL galleryUrl = new URL("https://api.mineskin.org/get/list/" + finalPage + "?size="
                            + PAGE_SIZE + (finalFilter != null ? "&filter=" + finalFilter : ""));
                    URLConnection galleryConnection = galleryUrl.openConnection();
                    galleryConnection.setRequestProperty("User-Agent", agent);
                    galleryConnection.connect();
                    InputStreamReader inputStream = new InputStreamReader(galleryConnection.getInputStream());
                    JsonElement galleryElement = new JsonParser().parse(inputStream);
                    JsonObject galleryObject = galleryElement.getAsJsonObject();

                    ((Player) sender).openInventory(inventory);

                    JsonArray skinArray = galleryObject.getAsJsonArray("skins");
                    if (skinArray.size() == 0) {
                        ItemStack itemStack = new ItemStack(Material.BARRIER);
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.setDisplayName("§cNothing found!");
                        itemStack.setItemMeta(meta);
                        inventory.addItem(itemStack);
                    } else
                        for (JsonElement skinElement : skinArray) {
                            int id = skinElement.getAsJsonObject().get("id").getAsInt();
                            connectionExecutor.execute(() -> {
                                if (inventory.getViewers().isEmpty())
                                    return;
                                try {
                                    JsonObject skinObject = getFromCacheOrDownload(id);

                                    ItemStack itemStack = makeSkull(id, skinObject);
                                    inventory.addItem(itemStack);
                                } catch (Exception exception) {
                                    throw new IllegalStateException(exception);
                                }
                            });
                        }

                    JsonObject pageInfoObject = galleryObject.getAsJsonObject("page");
                    int pageIndex = pageInfoObject.get("index").getAsInt();
                    int pageCount = pageInfoObject.get("amount").getAsInt();
                    if (pageIndex > 1) {
                        ItemStack itemStack = new ItemStack(Material.ARROW);
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.setDisplayName("§bPrevious page");
                        meta.setLore(Collections.singletonList((pageIndex - 1) + "/" + pageCount));
                        itemStack.setItemMeta(meta);
                        inventory.setItem(45, itemStack);
                    }
                    if (pageIndex < pageCount) {
                        ItemStack itemStack = new ItemStack(Material.ARROW);
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.setDisplayName("§bNext page");
                        meta.setLore(Collections.singletonList((pageIndex + 1) + "/" + pageCount));
                        itemStack.setItemMeta(meta);
                        inventory.setItem(53, itemStack);
                    }
                    if (finalFilter != null) {
                        ItemStack itemStack = new ItemStack(Material.PAPER);
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.setDisplayName("§7Filter: §b" + finalFilter);
                        itemStack.setItemMeta(meta);
                        inventory.setItem(49, itemStack);
                    }
                } catch (IOException exception) {
                    throw new IllegalStateException(exception);
                }
            });
            return true;
        }
        if (args[0].equalsIgnoreCase("view")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Sorry, you need to be a player to view skins");
                return true;
            }
            if (!sender.hasPermission("api.mineskin.view")) {
                sender.sendMessage(PaperT11EAPIMain.NO_PERMISSION);
                return true;
            }
            if (args.length == 1) {
                sender.sendMessage(PaperT11EAPIMain.PREFIX + "Please specify the skin ID");
                return true;
            }

            int id = -1;
            try {
                id = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
            }
            if (id < 0) {
                sender.sendMessage(PaperT11EAPIMain.PREFIX + "Please specify a valid skin ID");
                return true;
            }
            openView(id, (HumanEntity) sender);
            return true;
        }
        if (args[0].equalsIgnoreCase("generate")) {
            if (!sender.hasPermission("api.mineskin.generate")) {
                sender.sendMessage(PaperT11EAPIMain.NO_PERMISSION);
                return true;
            }
            if (args.length == 1) {
                sender.sendMessage(PaperT11EAPIMain.PREFIX + "Please specify an image URL");
                return true;
            }
            try {
                new URL(args[1]);
            } catch (MalformedURLException e) {
                sender.sendMessage(PaperT11EAPIMain.PREFIX + "Invalid URL");
                return true;
            }

            String name = "";
            boolean isPrivate = false;
            if (args.length > 2)
                name = args[2];
            if (args.length > 3)
                isPrivate = args[3].equalsIgnoreCase("private")
                        || args[3].equalsIgnoreCase("true");

            mineskinClient.generateUrl(args[1], SkinOptions.create(name, Model.DEFAULT, isPrivate ?
                    Visibility.PRIVATE : Visibility.PUBLIC), new SkinCallback() {
                @Override
                public void waiting(long time) {
                    sender.sendMessage(PaperT11EAPIMain.PREFIX + "§7Waiting " + (time / 1000D) + "s for upload...");
                }

                @Override
                public void uploading() {
                    sender.sendMessage(PaperT11EAPIMain.PREFIX + "§7Generating skin...");
                }

                @Override
                public void error(String error) {
                    sender.sendMessage(PaperT11EAPIMain.PREFIX + "Unexpected error: " + error);
                }

                @Override
                public void exception(Exception exception) {
                    sender.sendMessage(PaperT11EAPIMain.PREFIX + "Unexpected exception: " + exception.getMessage());
                    throw new IllegalStateException(exception);
                }

                @Override
                public void done(Skin skin) {
                    sender.sendMessage(PaperT11EAPIMain.PREFIX + "§aSkin generated!");
                    if (sender instanceof Player)
                        openView(skin.id, (Player) sender);
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1)
            completions.addAll(Arrays.asList("generate", "view", "gallery"));
        return PlayerUtils.convertTab(args, completions);
    }

    JsonObject getFromCacheOrDownload(int id) {
        File cachedFile = new File(cacheDirectory, String.valueOf(id));
        if (cachedFile.exists())
            try (FileReader reader = new FileReader(cachedFile)) {
                return new JsonParser().parse(reader).getAsJsonObject();
            } catch (IOException exception) {
                throw new IllegalStateException(exception);
            }
        else {
            try {
                URL skinUrl = new URL("https://api.mineskin.org/get/id/" + id);
                HttpURLConnection skinConnection = (HttpURLConnection) skinUrl.openConnection();
                skinConnection.setRequestProperty("User-Agent", agent);
                if (skinConnection.getResponseCode() == 200) {
                    JsonObject skinObject = new JsonParser().parse(new InputStreamReader(skinConnection
                            .getInputStream())).getAsJsonObject();
                    //noinspection ResultOfMethodCallIgnored
                    cachedFile.createNewFile();
                    try (FileWriter writer = new FileWriter(cachedFile)) {
                        writer.write(skinObject.toString());
                    }
                    return skinObject;
                } else if (skinConnection.getResponseCode() == 404) {
                    return null;
                }
            } catch (IOException exception) {
                throw new IllegalStateException(exception);
            }
        }
        throw new RuntimeException("No cached version of skin #" + id + " available and failed to connect to mineskin.org");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;

        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null)
            itemStack = event.getCursor();

        if (itemStack != null) {
            if (TITLE.equals(event.getView().getTitle())) {
                event.setCancelled(true);
                if (itemStack.hasItemMeta()) {
                    itemStack.getItemMeta().getDisplayName();
                    String filter = "";
                    ItemStack filterItem = event.getClickedInventory().getItem(49);
                    if (filterItem != null)
                        filter = filterItem.getItemMeta().getDisplayName().substring("§7Filter: §b".length());
                    if ("§bPrevious page".equals(itemStack.getItemMeta().getDisplayName())) {
                        String page =
                                Objects.requireNonNull(itemStack.getItemMeta().getLore()).get(0).split("/")[0];
                        event.getWhoClicked().closeInventory();
                        ((Player) event.getWhoClicked()).chat("/mineskin gallery " + page + " " + filter);
                    } else if ("§bNext page".equals(itemStack.getItemMeta().getDisplayName())) {
                        String page =
                                Objects.requireNonNull(itemStack.getItemMeta().getLore()).get(0).split("/")[0];
                        event.getWhoClicked().closeInventory();
                        ((Player) event.getWhoClicked()).chat("/mineskin gallery " + page + " " + filter);
                    } else {
                        int skinId =
                                Integer.parseInt(Objects.requireNonNull(itemStack.getItemMeta().getLore()).get(0).substring(1));
                        event.getWhoClicked().closeInventory();
                        ((Player) event.getWhoClicked()).chat("/mineskin view " + skinId);
                    }
                }
            }
            if (event.getView().getTitle().startsWith(PREFIX)) {
                event.setCancelled(true);
                if (itemStack.hasItemMeta()) {
                    itemStack.getItemMeta().getDisplayName();
                    ItemStack skullItem = Objects.requireNonNull(event.getClickedInventory().getItem(13)).clone();
                    int skinId =
                            Integer.parseInt(Objects.requireNonNull(skullItem.getItemMeta().getLore()).get(0).substring(1));
                    if ("§bAdd to your inventory".equals(itemStack.getItemMeta().getDisplayName())) {
                        if (!event.getWhoClicked().hasPermission("api.mineskin.give.item")) {
                            event.getWhoClicked().sendMessage(PaperT11EAPIMain.NO_PERMISSION);
                            return;
                        }
                        event.getWhoClicked().getInventory().addItem(skullItem);
                    } else if ("§bSet as your own head".equals(itemStack.getItemMeta().getDisplayName())) {
                        if (!event.getWhoClicked().hasPermission("api.mineskin.give.head")) {
                            event.getWhoClicked().sendMessage(PaperT11EAPIMain.NO_PERMISSION);
                            return;
                        }
                        if (event.getWhoClicked().getInventory().getHelmet() != null) {
                            event.getWhoClicked().getInventory().addItem(Objects.requireNonNull(event.getWhoClicked().getEquipment()).getHelmet());
                            event.getWhoClicked().getInventory().setHelmet(null);
                        }
                        event.getWhoClicked().getInventory().setHelmet(skullItem);
                    } else if ("§bSet as your own skin".equals(itemStack.getItemMeta().getDisplayName())) {
                        if (!event.getWhoClicked().hasPermission("api.mineskin.give.skin")) {
                            event.getWhoClicked().sendMessage(PaperT11EAPIMain.NO_PERMISSION);
                            return;
                        }
                        JsonObject skinObject = getFromCacheOrDownload(skinId);
                        JsonObject texture =
                                skinObject.get("data").getAsJsonObject().get("texture").getAsJsonObject();

                        NickNamerAPI.getNickManager().loadCustomSkin("MineSkinGallery-" + skinId,
                                Objects.requireNonNull(HeadTextureChanger.createProfile(texture.get("value").getAsString(), texture.get("signature").getAsString())));
                        NickNamerAPI.getNickManager().setCustomSkin(event.getWhoClicked().getUniqueId(),
                                "MineSkinGallery-" + skinId);
                    } else if ("§bShow online".equals(itemStack.getItemMeta().getDisplayName())) {
                        event.getWhoClicked().sendMessage(PaperT11EAPIMain.PREFIX + "§aClick here to view this skin on the " +
                                "MineSkin" +
                                " website: §lhttps://mineskin.org/" + skinId);
                        event.getWhoClicked().closeInventory();
                    } else if ("§bGo back".equalsIgnoreCase(itemStack.getItemMeta().getDisplayName())) {
                        int page = 1;
                        if (playerGalleryPages.containsKey(event.getWhoClicked().getUniqueId())) {
                            page = playerGalleryPages.get(event.getWhoClicked().getUniqueId());
                        }
                        event.getWhoClicked().closeInventory();
                        ((Player) event.getWhoClicked()).chat("/mineskin gallery " + page);
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    ItemStack makeSkull(int id, JsonObject skinObject) throws Exception {
        JsonObject textureObject = skinObject.get("data").getAsJsonObject().get("texture").getAsJsonObject();

        ItemStack itemStack;
        try {
            itemStack = new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } catch (Exception exception) {
            itemStack = new ItemStack(Material.valueOf("SKULL"), 1, (short) 3);
        }
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwner("MHF_MineSkin");
        skullMeta.setDisplayName(skinObject.get("name").getAsString().isEmpty() ? ("#" + id) : skinObject.get("name").getAsString());
        skullMeta.setLore(Collections.singletonList("#" + id));
        HeadTextureChanger.applyTextureToMeta(skullMeta, HeadTextureChanger.createProfile(textureObject.get("value").getAsString(), textureObject.get("signature").getAsString()));
        itemStack.setItemMeta(skullMeta);

        return itemStack;
    }

    void openView(int id, HumanEntity player) {
        JsonObject skinObject = getFromCacheOrDownload(id);

        if (skinObject == null) {
            player.sendMessage(PaperT11EAPIMain.PREFIX + "Failed to load skin #" + id + " (Not Found)");
            return;
        }

        Inventory inventory = Bukkit.createInventory(null, 9 * 6,
                PREFIX + (skinObject.get("name").getAsString().isEmpty() ? ("#" + id)
                        : skinObject.get("name").getAsString()));

        ItemStack skullItem;
        try {
            skullItem = makeSkull(id, skinObject);
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
        inventory.setItem(13, skullItem);

        ItemStack viewsItem;
        try {
            viewsItem = new ItemStack(skinObject.get("private").getAsBoolean()
                    ? Material.valueOf("ENDER_EYE") : Material.ENDER_PEARL);
        } catch (Exception exception) {
            viewsItem = new ItemStack(skinObject.get("private").getAsBoolean()
                    ? Material.valueOf("EYE_OF_ENDER") : Material.ENDER_PEARL);
        }
        ItemMeta viewsMeta = viewsItem.getItemMeta();
        viewsMeta.setDisplayName("§7" + skinObject.get("views").getAsInt() + " §8views");
        viewsMeta.setLore(Collections.singletonList("§8" + (skinObject.get("private").getAsBoolean() ? "private" : "public")));
        viewsItem.setItemMeta(viewsMeta);
        inventory.setItem(8, viewsItem);

        ItemStack addToInventoryItem = new ItemStack(Material.CHEST, 1);
        ItemMeta addToInventoryMeta = addToInventoryItem.getItemMeta();
        addToInventoryMeta.setDisplayName("§bAdd to your inventory");
        addToInventoryItem.setItemMeta(addToInventoryMeta);
        inventory.setItem(27, addToInventoryItem);

        inventory.setItem(29, new HeadBuilder(1, "§bSet as your own head").setSkinName(player.getName()).build());

        ItemStack setAsSkinItem = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
        ItemMeta setAsSkinMeta = setAsSkinItem.getItemMeta();
        setAsSkinMeta.setDisplayName(nickNamerEnabled ? "§bSet as your own skin" : "§8Set as your own skin §7(requires NickNamer)");
        setAsSkinItem.setItemMeta(setAsSkinMeta);
        inventory.setItem(31, setAsSkinItem);

        ItemStack openWebItem = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta openWebMeta = openWebItem.getItemMeta();
        openWebMeta.setDisplayName("§bShow online");
        openWebItem.setItemMeta(openWebMeta);
        inventory.setItem(35, openWebItem);

        ItemStack itemStack = new ItemStack(Material.ARROW);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("§bGo back");
        itemStack.setItemMeta(meta);
        inventory.setItem(49, itemStack);

        player.closeInventory();
        player.openInventory(inventory);
    }
}