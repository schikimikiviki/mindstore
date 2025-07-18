import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Text } from './text.model';
import { SearchResultDto } from '../searchResultDto/searchResultDto';
import { LoggerService } from '../services/logger/logger.service';

@Injectable({
  providedIn: 'root',
})
export class TextService {
  // urls that we fetch
  private baseUrl = 'http://localhost:8080/text-index/all';
  private searchUrl = 'http://localhost:8080/api/search';
  private historyUrl = 'http://localhost:8080/api/search/history';
  private tagSearchUrl = 'http://localhost:8080/text-index/all/tags'; // ?tags=JAVA&tags=PYTHON am ende
  private addTextUrl = 'http://localhost:8080/text-index/create';
  private autocompleteUrl =
    'http://localhost:8080/api/search/autocomplete?prefix=';

  // so soll das aussehen: http://localhost:8080/api/search/tag-search?tags=JAVA&query=documentation&page=0&size=10
  private searchUrlForTags = 'http://localhost:8080/api/search/tag-search?';
  private deleteUrl = 'http://localhost:8080/text-index/delete?id=';

  constructor(private http: HttpClient, private logger: LoggerService) {}

  getTagSearch(
    query: string,
    tags: string[],
    searchAfter?: string
  ): Observable<SearchResultDto<Text>> {
    let httpParams = new HttpParams();
    tags.forEach((tag) => {
      httpParams = httpParams.append('tags', tag);
    });

    if (searchAfter) {
      httpParams = httpParams.set('searchAfter', searchAfter);
    }

    httpParams = httpParams.set('query', query);
    httpParams = httpParams.set('page', 0);
    httpParams = httpParams.set('size', 10);

    this.logger.log(
      'Executing HTTP Request GET: ',
      this.searchUrlForTags + httpParams
    );
    return this.http.get<SearchResultDto<Text>>(this.searchUrlForTags, {
      params: httpParams,
    });
  }

  getAutocompletion(query: string): Observable<string[]> {
    this.logger.log(
      'Executing HTTP Request GET: ',
      this.autocompleteUrl + query
    );
    return this.http.get<string[]>(this.autocompleteUrl + query);
  }

  getTexts(searchAfter?: string): Observable<SearchResultDto<Text>> {
    if (searchAfter) {
      this.logger.log(
        'Executing HTTP Request GET: ',
        this.baseUrl + '?searchAfter=' + searchAfter
      );
      return this.http.get<SearchResultDto<Text>>(
        this.baseUrl + '?searchAfter=' + searchAfter
      );
    } else {
      this.logger.log('Executing HTTP Request GET: ', this.baseUrl);
      return this.http.get<SearchResultDto<Text>>(this.baseUrl);
    }
  }

  addText(
    title: string,
    contentRaw: string,
    contentHtml: string,
    tags: string[],
    commands: string[]
  ) {
    this.logger.log('Executing HTTP Request POST: ', this.addTextUrl);
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

  getTextsWithTags(
    tags: string[],
    searchAfter?: string
  ): Observable<SearchResultDto<Text>> {
    let httpParams = new HttpParams();
    tags.forEach((tag) => {
      httpParams = httpParams.append('tags', tag);
    });

    if (searchAfter) {
      httpParams = httpParams.set('searchAfter', searchAfter);
    }

    this.logger.log(
      'Executing HTTP Request GET: ',
      this.tagSearchUrl + httpParams
    );

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

    this.logger.log('Executing HTTP Request GET: ', this.searchUrl);

    return this.http.get<SearchResultDto<Text>>(this.searchUrl, {
      params,
      withCredentials: true,
    });
  }

  getHistory(): Observable<string[]> {
    this.logger.log('Executing HTTP Request GET: ', this.historyUrl);
    return this.http.get<string[]>(this.historyUrl);
  }

  deleteText(id: number): Observable<any> {
    this.logger.log('Executing HTTP Request DELETE: ', this.deleteUrl + id);
    return this.http.delete<any>(this.deleteUrl + id, {
      withCredentials: true,
    });
  }
}
