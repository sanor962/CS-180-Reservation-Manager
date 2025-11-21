/**
 * ClientInterface - Interface for Client class
 *
 * @author Saanvi Verma (verma279)
 * @version November 21, 2025
 */

public interface ClientInterface {
    boolean isRunning();
    String getAccount();
    int getPort();
    String getHost();
    void disconnect();
    void start();
    boolean connect();
    void setPort(int port);
    void setHost(String host);
}
