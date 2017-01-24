package firebaseapps.com.pass;

/**
 * Created by 1405214 on 27-09-2016.
 * The data  model that will help to fetch the data
 * from the firebase database
 */

public class Application {

  public  String Name;
  public  String Address;
  public  String Mobile;
  public  String ID_No;
  public  String Purpose;
  public  String ApplicantPhoto;
  public   String ApplicantScanId;
  public   String Uid;
  public   String ApplicationStatus;
  public   String DateOfBirth;
  public  String DateOfJourney;
  public  String Barcode_Image;
  public String ID_Source;
    public String Carnumber;
    public String Drivername;
    public String Gate;
    public String Destination;


    public Application() {
    }

    public Application(String name, String address, String mobile, String ID_No, String purpose, String applicantPhoto, String applicantScanId, String uid, String applicationStatus, String dateOfBirth, String dateOfJourney, String barcode_Image, String ID_Source, String carnumber, String drivername, String gate, String destination) {
        Name = name;
        Address = address;
        Mobile = mobile;
        this.ID_No = ID_No;
        Purpose = purpose;
        ApplicantPhoto = applicantPhoto;
        ApplicantScanId = applicantScanId;
        Uid = uid;
        ApplicationStatus = applicationStatus;
        DateOfBirth = dateOfBirth;
        DateOfJourney = dateOfJourney;
        Barcode_Image = barcode_Image;
        this.ID_Source = ID_Source;
        Carnumber = carnumber;
        Drivername = drivername;
        Gate = gate;
        Destination = destination;
    }
}
