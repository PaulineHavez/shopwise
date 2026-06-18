export type AppointmentStatus = 'UPCOMING' | 'HONORED' | 'COMPLETED';

export interface Appointment {
  appointmentId: string;
  startAt: string;
  endAt: string;
  status: AppointmentStatus;
  earnedPoints: number | null;
  serviceId: string;
  merchantId: string;
  customerId: string;
}
