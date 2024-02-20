//parser factory
//parseaza diferit in functie de ce fisier analizeaza
// fiecare parser init un obiect auxiliar care se va adauga la aplicatie

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ParserFactory {
    private static ParserFactory uniqueInstance;
    private ParserFactory() {}
    public static ParserFactory OnlyInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new ParserFactory();
        return uniqueInstance;
    }
    public Parser parseData(String filePath, CommandDispatcher commandDispatcher) throws IOException {

        if ( filePath.contains("streamers.csv") == true )
            return new ParserStreamer(filePath, null);

        if ( filePath.contains("streams.csv") == true )
            return new ParserStream(filePath, null);

        if ( filePath.contains("users.csv") == true )
            return new ParserUser(filePath, null);

        if ( filePath.contains("test") == true )
            return new ParserCommand(filePath, commandDispatcher);

        return null;
    }
}


abstract class Parser {
    CommandDispatcher dispatcher;
    String absolutePath;
    public Parser(String filePath, CommandDispatcher commandDispatcher) {
        absolutePath = "src/main/resources/";
        absolutePath = absolutePath.concat(filePath);
        this.dispatcher = commandDispatcher;
    }

    public void readAppData(Application app) {
        File file = new File(absolutePath);
        try {
            Scanner scanner = new Scanner(file);

            String line = null;
            while ( scanner.hasNextLine() ) {
                line = scanner.nextLine();
                initAppWithInfo(line,app);
            }
            scanner.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract int initAppWithInfo(String data, Application app);

}

/*linia care contine streamerType trebuie ignorata*/
class ParserStreamer extends Parser {

    public ParserStreamer(String filePath, CommandDispatcher commandDispatcher){
        super(filePath, commandDispatcher);
    }

    public int initAppWithInfo(String data, Application app) {
        String[] info = data.split(",", 3);

        if ( info.length != 3 )
            return 1;

        String type = info[0];
        if ( type.contains("streamer") == true )
            return -1;

        Integer streamerType = Integer.valueOf(info[0].substring(1,2));
        String id_str = info[1];
        int index = id_str.length() - 1;
        id_str = id_str.substring(1, index);
        Integer id = Integer.valueOf(id_str);
        String name = info[2];

        Streamer streamer = new Streamer(streamerType, id, name);
        app.registerUsers(streamer);

        return 0;
    }

}

/*linia care contine "streamType" nu trebuie parsata */
class ParserStream extends Parser {
    public ParserStream(String filePath, CommandDispatcher commandDispatcher){
        super(filePath, commandDispatcher);
    }

    public int initAppWithInfo(String data, Application app) {
        String[] info = data.split(",", 8);

        if ( info.length != 8 )
            return 1;

        String typeStr = info[0];
        if ( typeStr.contains("stream") == true )
            return -1;

        Integer type = Integer.valueOf(info[0].substring(1,2));
        int index = info[1].length() - 1;
        Integer id = Integer.valueOf(info[1].substring(1,index));
        index = info[2].length() - 1;
        Integer genre = Integer.valueOf(info[2].substring(1,index));
        index = info[3].length() - 1;
        Long nrStreams = Long.valueOf(info[3].substring(1,index));
        index = info[4].length() - 1;
        Integer streamerId = Integer.valueOf(info[4].substring(1,index));
        index = info[5].length() - 1;
        Long length = Long.valueOf(info[5].substring(1,index));
        index = info[6].length() - 1;
        Long dateAdded = Long.valueOf(info[6].substring(1,index));
        String name = info[7];


        Stream stream = new Stream(type,id,genre,nrStreams,streamerId,length,dateAdded,name);
        app.postStream(stream);
        return 0;
    }
}

/*linia care contine id trb ignorata*/
class ParserUser extends Parser {

    public ParserUser(String filePath, CommandDispatcher commandDispatcher){
        super(filePath, commandDispatcher);
    }

    public int initAppWithInfo(String data, Application app) {
        String[] info = data.split(",", 3);

        if ( info.length != 3 )
            return 1;

        String id_str = info[0];
        if ( id_str.contains("id") == true )
            return -1;

        int index = info[0].length() - 1;
        Integer id = Integer.valueOf(info[0].substring(1,index));
        String name = info[1];

        index = info[2].length() - 1;
        String listInfo = info[2].substring(1,index);

        StringTokenizer token = new StringTokenizer(listInfo, " ");
        ArrayList<Integer> streamHistory = new ArrayList<>();

        while ( token.hasMoreTokens() ) {
            Integer stream = Integer.valueOf(token.nextToken());
            streamHistory.add(stream);
        }

        User user = new User(id, name, streamHistory);
        app.registerUsers(user);
        return 0;
    }
}
