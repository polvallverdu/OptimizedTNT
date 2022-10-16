package engineer.pol.optimizedtnt.client.mixin;

import net.minecraft.client.render.entity.TntEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TntEntityRenderer.class)
public class TntEntityRendererMixin {

    /*@Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(CallbackInfo info) {
        info.cancel();
    }*/

}
