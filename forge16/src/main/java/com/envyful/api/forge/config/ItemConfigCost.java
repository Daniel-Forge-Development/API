package com.envyful.api.forge.config;

import com.envyful.api.config.type.ConfigItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class ItemConfigCost implements ConfigCost {

    private ConfigItem configItem;

    public ItemConfigCost() {
    }

    public ItemConfigCost(ConfigItem configItem) {
        this.configItem = configItem;
    }

    @Override
    public boolean has(ServerPlayerEntity player) {
        return player.inventory.contains(UtilConfigItem.fromConfigItem(this.configItem));
    }

    @Override
    public void take(ServerPlayerEntity player) {
        player.inventory.removeItem(UtilConfigItem.fromConfigItem(this.configItem));
    }
}