public abstract class CommandParserStrategy {
    public abstract Command parseCommand(String[] info, Integer id);
}

/*fiecare startegie de a face comenzi se bazeaza lungimea inputului*/
class ParserCommand extends Parser {
    public ParserCommand(String filePath, CommandDispatcher commandDispatcher) {
        super(filePath, commandDispatcher);
    }
    public int initAppWithInfo(String data, Application app) {

        String[] info = data.split(" ");
        Integer id = Integer.valueOf(info[0]);

        if ( info.length < 1 )
            return -1;

        CommandParserStrategy strategy = null;

        if ( info.length == 2 )
            strategy = new StrategyForLength2();
        if ( info.length == 3 ) {
            strategy = new StrategyForLength3();
        }
        if ( info.length > 6 )
            strategy = new StrategyForLentgh6();

        Command command = strategy.parseCommand(info, id);

        if ( command != null )
            dispatcher.addCommand(command);

        return 0;
    }
}

class StrategyForLength2 extends CommandParserStrategy {
    @Override
    public Command parseCommand(String[] info, Integer id) {
        if ( info[1].equals("LIST") == true ) {
            return new ListStreams(id);
        }
        return null;
    }
}

class StrategyForLength3 extends CommandParserStrategy {
    @Override
    public Command parseCommand(String[] info, Integer id) {

        if ( info[1].equals("LISTEN") == true ) {
            Integer streamId = Integer.valueOf(info[2]);
            return new UserListensToStream(id,streamId);
        }

        if ( info[1].equals("DELETE") == true ) {
            Integer streamId = Integer.valueOf(info[2]);
            return new StreamerUpdatesStreams(id, null, streamId, null,
                    null, null);
        }

        String streamTYpe = info[2];
        Integer idType = null;
        if (streamTYpe.equals("SONG") == true)
            idType = 1;

        if (streamTYpe.equals("PODCAST") == true)
            idType = 2;

        if (streamTYpe.equals("AUDIOBOOK") == true)
            idType = 3;


        if (info[1].equals("SURPRISE") == true) {
            if (idType != null)
                return new Surpise_Streams(id, idType);
        }

        if (info[1].equals("RECOMMEND") == true) {
            if (idType != null)
                return new Recommend_Streams(id, idType);
        }

        return null;
    }
}

class StrategyForLentgh6 extends CommandParserStrategy {
    @Override
    public Command parseCommand(String[] info, Integer id) {
        if ( info[1].equals("ADD") == true ) {
            Integer type = Integer.valueOf(info[2]);
            Integer streamId = Integer.valueOf(info[3]);
            Integer streamGnere = Integer.valueOf(info[4]);
            Long length = Long.valueOf(info[5]);
            String name = "\"";
            int i;
            for ( i = 6; i < info.length - 1; i++ ) {
                name = name.concat(info[i]);
                name = name.concat(" ");
            }
            name = name.concat(info[i]);
            name = name.concat("\"");

            if ( name.equals("\"") == false ) {
                return new StreamerUpdatesStreams(id, type, streamId, streamGnere,
                        length, name);
            }
        }

        return null;
    }
}
