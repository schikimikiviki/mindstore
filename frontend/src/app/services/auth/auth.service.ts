import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private loginUrl = 'http://localhost:8080/auth/login';
  private logoutUrl = 'http://localhost:8080/auth/logout';

  constructor(private http: HttpClient) {}

  loginUser(email: string, password: string): Observable<any> {
    const body = { email: email, password: password };
    return this.http.post(this.loginUrl, body);
  }

  logoutUser(): Observable<any> {
    localStorage.removeItem('token');
    localStorage.removeItem('token_expiry');
    return this.http.post(this.logoutUrl, {});
  }

  startTokenExpiryTimer(expiresIn: number) {
    setTimeout(() => {
      this.logoutUser(); // clear token and redirect
    }, expiresIn * 1000);
  }
}
