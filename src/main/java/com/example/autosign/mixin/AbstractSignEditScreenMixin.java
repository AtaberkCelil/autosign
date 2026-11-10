package com.example.autosign.mixin;

import com.example.autosign.AutoSignConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignTextSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AbstractSignEditScreen.class)
public abstract class AbstractSignEditScreenMixin {

    @Shadow @Final protected SignBlockEntity sign;
    @Shadow @Final private SignTextSlot slot;

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        if (!AutoSignConfig.INSTANCE.enabled) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            return;
        }

        String textToApply = AutoSignConfig.INSTANCE.defaultText;

        // Send sign text update packet directly to the server
        minecraft.getConnection().send(new ServerboundSignUpdatePacket(
                sign.getBlockPos(),
                List.of(textToApply, "", "", ""),
                slot
        ));

        // Automatically close the sign GUI
        if (AutoSignConfig.INSTANCE.autoCloseScreen) {
            minecraft.execute(() -> minecraft.setScreenAndShow(null));
        }
    }
}
