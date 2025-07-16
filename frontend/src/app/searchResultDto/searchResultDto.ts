export interface SearchResultDto<T> {
  content: T[];
  total: number;
  page: number;
  size: number;
  searchAfter: string;
}
