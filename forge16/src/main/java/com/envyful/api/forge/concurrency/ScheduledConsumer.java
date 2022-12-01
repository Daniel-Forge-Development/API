package com.envyful.api.forge.concurrency;

import com.envyful.api.concurrency.UtilConcurrency;

import java.util.function.Consumer;

public class ScheduledConsumer<T> {

    private final boolean async;
    private final long delayTicks;
    private final Consumer<T> handler;

    private ScheduledConsumer(boolean async, long delayTicks, Consumer<T> handler) {
        this.async = async;
        this.delayTicks = delayTicks;
        this.handler = handler;
    }

    public void accept(T t) {
        if (this.async) {
            if (this.delayTicks == -1) {
                UtilConcurrency.runAsync(() -> this.handler.accept(t));
            } else {
                UtilConcurrency.runLater(() -> this.handler.accept(t), 50 * this.delayTicks);
            }
            return;
        }

        if (this.delayTicks == -1) {
            UtilForgeConcurrency.runSync(() -> this.handler.accept(t));
        } else {
            UtilForgeConcurrency.runLater(() -> this.handler.accept(t), (int) this.delayTicks);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder<A> {

        private boolean async = true;
        private long delayTicks = -1;
        private Consumer<A> handler;

        private Builder() {
        }

        public Builder<A> async() {
            this.async = true;
            return this;
        }

        public Builder<A> sync() {
            this.async = false;
            return this;
        }

        public Builder<A> delayTicks(long delayTicks) {
            this.delayTicks = delayTicks;
            return this;
        }

        public Builder<A> noDelay() {
            this.delayTicks = -1;
            return this;
        }

        public Builder<A> handler(Consumer<A> handler) {
            this.handler = handler;
            return this;
        }

        public ScheduledConsumer<A> build() {
            return new ScheduledConsumer<>(this.async, this.delayTicks, this.handler);
        }
    }
}
