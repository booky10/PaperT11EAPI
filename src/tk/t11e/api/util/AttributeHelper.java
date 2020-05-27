package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (20:02 24.04.20)

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import java.util.Objects;

//TODO: more attributes
@SuppressWarnings("deprecation")
public class AttributeHelper {

    public static Double getMaxHealth(LivingEntity player) {
        try {
            return Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
        } catch (Exception exception) {
            return player.getMaxHealth();
        }
    }

    public static void setMaxHealth(LivingEntity player, Double maxHealth) {
        try {
            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(maxHealth);
        } catch (Exception exception) {
            player.setMaxHealth(maxHealth);
        }
    }
}