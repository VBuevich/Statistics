package raiload.statistics;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * @author vbuevich
 *
 * received entity class - statistics
 */
public class Statistics {
    private int statisticsId;
    private Timestamp datetime;
    private String passengerName;
    private String passengerSurname;
    private Date passengerDob;
    private String passengerEmail;
    private int trainNumber;
    private String trainType;
    private String departureStation;
    private String arrivalStation;
    private String seat;
    private Boolean isOneWay;
    private Date departureDate;
    private Time departureTime;

    public int getStatisticsId() {
        return statisticsId;
    }

    public void setStatisticsId(int statisticsId) {
        this.statisticsId = statisticsId;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerSurname() {
        return passengerSurname;
    }

    public void setPassengerSurname(String passengerSurname) {
        this.passengerSurname = passengerSurname;
    }

    public Date getPassengerDob() {
        return passengerDob;
    }

    public void setPassengerDob(Date passengerDob) {
        this.passengerDob = passengerDob;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public Boolean getIsOneWay() {
        return isOneWay;
    }

    public void setIsOneWay(Boolean oneWay) {
        isOneWay = oneWay;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }
}
