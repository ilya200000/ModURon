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

    @Inject(method = "damage", at = @At("RETURN"))
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // Проверяем: урон прошел успешно (return true) и атакующий — это игрок на сервере
        if (cir.getReturnValue() && source.getAttacker() instanceof ServerPlayerEntity player) {
            
            // Создаем текст сообщения: "Урон: [число]"
            // Округляем до 1 знака после запятой и красим в красный
            Text message = Text.literal("Урон: ")
                .append(Text.literal(String.format("%.1f", amount)).formatted(Formatting.RED));

            // Отправляем сообщение именно в Actionbar (над хотбаром)
            // У ServerPlayerEntity метод sendMessage поддерживает (Text, boolean)
            player.sendMessage(message, true);
        }
    }
}
