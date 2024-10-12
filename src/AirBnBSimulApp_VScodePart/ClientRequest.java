package AirBnBSimulApp_VScodePart;

import java.net.Socket;

public class ClientRequest {
    private static final long serialVersionUID = 8626459955423694374L;
    private Object paketaReducer;
    private String requestBody;

    public ClientRequest(Object paketaReducer, String requestBody) {
        this.paketaReducer = paketaReducer;
        this.requestBody = requestBody;
    }

    public Object getPaketaReducer() {
        return paketaReducer;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setPaketaReducer(Object PaketaReducer) {
        this.paketaReducer = PaketaReducer;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
}