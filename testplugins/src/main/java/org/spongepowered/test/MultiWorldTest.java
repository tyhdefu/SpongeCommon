/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.test;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.world.SerializationBehaviors;
import org.spongepowered.api.world.WorldArchetype;
import org.spongepowered.api.world.WorldArchetypes;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.IOException;

@Plugin(id = "multi-world-test", name = "Multi World Test", version = "0.0.0")
public final class MultiWorldTest implements LoadableModule {

    @Override
    public void disable(CommandSource src) {
        Sponge.getServer().getWorld("no-save").ifPresent(world -> {
            Sponge.getServer().unloadWorld(world);
            Sponge.getServer().deleteWorld(world.getProperties());
        });
        Sponge.getServer().getWorld("metadata-only").ifPresent(world -> {
            Sponge.getServer().unloadWorld(world);
            Sponge.getServer().deleteWorld(world.getProperties());
        });
    }

    @Override
    public void enable(CommandSource src) {
        try {
            final WorldArchetype archetype1 = Sponge.getRegistry().getType(WorldArchetype.class, "multi-world-test:overnether").orElseGet(() ->
                WorldArchetype.builder().
                    from(WorldArchetypes.THE_NETHER)
                    .serializationBehavior(SerializationBehaviors.NONE)
                    .generator(GeneratorTypes.OVERWORLD)
                    .build("multi-world-test:overnether", "Overnether")
            );
            final WorldProperties world1 = Sponge.getServer().createWorldProperties("no-save", archetype1);
            Sponge.getServer().loadWorld(world1);

            final WorldArchetype archetype2 = Sponge.getRegistry().getType(WorldArchetype.class, "multi-world-test:overend").orElseGet(() ->
                    WorldArchetype.builder().
                            from(WorldArchetypes.THE_END)
                            .serializationBehavior(SerializationBehaviors.METADATA_ONLY)
                            .generator(GeneratorTypes.OVERWORLD)
                            .build("multi-world-test:overend", "Overend")
            );
            final WorldProperties world2 = Sponge.getServer().createWorldProperties("metadata-only", archetype2);
            Sponge.getServer().loadWorld(world2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
