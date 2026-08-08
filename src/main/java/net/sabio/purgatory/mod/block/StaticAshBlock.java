package net.sabio.purgatory.mod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class StaticAshBlock extends Block {
    private static final float TOUCH_TELEPORT_CHANCE = 0.02f;
    private static final Random RANDOM = new Random();

    public StaticAshBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);

        if (!(entity instanceof LivingEntity living) || level.isClientSide()) {
            return;
        }

        // TODO: Add custom effect that glitches the screen or something cool idk
        MobEffectInstance existingNausea = living.getEffect(MobEffects.NAUSEA);
        if (existingNausea == null || existingNausea.getDuration() < 100) {
            living.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 5, false, false));
        }
        living.push(0.0, 0.05, 0.0);
        living.hurtMarked = true;

        if (RANDOM.nextFloat() < TOUCH_TELEPORT_CHANCE) {
            double range = 3.0;
            living.randomTeleport(
                    living.getX() + (RANDOM.nextDouble() - 0.5) * range,
                    living.getY(),
                    living.getZ() + (RANDOM.nextDouble() - 0.5) * range,
                    true
            );
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.PORTAL,
                        living.getX(),
                        living.getY() + 1.0,
                        living.getZ(),
                        20,
                        0.3,
                        0.5,
                        0.3,
                        0.02
                );
            }
        }
    }

    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        serverLevel.sendParticles(
                ParticleTypes.ELECTRIC_SPARK,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                30,
                0.4,
                0.4,
                0.4,
                0.15
        );

        Vec3 away = player.position().subtract(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5).normalize();
        player.push(away.x * 0.6, 0.15, away.z * 0.6);
        player.hurtMarked = true;
    }
}
