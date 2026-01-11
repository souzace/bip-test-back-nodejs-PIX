import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <div class="app-container">
      <nav class="nav">
        <h1>PIX Payment System</h1>
        <div class="nav-links">
          <a routerLink="/benefits" routerLinkActive="active">Benefícios</a>
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
    .nav-links a {
      color: white;
      text-decoration: none;
      margin-left: 20px;
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
}
