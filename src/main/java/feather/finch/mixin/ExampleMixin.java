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

    // Пытаемся внедриться в метод damage. Если одно имя не сработает, попробуем другое через маппинги.
    @Inject(method = "damage", at = @At("RETURN"), remap = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // Проверяем: урон прошел (true) и бил игрок
        if (cir.getReturnValue() && source.getAttacker() instanceof ServerPlayerEntity player) {
            
            LivingEntity target = (LivingEntity) (Object) this;
            float hp = target.getHealth();
            float maxHp = target.getMaxHealth();

            // Собираем красивую строку: Урон: [число] | HP: [X]/[Y]
            Text message = Text.literal("Урон: ")
                .append(Text.literal(String.format("%.1f", amount)).formatted(Formatting.RED))
                .append(Text.literal(" | HP: ").formatted(Formatting.GRAY))
                .append(Text.literal(String.format("%.1f", hp)).formatted(Formatting.GREEN))
                .append(Text.literal("/").formatted(Formatting.DARK_GREEN))
                .append(Text.literal(String.format("%.1f", maxHp)).formatted(Formatting.DARK_GREEN));

            player.sendMessage(message, true);
        }
    }
}
