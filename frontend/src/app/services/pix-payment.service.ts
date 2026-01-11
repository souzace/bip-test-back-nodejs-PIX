import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PixPayment, PagedResponse } from '../models/pix-payment.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PixPaymentService {
  private apiUrl = `${environment.apiUrl}/pix/payments`;

  constructor(private http: HttpClient) {}

  createPayment(payment: PixPayment): Observable<PixPayment> {
    return this.http.post<PixPayment>(this.apiUrl, payment);
  }

  getPayments(
    page: number = 0,
    size: number = 10,
    senderPixKey?: string,
    receiverPixKey?: string,
    status?: string
  ): Observable<PagedResponse<PixPayment>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (senderPixKey) {
      params = params.set('senderPixKey', senderPixKey);
    }
    if (receiverPixKey) {
      params = params.set('receiverPixKey', receiverPixKey);
    }
    if (status) {
      params = params.set('status', status);
    }

    return this.http.get<PagedResponse<PixPayment>>(this.apiUrl, { params });
  }
}
