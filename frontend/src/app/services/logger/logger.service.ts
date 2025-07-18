import { Injectable, isDevMode } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class LoggerService {
  log(...args: any[]): void {
    if (isDevMode()) {
      console.log('%c[LOG]', 'color: green', ...args);
    }
  }

  warn(...args: any[]): void {
    if (isDevMode()) {
      console.warn('%c[WARN]', 'color: orange', ...args);
    }
  }

  error(...args: any[]): void {
    console.error('%c[ERROR]', 'color: red; font-weight: bold', ...args);
  }

  debug(...args: any[]): void {
    if (isDevMode()) {
      console.debug('%c[DEBUG]', 'color: gray', ...args);
    }
  }
}
