import { Component, OnInit } from '@angular/core';
import { BenefitService } from '../../services/benefit.service';
import { Benefit } from '../../models/benefit.model';

@Component({
  selector: 'app-benefit-list',
  templateUrl: './benefit-list.component.html',
  styleUrls: ['./benefit-list.component.css']
})
export class BenefitListComponent implements OnInit {
  benefits: Benefit[] = [];
  loading = false;
  error: string | null = null;

  constructor(private benefitService: BenefitService) {}

  ngOnInit(): void {
    this.loadBenefits();
  }

  loadBenefits(): void {
    this.loading = true;
    this.error = null;
    
    this.benefitService.getAllBenefits().subscribe({
      next: (data) => {
        this.benefits = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar benefícios';
        this.loading = false;
        console.error(err);
      }
    });
  }
}
