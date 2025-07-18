import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, catchError, tap } from 'rxjs/operators';
import { of, BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private loginUrl = 'http://localhost:8080/auth/login';
  private logoutUrl = 'http://localhost:8080/auth/logout';
  private tagUrl = 'http://localhost:8080/api/search/tags';
  private checkLoginUrl = 'http://localhost:8080/auth/check';

  private loggedInSubject = new BehaviorSubject<boolean>(false);
  public loggedIn$ = this.loggedInSubject.asObservable();

  constructor(private http: HttpClient) {
    // on service init, check login state from backend
    this.checkLogin().subscribe((state) => {
      this.loggedInSubject.next(state);
    });
  }

  loginUser(email: string, password: string): Observable<any> {
    const body = { email, password };
    return this.http.post(this.loginUrl, body, { withCredentials: true }).pipe(
      map((res) => {
        this.loggedInSubject.next(true);
        return res;
      }),
      catchError((err) => {
        this.loggedInSubject.next(false);
        throw err;
      })
    );
  }

  logoutUser(): Observable<any> {
    localStorage.removeItem('token');
    localStorage.removeItem('token_expiry');
    this.loggedInSubject.next(false);
    return this.http.post(this.logoutUrl, {}, { withCredentials: true });
  }

  startTokenExpiryTimer(expiresIn: number) {
    setTimeout(() => {
      this.logoutUser().subscribe();
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

  isLoggedIn(): boolean {
    return this.loggedInSubject.value;
  }
}
