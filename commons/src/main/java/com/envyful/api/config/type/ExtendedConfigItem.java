package com.envyful.api.config.type;

import com.envyful.api.text.Placeholder;
import com.envyful.api.text.PlaceholderFactory;
import com.envyful.api.type.Pair;
import com.envyful.api.type.UtilParse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class ExtendedConfigItem {

    private boolean enabled = true;
    private String type = "minecraft:stained_glass_pane";
    private String amount = "1";
    private String damage;
    private String name = " ";
    private List<String> flags = Lists.newArrayList();
    private List<String> lore = Lists.newArrayList();
    private Map<String, ConfigItem.EnchantData> enchants = Maps.newHashMap();
    private Map<String, ConfigItem.NBTValue> nbt = Maps.newHashMap();
    private Map<String, Pair<Integer, Integer>> positions = Maps.newHashMap();
    private boolean requiresPermission;
    private String permission;
    private ConfigItem elseItem;
    private boolean closeOnClick;
    private List<String> commandsExecuted;

    public ExtendedConfigItem() {
    }

    private ExtendedConfigItem(boolean enabled, String type,
                               String amount, String name, List<String> flags,
                               List<String> lore,
                               Map<String, ConfigItem.EnchantData> enchants,
                               Map<String, ConfigItem.NBTValue> nbt,
                               Map<String, Pair<Integer, Integer>> positions,
                               boolean requiresPermission,
                               String permission, ConfigItem elseItem,
                               boolean closeOnClick,
                               List<String> commandsExecuted) {
        this.enabled = enabled;
        this.type = type;
        this.amount = String.valueOf(amount);
        this.name = name;
        this.flags = flags;
        this.lore = lore;
        this.enchants = enchants;
        this.nbt = nbt;
        this.positions = positions;
        this.requiresPermission = requiresPermission;
        this.permission = permission;
        this.elseItem = elseItem;
        this.closeOnClick = closeOnClick;
        this.commandsExecuted = commandsExecuted;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getType() {
        return this.type;
    }

    public int getAmount() {
        return UtilParse.parseInt(this.amount).orElse(0);
    }

    public int getAmount(List<Placeholder> placeholders) {
        List<Integer> integers = PlaceholderFactory.handlePlaceholders(
                Collections.singletonList(amount),
                s -> UtilParse.parseInt(s).orElse(0),
                placeholders);

        if (integers.isEmpty()) {
            return 0;
        }

        return integers.get(0);
    }

    public byte getDamage() {
        return (byte) UtilParse.parseInt(this.damage).orElse(0);
    }

    public byte getDamage(List<Placeholder> placeholders) {
        List<Integer> integers = PlaceholderFactory.handlePlaceholders(
                Collections.singletonList(damage),
                s -> UtilParse.parseInt(s).orElse(0),
                placeholders);

        if (integers.isEmpty()) {
            return 0;
        }

        return (byte) integers.get(0).intValue();
    }

    public String getName() {
        return this.name;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public Map<String, ConfigItem.EnchantData> getEnchants() {
        return enchants;
    }

    public List<String> getFlags() {
        return flags;
    }

    public Map<String, ConfigItem.NBTValue> getNbt() {
        return this.nbt;
    }

    public List<Pair<Integer, Integer>> getPositions() {
        return Lists.newArrayList(this.positions.values());
    }

    public boolean requiresPermission() {
        return this.requiresPermission;
    }

    public String getPermission() {
        return this.permission;
    }

    public ConfigItem getElseItem() {
        return this.elseItem;
    }

    public boolean shouldCloseOnClick() {
        return this.closeOnClick;
    }

    public List<String> getCommandsExecuted() {
        return this.commandsExecuted;
    }

    public ConfigItem asConfigItem() {
        return new ConfigItem(enabled, type,
                amount, damage, name,
                flags, lore, enchants, nbt);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private boolean enabled = true;
        private String type = "minecraft:stained_glass_pane";
        private String amount = "1";
        private String name = " ";
        private boolean requiresPermission = false;
        private String permission;
        private ConfigItem elseItem;
        private boolean closeOnClick = false;

        private final Map<String, ConfigItem.EnchantData> enchants =
                Maps.newHashMap();
        private final List<String> commandsExecuted = Lists.newArrayList();
        private final List<String> flags = Lists.newArrayList();
        private final List<String> lore = Lists.newArrayList();
        private final Map<String, ConfigItem.NBTValue> nbt = Maps.newHashMap();
        private final Map<String, Pair<Integer, Integer>> positions =
                Maps.newHashMap();

        protected Builder() {
        }

        public Builder enable() {
            this.enabled = true;
            return this;
        }

        public Builder disable() {
            this.enabled = false;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder amount(int amount) {
            return this.amount(amount + "");
        }

        public Builder amount(String amount) {
            this.amount = amount;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder flags(String... flags) {
            this.flags.addAll(Lists.newArrayList(flags));
            return this;
        }

        public Builder lore(String... lore) {
            this.lore.addAll(Lists.newArrayList(lore));
            return this;
        }

        public Builder enchants(ConfigItem.EnchantData... enchantData) {
            for (ConfigItem.EnchantData enchantDatum : enchantData) {
                this.enchants.put(this.enchants.size() + "", enchantDatum);
            }

            return this;
        }

        public Builder nbt(String key, ConfigItem.NBTValue value) {
            this.nbt.put(key, value);
            return this;
        }

        @Deprecated
        @SafeVarargs
        public final Builder positions(Pair<Integer, Integer>... positions) {
            for (Pair<Integer, Integer> position : positions) {
                this.positions.put(this.positions.size() + "", position);
            }

            return this;
        }

        public Builder positions(int x, int y) {
            this.positions.put(this.positions.size() + "", Pair.of(x, y));
            return this;
        }

        public Builder noPermission() {
            this.requiresPermission = false;
            return this;
        }

        public Builder requiresPermission(
                String permission,
                ConfigItem elseItem) {
            this.requiresPermission = true;
            this.permission = permission;
            this.elseItem = elseItem;
            return this;
        }

        public Builder remainOpenOnClick() {
            this.closeOnClick = false;
            return this;
        }

        public Builder closeOnClick() {
            this.closeOnClick = true;
            return this;
        }

        public Builder executeCommands(String... commands) {
            this.commandsExecuted.addAll(Lists.newArrayList(commands));
            return this;
        }

        public ExtendedConfigItem build() {
            return new ExtendedConfigItem(
                    this.enabled, this.type, this.amount,
                    this.name, this.flags, this.lore, this.enchants,
                    this.nbt, this.positions, this.requiresPermission,
                    this.permission, this.elseItem, this.closeOnClick,
                    this.commandsExecuted);
        }
    }
}
