import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { PixPaymentService } from '../../services/pix-payment.service';
import { PixPayment } from '../../models/pix-payment.model';

@Component({
  selector: 'app-payment-form',
  templateUrl: './payment-form.component.html',
  styleUrls: ['./payment-form.component.css']
})
export class PaymentFormComponent {
  transferForm: FormGroup;
  submitted = false;
  loading = false;
  success: string | null = null;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private pixPaymentService: PixPaymentService,
    private translate: TranslateService
  ) {
    this.transferForm = this.fb.group({
      senderPixKey: ['', [Validators.required]],
      receiverPixKey: ['', [Validators.required]],
      amount: ['', [Validators.required, Validators.min(0.01)]],
      description: ['']
    });
  }

  get f() { return this.transferForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    this.success = null;
    this.error = null;

    if (this.transferForm.invalid) {
      return;
    }

    this.loading = true;
    const payment: PixPayment = this.transferForm.value;

    this.pixPaymentService.createPayment(payment).subscribe({
      next: (response) => {
        this.translate.get('PAYMENT.SUCCESS', { id: response.id }).subscribe((text: string) => {
          this.success = text;
        });
        this.loading = false;
        this.submitted = false;
        this.transferForm.reset();
      },
      error: (err) => {
        this.translate.get('PAYMENT.ERROR').subscribe((text: string) => {
          this.error = err.error?.description || text;
        });
        this.loading = false;
        console.error(err);
      }
    });
  }
}
