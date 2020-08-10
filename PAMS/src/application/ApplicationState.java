package application;

import java.sql.Timestamp;
import java.util.Calendar;

public class ApplicationState {
	
	public static boolean AppointmentDisplayOn = false;
	public static Timestamp lastApplicationUpdate = new Timestamp(0);
	
	public static boolean isAppointmentDisplayOn() {
		return AppointmentDisplayOn;
	}
	
	public static void setAppointmentDisplayOn(boolean property) {
		AppointmentDisplayOn = property;
	}

	public static Timestamp getLastApplicationUpdate() {
		return lastApplicationUpdate;
	}

	public static void setLastApplicationUpdate(long time) {
		ApplicationState.lastApplicationUpdate.setTime(time);
	}
	
	public static void markUpdated() {
		setLastApplicationUpdate(Calendar.getInstance().getTime().getTime());
	}
}
