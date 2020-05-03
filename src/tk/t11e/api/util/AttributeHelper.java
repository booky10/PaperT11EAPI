package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (20:02 24.04.20)

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class AttributeHelper {

    public static Double getMaxHealth(Player player){
        return player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
    }

    public static void setMaxHealth(Player player,Double maxHealth){
       player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
    }
}