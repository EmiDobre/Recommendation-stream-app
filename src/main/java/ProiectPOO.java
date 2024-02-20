import java.io.IOException;

public class ProiectPOO {

    public static void main(String[] args) {
        if(args == null) {
            System.out.println("Nothing to read here");
            return;
        }
        if ( args.length < 4 )
            return;

        Application app = new Application();
        ParserFactory parserFactory = ParserFactory.OnlyInstance();
        CommandDispatcher commandDispatcher = new CommandDispatcher();

        for ( int i = 0; i < 4; i++ ) {
            try {
                Parser parser = parserFactory.parseData(args[i], commandDispatcher);
                parser.readAppData(app);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if ( app != null ) {
            if (commandDispatcher != null) {
                commandDispatcher.executeCommands(app);
            }
        }
    }
}
