//aplicatia are comenzi care sunt executat ede useri sau streameri astfel fac o fatada
//ce are o metoda command ce urmeaza sa fie implementata

//parserul citeste inputul in functie de ce fisier are
//dupa ce parsez datele initializez entitati difertie si apoi aplicatia insasi
//obiectul creat de parser este adaugat la array listul de useri, stremeri, si streams ai appului
import java.util.ArrayList;

public class Application implements Subject {
    private ArrayList<User> users;
    private ArrayList<Stream> all_streams;
    private ArrayList<Streamer> streamers;

    public Application() {
        users = new ArrayList<>();
        all_streams = new ArrayList<>();
        streamers = new ArrayList<>();
    }

    /*oberverii aplicatiei + notificarea lor */
    @Override
    public void registerUsers(Observer newUser) {
        if ( newUser instanceof Streamer ) {
            streamers.add((Streamer) newUser);
        } else if ( newUser instanceof  User ) {
            users.add((User) newUser);
        }
    }

    @Override
    public void notifyObserver(Observer observer, Stream stream, UpdateType updateType) {
        if ( observer instanceof Streamer ) {
            observer.updateStreams(this, stream, updateType );
        } else if ( observer instanceof  User ) {
            observer.updateStreams(this, stream, null);
        }
    }

    /*la un nou stream adaugat trb adaugat si la streamerul respectiv
     *streamerul posteaza/sterge*/

    public void postStream(Stream stream) {
        Integer streamerId = stream.getStreamerId();
        Streamer creator = findStreamerId(streamerId);
        if ( creator != null ) {
            stream.setCreatorName(creator.getName());
            notifyObserver(creator, stream, UpdateType.ADD);
        }
    }

    public ArrayList<Stream> getAll_streams() {
        return all_streams;
    }

    public void deleteStream(Integer streamId, Integer idStreamer){
        Streamer creator = findStreamerId(idStreamer);
        Stream deleteStream = new Stream(null,streamId,null,null,
                idStreamer,null,null,null);

        /* aplicatia e observeerul streamerului acum si sterge streamul, apoi invers */

        ArrayList<Stream> updatedStreams = new ArrayList<>();
        for ( Stream stream : creator.getStreams() ) {
            if ( stream.getId().equals(streamId) == false )
                updatedStreams.add(stream);
        }
        creator.setStreams(updatedStreams);

        /*acum streamerul e observer si va notifica aplicatia*/

        notifyObserver(creator, deleteStream, UpdateType.DELETE);
    }

    public void setAll_streams(ArrayList<Stream> all_streams) {
        this.all_streams = all_streams;
    }

    /*userul asculta un stream - notific aplicatia*/
    public void listenStream(User user, Stream stream) {
        notifyObserver(user, stream, null);
    }

    /*metode auxiliare gasit in app:*/
    public Streamer findStreamerId(Integer id){
        for ( Streamer streamer : streamers ) {
            if ( streamer.getId().equals(id) == true )
                return streamer;
        }
        return null;
    }

    public User findUserId(Integer id) {
        for ( User user : users ) {
            if ( user.getId().equals(id) == true )
                return user;
        }
        return null;
    }

    public Stream findStreamId( Integer id ) {
        for ( Stream stream : all_streams ) {
            if ( stream.getId().equals(id) == true )
                return stream;
        }
        return null;
    }
}
