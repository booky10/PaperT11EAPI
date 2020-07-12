package tk.t11e.api.util;
// Created by booky10 in PaperT11EAPI (20:02 24.04.20)

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class AttributeHelper {

    public static Double getMaxHealth(LivingEntity player) {
        if (VersionHelper.aboveOr19())
            return Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
        else
            return player.getMaxHealth();
    }

    public static void setMaxHealth(LivingEntity player, Double maxHealth) {
        if (VersionHelper.aboveOr19())
            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(maxHealth);
        else
            player.setMaxHealth(maxHealth);
    }
}