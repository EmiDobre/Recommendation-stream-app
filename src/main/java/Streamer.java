import java.util.ArrayList;
public class Streamer implements Observer {
    private Integer streamerType;
    private Integer id;
    private String name;
    private ArrayList<Stream> streams;

    public Streamer(Integer streamerType, Integer id, String name) {
        this.streamerType = streamerType;
        this.id = id;
        this.name = name;
        this.streams = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public ArrayList<Stream> getStreams() {
        return streams;
    }

    @Override
    public String toString() {
        return streamsToString();
    }

    public String streamsToString() {
        String string = "[";
        int index = 1;
        for ( Stream aux : streams ) {
            string = string.concat(aux.toString());
            if ( index != streams.size() )
                string = string.concat(",");
            index++;
        }
        string = string.concat("]");
        return string;
    }

    /*metodele observerului:*/
    public void setStreams(ArrayList<Stream> streams) {
        this.streams = streams;
    }

    @Override
    public void updateStreams(Subject app, Stream stream, UpdateType updateType) {

        if ( updateType.equals(UpdateType.DELETE) == true )
            delete(app,stream);

        if ( updateType.equals(UpdateType.ADD) == true )
            add(app, stream);

    }

    public void add(Subject app, Stream stream) {
        streams.add(stream);
        ArrayList<Stream> appStreams = ((Application)app).getAll_streams();
        appStreams.add(stream);
    }

    public void delete(Subject app, Stream stream) {
        ArrayList<Stream> updateAll = new ArrayList<>();
        Application myApp = ((Application)app);
        ArrayList<Stream> currentStreams = myApp.getAll_streams();
        Integer streamId = stream.getId();

        for ( Stream aux : currentStreams ) {
            if ( aux.getId().equals(streamId) == false )
                updateAll.add(stream);
        }
        myApp.setAll_streams(updateAll);
    }

    public void streamIsWatched(Stream stream) {
        for ( Stream aux : streams ) {
            if ( aux.equals(stream) == true ) {
                aux.updateNofStreams();
            }
        }
    }
}
