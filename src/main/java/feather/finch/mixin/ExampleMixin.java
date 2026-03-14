package feather.finch.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class ExampleMixin {
    
    @Inject(method = "damage", at = @At("RETURN"))
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // Проверяем, что урон прошел (cir.getReturnValue() == true) и атаковал игрок
        if (cir.getReturnValue() && source.getAttacker() != null && source.getAttacker().isPlayer()) {
            
            // Формируем сообщение: "Урон: [число]" красным цветом
            Text message = Text.literal("Урон: ")
                .append(Text.literal(String.format("%.1f", amount)).formatted(Formatting.RED));

            // Отправляем атакующему в Actionbar (над хотбаром)
            source.getAttacker().sendMessage(message, true);
        }
    }
}
