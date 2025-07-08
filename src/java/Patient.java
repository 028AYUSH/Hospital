import java.util.Date;

public class Patient {
    private String patientId;
    private String name;
    private String email;
    private Date dob;

    public Patient(String patientId, String name, String email, Date dob) {
        this.patientId = patientId;
        this.name = name;
        this.email = email;
        this.dob = dob;
    }

    public String getPatientId() { return patientId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Date getDob() { return dob; }
}
