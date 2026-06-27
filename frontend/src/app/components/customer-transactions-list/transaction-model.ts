export type TransactionStatus = 'PENDING' | 'COMPLETED' | 'CANCELLED';

export interface Transaction {
  transactionId: string;
  transaction_date: string;
  status: TransactionStatus;
  earnedPoints: number | null;
  serviceId: string;
  merchantId: string;
  customerId: string;
}
