import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Benefit } from '../models/benefit.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BenefitService {
  private apiUrl = `${environment.apiUrl}/benefits`;

  constructor(private http: HttpClient) {}

  getAllBenefits(): Observable<Benefit[]> {
    return this.http.get<Benefit[]>(this.apiUrl);
  }

  getBenefitById(id: string): Observable<Benefit> {
    return this.http.get<Benefit>(`${this.apiUrl}/${id}`);
  }

  createBenefit(benefit: Benefit): Observable<Benefit> {
    return this.http.post<Benefit>(this.apiUrl, benefit);
  }

  updateBenefit(id: string, benefit: Benefit): Observable<Benefit> {
    return this.http.put<Benefit>(`${this.apiUrl}/${id}`, benefit);
  }

  deleteBenefit(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
