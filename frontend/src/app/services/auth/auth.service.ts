import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private loginUrl = 'http://localhost:8080/auth/login';
  private logoutUrl = 'http://localhost:8080/auth/logout';
  private tagUrl = 'http://localhost:8080/api/search/tags';
  private checkLoginUrl = 'http://localhost:8080/auth/check';

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

  getTags(): Observable<any> {
    return this.http.get(this.tagUrl);
  }

  checkLogin(): Observable<boolean> {
    return this.http.get(this.checkLoginUrl, { withCredentials: true }).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }
}
