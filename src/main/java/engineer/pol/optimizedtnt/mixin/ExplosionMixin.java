package engineer.pol.optimizedtnt.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Final
    private @Shadow World world;

    @Nullable @Final
    private @Shadow Entity entity;

    @Final
    private @Shadow double x;
    @Final
    private @Shadow double y;
    @Final
    private @Shadow double z;

    @Final
    private @Shadow ObjectArrayList<BlockPos> affectedBlocks;
    @Final
    private @Shadow float power;

    @Inject(method = "collectBlocksAndDamageEntities", at = @At("HEAD"), cancellable = true)
    public void onCollectBlocksAndDamageEntities(CallbackInfo info) {
        this.world.emitGameEvent(this.entity, GameEvent.EXPLODE, new Vec3d(this.x, this.y, this.z));

        for (int x = (int) (Math.floor(this.x)-power); x < this.x+power; x++) {
            for (int y = (int) (Math.floor(this.y)-power); y < this.y+power; y++) {
                for (int z = (int) (Math.floor(this.z)-power); z < this.z+power; z++) {
                    this.affectedBlocks.add(new BlockPos(x, y, z));
                }
            }
        }

        info.cancel();
    }

    @Inject(method = "affectWorld", at = @At("HEAD"), cancellable = true)
    public void onAffectWorld(boolean bl, CallbackInfo info) {
        for (BlockPos blockPos : this.affectedBlocks) {
            BlockState blockState = this.world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (blockState.isAir()) continue;
            this.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
            block.onDestroyedByExplosion(this.world, blockPos, (Explosion)(Object)this);
        }
        info.cancel();
    }

}
