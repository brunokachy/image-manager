import {Component} from '@angular/core';
import {FormGroup, FormControl, Validators} from '@angular/forms';
import {fileExtensionValidator} from './validation/file-extension-validator.directive';
import {AlertComponent} from 'ngx-bootstrap/alert';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private http: HttpClient) {
  }

  title = 'image-manager-ui';

  alerts: any[] = [];

  spinnerAlerts: any[] = [];

  file: any;

  acceptedExtensions = "jpg, jpeg, png";

  // endpoint = window.location.origin + '/api/v1/image-manager/upload';

  endpoint = 'http://localhost:8080/api/v1/image-manager/upload';

  add(type, message): void {
    this.alerts.push({
      type,
      msg: message,
      timeout: 5000
    });
  }

  addSpinner(type, message): void {
    this.spinnerAlerts.push({
      type,
      msg: message
    });
  }


  onClosed(dismissedAlert: AlertComponent): void {
    this.alerts = this.alerts.filter(alert => alert !== dismissedAlert);
  }

  onClosedSpinner(dismissedAlert: AlertComponent): void {
    this.spinnerAlerts = this.spinnerAlerts.filter(alert => alert !== dismissedAlert);
  }

  form = new FormGroup({
    image: new FormControl('', [Validators.required, fileExtensionValidator(this.acceptedExtensions)]),
    description: new FormControl('', Validators.required)
  });

  get f() {
    return this.form.controls;
  }

  submit() {
    this.form.markAllAsTouched();

    this.validateFields();

    this.addSpinner('info', 'Uploading Image..................');

    let formData: any = new FormData();
    formData.append("description", this.form.value.description);
    formData.append("image", this.file);

    this.http.post(this.endpoint, formData).subscribe(
      (response) => {
        this.onClosedSpinner(this.spinnerAlerts[0]);
        this.add("success", "Image Uploaded Successfully");
      },
      (error) => {
        this.handleErrorResponse(error);
      }
    )

  }

  fileChanged(e) {

    const file = (e.target as HTMLInputElement).files[0];
    this.file = file;
    this.form.patchValue({
      image: file
    });
    this.form.get('image').updateValueAndValidity();

    if (file.size > 500000) {
      this.add('danger', 'File size is greater than 500KB');
    }

  }

  validateFields() {
    if (this.form.value.description == "") {
      this.add('danger', 'Failed');

      return;
    }

    if (this.file.size > 500000) {
      this.add('danger', 'File size is greater than 500KB');

      return;
    }
  }

  handleErrorResponse(error) {
    this.onClosedSpinner(this.spinnerAlerts[0]);
    if (error.status == 500) {
      this.add("danger", "An Unexpected Error occurred while trying to upload image. Please try again later");
    }

    if (error.status == 400) {
      this.add("danger", "An error occurred while trying to validate image. Please try again later");
    }
  }

}
