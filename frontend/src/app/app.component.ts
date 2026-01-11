import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  template: `
    <div class="app-container">
      <nav class="nav">
        <h1>{{ 'NAV.TITLE' | translate }}</h1>
        <div class="nav-links">
          <a routerLink="/benefits" routerLinkActive="active">{{ 'NAV.BENEFITS' | translate }}</a>
          <a routerLink="/payment" routerLinkActive="active">{{ 'NAV.PAYMENT' | translate }}</a>
          <a routerLink="/history" routerLinkActive="active">{{ 'NAV.HISTORY' | translate }}</a>
          <select (change)="switchLanguage($event)" class="lang-selector">
            <option value="pt-BR">🇧🇷 PT</option>
            <option value="en">🇺🇸 EN</option>
          </select>
        </div>
      </nav>
      <div class="content">
        <router-outlet></router-outlet>
      </div>
    </div>
  `,
  styles: [`
    .app-container {
      min-height: 100vh;
      display: flex;
      flex-direction: column;
    }
    .nav {
      background-color: #333;
      color: white;
      padding: 15px 30px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .nav h1 {
      margin: 0;
      font-size: 24px;
    }
    .nav-links {
      display: flex;
      align-items: center;
      gap: 15px;
    }
    .nav-links a {
      color: white;
      text-decoration: none;
      padding: 8px 16px;
      border-radius: 4px;
      transition: background-color 0.3s;
    }
    .nav-links a:hover {
      background-color: #555;
    }
    .nav-links a.active {
      background-color: #007bff;
    }
    .lang-selector {
      padding: 6px 10px;
      border-radius: 4px;
      border: none;
      background-color: #555;
      color: white;
      cursor: pointer;
      font-size: 14px;
    }
    .lang-selector:hover {
      background-color: #666;
    }
    .content {
      flex: 1;
      padding: 20px;
      max-width: 1200px;
      margin: 0 auto;
      width: 100%;
    }
  `]
})
export class AppComponent {
  title = 'PIX Payment System';

  constructor(private translate: TranslateService) {
    this.translate.setDefaultLang('pt-BR');
    this.translate.use('pt-BR');
  }

  switchLanguage(event: any): void {
    this.translate.use(event.target.value);
  }
}
