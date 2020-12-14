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
package org.spongepowered.common.mixin.core.entity.projectile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.projectile.arrow.Arrow;
import org.spongepowered.api.entity.projectile.arrow.ArrowEntity;
import org.spongepowered.api.projectile.source.ProjectileSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.bridge.world.WorldBridge;
import org.spongepowered.common.event.SpongeCommonEventFactory;

import javax.annotation.Nullable;

@Mixin(AbstractArrowEntity.class)
public abstract class AbstractArrowEntityMixin extends ProjectileEntityMixin {

    // @formatter:off
    @Shadow private int life;
    @Shadow protected boolean inGround;
    @Shadow public int shakeTime;
    @Shadow public AbstractArrowEntity.PickupStatus pickup;
    @Shadow @Nullable private BlockState lastState;

    @Shadow public abstract void shadow$setCritArrow(boolean critical);
    @Shadow public abstract void shadow$setPierceLevel(byte level);
    @Shadow public abstract void shadow$setShotFromCrossbow(boolean fromCrossbow);
    @Shadow protected abstract ItemStack shadow$getPickupItem();
    // @formatter:on


    @Shadow protected abstract void resetPiercedEntities();

    // Not all ProjectileSources are entities (e.g. BlockProjectileSource).
    // This field is used to store a ProjectileSource that isn't an entity.
    @Nullable public ProjectileSource projectileSource;

    /**
     * Collide impact event post for plugins to cancel impact.
     */
    @Inject(method = "onHitBlock", at = @At("HEAD"), cancellable = true)
    private void onProjectileHit(final BlockRayTraceResult hitResult, final CallbackInfo ci) {
        if (!((WorldBridge) this.world).bridge$isFake() && hitResult.getType() != RayTraceResult.Type.MISS) {
            if (SpongeCommonEventFactory.handleCollideImpactEvent((AbstractArrowEntity) (Object) this,
                    ((ArrowEntity) this).get(Keys.SHOOTER).orElse(null), hitResult)) {
                this.shadow$playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                // Make it almost look like it collided with something
                BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)hitResult;
                BlockState blockstate = this.world.getBlockState(blockraytraceresult.getBlockPos());
                this.lastState = blockstate;
                Vector3d vec3d = blockraytraceresult.getLocation().subtract(this.shadow$getX(), this.shadow$getY(), this.shadow$getZ());
                this.shadow$setDeltaMovement(vec3d);
                Vector3d vec3d1 = vec3d.normalize().scale(0.05F);
                this.shadow$setPosition(this.shadow$getX() - vec3d1.x, this.shadow$getY() - vec3d1.y, this.shadow$getZ() -
                        vec3d1.z);
                this.inGround = true;
                this.shakeTime = 7;
                this.shadow$setCritArrow(false);
                this.shadow$setPierceLevel((byte)0);
                this.shadow$setShotFromCrossbow(false);
                this.resetPiercedEntities();

                ci.cancel();
            }
        }
    }
    /**
     * Collide impact event post for plugins to cancel impact.
     */
    @Inject(method = "onHitEntity", at = @At("HEAD"), cancellable = true)
    private void onProjectileHit(final EntityRayTraceResult hitResult, final CallbackInfo ci) {
        if (!((WorldBridge) this.world).bridge$isFake() && hitResult.getType() != RayTraceResult.Type.MISS) {
            if (SpongeCommonEventFactory.handleCollideImpactEvent((AbstractArrowEntity) (Object) this,
                    ((ArrowEntity) this).get(Keys.SHOOTER).orElse(null), hitResult)) {
                this.shadow$playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                // Make it almost look like it collided with something

                // Deflect the arrow as if the entity was invulnerable
                this.shadow$setDeltaMovement(this.shadow$getDeltaMovement().scale(-0.1D));
                this.rotationYaw += 180.0F;
                this.prevRotationYaw += 180.0F;
                this.life = 0;
                if (!this.world.isClientSide && this.shadow$getDeltaMovement().lengthSqr() < 1.0E-7D) {
                    if (this.pickup == AbstractArrowEntity.PickupStatus.ALLOWED) {
                        this.shadow$entityDropItem(this.shadow$getPickupItem(), 0.1F);
                    }

                    this.shadow$remove();
                }
                ci.cancel();
            }
        }
    }

}
