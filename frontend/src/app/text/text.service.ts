import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Text } from './text.model';
import { SearchResultDto } from '../searchResultDto/searchResultDto';

@Injectable({
  providedIn: 'root',
})
export class TextService {
  private baseUrl = 'http://localhost:8080/text-index/all';
  private searchUrl = 'http://localhost:8080/api/search';
  private historyUrl = 'http://localhost:8080/api/search/history';
  private tagSearchUrl = 'http://localhost:8080/text-index/all/tags'; // ?tags=JAVA&tags=PYTHON am ende
  private addTextUrl = 'http://localhost:8080/text-index/create';

  constructor(private http: HttpClient) {}

  getTexts(): Observable<SearchResultDto<Text>> {
    return this.http.get<SearchResultDto<Text>>(this.baseUrl);
  }

  addText(
    title: string,
    contentRaw: string,
    contentHtml: string,
    tags: string[],
    commands: string[]
  ) {
    return this.http.post(
      this.addTextUrl,
      {
        title,
        content_raw: contentRaw,
        content_html: contentHtml,
        tags,
        commandList: commands,
      },
      { withCredentials: true }
    );
  }

  getTextsWithTags(tags: string[]): Observable<SearchResultDto<Text>> {
    let httpParams = new HttpParams();
    tags.forEach((tag) => {
      httpParams = httpParams.append('tags', tag);
    });

    return this.http.get<SearchResultDto<Text>>(this.tagSearchUrl, {
      params: httpParams,
    });
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
    return this.http.get<SearchResultDto<Text>>(this.searchUrl, {
      params,
      withCredentials: true,
    });
  }

  getHistory(): Observable<string[]> {
    return this.http.get<string[]>(this.historyUrl);
  }
}
