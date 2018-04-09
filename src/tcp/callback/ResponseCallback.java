package tcp.callback;

import tcp.protocol.DataProtocol;

public interface  ResponseCallback {
	void targetIsOffline(DataProtocol reciveMsg);

    void targetIsOnline(String clientIp);
}
