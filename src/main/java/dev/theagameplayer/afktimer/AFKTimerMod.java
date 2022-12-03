package dev.theagameplayer.afktimer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.theagameplayer.afktimer.AFKEventManager.ClientEvents;
import dev.theagameplayer.afktimer.AFKEventManager.ServerEvents;
import dev.theagameplayer.afktimer.network.AFKPacketHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = AFKTimerMod.MODID)
public final class AFKTimerMod {
	public static final String MODID = "afktimer";
	private static final Logger LOGGER = LogManager.getLogger(MODID);

	public AFKTimerMod() {
		//TODO:: Client-Side only
		//ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::commonSetup);
		DistExecutor.unsafeRunForDist(() -> ClientEvents::new, () -> ServerEvents::new);
	}
	
	private final void commonSetup(final FMLCommonSetupEvent eventIn) {
		AFKPacketHandler.registerPackets();
		LOGGER.info("Finished common setup.");
	}
}