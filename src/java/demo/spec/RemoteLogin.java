package demo.spec;

public interface RemoteLogin {
    UserAccess connect(String usr, String passwd);
}

// user will never directly access message wall, always through user access authentification