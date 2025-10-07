package com.gdar463.minefy;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinefyClient implements ClientModInitializer {
    public static final String MOD_ID = "minefy/client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

//    public static Screen toOpen;
//    public static int screenTicks = 0;

    @Override
    public void onInitializeClient() {
//        ClientTickEvents.START_CLIENT_TICK.register(client -> {
//            if (toOpen != null) {
//                screenTicks++;
//                if (screenTicks > 5) {
//                    client.setScreen(toOpen);
//                    toOpen = null;
//                    screenTicks = 0;
//                }
//            }
//        });
    }
}