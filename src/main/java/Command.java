import java.util.*;
class CommandDispatcher {
    public ArrayList<Command> commandList;

    public CommandDispatcher() {
        commandList = new ArrayList<Command>();
    }
    public void addCommand(Command command) {
        commandList.add(command);
    }

    public void executeCommands(Application app) {
        for ( Command currentCommand : commandList ) {
            currentCommand.execute(app);
        }
    }
}

public abstract class Command {
    public abstract void execute(Application app);
}

/*afisare lista streamuri pt streamer sau user*/
class ListStreams extends Command {
    Integer id;

    public ListStreams(Integer id) {
        this.id = id;
    }

    @Override
    public void execute(Application app) {

        User user = app.findUserId(id);
        if ( user == null ) {
            Streamer streamer = app.findStreamerId(id);
            if ( streamer == null )
                return;
            System.out.println(streamer.streamsToString());

            return;
        }

        /*in user trb sa gasesc streamurile:*/

        String userStreamsStr = "[";
        int index = 1;
        for ( Integer idStream : user.getStreams() ) {
            Stream stream = app.findStreamId(idStream);
            if ( stream != null ) {
                userStreamsStr = userStreamsStr.concat(stream.toString());
                if ( index != user.getStreams().size() )
                    userStreamsStr = userStreamsStr.concat(",");
                index++;
            }
        }
        userStreamsStr = userStreamsStr.concat("]");
        System.out.println(userStreamsStr);
    }
}

class StreamerUpdatesStreams extends Command {
    Integer idStreamer;
    Integer type;
    Integer streamId;
    Integer streamGnere;
    Long length;
    String name;

    public StreamerUpdatesStreams( Integer idStreamer, Integer type, Integer streamId,
                                   Integer streamGnere, Long length, String name) {
        this.idStreamer = idStreamer;
        this.type = type;
        this.streamId = streamId;
        this.streamGnere = streamGnere;
        this.length = length;
        this.name = name;
    }
    @Override
    public void execute(Application app) {
        if ( type == null ) {
            app.deleteStream(streamId, idStreamer);
            return;
        }

        Stream stream = new Stream(type,streamId,streamGnere,0l,idStreamer,length,
                1673560800l,name);
        app.postStream(stream);
    }
}

/*comenzi useri:*/
class UserListensToStream extends Command {
    Integer userId;
    Integer streamId;

    public UserListensToStream(Integer userId, Integer streamId) {
        this.userId = userId;
        this.streamId = streamId;
    }

    @Override
    public void execute(Application app) {

        //gasesc userul care asculta streamul
        User user = app.findUserId(userId);
        if ( user == null )
            return;
        Stream stream = app.findStreamId(streamId);
        if ( stream == null )
            return;
        user.historyUpdate(streamId);

        /*acum userul va schimba datele aplicatiei pt ca a ascultat un stream:*/
        app.listenStream(user, stream);
    }
}

class Recommend_Streams extends Command {
    Integer userId;
    Integer streamType;

    public Recommend_Streams(Integer userId, Integer streamType) {
        this.userId = userId;
        this.streamType = streamType;
    }
    @Override
    public void execute(Application app) {

        User user = app.findUserId(userId);
        if ( user == null )
            return;
        List<Integer> userSLists = user.getStreams();
        PriorityQueue<Stream> streamsToRecc = new PriorityQueue<Stream>( new StreamComp());

        /* gasesc streamul ascultat -  streamerul sau - stochez streamurile acestuia */
        for ( Integer id : userSLists ) {
            Stream stream  = app.findStreamId(id);
            Integer idSTreamer = stream.getStreamerId();
            Streamer streamer = app.findStreamerId(idSTreamer);
            for ( Stream auxStr : streamer.getStreams() ) {
                if ( auxStr.equals(stream) == false ) {
                    Integer auxType = auxStr.getStreamType();
                    if ( auxType.equals(streamType) == true )
                        streamsToRecc.add(auxStr);
                }
            }
        }

        String result = "[";
        int n = Math.min(streamsToRecc.size(), 5);
        for ( int i = 0; i < n; i++ ) {
            Stream stream = streamsToRecc.poll();
            result = result.concat(stream.toString());
            if ( i != n-1 )
                result = result.concat(",");
        }
        result = result.concat("]");
        System.out.println(result);

    }
}

class Surpise_Streams extends Command {

    Integer userId;
    Integer streamType;

    public Surpise_Streams(Integer userId, Integer streamType) {
        this.userId = userId;
        this.streamType = streamType;
    }
    @Override
    public void execute(Application app) {

        User user = app.findUserId(userId);
        if ( user == null )
            return;
        List<Integer> userSLists = user.getStreams();
        ArrayList<Integer> userIDStreamers = new ArrayList<Integer>();

        /* lista id-uri streameri ascultati */
        for ( Integer id : userSLists ) {
            Stream stream = app.findStreamId(id);
            Integer idSTreamer = stream.getStreamerId();
            userIDStreamers.add(idSTreamer);
        }

        /* */
        PriorityQueue<Stream> surpriseStr = new PriorityQueue<Stream>(new SurpriseComp());
        for (Stream auxStr : app.getAll_streams()) {
            Integer auxType = auxStr.getStreamType();
            if (auxType.equals(streamType) == true) {
                Integer auxStreamerId = auxStr.getStreamerId();

                int alreadyListened = 0;
                for (Integer creatorID : userIDStreamers) {
                    if (auxStreamerId.equals(creatorID) == false)
                        alreadyListened++;
                }

                if (alreadyListened == userIDStreamers.size())
                    surpriseStr.add(auxStr);
            }
        }

        String result = "[";
        int n = Math.min(surpriseStr.size(), 3);
        for ( int i = 0; i < n; i++ ) {
            Stream stream = surpriseStr.poll();
            result = result.concat(stream.toString());
            if ( i != n-1 )
                result = result.concat(",");
        }
        result = result.concat("]");
        System.out.println(result);

    }
}
