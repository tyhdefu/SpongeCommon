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
package org.spongepowered.common.mixin.core.tileentity;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import org.apache.logging.log4j.Level;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.ConstructEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.SpongeImplHooks;
import org.spongepowered.common.event.ShouldFire;
import org.spongepowered.common.event.tracking.PhaseTracker;
import org.spongepowered.common.registry.type.entity.EntityTypeRegistryModule;
import org.spongepowered.common.util.Constants;

import javax.annotation.Nullable;

@Mixin(MobSpawnerBaseLogic.class)
public abstract class MobSpawnerBaseLogicMixin {

    @Shadow public abstract World getSpawnerWorld();

    @Nullable
    private Entity impl$cachedEntity = null;

    private int ticks;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void initTicks(CallbackInfo ci) {
        World world = getSpawnerWorld();
        if (world != null) {
            this.ticks = world.rand.nextInt(20);
        }
    }

    /**
     * @author gabizou - January 30th, 2016
     * @author gabizou - Updated April 10th, 2016 - Update for 1.9 since it's passed to the AnvilChunkLoader
     *     * @reason Because this is self referencing with passengers
     * being recursively read from compound, this needs to remain static and
     * isolated as it's own method.
     *
     * This is close to a verbatim copy of {@link AnvilChunkLoader#readWorldEntityPos(NBTTagCompound, World, double, double, double, boolean)}
     * with the added bonus of throwing events before entities are constructed with appropriate causes.
     *
     * Redirects to throw a ConstructEntityEvent.PRE
     * @param compound The compound of the entity to spawn with
     * @param world The world to spawn at
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * @param doesNotForceSpawn If false, the entity is not going to be spawned into the world yet
     * @return The entity, if successfully created
     */
    @Nullable
    @Redirect(method = "updateSpawner",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/chunk/storage/AnvilChunkLoader;readWorldEntityPos(Lnet/minecraft/nbt/NBTTagCompound;Lnet/minecraft/world/World;DDDZ)Lnet/minecraft/entity/Entity;"
        )
    )
    private Entity impl$ThrowEventAndConstruct(
        final NBTTagCompound compound, final World world, final double x, final double y, final double z, final boolean doesNotForceSpawn) {

        if (impl$cachedEntity != null) {
            impl$cachedEntity.setPosition(x, y, z);
            return impl$cachedEntity;
        }

        final String entityTypeString = compound.getString(Constants.Entity.ENTITY_TYPE_ID);
        final Class<? extends Entity> clazz = SpongeImplHooks.getEntityClass(new ResourceLocation(entityTypeString));
        if (clazz == null) {
            final PrettyPrinter printer = new PrettyPrinter(60).add("Unknown Entity for MobSpawners").centre().hr()
                .addWrapped(60, "Sponge has found a MobSpawner attempting to locate potentially"
                                + "a foreign entity type for a MobSpawner, unfortunately, there isn't a"
                                + "way to get around the deserialization process looking up unregistered"
                                + "entity types. This may be a bug with a mod or sponge.")
                .add("%s : %s", "Entity Name", entityTypeString)
                .add();
            PhaseTracker.getInstance().generateVersionInfo(printer);
            printer.trace(System.err, SpongeImpl.getLogger(), Level.WARN);
            return null;
        }
        final EntityType type = EntityTypeRegistryModule.getInstance().getForClass(clazz);
        if (type == null) {
            return null;
        }
        if (ShouldFire.CONSTRUCT_ENTITY_EVENT_PRE) {
            try (final CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
                frame.addContext(EventContextKeys.SPAWN_TYPE, SpawnTypes.MOB_SPAWNER);
                final Transform<org.spongepowered.api.world.World> transform = new Transform<>(
                    ((org.spongepowered.api.world.World) world), new Vector3d(x, y, z));
                final ConstructEntityEvent.Pre event = SpongeEventFactory.createConstructEntityEventPre(frame.getCurrentCause(), type, transform);
                SpongeImpl.postEvent(event);
                if (event.isCancelled()) {
                    return null;
                }
            }
        }
        final Entity entity;
        try {
            entity = EntityList.createEntityFromNBT(compound, world);
            this.impl$cachedEntity = entity;
        } catch (Exception e) {
            return null;
        }

        if (entity == null) {
            return null;
        }

        entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);

        if (doesNotForceSpawn && !world.spawnEntity(entity)) {
            return null;
        }


        if (compound.hasKey(Constants.Entity.PASSENGERS, Constants.NBT.TAG_LIST)) {
            final NBTTagList passengerList = compound.getTagList(Constants.Entity.PASSENGERS, Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < passengerList.tagCount(); i++) {
                final Entity passenger = impl$ThrowEventAndConstruct(passengerList.getCompoundTagAt(i), world, x, y, z, doesNotForceSpawn);
                if (passenger != null) {
                    passenger.startRiding(entity, true);
                }
            }
        }
        return entity;
    }

    @Inject(method = "updateSpawner", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/storage/AnvilChunkLoader;spawnEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/world/World;)V"))
    public void resetCachedEntity(CallbackInfo ci) {
        this.impl$cachedEntity = null;
    }

    @Inject(method = "updateSpawner", at = @At(value = "RETURN"))
    public void resetCachedEntityOnReturn(CallbackInfo ci) {
        ticks++;
        if (ticks > 20) {
            this.impl$cachedEntity = null;
            ticks = 0;
        }
    }

}
