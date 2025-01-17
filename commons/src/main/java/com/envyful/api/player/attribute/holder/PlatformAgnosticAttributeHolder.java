package com.envyful.api.player.attribute.holder;

import com.envyful.api.player.Attribute;
import com.envyful.api.player.attribute.AttributeHolder;
import com.envyful.api.player.attribute.AttributeInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class PlatformAgnosticAttributeHolder implements AttributeHolder {

    protected final Map<Class<?>, AttributeInstance<?>> attributes = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <A extends Attribute> CompletableFuture<A> getAttribute(Class<A> attributeClass) {
        if (!this.attributes.containsKey(attributeClass)) {
            return null;
        }

        var instance = (AttributeInstance<A>) this.attributes.get(attributeClass);

        return instance.getAttribute();
    }

    @Override
    public <A extends Attribute> boolean hasAttribute(Class<A> attributeClass) {
        var instance = this.attributes.get(attributeClass);

        if (instance == null) {
            return false;
        }

        if (instance.getAttributeNow() != null) {
            return true;
        }

        return instance.getAttributeNow() != null && instance.getAttribute().isDone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A extends Attribute> A getAttributeNow(Class<A> attributeClass) {
        if (!this.attributes.containsKey(attributeClass)) {
            return null;
        }

        var instance = (AttributeInstance<A>) this.attributes.get(attributeClass);

        if (instance.getAttributeNow() != null) {
            return instance.getAttributeNow();
        }

        if (instance.getAttribute() == null) {
            return null;
        }

        return instance.getAttribute().join();
    }

    @Override
    public List<Attribute> getAttributes() {
        List<Attribute> attributes = new ArrayList<>();

        for (var attribute : this.attributes.values()) {
            if (attribute.getAttributeNow() != null) {
                attributes.add(attribute.getAttributeNow());
            }
        }

        return attributes;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A extends Attribute> A removeAttribute(Class<A> attributeClass) {
        var instance = this.attributes.remove(attributeClass);

        if (instance == null) {
            return null;
        }

        return (A) instance.getAttributeNow();
    }

    @Override
    public <A extends Attribute> void setAttribute(Class<A> attributeClass, CompletableFuture<A> attribute) {
        this.attributes.put(attributeClass, new AttributeInstance<>(attribute));
    }

    @Override
    public <A extends Attribute> void setAttribute(A attribute) {
        if (attribute == null) {
            return;
        }

        this.attributes.put(attribute.getClass(), new AttributeInstance<>(attribute));
    }
}
