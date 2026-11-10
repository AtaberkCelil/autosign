package com.ataberkcelil.autosign.mixin;

import com.example.autosign.AutoSignConfig;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignEditScreen.class)
public abstract class AbstractSignEditScreenMixin {

    @Shadow @Final private SignBlockEntity blockEntity;
    @Shadow @Final private boolean front;

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        if (!AutoSignConfig.INSTANCE.enabled) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) {
            return;
        }

        String textToApply = AutoSignConfig.INSTANCE.defaultText;

        // Send sign text update packet directly to the server
        client.player.networkHandler.sendPacket(new UpdateSignC2SPacket(
                this.blockEntity.getPos(),
                this.front,
                textToApply,
                "",
                "",
                ""
        ));

        // Automatically close the sign GUI
        if (AutoSignConfig.INSTANCE.autoCloseScreen) {
            client.execute(() -> client.setScreen(null));
        }
    }
}