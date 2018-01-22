package entity;

public class HttpResult<T> {

    private int code;
    private String msg;
    private T result;
    
	public void setCode(int code) {
		this.code = code;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public void setResult(T result) {
		this.result = result;
	}

    

}
