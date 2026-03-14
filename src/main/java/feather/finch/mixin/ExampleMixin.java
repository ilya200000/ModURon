package feather.finch.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ExampleMixin {

    // Внедряемся в метод установки последнего полученного урона
    @Inject(method = "setLastDamageTaken", at = @At("HEAD"))
    private void onSetLastDamage(float amount, CallbackInfo ci) {
        LivingEntity target = (LivingEntity) (Object) this;
        
        // Проверяем, что урон больше 0 и атакующий — игрок
        if (amount > 0 && target.getAttacker() instanceof ServerPlayerEntity player) {
            
            float hp = target.getHealth();
            float maxHp = target.getMaxHealth();

            // Собираем сообщение
            Text message = Text.literal("Урон: ")
                .append(Text.literal(String.format("%.1f", amount)).formatted(Formatting.RED))
                .append(Text.literal(" | HP: ").formatted(Formatting.GRAY))
                .append(Text.literal(String.format("%.1f", hp)).formatted(Formatting.GREEN))
                .append(Text.literal("/").formatted(Formatting.DARK_GREEN))
                .append(Text.literal(String.format("%.1f", maxHp)).formatted(Formatting.DARK_GREEN));

            // Выводим над хотбаром
            player.sendMessage(message, true);
        }
    }
}
