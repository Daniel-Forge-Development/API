package com.envyful.api.player.attribute;

import com.envyful.api.concurrency.UtilLogger;
import com.envyful.api.database.Database;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.api.player.attribute.data.TableData;
import com.envyful.api.player.save.SaveManager;
import com.envyful.api.player.save.attribute.DataDirectory;
import com.envyful.api.player.save.impl.JsonSaveManager;
import com.envyful.api.player.save.impl.SQLSaveManager;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 *
 * An interface designed for storing specific
 * data for each mod / plugin about a player.
 *
 *
 * @param <A> The manager type
 * @param <B> The API's player type
 * @param <C> The platform's player type
 */
public abstract class PlayerAttribute<A, B extends EnvyPlayer<C>, C>
        extends ManagedAttribute<UUID, A> {

    protected transient Database database;
    protected transient B parent;

    protected PlayerAttribute(A manager) {
        super(manager);
    }

    public void setParent(B parent) {
        this.parent = parent;
    }

    @Override
    public CompletableFuture<UUID> getId() {
        return CompletableFuture.completedFuture(this.id);
    }

    public UUID getUuid() {
        return this.id;
    }

    @Override
    public void save(UUID id) {
        this.id = id;

        if (!this.shouldSave()) {
            return;
        }

        this.save();
    }

    @Override
    public void load(UUID id) {
        this.id = id;

        this.load();
    }

    @Override
    public void deleteAll(SaveManager<?> saveManager) {
        this.findTargets(saveManager).forEach(s -> {
            if (this.getDatabase() == null) {
                saveManager.delete(s);
            } else {
                saveManager.delete(this.getDatabase(), s);
            }
        });
    }

    protected Database getDatabase() {
        return this.database;
    }

    protected List<String> findTargets(SaveManager<?> saveManager) {
        List<String> targets = Lists.newArrayList();

        if (saveManager instanceof SQLSaveManager) {
            TableData tableData = this.getClass().getAnnotation(TableData.class);

            if (tableData == null) {
                UtilLogger.logger().ifPresent(logger -> logger.error("No table data annotation found for {}", this.getClass().getName()));
                return Collections.emptyList();
            }

            targets.addAll(Lists.newArrayList(tableData.value()));
        } else if (saveManager instanceof JsonSaveManager) {
            DataDirectory dataDirectory = this.getClass().getAnnotation(DataDirectory.class);

            if (dataDirectory == null) {
                UtilLogger.logger().ifPresent(logger -> logger.error("No data directory annotation found for {}", this.getClass().getName()));
                return Collections.emptyList();
            }

            targets.addAll(Lists.newArrayList(dataDirectory.value()));
        } else {
            UtilLogger.logger().ifPresent(logger -> logger.error("Unknown save manager type {}", saveManager.getClass().getName()));
            return Collections.emptyList();
        }

        return targets;
    }
}
