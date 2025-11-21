public interface ClientInterface {
    boolean isRunning();
    String getAccount();
    int getPort();
    String getHost();
    void disconnect();
    void run();
    boolean connect();
}
