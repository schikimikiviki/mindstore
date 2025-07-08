import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Text } from './text.model';
import { SearchResultDto } from '../searchResultDto/searchResultDto';

@Injectable({
  providedIn: 'root',
})
export class TextService {
  private baseUrl = 'http://localhost:8080/text-index/all';
  private searchUrl = 'http://localhost:8080/api/search';

  constructor(private http: HttpClient) {}

  getTexts(): Observable<SearchResultDto<Text>> {
    return this.http.get<SearchResultDto<Text>>(this.baseUrl);
  }

  searchTexts(
    query: string,
    page = 0,
    size = 10
  ): Observable<SearchResultDto<Text>> {
    const params = {
      query,
      page: page.toString(),
      size: size.toString(),
    };
    return this.http.get<SearchResultDto<Text>>(this.searchUrl, { params });
  }
}
