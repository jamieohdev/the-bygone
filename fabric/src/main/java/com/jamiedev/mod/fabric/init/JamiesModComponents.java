package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import com.jamiedev.mod.common.blocks.entity.CasterBlockEntity;
import com.jamiedev.mod.common.compounds.CasterComponent;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class JamiesModComponents implements BlockComponentInitializer {
    public static final ComponentKey<CasterComponent> CASTER =
            ComponentRegistry.getOrCreate(JamiesModFabric.getModId("caster"), CasterComponent.class);

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(CasterBlockEntity.class, CASTER, (be) -> be);
    }
}
