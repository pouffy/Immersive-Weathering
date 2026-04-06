package io.github.pouffy.immersive_weathering.network;

import net.mehvahdjukaar.moonlight.api.platform.network.NetworkHelper;

public class NetworkHandler {


    public static void init() {
        NetworkHelper.addNetworkRegistration(NetworkHandler::registerMessages, 1);
    }

    private static void registerMessages(NetworkHelper.RegisterMessagesEvent event) {
        event.registerClientBound(SendCustomParticlesMessage.TYPE);
    }
}
