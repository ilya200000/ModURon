package feather.finch;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeatherFinch implements ModInitializer {
    public static final String MOD_ID = "featherfinch";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Тут пока пусто, чтобы ничего не ломалось
        LOGGER.info("FeatherFinch загружен. Жду приказов!");
    }
}
