import { Component, OnInit } from '@angular/core';
import { PixPaymentService } from '../../services/pix-payment.service';
import { PixPayment, PagedResponse } from '../../models/pix-payment.model';

@Component({
  selector: 'app-payment-history',
  templateUrl: './payment-history.component.html',
  styleUrls: ['./payment-history.component.css']
})
export class PaymentHistoryComponent implements OnInit {
  payments: PixPayment[] = [];
  loading = false;
  error: string | null = null;
  
  // Pagination
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;
  
  // Filters
  senderPixKey = '';
  receiverPixKey = '';
  status = '';

  constructor(private pixPaymentService: PixPaymentService) {}

  ngOnInit(): void {
    this.loadPayments();
  }

  loadPayments(): void {
    this.loading = true;
    this.error = null;
    
    this.pixPaymentService.getPayments(
      this.currentPage,
      this.pageSize,
      this.senderPixKey || undefined,
      this.receiverPixKey || undefined,
      this.status || undefined
    ).subscribe({
      next: (data: PagedResponse<PixPayment>) => {
        this.payments = data.content;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
        this.currentPage = data.number;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'HISTORY.ERROR';
        this.loading = false;
        console.error(err);
      }
    });
  }

  applyFilters(): void {
    this.currentPage = 0;
    this.loadPayments();
  }

  clearFilters(): void {
    this.senderPixKey = '';
    this.receiverPixKey = '';
    this.status = '';
    this.currentPage = 0;
    this.loadPayments();
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadPayments();
    }
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleString('pt-BR');
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('pt-BR', { 
      style: 'currency', 
      currency: 'BRL' 
    }).format(amount);
  }
}
