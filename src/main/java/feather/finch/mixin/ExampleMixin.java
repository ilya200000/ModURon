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

    // Используем remap = true, чтобы Fabric сам подставил нужное имя метода
    @Inject(method = "damage", at = @At("RETURN"), remap = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // Проверяем: урон нанесен (return true) и атакующий — игрок
        if (cir.getReturnValue() && source.getAttacker() instanceof ServerPlayerEntity player) {
            
            // Получаем цель (сущность, в которую "подмешан" этот код)
            LivingEntity target = (LivingEntity) (Object) this;
            
            float currentHealth = target.getHealth();
            float maxHealth = target.getMaxHealth();

            // Создаем красивую строку урона и здоровья
            Text message = Text.literal("Урон: ")
                .append(Text.literal(String.format("%.1f", amount)).formatted(Formatting.RED))
                .append(Text.literal(" | HP: ").formatted(Formatting.GRAY))
                .append(Text.literal(String.format("%.1f", currentHealth)).formatted(Formatting.GREEN))
                .append(Text.literal("/").formatted(Formatting.DARK_GRAY))
                .append(Text.literal(String.format("%.1f", maxHealth)).formatted(Formatting.DARK_GREEN));

            // Отправляем в Actionbar (над хотбаром)
            player.sendMessage(message, true);
        }
    }
}
