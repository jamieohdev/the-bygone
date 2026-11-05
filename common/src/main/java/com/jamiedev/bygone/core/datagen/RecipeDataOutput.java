package com.jamiedev.bygone.core.datagen;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.util.GsonHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Set;

public class RecipeDataOutput implements CachedOutput {
    private static final Set<String> COOKING_RECIPES = Set.of(
            "minecraft:smelting", "minecraft:blasting", "minecraft:smoking", "minecraft:campfire_cooking");
    private final CachedOutput output;

    public RecipeDataOutput(CachedOutput output) {
        this.output = output;
    }

    @Override
    public void writeIfNeeded(Path path, byte[] data, HashCode hash) throws IOException {
        JsonObject json = JsonParser.parseString(new String(data, StandardCharsets.UTF_8)).getAsJsonObject();
        if (COOKING_RECIPES.contains(GsonHelper.getAsString(json, "type", ""))) {
            JsonObject result = GsonHelper.getAsJsonObject(json, "result");
            if (result.has("count") && GsonHelper.getAsInt(result, "count") == 1) {
                result.remove("count");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(bytes, StandardCharsets.UTF_8))) {
                    writer.setSerializeNulls(false);
                    writer.setIndent("  ");
                    GsonHelper.writeValue(writer, json, DataProvider.KEY_COMPARATOR);
                }
                data = bytes.toByteArray();
                hash = Hashing.sha1().hashBytes(data);
            }
        }
        this.output.writeIfNeeded(path, data, hash);
    }
}
