package entity;

public interface HttpCallback {
	void onSuccess();
	void onError(int errorcode,String errormsg);
	void onComplete();

}
