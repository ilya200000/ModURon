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

    /**
     * Внедряемся в метод setLastDamageTaken (техническое имя: method_6068).
     * Мы используем remap = false, чтобы Mixin не пытался переименовать это при сборке.
     */
    @Inject(
        method = "method_6068(F)V", 
        at = @At("HEAD"), 
        remap = false
    )
    private void onSetLastDamage(float amount, CallbackInfo ci) {
        LivingEntity target = (LivingEntity) (Object) this;
        
        // Проверяем, что атакующий — это игрок на сервере
        if (amount > 0 && target.getAttacker() instanceof ServerPlayerEntity player) {
            
            float hp = target.getHealth();
            float maxHp = target.getMaxHealth();

            // Создаем текст: Урон: [X] | HP: [Y]/[Z]
            Text message = Text.literal("Урон: ")
                .append(Text.literal(String.format("%.1f", amount)).formatted(Formatting.RED))
                .append(Text.literal(" | HP: ").formatted(Formatting.GRAY))
                .append(Text.literal(String.format("%.1f", hp)).formatted(Formatting.GREEN))
                .append(Text.literal("/").formatted(Formatting.DARK_GREEN))
                .append(Text.literal(String.format("%.1f", maxHp)).formatted(Formatting.DARK_GREEN));

            // Отправляем в Actionbar (над хотбаром)
            player.sendMessage(message, true);
        }
    }
}
