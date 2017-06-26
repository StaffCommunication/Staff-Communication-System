package loginui;

import javafx.beans.property.SimpleStringProperty;

public class Person {
    private final SimpleStringProperty event;
    private final SimpleStringProperty venue;
    private final SimpleStringProperty date;
    public Person(String eve, String ven,String date) {
            this.event = new SimpleStringProperty(eve);
            this.venue = new SimpleStringProperty(ven);
            this.date = new SimpleStringProperty(date);
        }
 
        public String getEvent() {
            return event.get();
        }
 
        public void setEvent(String eve) {
            event.set(eve);
        }
 
        public String getVenue() {
            return venue.get();
        }
 
        public void setVenue(String ven) {
            venue.set(ven);
        }
        
        public String getDate() {
            return date.get();
        }
 
        public void setDate(String date1) {
            date.set(date1);
        }}
