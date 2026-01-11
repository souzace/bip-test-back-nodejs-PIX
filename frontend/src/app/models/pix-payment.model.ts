export interface PixPayment {
  id?: string;
  senderPixKey: string;
  receiverPixKey: string;
  amount: number;
  description: string;
  status?: string;
  createdAt?: string;
}

export interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
