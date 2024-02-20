import java.util.Comparator;

public class StreamComp implements Comparator<Stream> {
    @Override
    public int compare(Stream left, Stream right) {
        if ( left.getNoOfStreams() > right.getNoOfStreams() )
            return 1;
        if ( left.getNoOfStreams() < right.getNoOfStreams() )
            return -1;
        return 0;
    }
}

class SurpriseComp implements Comparator<Stream> {
    @Override
    public int compare(Stream left, Stream right) {
        if ( left.getDateAdded() < right.getDateAdded() )
            return 1;
        if ( left.getDateAdded() > right.getDateAdded() )
            return -1;

        if ( left.getDateAdded() == left.getDateAdded() ) {
            StreamComp streamComp = new StreamComp();
            return streamComp.compare(right, left);
        }

        return 0;
    }
}
