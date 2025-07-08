import java.sql.Timestamp;

public class Appointments {
    private int appointmentId;
    private long patientId;
    private long doctorId;
    private Timestamp appointmentDate;
    private String status;

    public Appointments(int appointmentId, long patientId, long doctorId, Timestamp appointmentDate, String status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    public int getAppointmentId() { return appointmentId; }
    public long getPatientId() { return patientId; }
    public long getDoctorId() { return doctorId; }
    public Timestamp getAppointmentDate() { return appointmentDate; }
    public String getStatus() { return status; }

    public void setAppointmentDate(Timestamp appointmentDate) { this.appointmentDate = appointmentDate; }
    public void setStatus(String status) { this.status = status; }
}
