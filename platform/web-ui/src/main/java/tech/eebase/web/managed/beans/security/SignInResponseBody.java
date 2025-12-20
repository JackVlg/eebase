package tech.eebase.web.managed.beans.security;

public class SignInResponseBody {
    private final String msg;

    public SignInResponseBody(String msg) {
        super();
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
    
}
