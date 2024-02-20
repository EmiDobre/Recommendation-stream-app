import java.util.List;

public class User implements Observer {
    private Integer id;
    private String name;
    private List<Integer> streams;

    public User(Integer id, String name, List<Integer> streams) {
        this.id = id;
        this.name = name;
        this.streams = streams;
    }

    public Integer getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public List<Integer> getStreams(){
        return streams;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", streams: " + userListToString() ;
    }

    public String userListToString() {
        String list = new String();
        for ( Integer aux : streams ) {
            String str = aux.toString();
            list = list.concat(" -" + str + "- ");
        }
        list = list.concat("\n");
        return list;
    }

    public void historyUpdate(Integer idStream) {
        streams.add(idStream);
    }

    /* userul asculta un stream - nofStreams al celui stream creste
    * se updateaza streamerul cu streamul respectiv si ca urmare si streamul din app*/

    @Override
    public void updateStreams(Subject subject, Stream stream, UpdateType updateType) {
        Application app = (Application) subject;
        Integer streamerId = stream.getStreamerId();
        Streamer creator = app.findStreamerId(streamerId);
        if ( creator == null )
            return;
        creator.streamIsWatched(stream);
    }

}
