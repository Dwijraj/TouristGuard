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

    public Application(String name, String address, String mobile, String ID_No, String purpose, String applicantPhoto, String applicantScanId, String uid, String applicationStatus, String dateOfBirth, String dateOfJourney,String barcode_Image,String id_source) {
        Name = name;
        Address = address;
        Mobile = mobile;
        Barcode_Image=barcode_Image;
        this.ID_No = ID_No;
        Purpose = purpose;
        ApplicantPhoto = applicantPhoto;
        ApplicantScanId = applicantScanId;
        Uid = uid;
        ID_Source=id_source;
        ApplicationStatus = applicationStatus;
        DateOfBirth = dateOfBirth;
        DateOfJourney = dateOfJourney;
    }

    public Application() {              //Important or else the app crashes firebase requires a datamodel class with default constructor
    }
}
