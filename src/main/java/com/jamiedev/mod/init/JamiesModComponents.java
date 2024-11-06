package com.jamiedev.mod.init;

import com.jamiedev.mod.JamiesMod;
import com.jamiedev.mod.blocks.entity.CasterBlockEntity;
import com.jamiedev.mod.compounds.CasterComponent;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class JamiesModComponents implements BlockComponentInitializer {
    public static final ComponentKey<CasterComponent> CASTER =
            ComponentRegistry.getOrCreate(JamiesMod.getModId("caster"), CasterComponent.class);

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(CasterBlockEntity.class, CASTER, (be) -> be);
    }
}
