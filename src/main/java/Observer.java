public interface Observer {
    void updateStreams(Subject subject, Stream stream, UpdateType updateType);
}

interface Subject {
    void registerUsers(Observer newUser);
    void notifyObserver(Observer observer, Stream stream, UpdateType updateType);

}

/*clasa pt a stii ce update fac:*/
enum UpdateType {
    DELETE,
    ADD
}