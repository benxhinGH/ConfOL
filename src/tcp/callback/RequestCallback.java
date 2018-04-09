package tcp.callback;

import tcp.protocol.BasicProtocol;

public interface RequestCallback {
	
	void onSuccess(BasicProtocol msg);

    void onFailed(int errorCode, String msg);

}
