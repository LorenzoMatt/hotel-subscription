import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class HttpConfigInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An unknown error occurred!';

        if (error.error instanceof ErrorEvent) {
          // Client-side error
          errorMessage = `Error: ${error.error.message}`;
        } else {
          // Server-side error
          if (error.error && error.error.errors) {
            const validationErrors = error.error.errors.map((err: any) => `Field: ${err.field}, Error: ${err.error}, Value: ${err.value}`).join('\n');
            errorMessage = `Error Code: ${error.status}\nMessage: ${error.error.message}\n${validationErrors}`;
          } else {
            errorMessage = `Error Code: ${error.status}\nMessage: ${error.error.message || error.message}`;
          }
        }

        return throwError(errorMessage);
      })
    );
  }
}
