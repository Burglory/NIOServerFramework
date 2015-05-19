package com.nionetframework.client;

import com.nionetframework.common.NetworkThread;
import com.nionetframework.common.Packet;

public interface ClientNetworkThread extends NetworkThread {

	void offer(Packet p);

}
