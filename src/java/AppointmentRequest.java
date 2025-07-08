import java.sql.Timestamp;

public class AppointmentRequest {
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private Timestamp requestedDate;
    private Timestamp requestedTime;

    public AppointmentRequest(String appointmentId, String patientId, String doctorId, Timestamp requestedDate, Timestamp requestedTime) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.requestedDate = requestedDate;
        this.requestedTime = requestedTime;
    }

    public String getAppointmentId() { return appointmentId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public Timestamp getRequestedDate() { return requestedDate; }
    public Timestamp getRequestedTime() { return requestedTime; }
}
