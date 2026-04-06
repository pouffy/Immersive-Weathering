package io.github.pouffy.immersive_weathering.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ParticleHelper {
    public static void spawnParticlesOnBlockFaces(Level level, BlockPos pos, ParticleOptions particle, UniformInt uniformInt) {
        for(Direction direction : Direction.values()) {
            int i = uniformInt.sample(level.random);

            for(int j = 0; j < i; ++j) {
                spawnParticleOnFace(level, pos, direction, particle);
            }
        }

    }

    //whats the diff from ParticleUtils one?
    //TODO: replace this with the function I made for sup
    public static void spawnParticleOnFace(Level level, BlockPos pos, Direction direction, ParticleOptions particle) {
        Vec3 vec3 = Vec3.atCenterOf(pos);
        int i = direction.getStepX();
        int j = direction.getStepY();
        int k = direction.getStepZ();
        double d0 = vec3.x + (i == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double)i * 0.55D);
        double d1 = vec3.y + (j == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double)j * 0.55D);
        double d2 = vec3.z + (k == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double)k * 0.55D);
        //TODO: generalize this and redo leaf particle physics
        level.addParticle(particle, d0, d1, d2, 0, 0, 0);
    }
}
