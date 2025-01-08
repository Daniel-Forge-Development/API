package com.envyful.api.velocity.player.attribute;

import com.envyful.api.player.attribute.AttributeTrigger;
import com.envyful.api.player.attribute.trigger.ClearAttributeTrigger;
import com.envyful.api.player.attribute.trigger.SaveAttributeTrigger;
import com.envyful.api.player.attribute.trigger.SetAttributeTrigger;
import com.envyful.api.velocity.player.VelocityEnvyPlayer;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * Static helper class for creating triggers for Velocity
 *
 */
public class VelocityTrigger {

    private VelocityTrigger() {
        throw new UnsupportedOperationException("Cannot instantiate a static class");
    }

    /**
     *
     * Creates a trigger to set the attribute for a single player
     *
     * @param server the server
     * @param plugin the plugin
     * @param event the event
     * @param converter the converter to convert the event to a player
     * @return the trigger
     * @param <A> the event type
     */
    public static <A> AttributeTrigger<VelocityEnvyPlayer> singleSet(ProxyServer server, Object plugin, Class<A> event, Function<A, VelocityEnvyPlayer> converter) {
        return set(server, plugin, event, a -> List.of(converter.apply(a)));
    }

    /**
     *
     * Creates a trigger to set the attribute for multiple players
     *
     * @param server the server
     * @param plugin the plugin
     * @param event the event
     * @param converter the converter to convert the event to a list of players
     * @return the trigger
     * @param <A> the event type
     */
    public static <A> AttributeTrigger<VelocityEnvyPlayer> set(ProxyServer server, Object plugin, Class<A> event, Function<A, List<VelocityEnvyPlayer>> converter) {
        var trigger = new SetAttributeTrigger<VelocityEnvyPlayer>();
        createHandler(server, plugin, event, converter, trigger::trigger);
        return trigger;
    }

    /**
     *
     * Creates a trigger to clear the attribute for a single player
     *
     * @param server the server
     * @param plugin the plugin
     * @param event the event
     * @param converter the converter to convert the event to a player
     * @return the trigger
     * @param <A> the event type
     */
    public static <A> AttributeTrigger<VelocityEnvyPlayer> singleClear(ProxyServer server, Object plugin, Class<A> event, Function<A, VelocityEnvyPlayer> converter) {
        return clear(server, plugin, event, a -> List.of(converter.apply(a)));
    }

    /**
     *
     * Creates a trigger to clear the attribute for multiple players
     *
     * @param server the server
     * @param plugin the plugin
     * @param event the event
     * @param converter the converter to convert the event to a list of players
     * @return the trigger
     * @param <A> the event type
     */
    public static <A> AttributeTrigger<VelocityEnvyPlayer> clear(ProxyServer server, Object plugin, Class<A> event, Function<A, List<VelocityEnvyPlayer>> converter) {
        var trigger = new ClearAttributeTrigger<VelocityEnvyPlayer>();
        createHandler(server, plugin, event, converter, trigger::trigger);
        return trigger;
    }

    /**
     *
     * Creates a trigger to save the attribute data for a single player
     *
     * @param server the server
     * @param plugin the plugin
     * @param event the event
     * @param converter the converter to convert the event to a player
     * @return the trigger
     * @param <A> the event type
     */
    public static <A> AttributeTrigger<VelocityEnvyPlayer> singleSave(ProxyServer server, Object plugin, Class<A> event, Function<A, VelocityEnvyPlayer> converter) {
        return save(server, plugin, event, a -> List.of(converter.apply(a)));
    }

    /**
     *
     * Creates a trigger to save the attribute data for multiple players
     *
     * @param server the server
     * @param plugin the plugin
     * @param event the event
     * @param converter the converter to convert the event to a list of players
     * @return the trigger
     * @param <A> the event type
     */
    public static <A> AttributeTrigger<VelocityEnvyPlayer> save(ProxyServer server, Object plugin, Class<A> event, Function<A, List<VelocityEnvyPlayer>> converter) {
        var trigger = new SaveAttributeTrigger<VelocityEnvyPlayer>();
        createHandler(server, plugin, event, converter, trigger::trigger);
        return trigger;
    }

    @SuppressWarnings("unchecked")
    private static <A> void createHandler(ProxyServer server, Object plugin, Class<A> eventClass, Function<A, List<VelocityEnvyPlayer>> converter, Consumer<VelocityEnvyPlayer> trigger) {
        server.getEventManager().register(plugin, eventClass, PostOrder.LAST, event -> {
            if (!eventClass.isAssignableFrom(event.getClass())) {
                return;
            }

            for (var player : converter.apply(event)) {
                if (player != null) {
                    trigger.accept(player);
                }
            }
        });
    }
}
