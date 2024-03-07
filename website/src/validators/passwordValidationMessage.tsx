export function passwordValidationMessage(password: string): string {
  if (password.length < 8) {
    return "Must be at least 8 characters long";
  }
  if (!password.match(/[a-z]/)) {
    return "Must contain at least one lowercase letter";
  }

  if (!password.match(/[A-Z]/)) {
    return "Must contain at least one uppercase letter";
  }
  if (!password.match(/[0-9]/)) {
    return "Must contain at least one number";
  }

  if (!password.match(/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/)) {
    return "Must contain at least one special character";
  }

  return "Password is valid";
}
