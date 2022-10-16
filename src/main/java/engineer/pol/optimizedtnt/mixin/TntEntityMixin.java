package engineer.pol.optimizedtnt.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntity.class)
public class TntEntityMixin {

    @Mixin(TntEntity.class)
    public interface TntEntityAccessor {
        @Invoker("explode")
        public void explode();
    }

    @Mixin(Entity.class)
    public interface EntityAccessor {
        @Accessor("world")
        public World getWorld();
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo info) {
        TntEntity tntEntity = (TntEntity) (Object) this;
        int i = tntEntity.getFuse() - 1;
        tntEntity.setFuse(i);
        if (i <= 0) {
            tntEntity.discard();
            if (!((EntityAccessor) this).getWorld().isClient) {
                ((TntEntityAccessor) this).explode();
            }
        }
        info.cancel();
    }
}
