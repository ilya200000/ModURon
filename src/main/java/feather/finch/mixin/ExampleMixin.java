package feather.finch.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class ExampleMixin {

    @Inject(
        method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", 
        at = @At("RETURN"), 
        remap = true
    )
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // Если урон прошел (вернул true) и атакующий — игрок
        if (Boolean.TRUE.equals(cir.getReturnValue()) && source.getAttacker() instanceof ServerPlayerEntity player) {
            
            LivingEntity target = (LivingEntity) (Object) this;
            float currentHealth = target.getHealth();
            float maxHealth = target.getMaxHealth();

            Text message = Text.literal("Урон: ")
                .append(Text.literal(String.format("%.1f", amount)).formatted(Formatting.RED))
                .append(Text.literal(" | HP: ").formatted(Formatting.GRAY))
                .append(Text.literal(String.format("%.1f", currentHealth)).formatted(Formatting.GREEN))
                .append(Text.literal("/").formatted(Formatting.DARK_GREEN))
                .append(Text.literal(String.format("%.1f", maxHealth)).formatted(Formatting.DARK_GREEN));

            player.sendMessage(message, true);
        }
    }
}
