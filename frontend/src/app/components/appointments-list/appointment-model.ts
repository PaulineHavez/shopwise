export type AppointmentStatus = 'UPCOMING' | 'CANCELLED' | 'COMPLETED';

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
