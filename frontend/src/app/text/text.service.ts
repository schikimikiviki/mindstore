import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Text } from './text.model';

@Injectable({
  providedIn: 'root',
})
export class TextService {
  private baseUrl = 'http://localhost:8080/texts/all';

  constructor(private http: HttpClient) {}

  getTexts(): Observable<Text[]> {
    return this.http.get<Text[]>(this.baseUrl);
  }
}
