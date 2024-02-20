import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Stream {
    private Integer streamType;
    private Integer id;
    private Integer streamGenre;
    private Long noOfStreams;
    private Integer streamerId;
    private Long length;
    private Long dateAdded;
    private String name;
    private String creatorName;

    public Stream(Integer type, Integer id, Integer genre, Long nOfStreams,
                  Integer streamerId, Long len, Long dateAdded, String name) {
        this.streamType = type;
        this.id = id;
        this.streamGenre = genre;
        this.noOfStreams = nOfStreams;
        this.streamerId = streamerId;
        this.length = len;
        this.dateAdded = dateAdded;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }
    public Integer getStreamerId() { return streamerId;}
    public Integer getStreamType() { return  streamType;}
    public Long getNoOfStreams() { return noOfStreams;}
    public Long getDateAdded() { return dateAdded;}

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    public void updateNofStreams() {
        noOfStreams++;
    }
    public String formatData() {
        java.util.Date time=new java.util.Date((long)dateAdded*1000 - 7200);
        DateTimeFormatter f = DateTimeFormatter.ofPattern( "E MMM dd HH:mm:ss z uuuu" )
                .withLocale( Locale.US );
        ZonedDateTime zdt = ZonedDateTime.parse( time.toString() , f );
        LocalDate ld = zdt.toLocalDate();
        DateTimeFormatter fLocalDate = DateTimeFormatter.ofPattern( "dd-MM-uuuu" );
        String dateFormat = ld.format( fLocalDate) ;
        return dateFormat;
    }

    private String getDurationString() {
        Long seconds = length;
        Long hours = seconds / 3600;
        Long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        if ( hours == 0 )
            return twoDigitString(minutes) + ":" + twoDigitString(seconds);
        else
            return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":" + twoDigitString(seconds);
    }

    private String twoDigitString(Long number) {
        if (number / 10 == 0) {
            return "0" + number;
        }
        return String.valueOf(number);
    }

    public String toString() {
        formatData();
        return "{" +
                "\"id\":" + "\"" + id + "\"" +
                ",\"name\":" + name +
                ",\"streamerName\":" + creatorName +
                ",\"noOfListenings\":" + "\"" +  noOfStreams + "\"" +
                ",\"length\":" + "\"" + getDurationString() + "\"" +
                ",\"dateAdded\":" + "\"" + formatData() + "\"" +
                "}";
    }
}
