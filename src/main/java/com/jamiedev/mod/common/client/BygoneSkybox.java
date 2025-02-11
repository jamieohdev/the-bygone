package com.jamiedev.mod.common.client;

import io.github.amerebagatelle.fabricskyboxes.api.skyboxes.Skybox;
import io.github.amerebagatelle.fabricskyboxes.mixin.skybox.WorldRendererAccess;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.joml.Matrix4f;

public abstract class BygoneSkybox implements Skybox {

 //   @Override
  //  public void render(WorldRendererAccess worldRendererAccess, MatrixStack matrixStack, Matrix4f matrix4f, float v, Camera camera, boolean b, Runnable runnable) {
//
  //  }

    @Override
    public void tick(ClientWorld clientWorld) {

    }

    @Override
    public boolean isActive() {
        return true; // Always enabled
    }


    public boolean isActiveLater() {
        return this.isActive(); // Is enabled next render?
    }
}
